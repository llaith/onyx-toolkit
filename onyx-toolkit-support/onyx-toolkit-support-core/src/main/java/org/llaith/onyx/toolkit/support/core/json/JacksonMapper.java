package org.llaith.onyx.toolkit.support.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.io.IOException;
import java.util.Collection;

import static java.lang.String.format;
import static org.llaith.onyx.toolkit.util.lang.ToStringUtil.asString;

/**
 *
 */
public class JacksonMapper implements JsonMapper {

    private final ObjectMapper objectMapper;

    public JacksonMapper() {
        this(new ObjectMapper().registerModule(new ParameterNamesModule()));
    }

    public JacksonMapper(final Collection<Module> modules) {
        this(new ObjectMapper().registerModules(modules));
    }

    public JacksonMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public String toJson(final Object o) {

        try {

            return this.objectMapper.writeValueAsString(Guard.notNull(o));

        } catch (JsonProcessingException e) {

            throw UncheckedException.wrap(format("Failed to serialize-to-json the object: %s", asString(o)), e);

        }

    }

    @Override
    public <X> X fromJson(final Class<X> klass, final String s) {

        try {

            return this.objectMapper.readValue(s, klass);

        } catch (IOException e) {

            throw UncheckedException.wrap(format("Failed to serialize-from-json the string: %s", s), e);

        }


    }

}
