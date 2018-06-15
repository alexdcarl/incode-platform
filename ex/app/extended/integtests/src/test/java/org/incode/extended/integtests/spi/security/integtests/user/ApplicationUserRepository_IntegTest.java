package org.incode.extended.integtests.spi.security.integtests.user;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDODataStoreException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.isis.applib.value.Password;

import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoleRepository;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserMenu;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;

import org.incode.extended.integtests.spi.security.SecurityModuleAppIntegTestAbstract;

import org.incode.domainapp.extended.module.fixtures.per_cpt.spi.security.fixture.SecurityModuleAppTearDown;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class ApplicationUserRepository_IntegTest extends SecurityModuleAppIntegTestAbstract {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpData() throws Exception {
        scenarioExecution().install(new SecurityModuleAppTearDown());
    }

    @Inject
    ApplicationUserMenu applicationUserMenu;
    @Inject
    ApplicationUserRepository applicationUserRepository;
    @Inject
    ApplicationRoleRepository applicationRoleRepository;

    public static class NewUser extends ApplicationUserRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<ApplicationUser> before = applicationUserMenu.allUsers();
            assertThat(before.size(), is(0));

            // when
            final ApplicationUser applicationUser = applicationUserMenu.newDelegateUser("fred", null, true);
            assertThat(applicationUser.getUsername(), is("fred"));

            // then
            final List<ApplicationUser> after = applicationUserMenu.allUsers();
            assertThat(after.size(), is(1));
        }

        @Test
        public void alreadyExists() throws Exception {

            // then
            expectedExceptions.expect(causedBy(JDODataStoreException.class));

            // given
            applicationUserMenu.newDelegateUser("fred", null, true);

            // when
            applicationUserMenu.newDelegateUser("fred", null, true);
        }
    }

    public static class FindByName extends ApplicationUserRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            applicationUserMenu.newDelegateUser("fred", null, true);
            applicationUserMenu.newDelegateUser("mary", null, true);

            // when
            final ApplicationUser fred = applicationUserRepository.findOrCreateUserByUsername("fred");

            // then
            assertThat(fred, is(not(nullValue())));
            assertThat(fred.getUsername(), is("fred"));
        }

        @Test
        public void whenDoesntMatchWillAutoCreate() throws Exception {

            // given
            applicationUserMenu.newDelegateUser("fred", null, true);
            applicationUserMenu.newDelegateUser("mary", null, true);

            // when
            final ApplicationUser autoCreated = applicationUserRepository.findOrCreateUserByUsername("bill");

            // then
            assertThat(autoCreated, is(not(nullValue())));
            assertThat(autoCreated.getUsername(), is("bill"));
        }

    }

    public static class Find extends ApplicationUserRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            applicationUserMenu.newDelegateUser("fred", null, true);
            final ApplicationUser mary = applicationUserMenu.newDelegateUser("mary", null, true);
            mary.setEmailAddress("mary@example.com");

            // when, then
            assertThat(applicationUserRepository.find("r").size(), is(2));
            assertThat(applicationUserRepository.find("x").size(), is(1));
        }
    }



        public static class AutoComplete extends ApplicationUserRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            applicationUserMenu.newDelegateUser("fred", null, true);
            applicationUserMenu.newDelegateUser("mary", null, true);
            applicationUserMenu.newDelegateUser("bill", null, true);

            // when
            final List<ApplicationUser> after = applicationUserRepository.findMatching("r");

            // then
            assertThat(after.size(), is(2)); // fred and mary
        }
    }

    public static class AllTenancies extends ApplicationUserRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            applicationUserMenu.newDelegateUser("fred", null, true);
            applicationUserMenu.newDelegateUser("mary", null, true);

            // when
            final List<ApplicationUser> after = applicationUserMenu.allUsers();

            // then
            assertThat(after.size(), is(2));
        }
    }

    public static class CloneUser extends ApplicationUserRepository_IntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final ApplicationRole fredsRole = applicationRoleRepository.newRole("role1", "role1");
            final ApplicationUser fred = applicationUserRepository
                    .newLocalUser("fred", new Password("fredPass"), new Password("fredPass"), fredsRole, true,
                            "fred@mail.com");

            // when
            final ApplicationUser fredsClone = applicationUserRepository
                    .newLocalUserBasedOn("fredsClone", new Password("fredClonePass"), new Password("fredClonePass"),
                            fred, true, "fredClone@mail.com");

            // then
            assertThat(fred.getRoles(), is(fredsClone.getRoles()));
        }

        @Test
        public void whenUserHaveDifferentRoles() throws Exception {

            // given
            final ApplicationRole fredsRole = applicationRoleRepository.newRole("role1", "role2");
            final ApplicationUser fred = applicationUserRepository
                    .newLocalUser("fred", new Password("fredPass"), new Password("fredPass"), fredsRole, true,
                            "fred@mail.com");

            // when
            final ApplicationUser fredsClone = applicationUserRepository
                    .newLocalUserBasedOn("fredsClone", new Password("fredClonePass"), new Password("fredClonePass"),
                            fred, true, "fredClone@mail.com");

            // and when
            fredsClone.addRole(applicationRoleRepository.newRole("role2", "role2"));

            // then
            assertNotEquals(fred.getRoles(), fredsClone.getRoles());
        }

    }

}