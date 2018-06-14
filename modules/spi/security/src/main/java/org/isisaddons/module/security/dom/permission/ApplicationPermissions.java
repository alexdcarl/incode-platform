package org.isisaddons.module.security.dom.permission;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.core.metamodel.services.appfeat.ApplicationFeatureId;
import org.apache.isis.core.metamodel.services.appfeat.ApplicationFeatureType;

import org.isisaddons.module.security.SecurityModule;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.user.ApplicationUser;

/**
 * @deprecated - use {@link ApplicationPermissionRepository} or {@link ApplicationPermissionMenu}.
 */
@Deprecated
@DomainService(nature = NatureOfService.DOMAIN)
public class ApplicationPermissions {

    //region > domain event classes

    public static abstract class PropertyDomainEvent<T> extends SecurityModule.PropertyDomainEvent<ApplicationPermissions, T> {}

    public static abstract class CollectionDomainEvent<T> extends SecurityModule.CollectionDomainEvent<ApplicationPermissions, T> {}

    public static abstract class ActionDomainEvent extends SecurityModule.ActionDomainEvent<ApplicationPermissions> {}
    //endregion

    //region > iconName

    public String iconName() {
        return "applicationPermission";
    }

    //endregion

    //region > findByRole (programmatic)

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#findByRoleCached(ApplicationRole)} or {@link ApplicationPermissionRepository#findByRole(ApplicationRole)} instead.
     */
    @Deprecated
    @Programmatic
    public List<ApplicationPermission> findByRole(final ApplicationRole role) {
        return applicationPermissionRepository.findByRole(role);
    }
    //endregion

    //region > findByUser (programmatic)

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#findByUserCached(ApplicationUser)} or {@link ApplicationPermissionRepository#findByUser(ApplicationUser)} instead.
     */
    @Deprecated
    @Programmatic
    public List<ApplicationPermission> findByUser(final ApplicationUser user) {
        return applicationPermissionRepository.findByUser(user);
    }

    //endregion

    //region > findByUserAndPermissionValue (programmatic)
    /**
     * @deprecated - use {@link ApplicationPermissionRepository#findByUserAndPermissionValue(String, ApplicationPermissionValue)} instead.
     */
    @Deprecated
    @Programmatic
    public ApplicationPermission findByUserAndPermissionValue(final String username, final ApplicationPermissionValue permissionValue) {
        return applicationPermissionRepository.findByUserAndPermissionValue(username, permissionValue);
    }
    //endregion

    //region > findByRoleAndRuleAndFeatureType (programmatic)

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#findByRoleAndRuleAndFeatureTypeCached(ApplicationRole, ApplicationPermissionRule, ApplicationFeatureType)} or {@link ApplicationPermissionRepository#findByRoleAndRuleAndFeatureType(ApplicationRole, ApplicationPermissionRule, ApplicationFeatureType)} instead.
     */
    @Deprecated
    @Programmatic
    public List<ApplicationPermission> findByRoleAndRuleAndFeatureType(
            final ApplicationRole role, final ApplicationPermissionRule rule,
            final ApplicationFeatureType type) {
        return applicationPermissionRepository.findByRoleAndRuleAndFeatureType(role, rule, type);
    }
    //endregion

    //region > findByRoleAndRuleAndFeature (programmatic)

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#findByRoleAndRuleAndFeatureCached(ApplicationRole, ApplicationPermissionRule, ApplicationFeatureType, String)} or {@link ApplicationPermissionRepository#findByRoleAndRuleAndFeature(ApplicationRole, ApplicationPermissionRule, ApplicationFeatureType, String)} instead.
     */
    @Deprecated
    @Programmatic
    public ApplicationPermission findByRoleAndRuleAndFeature(final ApplicationRole role, final ApplicationPermissionRule rule, final ApplicationFeatureType type, final String featureFqn) {
        return applicationPermissionRepository.findByRoleAndRuleAndFeature(role, rule, type, featureFqn);
    }
    //endregion

    //region > findByFeature (programmatic)

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#findByFeatureCached(ApplicationFeatureId)} or {@link ApplicationPermissionRepository#findByFeature(ApplicationFeatureId)} instead.
     */
    @Deprecated
    @Programmatic
    public List<ApplicationPermission> findByFeature(final ApplicationFeatureId featureId) {
        return applicationPermissionRepository.findByFeature(featureId);
    }
    //endregion

    //region > newPermission (programmatic)

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#newPermission(ApplicationRole, ApplicationPermissionRule, ApplicationPermissionMode, ApplicationFeatureType, String)} instead.
     */
    @Deprecated
    @Programmatic
    public ApplicationPermission newPermission(
            final ApplicationRole role,
            final ApplicationPermissionRule rule,
            final ApplicationPermissionMode mode,
            final ApplicationFeatureType featureType,
            final String featureFqn) {
        return applicationPermissionRepository.newPermission(role, rule, mode, featureType, featureFqn);
    }

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#newPermissionNoCheck(ApplicationRole, ApplicationPermissionRule, ApplicationPermissionMode, ApplicationFeatureType, String)} instead.
     */
    @Deprecated
    @Programmatic
    public ApplicationPermission newPermissionNoCheck(
            final ApplicationRole role,
            final ApplicationPermissionRule rule,
            final ApplicationPermissionMode mode,
            final ApplicationFeatureType featureType,
            final String featureFqn) {
        return applicationPermissionRepository.newPermissionNoCheck(role, rule, mode, featureType, featureFqn);
    }

    /**
     * @deprecated - use {@link ApplicationPermissionRepository#newPermission(ApplicationRole, ApplicationPermissionRule, ApplicationPermissionMode, String, String, String)} instead.
     */
    @Deprecated
    @Programmatic
    public ApplicationPermission newPermission(
            final ApplicationRole role,
            final ApplicationPermissionRule rule,
            final ApplicationPermissionMode mode,
            final String featurePackage,
            final String featureClassName,
            final String featureMemberName) {
        return applicationPermissionRepository.newPermission(role, rule, mode, featurePackage, featureClassName, featureMemberName);
    }
    //endregion

    //region > allPermission (action)
    public static class AllPermissionsDomainEvent extends ActionDomainEvent {}

    /**
     * @deprecated - use {@link ApplicationPermissionMenu#allPermissions()}
     */
    @Deprecated
    @Action(
            domainEvent=AllPermissionsDomainEvent.class,
            semantics = SemanticsOf.SAFE,
            restrictTo = RestrictTo.PROTOTYPING
    )
    public List<ApplicationPermission> allPermissions() {
        return applicationPermissionRepository.allPermissions();
    }
    //endregion

    //region  > injected
    @Inject
    ApplicationPermissionRepository applicationPermissionRepository;
    //endregion
}
