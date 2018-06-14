package org.isisaddons.module.security.dom.tenancy;

import org.apache.isis.applib.annotation.Programmatic;
import org.isisaddons.module.security.dom.user.ApplicationUser;

/**
 * Optional SPI interface to be implemented by a domain service, providing an alternative mechanism for evaluating the
 * application tenancy of the object being interacted with (the "what") and optionally also the tenancy of the user
 * making the call (the "who").
 *
 * @see #handles(Class)
 */
public interface ApplicationTenancyEvaluator {

    /**
     * Whether this evaluator can determine the tenancy of the specified domain entity (such as <tt>ToDoItem</tt>)
     * being interacted with (the "what").
     *
     * <p>
     *     This method is also called to determine if the evaluator is also able to determine the tenancy of the
     *     security module's own {@link ApplicationUser}, ie the "who" is
     *     doing the interacting.  If the evaluator does not handle the class, then the fallback behaviour is
     *     to invoke {@link ApplicationUser#getAtPath()}} on the {@link ApplicationUser} and use the
     *     path from that.
     * </p>
     */
    @Programmatic
    boolean handles(Class<?> cls);

    @Programmatic
    String hides(Object domainObject, ApplicationUser applicationUser);

    @Programmatic
    String disables(Object domainObject, ApplicationUser applicationUser);


}
