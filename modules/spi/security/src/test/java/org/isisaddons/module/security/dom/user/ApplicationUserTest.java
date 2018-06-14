package org.isisaddons.module.security.dom.user;

import java.util.List;
import com.danhaywood.java.testsupport.coverage.PojoTester;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.apache.isis.core.unittestsupport.comparable.ComparableContractTest_compareTo;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.isisaddons.module.security.dom.FixtureDatumFactories.tenancies;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ApplicationUserTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    ApplicationUser applicationUser;

    @Before
    public void setUp() throws Exception {
        applicationUser = new ApplicationUser();
    }

    public static class Title extends ApplicationUserTest {
        @Test
        public void whenNoFamilyName() throws Exception {
            // given
            applicationUser.setUsername("fred");

            // when, then
            final String title = applicationUser.title();
            assertThat(title, is("fred"));
        }

        @Test
        public void whenFamilyNameAndGivenName() throws Exception {
            // given
            applicationUser.setUsername("fred");
            applicationUser.setFamilyName("Jones");
            applicationUser.setGivenName("Frederick");

            // when, then
            final String title = applicationUser.title();
            assertThat(title, is("Frederick Jones (fred)"));
        }

        @Test
        public void whenFamilyNameAndKnownAs() throws Exception {
            // given
            applicationUser.setUsername("fred");
            applicationUser.setFamilyName("Jones");
            applicationUser.setGivenName("Frederick");
            applicationUser.setKnownAs("Freddy");

            // when, then
            final String title = applicationUser.title();
            assertThat(title, is("Freddy Jones (fred)"));
        }
    }

    public static class UpdateName_and_ValidateUpdateName extends ApplicationUserTest {

        @Test
        public void withFamilyNameAndGivenName() throws Exception {

            // when
            final String reason = applicationUser.validateUpdateName("Jones", "Frederick", null);

            // then
            assertThat(reason, is(nullValue()));

            // and when
            applicationUser.updateName("Jones", "Frederick", null);

            // then
            assertThat(applicationUser.getFamilyName(), is("Jones"));
            assertThat(applicationUser.getGivenName(), is("Frederick"));
            assertThat(applicationUser.getKnownAs(), is(nullValue()));
        }

        @Test
        public void withFamilyNameAndGivenNameAndKnownAs() throws Exception {

            // when
            String reason = applicationUser.validateUpdateName("Jones", "Frederick", "Freddy");

            // then
            assertThat(reason, is(nullValue()));

            // when
            applicationUser.updateName("Jones", "Frederick", "Freddy");

            // then
            assertThat(applicationUser.getFamilyName(), is("Jones"));
            assertThat(applicationUser.getGivenName(), is("Frederick"));
            assertThat(applicationUser.getKnownAs(), is("Freddy"));
        }

        @Test
        public void withNulls() throws Exception {

            // when
            String reason = applicationUser.validateUpdateName(null, null, null);

            // then
            assertThat(reason, is(nullValue()));

            // when
            applicationUser.updateName(null, null, null);

            // then
            assertThat(applicationUser.getFamilyName(), is(nullValue()));
            assertThat(applicationUser.getGivenName(), is(nullValue()));
            assertThat(applicationUser.getKnownAs(), is(nullValue()));
        }

        @Test
        public void withNoFamilyNameButWithGivenName() throws Exception {

            // when
            String reason = applicationUser.validateUpdateName(null, "Frederick", null);

            // then
            assertThat(reason, is("Must provide family name if given name or 'known as' name has been provided."));
        }

        @Test
        public void withNoFamilyNameButWithKnownAs() throws Exception {

            // when
            String reason = applicationUser.validateUpdateName(null, null, "Freddy");

            // then
            assertThat(reason, is("Must provide family name if given name or 'known as' name has been provided."));
        }

        @Test
        public void withFamilyNameAndKnownAsButNoGiven() throws Exception {

            // when
            String reason = applicationUser.validateUpdateName("Jones", null, "Freddy");

            // then
            assertThat(reason, is("Must provide given name if family name has been provided."));
        }
    }

    public static class CompareTo extends ComparableContractTest_compareTo<ApplicationUser> {

        @SuppressWarnings("unchecked")
        @Override
        protected List<List<ApplicationUser>> orderedTuples() {
            return listOf(
                    listOf(
                            newApplicationUser(null),
                            newApplicationUser("X"),
                            newApplicationUser("X"),
                            newApplicationUser("Y")
                    )
            );
        }

        private ApplicationUser newApplicationUser(
                String username) {
            final ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setUsername(username);
            return applicationUser;
        }

    }

    public static class BeanProperties extends ApplicationUserTest {

        @Test
        public void exercise() throws Exception {
            PojoTester.relaxed()
                    .withFixture(tenancies())
                    .exercise(new ApplicationUser());
        }

    }

    public static class MultipleAtPaths extends ApplicationUserTest {

        @Test
        public void all_atPaths_Using_Separator_works() throws Exception {

            // when
            applicationUser.setAtPath("/FRA;/BEL");
            // then
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').get(0), is("/FRA"));
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').get(1), is("/BEL"));

            // when
            applicationUser.setAtPath("/FRA/AT;/BEL/WO;");
            // then
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').size(), is(2));
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').get(0), is("/FRA/AT"));
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').get(1), is("/BEL/WO"));

            // when
            applicationUser.setAtPath("/FRA");
            // then
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').get(0), is("/FRA"));

            // when
            applicationUser.setAtPath("/;");
            // then
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').size(), is(1));
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').get(0), is("/"));

            // when
            applicationUser.setAtPath(null);
            // then
            assertThat(applicationUser.getAllAtPathsUsingSeparator(';').size(), is(0));

        }

        @Test
        public void first_AtPath_Using_Separator_works() throws Exception {

            // when
            applicationUser.setAtPath("/FRA;/BEL");
            // then
            assertThat(applicationUser.getFirstAtPathUsingSeparator(';'), is("/FRA"));

            // when
            applicationUser.setAtPath("/FRA");
            // then
            assertThat(applicationUser.getFirstAtPathUsingSeparator(';'), is("/FRA"));

            // when
            applicationUser.setAtPath("/;");
            // then
            assertThat(applicationUser.getFirstAtPathUsingSeparator(';'), is("/"));

            // when
            applicationUser.setAtPath(null);
            // then
            assertNull(applicationUser.getFirstAtPathUsingSeparator(';'));


        }

    }


}