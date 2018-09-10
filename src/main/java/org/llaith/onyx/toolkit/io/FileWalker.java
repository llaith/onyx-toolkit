package org.llaith.onyx.toolkit.io;

import com.google.common.primitives.Ints;
import org.llaith.onyx.toolkit.fn.impl.CompoundConsumer.CompoundConsumerFactory;
import org.llaith.onyx.toolkit.fn.impl.DefaultConsumers;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;


/**
 * Because java is too lame to have done it sensibly.
 * https://stackoverflow.com/questions/22867286/files-walk-calculate-total-size/22868706#22868706
 * Ripped from inside java stdlib.
 */
public class FileWalker {

    // mine
    public static class Builder implements Consumer<Exception> {

        Path start;

        int maxDepth = Integer.MAX_VALUE;

        CompoundConsumerFactory<Exception> exceptionConsumers = new CompoundConsumerFactory<>();

        CompoundConsumerFactory<Path> fileConsumers = new CompoundConsumerFactory<>(this);

        CompoundConsumerFactory<Path> dirConsumers = new CompoundConsumerFactory<>(this);

        Collection<FileVisitOption> options;

        public Builder(final Path start) {

            this.start = notNull(start);

            this.options = Collections.emptyList();

        }

        public Builder setMaxDepth(final int maxDepth) {
            this.maxDepth = Ints.constrainToRange(maxDepth, 1, Integer.MAX_VALUE);
            return this;
        }

        public Builder addExceptionConsumer(final Consumer<Exception> exceptionConsumer) {
            this.exceptionConsumers.addConsumer(exceptionConsumer);
            return this;
        }

        public Builder addFileConsumer(final Consumer<Path> fileConsumer) {
            this.fileConsumers.addConsumer(fileConsumer);
            return this;
        }

        public Builder addDirConsumer(final Consumer<Path> dirConsumer) {
            this.dirConsumers.addConsumer(dirConsumer);
            return this;
        }

        public Builder setOptions(final Collection<FileVisitOption> options) {
            this.options = notNull(options);
            return this;
        }

        @Override
        public void accept(final Exception e) {

            // tack the default exception consumer in here 
            this.exceptionConsumers.get().accept(e);


        }

    }

    public static Builder newConfig(final Path start) {
        return new Builder(start);
    }

    public static Stream<Path> walk(final Builder builder) throws IOException {

        final FileTreeIterator iterator = new FileTreeIterator(
                builder.start,
                builder.maxDepth,
                builder.exceptionConsumers.get(),
                builder.dirConsumers.get(),
                builder.fileConsumers.get(),
                builder.options);

        try {

            return StreamSupport
                    .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT), false)
                    .onClose(iterator::close)
                    .map(FileTreeWalker.Event::file);

        } catch (Error | RuntimeException e) {

            iterator.close();

            throw e;

        }

    }


    private static class FileTreeIterator implements Iterator<FileTreeWalker.Event>, Closeable {

        private final Path start;

        private final FileTreeWalker walker;

        private FileTreeWalker.Event next;

        // mine
        private Consumer<Path> fileConsumer;

        // mine
        private Consumer<Path> dirConsumer;

        // mine
        private Consumer<Exception> exceptionConsumer;

        /**
         * Creates a new iterator to walk the file tree starting at the given file.
         *
         * @throws IllegalArgumentException if {@code maxDepth} is negative
         * @throws IOException              if an I/O errors occurs opening the starting file
         * @throws SecurityException        if the security manager denies access to the starting file
         * @throws NullPointerException     if {@code start} or {@code options} is {@ocde null} or
         *                                  the options array contains a {@code null} element
         */
        FileTreeIterator(
                Path start,
                int maxDepth,
                Consumer<Exception> exceptionConsumer,
                Consumer<Path> dirConsumer,
                Consumer<Path> fileConsumer,
                Collection<FileVisitOption> options) throws IOException {

            // mine
            this.start = notNull(start);
            this.exceptionConsumer = DefaultConsumers.defaultRethrowIfNull(exceptionConsumer);
            this.dirConsumer = DefaultConsumers.defaultConsumerIfNull(dirConsumer);
            this.fileConsumer = DefaultConsumers.defaultConsumerIfNull(fileConsumer);

            this.walker = new FileTreeWalker(options, maxDepth);

            this.next = walker.walk(start);

            assert next.type() == FileTreeWalker.EventType.ENTRY
                    || next.type() == FileTreeWalker.EventType.START_DIRECTORY;

            // IOException if there a problem accessing the starting file
            IOException ioe = next.ioeException();

            if (ioe != null) throw ioe; //exceptionConsumer.accept(ioe);

        }

        private void fetchNextIfNeeded() {

            if (next == null) {

                FileTreeWalker.Event ev = walker.next();

                while (ev != null) {

                    IOException ioe = ev.ioeException();

                    // old code - if (ioe != null) throw new UncheckedIOException(ioe);
                    if (ioe != null) exceptionConsumer.accept(ioe);
                    else { // mine

                        // END_DIRECTORY events are ignored
                        if (ev.type() != FileTreeWalker.EventType.END_DIRECTORY) {

                            // mine:
                            if (ev.type() == FileTreeWalker.EventType.START_DIRECTORY) {
                                final Path next = ev.file();
                                this.dirConsumer.accept(next);
                            } else if (ev.type() == FileTreeWalker.EventType.ENTRY) {
                                final Path next = ev.file();
                                this.fileConsumer.accept(next);
                            }

                            // my-note: keep it if its not an error or an end-dir
                            next = ev;

                            return;

                        }

                    } // mine

                    // my-note: try to get a new (valid) value before returning
                    ev = walker.next();

                }
            }
        }

        @Override
        public boolean hasNext() {

            if (!walker.isOpen()) throw new IllegalStateException();

            fetchNextIfNeeded();

            return next != null;

        }

        @Override
        public FileTreeWalker.Event next() {

            if (!walker.isOpen()) throw new IllegalStateException();

            fetchNextIfNeeded();

            if (next == null) throw new NoSuchElementException();

            FileTreeWalker.Event result = next;

            next = null;

            return result;

        }

        @Override
        public void close() {

            walker.close();

        }
    }

    private static class FileTreeWalker implements Closeable {

        private final boolean followLinks;
        private final LinkOption[] linkOptions;
        private final int maxDepth;
        private final ArrayDeque<FileTreeWalker.DirectoryNode> stack = new ArrayDeque<>();
        private boolean closed;

        /**
         * The element on the walking stack corresponding to a directory node.
         */
        private static class DirectoryNode {

            private final Path dir;
            private final Object key;
            private final DirectoryStream<Path> stream;
            private final Iterator<Path> iterator;
            private boolean skipped;

            DirectoryNode(Path dir, Object key, DirectoryStream<Path> stream) {
                this.dir = dir;
                this.key = key;
                this.stream = stream;
                this.iterator = stream.iterator();
            }

            Path directory() {
                return dir;
            }

            Object key() {
                return key;
            }

            DirectoryStream<Path> stream() {
                return stream;
            }

            Iterator<Path> iterator() {
                return iterator;
            }

            void skip() {
                skipped = true;
            }

            boolean skipped() {
                return skipped;
            }
        }

        /**
         * The event types.
         */
        static enum EventType {
            /**
             * Start of a directory
             */
            START_DIRECTORY,
            /**
             * End of a directory
             */
            END_DIRECTORY,
            /**
             * An entry in a directory
             */
            ENTRY;
        }

        /**
         * Events returned by the {@link #walk} and {@link #next} methods.
         */
        private static class Event {

            private final FileTreeWalker.EventType type;
            private final Path file;
            private final BasicFileAttributes attrs;
            private final IOException ioe;

            private Event(FileTreeWalker.EventType type, Path file, BasicFileAttributes attrs, IOException ioe) {
                this.type = type;
                this.file = file;
                this.attrs = attrs;
                this.ioe = ioe;
            }

            Event(FileTreeWalker.EventType type, Path file, BasicFileAttributes attrs) {
                this(type, file, attrs, null);
            }

            Event(FileTreeWalker.EventType type, Path file, IOException ioe) {
                this(type, file, null, ioe);
            }

            FileTreeWalker.EventType type() {
                return type;
            }

            Path file() {
                return file;
            }

            BasicFileAttributes attributes() {
                return attrs;
            }

            IOException ioeException() {
                return ioe;
            }
        }

        /**
         * Creates a {@code FileTreeWalker}.
         *
         * @throws IllegalArgumentException if {@code maxDepth} is negative
         * @throws ClassCastException       if (@code options} contains an element that is not a
         *                                  {@code FileVisitOption}
         * @throws NullPointerException     if {@code options} is {@ocde null} or the options
         *                                  array contains a {@code null} element
         */
        FileTreeWalker(Collection<FileVisitOption> options, int maxDepth) {

            boolean fl = false;

            for (FileVisitOption option : options) {

                // will throw NPE if options contains null
                switch (option) {

                    case FOLLOW_LINKS:
                        fl = true;
                        break;

                    default:
                        throw new AssertionError("Should not get here");

                }

            }

            if (maxDepth < 0) throw new IllegalArgumentException("'maxDepth' is negative");

            this.followLinks = fl;

            this.linkOptions = (fl) ? new LinkOption[0] : new LinkOption[] {LinkOption.NOFOLLOW_LINKS};

            this.maxDepth = maxDepth;

        }

        /**
         * Returns the attributes of the given file, taking into account whether
         * the walk is following sym links is not. The {@code canUseCached}
         * argument determines whether this method can use cached attributes.
         */
        private BasicFileAttributes getAttributes(Path file, boolean canUseCached) throws IOException {

            // if attributes are cached then use them if possible
            if (canUseCached && (file instanceof BasicFileAttributesHolder) && (System.getSecurityManager() == null)) {

                BasicFileAttributes cached = ((BasicFileAttributesHolder)file).get();

                if (cached != null && (!followLinks || !cached.isSymbolicLink())) {

                    return cached;

                }

            }

            // attempt to get attributes of file. If fails and we are following
            // links then a link target might not exist so get attributes of link
            BasicFileAttributes attrs;

            try {

                attrs = Files.readAttributes(file, BasicFileAttributes.class, linkOptions);

            } catch (IOException ioe) {

                if (!followLinks) throw ioe;

                // attempt to get attrmptes without following links
                attrs = Files.readAttributes(file,
                                             BasicFileAttributes.class,
                                             LinkOption.NOFOLLOW_LINKS);
            }
            return attrs;
        }

        /**
         * Returns true if walking into the given directory would result in a
         * file system loop/cycle.
         */
        private boolean wouldLoop(Path dir, Object key) {
            // if this directory and ancestor has a file key then we compare
            // them; otherwise we use less efficient isSameFile test.
            for (FileTreeWalker.DirectoryNode ancestor : stack) {
                Object ancestorKey = ancestor.key();
                if (key != null && ancestorKey != null) {
                    if (key.equals(ancestorKey)) {
                        // cycle detected
                        return true;
                    }
                } else {
                    try {
                        if (Files.isSameFile(dir, ancestor.directory())) {
                            // cycle detected
                            return true;
                        }
                    } catch (IOException | SecurityException x) {
                        // ignore
                    }
                }
            }
            return false;
        }

        /**
         * Visits the given file, returning the {@code Event} corresponding to that
         * visit.
         * <p>
         * The {@code ignoreSecurityException} parameter determines whether
         * any SecurityException should be ignored or not. If a SecurityException
         * is thrown, and is ignored, then this method returns {@code null} to
         * mean that there is no event corresponding to a visit to the file.
         * <p>
         * The {@code canUseCached} parameter determines whether cached attributes
         * for the file can be used or not.
         */
        private FileTreeWalker.Event visit(Path entry, boolean ignoreSecurityException, boolean canUseCached) {
            // need the file attributes
            BasicFileAttributes attrs;
            try {
                attrs = getAttributes(entry, canUseCached);
            } catch (IOException ioe) {
                return new FileTreeWalker.Event(FileTreeWalker.EventType.ENTRY, entry, ioe);
            } catch (SecurityException se) {
                if (ignoreSecurityException)
                    return null;
                throw se;
            }

            // at maximum depth or file is not a directory
            int depth = stack.size();
            if (depth >= maxDepth || !attrs.isDirectory()) {
                return new FileTreeWalker.Event(FileTreeWalker.EventType.ENTRY, entry, attrs);
            }

            // check for cycles when following links
            if (followLinks && wouldLoop(entry, attrs.fileKey())) {
                return new FileTreeWalker.Event(FileTreeWalker.EventType.ENTRY, entry,
                                                new FileSystemLoopException(entry.toString()));
            }

            // file is a directory, attempt to open it
            DirectoryStream<Path> stream = null;
            try {
                stream = Files.newDirectoryStream(entry);
            } catch (IOException ioe) {
                return new FileTreeWalker.Event(FileTreeWalker.EventType.ENTRY, entry, ioe);
            } catch (SecurityException se) {
                if (ignoreSecurityException)
                    return null;
                throw se;
            }

            // push a directory node to the stack and return an event
            stack.push(new FileTreeWalker.DirectoryNode(entry, attrs.fileKey(), stream));
            return new FileTreeWalker.Event(FileTreeWalker.EventType.START_DIRECTORY, entry, attrs);
        }


        /**
         * Start walking from the given file.
         */
        FileTreeWalker.Event walk(Path file) {
            if (closed)
                throw new IllegalStateException("Closed");

            FileTreeWalker.Event ev = visit(file,
                                            false,   // ignoreSecurityException
                                            false);  // canUseCached
            assert ev != null;
            return ev;
        }

        /**
         * Returns the next Event or {@code null} if there are no more events or
         * the walker is closed.
         */
        FileTreeWalker.Event next() {
            FileTreeWalker.DirectoryNode top = stack.peek();
            if (top == null)
                return null;      // stack is empty, we are done

            // continue iteration of the directory at the top of the stack
            FileTreeWalker.Event ev;
            do {
                Path entry = null;
                IOException ioe = null;

                // get next entry in the directory
                if (!top.skipped()) {
                    Iterator<Path> iterator = top.iterator();
                    try {
                        if (iterator.hasNext()) {
                            entry = iterator.next();
                        }
                    } catch (DirectoryIteratorException x) {
                        ioe = x.getCause();
                    }
                }

                // no next entry so close and pop directory, creating corresponding event
                if (entry == null) {
                    try {
                        top.stream().close();
                    } catch (IOException e) {
                        if (ioe != null) {
                            ioe = e;
                        } else {
                            ioe.addSuppressed(e);
                        }
                    }
                    stack.pop();
                    return new FileTreeWalker.Event(FileTreeWalker.EventType.END_DIRECTORY, top.directory(), ioe);
                }

                // visit the entry
                ev = visit(entry,
                           true,   // ignoreSecurityException
                           true);  // canUseCached

            } while (ev == null);

            return ev;
        }

        /**
         * Pops the directory node that is the current top of the stack so that
         * there are no more events for the directory (including no END_DIRECTORY)
         * event. This method is a no-op if the stack is empty or the walker is
         * closed.
         */
        void pop() {
            if (!stack.isEmpty()) {
                FileTreeWalker.DirectoryNode node = stack.pop();
                try {
                    node.stream().close();
                } catch (IOException ignore) { }
            }
        }

        /**
         * Skips the remaining entries in the directory at the top of the stack.
         * This method is a no-op if the stack is empty or the walker is closed.
         */
        void skipRemainingSiblings() {
            if (!stack.isEmpty()) {
                stack.peek().skip();
            }
        }

        /**
         * Returns {@code true} if the walker is open.
         */
        boolean isOpen() {
            return !closed;
        }

        /**
         * Closes/pops all directories on the stack.
         */
        @Override
        public void close() {
            if (!closed) {
                while (!stack.isEmpty()) {
                    pop();
                }
                closed = true;
            }
        }
    }


    public interface BasicFileAttributesHolder {

        /**
         * Returns cached attributes (may be null). If file is a symbolic link then
         * the attributes are the link attributes and not the final target of the
         * file.
         */
        BasicFileAttributes get();

        /**
         * Invalidates cached attributes
         */
        void invalidate();
    }

}