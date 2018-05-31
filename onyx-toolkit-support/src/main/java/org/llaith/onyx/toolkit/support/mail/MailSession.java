/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 */
public class MailSession {

    private static Logger logger = LoggerFactory.getLogger(MailSession.class);

    public interface Handler<T> {

        T execute(Session session, Store store) throws MessagingException;

    }

    public static class Login {

        public String host;
        public String user;
        public String pass;

        @Override
        public String toString() {
            return "Login{" +
                    ", host='" + host + '\'' +
                    ", user='" + user + '\'' +
                    ", pass='<skipped>'" +
                    '}';
        }

    }

    private final Session session;
    private final Login login;

    MailSession(final Session session, final Login login) {

        this.session = session;
        this.login = login;

    }

    public <X> X execute(final Handler<X> handler) throws MessagingException {

        Store store = null;

        try {

            logger.debug("Connection to mail-store: " + login);

            store = this.session.getStore();

            store.connect(
                    this.login.host,
                    this.login.user,
                    this.login.pass);

            X result = handler.execute(session, store);

            store.close();

            return result;

        } catch (MessagingException e) {

            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e1) {
                    e.addSuppressed(e1);
                }
            }

            throw e;

        }

    }

    public <X> X executeInFolder(
            final String name,
            final FolderSession.Mode mode,
            final boolean expunge,
            final FolderSession.Handler<X> handler) throws MessagingException {

        return this.execute((session1, store) -> new FolderSession(store).execute(name, mode, expunge, handler));

    }

}
