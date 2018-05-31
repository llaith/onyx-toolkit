/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.repository;

import org.junit.Test;
import org.llaith.onyx.toolkit.repository.impl.DefaultRepositoryBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 */
public class RepositoryTest {

    @Test
    public void indexingWorks() {

        final Set<String> set = new HashSet<String>() {{
            this.add("1");
            this.add("2");
            this.add("3");
        }};

        final Repository<String> repo = new DefaultRepositoryBuilder<String>().addAll(set);
        final RepositoryView<String> view = repo.select(String.class);

        final Map<Integer,String> index = view.collect(toMap(
                Integer::valueOf,
                Function.identity()));

        final Map<Integer,String> desired = new HashMap<Integer,String>() {{
            this.put(1, "1");
            this.put(2, "2");
            this.put(3, "3");
        }};

        assertThat(index, is(equalTo(desired)));

    }

}
