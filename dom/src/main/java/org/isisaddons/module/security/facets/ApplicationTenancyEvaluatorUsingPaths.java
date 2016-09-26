/*
 *  Copyright 2016~date Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.security.facets;

import org.apache.isis.applib.services.queryresultscache.QueryResultsCache;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyEvaluator;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyPathEvaluator;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import javax.inject.Inject;
import java.util.concurrent.Callable;

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

        // if in same hierarchy
        if (objectTenancyPath.startsWith(userTenancyPath) ||
                userTenancyPath.startsWith(objectTenancyPath)) {
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

        // if user's tenancy "above" object's tenancy in the hierarchy
        if (objectTenancyPath.startsWith(userTenancyPath)) {
            return null;
        }

        return String.format(
                "User with tenancy '%s' is not permitted to edit object with tenancy '%s'",
                userTenancyPath,
                objectTenancyPath);
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
        final ApplicationTenancy userTenancy = applicationUser.getTenancy();
        if (userTenancy == null) {
            return null;
        }
        return userTenancy.getPath();
    }


    @Inject
    QueryResultsCache queryResultsCache;

}
