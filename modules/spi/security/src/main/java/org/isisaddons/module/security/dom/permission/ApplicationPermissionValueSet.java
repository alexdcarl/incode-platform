package org.isisaddons.module.security.dom.permission;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.core.metamodel.services.appfeat.ApplicationFeatureId;

import org.isisaddons.module.security.SecurityModule;

/**
 * A serializable value object representing a set of (anonymized) {@link org.isisaddons.module.security.dom.permission.ApplicationPermissionValue permission}s.
 *
 * <p>
 *     Intended for value type arithmetic and also for caching.
 * </p>
 */
public class ApplicationPermissionValueSet implements Serializable {

    public static abstract class PropertyDomainEvent<T> extends SecurityModule.PropertyDomainEvent<ApplicationPermissionValueSet, T> {}

    public static abstract class CollectionDomainEvent<T> extends SecurityModule.CollectionDomainEvent<ApplicationPermissionValueSet, T> {}

    public static abstract class ActionDomainEvent extends SecurityModule.ActionDomainEvent<ApplicationPermissionValueSet> {}

    // //////////////////////////////////////

    //region > values
    private final List<ApplicationPermissionValue> values;
    /**
     * Partitions the {@link ApplicationPermissionValue permissions} by feature and within that orders according to their
     * evaluation precedence.
     *
     * <p>
     *     The following sketches out what is stored:
     * </p>
     * <pre>
     *     com.foo.Bar#bip -> ALLOW, CHANGING
     *                     -> ALLOW, VIEWING
     *                     -> VETO, VIEWING
     *                     -> VETO, CHANGING
     *     com.foo.Bar     -> ALLOW, CHANGING
     *                     -> ALLOW, VIEWING
     *                     -> VETO, VIEWING
     *                     -> VETO, CHANGING
     *     com.foo         -> ALLOW, CHANGING
     *                     -> ALLOW, VIEWING
     *                     -> VETO, VIEWING
     *                     -> VETO, CHANGING
     *     com             -> ALLOW, CHANGING
     *                     -> ALLOW, VIEWING
     *                     -> VETO, VIEWING
     *                     -> VETO, CHANGING
     * </pre>
     * 
     * <p>
     *     Note that {@link org.isisaddons.module.security.dom.permission.ApplicationPermissionRule#ALLOW allow} rule
     *     is ordered before {@link org.isisaddons.module.security.dom.permission.ApplicationPermissionRule#VETO veto} rule
     *     meaning that it is checked first and therefore also takes precedence.
     * </p>
     */
    private final Multimap<ApplicationFeatureId, ApplicationPermissionValue> permissionsByFeature = TreeMultimap.create(
            Collections.reverseOrder(ApplicationFeatureId.Comparators.natural()),
            ApplicationPermissionValue.Comparators.natural());

    /**
     * Note that we require PermissionsEvaluationService to be serializable.
     */
    private final PermissionsEvaluationService permissionsEvaluationService;


    //endregion

    //region > constructor
    ApplicationPermissionValueSet(final ApplicationPermissionValue... permissionValues) {
        this(Arrays.asList(permissionValues));
    }
    public ApplicationPermissionValueSet(final Iterable<ApplicationPermissionValue> permissionValues) {
        this(permissionValues, null);
    }
    public ApplicationPermissionValueSet(final Iterable<ApplicationPermissionValue> permissionValues, final PermissionsEvaluationService permissionsEvaluationService) {
        this.values = Collections.unmodifiableList(Lists.newArrayList(permissionValues));
        for (final ApplicationPermissionValue permissionValue : permissionValues) {
            final ApplicationFeatureId featureId = permissionValue.getFeatureId();
            permissionsByFeature.put(featureId, permissionValue);
        }
        this.permissionsEvaluationService =
                permissionsEvaluationService != null
                        ? permissionsEvaluationService
                        : PermissionsEvaluationService.DEFAULT;
    }
    //endregion

    //region > grants, evaluate

    public static class Evaluation {
        private final ApplicationPermissionValue permissionValue;
        private final boolean granted;

        public Evaluation(final ApplicationPermissionValue permissionValue, final boolean granted) {
            this.permissionValue = permissionValue;
            this.granted = granted;
        }

        public ApplicationPermissionValue getCause() {
            return permissionValue;
        }

        public boolean isGranted() {
            return granted;
        }
    }

    @Programmatic
    public boolean grants(final ApplicationFeatureId featureId, final ApplicationPermissionMode mode) {
        return evaluate(featureId, mode).isGranted();
    }

    @Programmatic
    public Evaluation evaluate(
            final ApplicationFeatureId featureId,
            final ApplicationPermissionMode mode) {
        final List<ApplicationFeatureId> pathIds = featureId.getPathIds();
        for (final ApplicationFeatureId pathId : pathIds) {
            final Collection<ApplicationPermissionValue> permissionValues = permissionsByFeature.get(pathId);
            final Evaluation evaluation = permissionsEvaluationService.evaluate(featureId, mode, permissionValues);
            if(evaluation != null) {
                return evaluation;
            }
        }
        return new Evaluation(null, false);
    }

    //endregion



    //region > equals, hashCode, toString
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApplicationPermissionValueSet that = (ApplicationPermissionValueSet) o;

        return !(values != null ? !values.equals(that.values) : that.values != null);

    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "ApplicationPermissionValueSet{" +
                "values=" + values +
                '}';
    }

    //endregion


}
