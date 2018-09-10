/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.depends;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Root is an abstract link
 * Trunk is the first concrete
 * Branch is a concrete/abstract class attached to trunk
 * Leaf is a concrete/abstract class with no children
 *
 * @param <T>
 */
public class DependencyFactory<T> {

    public DependencyFactory() {
        super();
    }

    public DependencyManager<T> calculate(final Collection<Dependency<T>> dependencies) throws UnsatisfiedDependencyException, CircularDependencyException {

        final Map<T,ReverseDependency<T>> index = this.indexTargets(this.addTargets(dependencies));

        this.linkDependants(index);

        this.checkUnsatisfiedDependencies(new HashSet<>(index.values()));

        return new DependencyManager<>(index);

    }

    private Collection<ReverseDependency<T>> addTargets(final Collection<Dependency<T>> dependencies) {
        final Collection<ReverseDependency<T>> targets = new ArrayList<>();
        for (final Dependency<T> dependency : dependencies) {
            targets.add(new ReverseDependency<>(dependency));
        }
        return targets;
    }

    private Map<T,ReverseDependency<T>> indexTargets(final Collection<ReverseDependency<T>> targets) {
        final Map<T,ReverseDependency<T>> index = new HashMap<>();

        for (final ReverseDependency<T> info : targets) {
            index.put(info.dependency().target(),info);
        }

        return index;
    }

    private void linkDependants(final Map<T,ReverseDependency<T>> index) {

        for (final ReverseDependency<T> info : index.values()) {
            this.linkDependant(index,info);
        }

    }

    private void linkDependant(final Map<T,ReverseDependency<T>> index, final ReverseDependency<T> info) {
        for (final T dependency : info.dependency().dependencies()) {
            if (index.containsKey(dependency)) index.get(dependency).reverseDependants().add(info.dependency().target());
        }
    }


    private void checkUnsatisfiedDependencies(final Set<ReverseDependency<T>> targets) throws UnsatisfiedDependencyException {
        final Set<T> all = this.extractDependencies(targets);

        final Set<Dependency<?>> unsatisfied = new HashSet<>();

        for (final ReverseDependency<T> target : targets) {
            if ((!target.dependency().dependencies().isEmpty()) && (!all.containsAll(target.dependency().dependencies())))
                unsatisfied.add(target.dependency());
        }

        if (!unsatisfied.isEmpty()) throw new UnsatisfiedDependencyException(unsatisfied);
    }

    private Set<T> extractDependencies(final Set<ReverseDependency<T>> targets) {
        final Set<T> extracted = new HashSet<>();

        for (ReverseDependency<T> depends : targets) {
            extracted.add(depends.dependency().target());
        }

        return extracted;
    }

}
