/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.depends;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/*
 * Could improve the performance of this by incorporating cycle detection with
 * 'the tortoise and the hare' algorithm.
 *
 */
public class DependencyManager<T> {

    public enum Direction {
        TOWARDS_BASE,TOWARDS_TIP
    }

    public enum Order {
        BASE_FIRST,TIP_FIRST
    }

    private final Map<T,ReverseDependency<T>> index = new HashMap<>();

    public DependencyManager(final Map<T,ReverseDependency<T>> index) {
        super();
        this.index.putAll(index);
    }

    public Set<T> all() {
        return new HashSet<>(this.index.keySet());
    }

    public List<T> generateOrderedList(final Direction dir, final Order order) throws CircularDependencyException {
        return this.orderedListFor(this.index.values(),dir,order);
    }

    public List<T> generateOrderedListSubset(final T start, final boolean includeTarget, final Direction dir, final Order order) throws CircularDependencyException, MissingReverseDependencyException {
        return this.orderedListFor(this.subset(start,includeTarget,dir),dir,order);
    }

    public Set<ReverseDependency<T>> subset(final T start, final boolean includeStart, final Direction dir) throws MissingReverseDependencyException {
        final Set<ReverseDependency<T>> found = new HashSet<>();
        final Stack<ReverseDependency<T>> hunt = new Stack<>();

        final ReverseDependency<T> first = this.index.get(start);
        if (first == null) throw new MissingReverseDependencyException(start);
        hunt.push(first);

        while (!hunt.isEmpty()) {
            final ReverseDependency<T> current = hunt.pop();
            if (!found.contains(current)) {
                if (dir == Direction.TOWARDS_BASE) scan(hunt,current.dependency().dependencies());
                else scan(hunt,current.reverseDependants());
            }
            if (includeStart || (!start.equals(current.dependency().target()))) found.add(current);
        }

        return found;
    }

    private List<T> orderedListFor(final Collection<ReverseDependency<T>> subset, final Direction dir, final Order order) throws CircularDependencyException {

        final List<T> checked = new ArrayList<>();
        final List<T> ordered = new ArrayList<>();

        final List<ReverseDependency<T>> clone = new ArrayList<>(subset);
        final List<ReverseDependency<T>> removals = new ArrayList<>();

        while (!clone.isEmpty()) {

            final int sizeAtStart = clone.size();

            for (final ReverseDependency<T> target : clone) {

                final List<T> search = (dir == Direction.TOWARDS_BASE) ? target.dependency().dependencies() : target.reverseDependants();

                if (search.isEmpty() || checked.containsAll(search)) {
                    ordered.add(target.dependency().target());
                    checked.add(target.dependency().target());
                    removals.add(target);
                }

            }

            clone.removeAll(removals);
            removals.clear();

            final int sizeAtEnd = clone.size();
            if (sizeAtEnd == sizeAtStart) throw new CircularDependencyException(removeIrrelevant(clone));
        }

        if ( (dir == Direction.TOWARDS_BASE) && (order == Order.TIP_FIRST) ||
                (dir == Direction.TOWARDS_TIP) && (order == Order.BASE_FIRST)) Collections.reverse(ordered);

        return ordered;
    }

    private Set<Dependency<?>> removeIrrelevant(List<ReverseDependency<T>> clone) {
        final Set<Dependency<T>> remaining = this.dependenciesFrom(clone);
        final Set<T> dependsOnly = this.getAllDependedUpons(remaining);
        return this.removeIrrelevant(remaining,dependsOnly);
    }

    private Set<Dependency<T>> dependenciesFrom(final Collection<ReverseDependency<T>> list) {
        final Set<Dependency<T>> circles = new HashSet<>();

        for (ReverseDependency<T> info : list) {
            circles.add(info.dependency());
        }

        return circles;
    }

    private Set<T> getAllDependedUpons(final Collection<Dependency<T>> list) {
        final Set<T> depends = new HashSet<>();

        for (Dependency<T> info : list) {
            depends.addAll(info.dependencies());
        }

        return depends;
    }

    private Set<Dependency<?>> removeIrrelevant(final Collection<Dependency<T>> list, final Set<T> depends) {
        final Set<Dependency<?>> s = new HashSet<>();

        for (Dependency<T> t : list) {
            if (depends.contains(t.target())) s.add(t);
        }

        return s;
    }

    private void scan(final Stack<ReverseDependency<T>> hunt, final List<T> scan) {
        for (final T t : scan) {
            if (this.index.containsKey(t)) hunt.add(this.index.get(t));
        }
    }

}
