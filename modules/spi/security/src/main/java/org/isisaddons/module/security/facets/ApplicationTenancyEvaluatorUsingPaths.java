package org.isisaddons.module.security.facets;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.isis.applib.services.queryresultscache.QueryResultsCache;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyEvaluator;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyPathEvaluator;
import org.isisaddons.module.security.dom.user.ApplicationUser;

class ApplicationTenancyEvaluatorUsingPaths implements ApplicationTenancyEvaluator {

    private final ApplicationTenancyPathEvaluator evaluator;
    ApplicationTenancyEvaluatorUsingPaths(final ApplicationTenancyPathEvaluator pathEvaluator) {
        this.evaluator = pathEvaluator;
    }

    @Override
    public boolean handles(Class<?> cls) {
        return evaluator.handles(cls);
    }

    @Override
    public String hides(Object domainObject, ApplicationUser applicationUser) {

        // eg /x/y
        String objectTenancyPath = applicationTenancyPathForCached(domainObject);
        if (objectTenancyPath == null) {
            return null;
        }

        // eg /x  or /x/y/z
        String userTenancyPath = userTenancyPathForCached(applicationUser);
        if (userTenancyPath == null) {
            return "User has no tenancy";
        }

        if (objectVisibleToUser(objectTenancyPath, userTenancyPath)) {
            return null;
        }

        // it's ok to return this info, because it isn't actually rendered (helpful if debugging)
        return String.format(
                "User with tenancy '%s' is not permitted to view object with tenancy '%s'",
                userTenancyPath,
                objectTenancyPath);
    }

    @Override
    public String disables(Object domainObject, ApplicationUser applicationUser) {

        // eg /x/y
        String objectTenancyPath = applicationTenancyPathForCached(domainObject);
        if (objectTenancyPath == null) {
            return null;
        }

        // eg /x  or /x/y/z
        String userTenancyPath = userTenancyPathForCached(applicationUser);
        if (userTenancyPath == null) {
            return "User has no tenancy";
        }

        if (objectEnabledForUser(objectTenancyPath, userTenancyPath)) {
            return null;
        }

        return String.format(
                "User with tenancy '%s' is not permitted to edit object with tenancy '%s'",
                userTenancyPath,
                objectTenancyPath);
    }

    /**
     * Protected visibility so can be overridden if required, eg using wildcard matches.
     */
    protected boolean objectVisibleToUser(String objectTenancyPath, String userTenancyPath) {
        // if in "same hierarchy"
        return objectTenancyPath.startsWith(userTenancyPath) ||
                userTenancyPath.startsWith(objectTenancyPath);
    }


    /**
     * Protected visibility so can be overridden if required, eg using wildcard matches
     */
    protected boolean objectEnabledForUser(String objectTenancyPath, String userTenancyPath) {
        // if user's tenancy "above" object's tenancy in the hierarchy
        return objectTenancyPath.startsWith(userTenancyPath);
    }

    /**
     * Per {@link #applicationTenancyPathFor(Object)}, with result cached for the remainder of the request using the {@link QueryResultsCache}.
     */
    protected String applicationTenancyPathForCached(final Object domainObject) {
        return queryResultsCache.execute(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return applicationTenancyPathFor(domainObject);
            }
        }, TenantedAuthorizationFacetDefault.class, "applicationTenancyPathFor", domainObject);
    }

    protected String applicationTenancyPathFor(final Object domainObject) {
        return evaluator.applicationTenancyPathFor(domainObject);
    }

    /**
     * Per {@link #userTenancyPathFor(ApplicationUser)}, with result cached for the remainder of the request using the {@link QueryResultsCache}.
     */
    protected String userTenancyPathForCached(final ApplicationUser applicationUser) {
        return queryResultsCache.execute(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return userTenancyPathFor(applicationUser);
            }
        }, TenantedAuthorizationFacetDefault.class, "userTenancyPathFor", applicationUser);
    }

    protected String userTenancyPathFor(final ApplicationUser applicationUser) {
        if (evaluator.handles(applicationUser.getClass())) {
            return evaluator.applicationTenancyPathFor(applicationUser);
        }
        return applicationUser.getAtPath();
    }


    @Inject
    QueryResultsCache queryResultsCache;

}
