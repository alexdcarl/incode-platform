package org.incode.domainapp.extended.module.fixtures.shared;

import org.apache.isis.applib.AppManifestAbstract;

/**
 * Used by <code>isis-maven-plugin</code> (build-time validation of the module) and also by module-level integration tests.
 */
public class ExampleDomDemoManifest extends AppManifestAbstract {

    public static final Builder BUILDER = Builder.forModules(
            ExampleDomDemoDomSubmodule.class
    );

    public ExampleDomDemoManifest() {
        super(BUILDER);
    }


}
