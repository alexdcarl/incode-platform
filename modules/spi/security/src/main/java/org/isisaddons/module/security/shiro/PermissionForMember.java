package org.isisaddons.module.security.shiro;

import org.apache.shiro.authz.Permission;

import org.apache.isis.core.metamodel.services.appfeat.ApplicationFeatureId;

import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;

/**
 * As created by {@link org.isisaddons.module.security.shiro.PermissionResolverForIsisShiroAuthorizor}, interprets the
 * permission strings formatted by <code>IsisShiroAuthorizor</code>.
 */
class PermissionForMember implements org.apache.shiro.authz.Permission {

    private final ApplicationFeatureId featureId;
    private final ApplicationPermissionMode mode;

    /**
     * Expects in format <code>package:className:methodName:r|w</code>
     */
    public PermissionForMember(String permissionString) {
        final String[] split = permissionString.split("\\:");
        if(split.length == 4) {
            String packageName = split[0];
            String className = split[1];
            String memberName = split[2];
            this.featureId = ApplicationFeatureId.newMember(packageName + "." + className, memberName);

            ApplicationPermissionMode mode = modeFrom(split[3]);
            if(mode != null) {
                this.mode = mode;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid format for permission: " + permissionString + "; expected 'packageName:className:methodName:r|w");
    }

    private static ApplicationPermissionMode modeFrom(String s) {
        if("r".equals(s)) {
            return ApplicationPermissionMode.VIEWING;
        }
        if("w".equals(s)) {
            return ApplicationPermissionMode.CHANGING;
        }
        return null;
    }

    /**
     */
    @Override
    public boolean implies(Permission p) {
        return false;
    }

    ApplicationFeatureId getFeatureId() {
        return featureId;
    }

    ApplicationPermissionMode getMode() {
        return mode;
    }
}
