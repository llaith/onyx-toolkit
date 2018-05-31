package org.llaith.onyx.toolkit.support.core.validation;


import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 *
 */
public class ValidationUtil {

    public static <T> void throwIfFails(final Set<ConstraintViolation<T>> violations) {

        if (!violations.isEmpty()) {

            logIfFails(violations); // quick and dirty - do properly soon

            throw new UncheckedException("There are validation failure.");

        }

    }

    public static <T> void logIfFails(final Set<ConstraintViolation<T>> violations) {

        for (final ConstraintViolation<T> cv : violations) {

            System.out.println("ValidatationConstraint: " + cv.getConstraintDescriptor()
                                                              .getAnnotation());
            System.out.println("ValidatationConstraint: " + cv.getConstraintDescriptor());
            System.out.println("ValidatationConstraint: " + cv.getMessageTemplate());
            System.out.println("ValidatationConstraint: " + cv.getInvalidValue());
            System.out.println("ValidatationConstraint: " + cv.getLeafBean());
            System.out.println("ValidatationConstraint: " + cv.getRootBeanClass());
            System.out.println("ValidatationConstraint: " + cv.getPropertyPath()
                                                              .toString());
            System.out.println("ValidatationConstraint: " + cv.getMessage());

        }

    }

}
