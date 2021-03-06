package com.damonallison.libraries.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@code Files}, along with {@code Path} are main entry point into NIO.2. NIO.2
 * is all about better file management.
 * <p>
 * Unlike {@code Path}, which typically does not touch the file system,
 * {@code File} performs operations against the file system.
 * <p>
 * {@link Files} is a main entry point into the NIO.2 package. Files contains
 * static methods.
 */
class IOFileTests {

    private static final Logger LOGGER = Logger.getLogger(IOFileTests.class.getName());
    /**
     * The static class {@link Files} contains a comprehensive list of
     * operations that can be performed against files. It delegates to other
     * classes to perform the actual operations.
     * <p>
     * For example, most file system operations are delegated to an internal
     * {@link FileSystemProvider} class.
     * <p>
     * This test shows the use of {@link Files} to read metadata (attributes).
     */
    @Test
    void fileAttributes() throws IOException {

        Path tempDir = Files.createTempDirectory("temp"); // creates temp.tmp
        LOGGER.info("tempDir: " + tempDir.toAbsolutePath().toString());

        // Reading file attributes.
        //
        // These functions all read a single file attribute with each call.
        //
        // Each attribute read hits the disk - so read attributes in bulk if
        // possible.

        assertTrue(Files.exists(tempDir));
        assertTrue(Files.isDirectory(tempDir));
        assertTrue(Files.isReadable(tempDir));
        assertTrue(Files.isWritable(tempDir));
        assertTrue(Files.isExecutable(tempDir));

        //
        // Basic Attributes
        //
        // If you are going to read attributes in bulk, or need attributes
        // that are not provided at a high level via the {@code Files} class,
        // use {@code ReadAttributes}.
        //
        // File attributes are grouped into "Views". A view maps to
        // a particular file system implementation. In the Following example,
        // {@code BasicFileAttributes} is a view.
        //
        long lastMod = Files.getLastModifiedTime(tempDir).toMillis();
        BasicFileAttributes basicAttrs = Files.readAttributes(tempDir, BasicFileAttributes.class);
        assertEquals(lastMod, basicAttrs.lastModifiedTime().toMillis());

        //
        // Posix attributes
        //
        // Posix attributes extends basic attributes by providing owner, group,
        // and permissions.
        PosixFileAttributes posixAttrs = Files.readAttributes(tempDir,
                PosixFileAttributes.class);

        assertTrue(posixAttrs.permissions().contains(PosixFilePermission.OWNER_READ));

        // Get the current username / groupGid from the system.

        String userName = System.getProperty("user.name");

        // Get the currently logged in user from the file. This user should be the owner.
        assertEquals(posixAttrs.owner().getName(), userName, "The current user should have created the temp file");

        // Get the GID of the file.
        // Integer gid = (Integer) Files.readAttributes(tempDir, "unix:gid")
        // TODO: How to retrieve the current user group from the file?
        // assertEquals(us.getGid(), gid.longValue(), "The current group should be the group on the temp file");

        // Example reading / writing custom attributes.
        UserDefinedFileAttributeView view = Files.getFileAttributeView(tempDir,
                UserDefinedFileAttributeView.class);

        // Note : this is *not* supported on all operating systems. If not
        // supported,
        // we get null here.
        if (view != null) {
            // Write
            view.write("mime-type", Charset.defaultCharset().encode("dir"));

            // Read it back.
            ByteBuffer buf = ByteBuffer.allocate(view.size("mime-type"));
            view.read("mime-type", buf);
            buf.flip();
            assertEquals("dir", Charset.defaultCharset().decode(buf).toString());
        }
    }

    /**
     * Tests symbolic link creation and other operations.
     * <p>
     * {@code .toRealPath()} Does the following:
     *
     * <ul>
     * <li>Resolves symbolic links
     * <li>If the path is relative, returns the absolute path.
     * <li>If the path contains any redundant elements, they are removed.
     * <li>Throws an exception if the file does not exist or cannot be accessed.
     * </ul>
     */
    @Test
    void testSymbolicLinks() throws IOException {

        /*-
         * Creates
         * tmpXXX/
         *   a/
         *     b/
         *       test.txt
         * test.txt (symlink to a/b/test.txt)
         *
         */

        final Path tempDir = Files.createTempDirectory("tmp");
        final Path tempDirName = tempDir.getFileName();
        Path ab = Files.createDirectories(tempDir.resolve("a/b"));
        assertTrue(Files.exists(tempDir.resolve("a/b")));

        Path textFile = ab.resolve("test.txt");
        Files.write(textFile, "hello, world".getBytes());
        assertTrue(Files.exists(textFile));

        // Create a path /tmp/test.txt that points to /tmp/a/b/test.txt to test
        // symbolic link resolution.
        Path symlink = tempDir.resolve("test.txt");
        Files.createSymbolicLink(symlink, textFile);
        assertTrue(Files.exists(symlink));
        assertTrue(Files.isSymbolicLink(symlink));

        // The real file ends in a/b/test.txt
        // The symlink ends with tmp/test.txt
        assertTrue(textFile.endsWith("a/b/test.txt"));
        assertTrue(symlink.endsWith(tempDirName.resolve("test.txt")));

        assertTrue(Files.isSameFile(symlink, textFile));

        // .toRealPath() returns an absolute, case correct, and symlinked
        // resolved path.
        //
        // Note : we need to convert *both* to their real paths - the /tmp
        // directory is also symlinked in OS X.
        assertEquals(textFile.toRealPath(), symlink.toRealPath());
    }

    @Test
    public void testCircularLinks() throws IOException {
        assertThrows(FileSystemException.class, () -> {
            final Path tempDir = Files.createTempDirectory("tmp").toRealPath();
            final Path a = tempDir.resolve("a");
            final Path b = tempDir.resolve("b");

            // Create and test valid symlinks
            //
            // /real
            // /a -> /real
            // /b -> /a
            Path real = Files.createDirectory(tempDir.resolve("real"));
            Files.createSymbolicLink(a, real);
            Files.createSymbolicLink(b, a);

            assertEquals(real, a.toRealPath());
            assertEquals(real, b.toRealPath());

            // Create a circular dependency c -> a and a -> c
            final Path c = tempDir.resolve("c");
            Files.createSymbolicLink(c, a);
            Files.delete(a);
            Files.createSymbolicLink(a, c);

            // Now try to resolve a symbolic link.
            // This will throw a FileSystemException:
            // "Too many levels of symbolic links"
            final Path resolved = c.toRealPath();
            fail("Circular path dependency should have failed "
                    + resolved.toString());
        });
    }

    /**
     * Shows retrieving information on all file stores on the system
     *
     * @throws IOException
     */
    @Test
    void testFileStore() throws IOException {
        FileSystem fs = FileSystems.getDefault();
        for (FileStore store : fs.getFileStores()) {
            long total = store.getTotalSpace();
            long used = store.getTotalSpace() - store.getUnallocatedSpace();
            long avail = store.getUsableSpace();

            assertTrue(total >= used);
            assertTrue(total >= avail);
        }
    }
}
