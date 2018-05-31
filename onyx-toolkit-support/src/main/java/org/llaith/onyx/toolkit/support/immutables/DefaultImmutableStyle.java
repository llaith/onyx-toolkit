package org.llaith.onyx.toolkit.support.immutables;

import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.immutables.value.Value.Immutable;

/**
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Style(
        get = {"is*", "get*"}, // Detect 'get' and 'is' prefixes in accessor methods
        init = "set*", // Builder initialization methods will have 'set' prefix
        typeAbstract = {"Abstract*"}, // 'Abstract' prefix will be detected and trimmed
        typeImmutable = "*", // No prefix or suffix for generated immutable type
        builder = "new", // construct builder using 'new' instead of factory method
        build = "create", // rename 'build' method on builder to 'create'
        visibility = ImplementationVisibility.PUBLIC, // Generated class will be always public
        defaults = @Immutable(copy = true)) // Disable copy methods by default
public @interface DefaultImmutableStyle {
    // nothing needed
}
