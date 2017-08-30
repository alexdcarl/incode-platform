#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package org.incode.domainapp.example.app;

import java.util.List;
import java.util.Map;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.domainapp.example.app.fixtures.RecreateDemoFixtures;
import org.incode.domainapp.example.app.seed.security.SeedSuperAdministratorRoleAndSvenSuperUser;

public class DomainAppAppManifestWithExampleModulesAndFixtures extends DomainAppAppManifestWithExampleModules {

    @Override protected void overrideFixtures(final List<Class<? extends FixtureScript>> fixtureScripts) {
        fixtureScripts.add(RecreateDemoFixtures.class);
        fixtureScripts.add(SeedSuperAdministratorRoleAndSvenSuperUser.class);
    }

    @Override
    protected void overrideConfigurationProperties(final Map<String, String> configurationProperties) {
        disableAuditingAndCommandAndPublishGlobally(configurationProperties);
    }

    protected void disableAuditingAndCommandAndPublishGlobally(final Map<String, String> configurationProperties) {
        configurationProperties.put("isis.services.audit.objects","none");
        configurationProperties.put("isis.services.command.actions","none");
        configurationProperties.put("isis.services.publish.objects","none");
        configurationProperties.put("isis.services.publish.actions","none");
        configurationProperties.put("isis.services.publish.properties","none");
    }
}
