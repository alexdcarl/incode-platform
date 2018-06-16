package org.incode.domainapp.extended.integtests.spi.security.integtests.permission;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.isisaddons.module.security.dom.permission.ApplicationPermissionRepository;
import org.incode.domainapp.extended.module.fixtures.per_cpt.spi.security.fixture.SecurityModuleAppTearDown;
import org.incode.domainapp.extended.integtests.spi.security.SecurityModuleAppIntegTestAbstract;

public class ApplicationPermissions_IntegTest extends SecurityModuleAppIntegTestAbstract {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpData() throws Exception {
        scenarioExecution().install(new SecurityModuleAppTearDown());
    }

    @Inject
    ApplicationPermissionRepository applicationPermissionRepository;

    public static class Xxx extends ApplicationPermissions_IntegTest {

        @Ignore("TODO")
        @Test
        public void happyCase() throws Exception {

            // when

            // then

        }
    }

}