/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.core.mail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 */
public class FolderSession {

    public interface Handler<T> {

        T execute(Folder folder) throws MessagingException;
    }

    public enum Mode {

        READ_ONLY(Folder.READ_ONLY), READ_WRITE(Folder.READ_WRITE);

        private final int folderMode;

        Mode(final int folderMode) {this.folderMode = folderMode;}
    }

    private final Store store;

    FolderSession(final Store store) {

        this.store = store;

    }

    public <X> X execute(final String name, final Mode mode, final boolean expunge, FolderSession.Handler<X> handler) throws MessagingException {

        Folder folder = null;

        try {

            folder = this.store.getFolder(name);

            folder.open(mode.folderMode);

            final X result = handler.execute(folder);

            folder.close(expunge);

            return result;

        } catch (MessagingException e) {

            if (folder != null) {
                try {
                    folder.close(expunge);
                } catch (MessagingException e1) {
                    e.addSuppressed(e1);
                }
            }

            throw e;
        }

    }

}
