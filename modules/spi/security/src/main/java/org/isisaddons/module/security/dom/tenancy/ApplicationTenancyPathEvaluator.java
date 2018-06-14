package org.isisaddons.module.security.dom.tenancy;

import org.apache.isis.applib.annotation.Programmatic;

import org.isisaddons.module.security.dom.user.ApplicationUser;

/**
 * Optional SPI interface to be implemented by a domain service, providing an alternative mechanism for evaluating the
 * application tenancy of the object being interacted with (the "what") and optionally also the tenancy of the user
 * making the call (the "who").
 *
 * @see #handles(Class)
 * @see #applicationTenancyPathFor(Object)
 *
 * @deprecated - replaced by {@link ApplicationTenancyEvaluator}.
 */
@Deprecated
public interface ApplicationTenancyPathEvaluator {

    /**
     * Whether this evaluator can determine the tenancy of the specified domain entity (such as <tt>ToDoItem</tt>)
     * being interacted with (the "what").
     *
     * <p>
     *     This method is also called to determine if the evaluator is also able to determine the tenancy of the
     *     security module's own {@link org.isisaddons.module.security.dom.user.ApplicationUser}, ie the "who" is
     *     doing the interacting.  If the evaluator does not handle the class, then the fallback behaviour is
     *     to invoke {@link ApplicationUser#getAtPath()} getAtPath} on the {@link ApplicationUser} and use the
     *     path from that.
     * </p>
     */
    @Programmatic
    boolean handles(Class<?> cls);

    /**
     * Return the tenancy path for the target domain object being interacted with, either the "what" (a domain entity
     * such as <tt>ToDoItem</tt>, say) and optionally the "who", ie the security module's own
     * {@link org.isisaddons.module.security.dom.user.ApplicationUser}.  This latter depends on the result of the
     * earlier call to {@link #handles(Class)}.
     */
    @Programmatic
    String applicationTenancyPathFor(final Object domainObject);

}
