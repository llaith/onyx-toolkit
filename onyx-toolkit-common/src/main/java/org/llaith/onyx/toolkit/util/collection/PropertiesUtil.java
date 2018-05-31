package org.llaith.onyx.toolkit.util.collection;


import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class PropertiesUtil {

    public static Map<String,String> fromFileToMap(final File file) {

        return PropertiesUtil.toMap(PropertiesUtil.fromFile(file));

    }


    public static Properties fromFile(final File file) {

        try {

            final Properties properties = new Properties();

            if (!file.exists()) throw new RuntimeException(String.format(
                    "File does not exist. Requested file: [%s], Working dir: [%s]",
                    file,
                    (new java.io.File(".").getCanonicalPath())));

            properties.load(new FileInputStream(file));

            return properties;

        } catch (IOException e) {
            throw UncheckedException.wrap("Cannot open file: " + file, e);
        }

    }

    public static Map<String,String> toMap(final Properties properties) {

        final Map<String,String> map = new HashMap<>();

        for (Map.Entry<Object,Object> entry : properties.entrySet()) {

            map.put(
                    (String)entry.getKey(),
                    (String)entry.getValue());

        }

        return map;

    }

}
