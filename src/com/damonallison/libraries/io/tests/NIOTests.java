package com.damonallison.libraries.io.tests;

/**
 * Java 1.4 introduced NIO. Before NIO, server applications had to dedicate a
 * worker thread for each socket.
 *
 * Java 1.7 introduced NIO.2. NIO.2 introduces {@link Path}, {@link File}, and
 * improved file management operations.
 *
 * Despite their similar naming (NIO and NIO.2) NIO improves low level data I/O
 * (non-blocking channels).
 *
 * NIO:
 *
 * <ul>
 * <li>Non-blocking.</li>
 * <li>Buffer based. All data is transmitted thru buffers.</li>
 * <li>Channels. Blocking or non-blocking connections where buffers are
 * read/written.
 * <li>FileChannel. Blocking.
 * <li>SocketChannel. Blocking or non-blocking.
 * <li>Selectors. Callbacks when events occur.
 * </ul>
 *
 * NIO.2:
 *
 * NIO.2 is not related to NIO. NIO2 introduces:
 * <ul>
 * <li>java.nio.file</li>
 * <li>File, Path, and better file management.</li>
 * <li>Better support for file metadata.</li>
 * <li>Support for symlinks.</li>
 * </ul>
 */
public class NIOTests {

}
