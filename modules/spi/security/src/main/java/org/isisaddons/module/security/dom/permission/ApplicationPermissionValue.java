package org.isisaddons.module.security.dom.permission;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.core.metamodel.services.appfeat.ApplicationFeatureId;

/**
 * A serializable value object representing an (anonymized)
 * {@link org.isisaddons.module.security.dom.permission.ApplicationPermission}.
 *
 * <p>
 *     Intended for value type arithmetic and also for caching.  No user/role information is held because that information
 *     is not required to perform the arithmetic.
 * </p>
 */
public class ApplicationPermissionValue implements Comparable<ApplicationPermissionValue>, Serializable {

    //region > constructor
    public ApplicationPermissionValue(
            final ApplicationFeatureId featureId,
            final ApplicationPermissionRule rule,
            final ApplicationPermissionMode mode) {
        this.featureId = featureId;
        this.rule = rule;
        this.mode = mode;
    }
    //endregion

    //region > featureId
    private final ApplicationFeatureId featureId;
    @Programmatic
    public ApplicationFeatureId getFeatureId() {
        return featureId;
    }
    //endregion

    //region > rule
    private final ApplicationPermissionRule rule;
    @Programmatic
    public ApplicationPermissionRule getRule() {
        return rule;
    }
    //endregion

    //region > mode
    private final ApplicationPermissionMode mode;
    @Programmatic
    public ApplicationPermissionMode getMode() {
        return mode;
    }
    //endregion

    //region > implies, refutes
    @Programmatic
    public boolean implies(final ApplicationFeatureId featureId, final ApplicationPermissionMode mode) {
        if(getRule() != ApplicationPermissionRule.ALLOW) {
            // only allow rules can imply
            return false;
        }
        if(getMode() == ApplicationPermissionMode.VIEWING && mode == ApplicationPermissionMode.CHANGING) {
            // an "allow viewing" permission does not imply ability to change
            return false;
        }

        // determine if this permission is on the path (ie the feature or one of its parents)
        return onPathOf(featureId);
    }

    @Programmatic
    public boolean refutes(final ApplicationFeatureId featureId, final ApplicationPermissionMode mode) {
        if(getRule() != ApplicationPermissionRule.VETO) {
            // only veto rules can refute
            return false;
        }
        if(getMode() == ApplicationPermissionMode.CHANGING && mode == ApplicationPermissionMode.VIEWING) {
            // an "veto changing" permission does not refute ability to view
            return false;
        }
        // determine if this permission is on the path (ie the feature or one of its parents)
        return onPathOf(featureId);
    }

    private boolean onPathOf(final ApplicationFeatureId featureId) {

        final List<ApplicationFeatureId> pathIds = featureId.getPathIds();
        for (final ApplicationFeatureId pathId : pathIds) {
            if(getFeatureId().equals(pathId)) {
                return true;
            }
        }

        return false;
    }

    //endregion

    //region > Comparators
    public static final class Comparators {
        private Comparators(){}
        public static Comparator<ApplicationPermissionValue> natural() {
            return new ApplicationPermissionValueComparator();
        }

        static class ApplicationPermissionValueComparator implements Comparator<ApplicationPermissionValue>, Serializable {
            @Override
            public int compare(final ApplicationPermissionValue o1, final ApplicationPermissionValue o2) {
                return o1.compareTo(o2);
            }
        }
    }
    //endregion

    //region > equals, hashCode, compareTo, toString

    private final static String propertyNames = "rule, mode, featureId";

    @Override
    public int compareTo(final ApplicationPermissionValue o) {
        return ObjectContracts.compare(this, o, propertyNames);
    }

    @Override
    public boolean equals(final Object o) {
        // not using because trying to be efficient.  Premature optimization?
        // return ObjectContracts.equals(this, obj, propertyNames);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApplicationPermissionValue that = (ApplicationPermissionValue) o;

        if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null) return false;
        if (mode != that.mode) return false;
        if (rule != that.rule) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // not using because trying to be efficient.  Premature optimization?
        // return ObjectContracts.hashCode(this, propertyNames);
        int result = featureId != null ? featureId.hashCode() : 0;
        result = 31 * result + (rule != null ? rule.hashCode() : 0);
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
            return ObjectContracts.toString(this, propertyNames);
    }

    //endregion

}
