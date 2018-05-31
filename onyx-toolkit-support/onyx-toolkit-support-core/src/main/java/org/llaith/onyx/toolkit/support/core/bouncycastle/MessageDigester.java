package org.llaith.onyx.toolkit.support.core.bouncycastle;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

/**
 *
 */
public class MessageDigester {


    public interface DigesterProgressHandler {

        void reportProgress(final Path path, long count, long total);

    }


    private final DigesterProgressHandler handler;


    public MessageDigester(final DigesterProgressHandler handler) {

        // register the handler
        this.handler = handler;

        // register the BouncyCastleProvider with the Security Manager
        Security.addProvider(new BouncyCastleProvider());

    }

    public List<byte[]> hash(final Path path, final String... names) {

        return calc(hash(path, this.init(asList(notNull(names)))));

    }

    public List<String> hashAndEncode(final Path path, final String... names) {

        return encode(hash(path, names));

    }

    public String[] hashAndEncodeArr(final Path path, final String... names) {

        return hashAndEncode(path, names).toArray(new String[names.length]);

    }

    private List<MessageDigest> init(final List<String> names) {

        try {

            final List<MessageDigest> digests = new ArrayList<>();

            for (final String name : names) {

                digests.add(MessageDigest.getInstance(name));

            }

            return digests;

        } catch (NoSuchAlgorithmException e) {

            throw UncheckedException.wrap(e);

        }

    }

    private List<MessageDigest> hash(final Path path, final List<MessageDigest> digests) {

        final long total = ExceptionUtil.rethrowOrReturn(() -> Files.size(path));

        if (this.handler != null) this.handler.reportProgress(path, 0, total);

        long completed = 0;

        try (final FileInputStream fis = new FileInputStream(path.toFile())) {

            byte[] buffer = new byte[1024 * 1024]; // 1 mb

            int read;

            while ((read = fis.read(buffer)) != -1) {

                for (final MessageDigest digest : digests) {

                    digest.update(buffer, 0, read);

                }

                completed += read;

                if (this.handler != null) this.handler.reportProgress(path, completed, total);

            }

            if (this.handler != null) this.handler.reportProgress(path, completed, total);

            return digests;

        } catch (IOException e) {

            throw UncheckedException.wrap(e);

        }

    }

    private List<byte[]> calc(final List<MessageDigest> digests) {

        final List<byte[]> hashes = new ArrayList<>();

        for (final MessageDigest digest : digests) {

            hashes.add(digest.digest());

        }

        return hashes;

    }

    private List<String> encode(final List<byte[]> digests) {

        final List<String> hashes = new ArrayList<>();

        for (final byte[] digest : digests) {

            hashes.add(new String(Hex.encode(digest)));

        }

        return hashes;

    }

}