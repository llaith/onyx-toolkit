/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.registry;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
public class ServiceRegistryFactory {

    private static final Logger logger = getLogger(ServiceRegistryFactory.class);

    /**
     *
     */
    public static class RegistryConstructionException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final Map<Class<?>,String> failures = new HashMap<>();

        public RegistryConstructionException(final String message, final Map<Class<?>,String> failures) {

            super(String.format("%s Failures are: %s", message, failures));

            this.failures.putAll(failures);

        }

        public Map<Class<?>,String> getFailures() {
            return new HashMap<>(failures);
        }

    }


    private final List<ServiceRegistryConfiguration> configurations = new ArrayList<>();


    public ServiceRegistryFactory() {

        super();

    }

    public ServiceRegistryFactory(Collection<ServiceRegistryConfiguration> configurations) {

        this.configurations.addAll(configurations);

    }


    public ServiceRegistryFactory configureWith(final ServiceRegistryConfiguration configurationFactory) {

        this.configurations.add(configurationFactory);

        return this;

    }

    @SuppressWarnings("squid:S134") // logic is clearer as is
    public ServiceRegistry construct() {

        // create the new registry
        final ServiceRegistryImpl registry = new ServiceRegistryImpl();

        // hold failures
        final Map<Class<?>,String> failures = new HashMap<>();

        // get the list to work with
        final List<ServiceRegistryConfiguration> activeConfigurations = this.configurations;

        // create a list to hold successes
        final List<ServiceRegistryConfiguration> ranConfigurations = new ArrayList<>();

        // check we're not asking to initialise a blank list of services
        if (!this.configurations.isEmpty()) {

            // then init looping over them
            while (true) {

                failures.clear();

                // try to init the systems
                for (final ServiceRegistryConfiguration configuration : activeConfigurations) {

                    try {

                        // attempt initialisation
                        configuration.execute(registry);

                        // successful ones are removed
                        ranConfigurations.add(configuration);

                    } catch (RuntimeException e) {

                        logger.debug("Ignoring exception:", e);

                        failures.put(
                                configuration.getClass(),
                                e.getMessage());

                    }

                }

                // check if we are cycling endlessly
                if (ranConfigurations.isEmpty()) {

                    throw new RegistryConstructionException(
                            "Some registry providers could not be initialised.",
                            failures);

                }

                // remove the ones we successfully ran
                activeConfigurations.removeAll(ranConfigurations);

                // clear the ran configs
                ranConfigurations.clear();

                // exit when done
                if (activeConfigurations.isEmpty()) break;
            }

        }

        return registry;

    }

}
