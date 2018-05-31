package org.llaith.onyx.toolkit.support.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JacksonSupplier {

    public static <X> Stage1<X> deserialize(final Class<X> klass) {
        return new Holder<>(klass);
    }

    public interface Stage1<T> {

        Stage2<T> withMapper(ObjectMapper mapper);
    }

    public interface Stage2<T> {

        Stage3<T> andJson(final String json);
    }

    public interface Stage3<T> extends Supplier<T> {

        Stage3<T> mutateWith(Consumer<T> mutator);

    }

    public static class Holder<T> implements Stage1<T>, Stage2<T>, Stage3<T> {

        private final Class<T> klass;

        private ObjectMapper mapper;
        private String json;
        private Consumer<T> transform;

        public Holder(final Class<T> klass) {
            this.klass = klass;
        }

        @Override
        public Stage2<T> withMapper(final ObjectMapper mapper) {

            this.mapper = mapper;

            return this;

        }

        @Override
        public Stage3<T> andJson(final String json) {

            this.json = json;

            return this;

        }

        @Override
        public Stage3<T> mutateWith(final Consumer<T> mutator) {

            this.transform = mutator;

            return this;

        }

        @Override
        public T get() {

            try {

                T result = mapper.readValue(json, klass);

                if (transform != null) transform.accept(result);

                return result;

            } catch (IOException e) {

                throw UncheckedException.wrap(e);

            }

        }

    }

}
