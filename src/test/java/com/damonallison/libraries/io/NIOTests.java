package com.damonallison.libraries.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * An example {@link WatchService} that will listen for file system events.
	 *
	 * Shows how to execute a background callable in a new thread.
	 */
	@Test
	void watchService() throws IOException {

		Path tempDir = Files.createTempDir().toPath();
		WatchService watchService = FileSystems.getDefault().newWatchService();
		tempDir.register(watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

		final Runnable watcherReadyRunnable;

		Callable<List<WatchEvent<Path>>> callable = new Callable<List<WatchEvent<Path>>>() {
			@Override
			public List<WatchEvent<Path>> call() throws Exception {

				List<WatchEvent<Path>> events = new ArrayList<WatchEvent<Path>>();
				for (;;) {
					WatchKey key;
					try {
						// blocks until a key is signaled.
						key = watchService.take();
					} catch (InterruptedException ex) {
						break;
					}
					for (WatchEvent<?> event : key.pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
							// An overflow is unexpected in this test. In a
							// non-test application,
							// you need to handle an overflow event.
							fail();
						}

						WatchEvent<Path> typedEvent = cast(event);
						events.add(typedEvent);

						if (events.size() >= 2) {
							return events;
						}
					}
					// You must put the watch key back into a ready state or
					// will not capture new events.
					if (!key.reset()) {
						fail();
					}
				}
				return events;
			}
		};

		// Give the watcher time to start (there has to be a better way)
		// Is it possible to get a future to when the watcher service is
		// ready?
		watcherReadyRunnable = () -> {
			// Create some files
			File tempDirFile = tempDir.toFile();
			try {
				Path tmp1 = File.createTempFile("one", "tmp", tempDirFile)
						.toPath();
				Path tmp2 = File.createTempFile("two", "tmp", tempDirFile)
						.toPath();
			} catch (Exception e) {
				fail();
			}
		};

		// Starts watching
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<List<WatchEvent<Path>>> f = executor.submit(callable);

		try {
			// NOTE : it takes a while for the events to be propagated from
			// our
			// watcher to the handler.
			List<WatchEvent<Path>> events = f.get();
			assertTrue(events.size() == 2);

			// List<Path> expected = Lists.newArrayList(tmp1, tmp2);
			//
			// List<Path> actual = events.stream()
			// .map(event -> tempDir.resolve(event.context()))
			// .collect(Collectors.toList());
			//
			// assertEquals(expected, actual);

		} catch (ExecutionException | InterruptedException ex) {
			fail();
		}

	}
}
