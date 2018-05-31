/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.core.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.Properties;

/**
 *
 */
public class MailSystem {

    private static Logger logger = LoggerFactory.getLogger(MailSystem.class);

    private final Session session;

    public MailSystem(final Properties props) {

        this.session = Session.getInstance(props, null);

    }

    public MailSession connect(final MailSession.Login login) throws MessagingException {

        return new MailSession(
                this.session,
                login);

    }

}
