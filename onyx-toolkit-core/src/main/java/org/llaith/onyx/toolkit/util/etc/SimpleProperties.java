package org.llaith.onyx.toolkit.util.etc;

import com.google.common.base.MoreObjects;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A very simple immutable replacement for 'properties', that is simple enough to have
 * equals implemented safely. Always string/string, so number etc will change to string versions.
 */
public class SimpleProperties {

    public static SimpleProperties load(final File file) {


        try (final FileInputStream fis = new FileInputStream(file)) {

            final Properties properties = new Properties();

            properties.load(fis);

            final Stream<Map.Entry<Object,Object>> stream = properties.entrySet().stream();

            final Map<String,String> values = stream.collect(Collectors.toMap(
                    e -> String.valueOf(e.getKey()),
                    e -> String.valueOf(e.getValue())));

            return new SimpleProperties(values);

        } catch (IOException e) {
            throw UncheckedException.wrap(e);
        }

    }

    public static SimpleProperties readHeaderProperties(final Path path) {

        // 100 lines is the default max to scan
        return readHeaderProperties(path, 100);

    }

    public static SimpleProperties readHeaderProperties(final Path path, final long maxLines) {

        // map of properties
        final Map<String,String> props = new HashMap<>();

        // simple properties, matched 'keywords' must be identifiers (no spaces etc)
        final Pattern pattern = Pattern.compile("(#[ \\t]*@([-a-zA-Z0-9._]+):)");

        // open and read header
        try (final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            // we only read first (maxLines) of file
            for (int i = 0; i < maxLines; i++) {

                final String line = reader.readLine();

                final Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {

                    final String found = matcher.group(1);
                    final String key = matcher.group(2);

                    props.put(key, line.substring(found.length()).trim());

                }

            }

            return new SimpleProperties(props);

        } catch (IOException e) {
            throw UncheckedException.wrap(e);
        }

    }

    private final Map<String,String> values;

    public SimpleProperties(final Map<String,String> values) {
        this.values = Collections.unmodifiableMap(new HashMap<>(values));
    }

    public String get(final String key) {

        return this.values.get(key);

    }

    public String expect(final String key) {

        return Guard.notBlankOrNull(this.values.get(key));

    }

    public Path getAsPath(final String key) {

        return Paths.get(this.get(key));

    }

    public Path expectAsPath(final String key) {

        return Paths.get(this.expect(key));

    }

    public boolean contains(final String key) {

        return this.values.containsKey(key);

    }

    public <X> X assemble(final Function<SimpleProperties,X> assembler) {

        return assembler.apply(this);

    }

    public Map<String,String> asMap() {
        return new HashMap<>(this.values);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("values", values)
                          .toString();
    }

}
