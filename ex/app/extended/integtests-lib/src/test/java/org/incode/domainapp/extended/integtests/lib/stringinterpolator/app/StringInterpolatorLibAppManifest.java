package org.incode.domainapp.extended.integtests.lib.stringinterpolator.app;

import org.apache.isis.applib.AppManifestAbstract;

import org.isisaddons.module.stringinterpolator.StringInterpolatorModule;

import org.incode.domainapp.extended.module.fixtures.per_cpt.lib.stringinterpolator.FixturesModuleLibStringInterpolatorSubmodule;

public class StringInterpolatorLibAppManifest extends AppManifestAbstract {

    public static final Builder BUILDER = Builder.forModules(
            StringInterpolatorModule.class,
            FixturesModuleLibStringInterpolatorSubmodule.class
    );

    public StringInterpolatorLibAppManifest() {
        super(BUILDER);
    }

}