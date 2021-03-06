package org.incode.module.settings.fixture.teardown;

import org.apache.isis.applib.fixturescripts.teardown.TeardownFixtureAbstract2;

import org.incode.module.settings.dom.jdo.ApplicationSettingJdo;
import org.incode.module.settings.dom.jdo.UserSettingJdo;

public class SettingsModule_teardown extends TeardownFixtureAbstract2 {
    @Override protected void execute(final ExecutionContext executionContext) {
        deleteFrom(UserSettingJdo.class);
        deleteFrom(ApplicationSettingJdo.class);
    }
}
