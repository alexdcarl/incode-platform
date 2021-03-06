package org.incode.module.settings.integtests.tests;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.incode.module.settings.integtests.SettingsModuleIntegTestAbstract;
import org.incode.module.settings.dom.UserSetting;
import org.incode.module.settings.dom.UserSettingsServiceRW;
import org.incode.module.settings.fixture.ApplicationSetting_and_UserSetting_create5;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserSettingsService_IntegTest extends SettingsModuleIntegTestAbstract {

    @Before
    public void setUpData() throws Exception {
        runFixtureScript(new ApplicationSetting_and_UserSetting_create5());
    }

    @javax.inject.Inject
    private UserSettingsServiceRW userSettingsServiceRW;

    @Test
    public void listAll() throws Exception {

        final List<UserSetting> all = wrap(userSettingsServiceRW).listAll();

        assertThat(all.size(), is(6));
    }

    @Test
    public void listAllFor() throws Exception {

        final List<UserSetting> all = wrap(userSettingsServiceRW).listAllFor("sven");

        assertThat(all.size(), is(5));
    }


}