package org.incode.platform.dom.docfragment.integtests.app;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.domainapp.module.fixtures.per_cpt.examples.docfragment.fixture.DemoCustomer_and_DemoInvoiceWithAtPath_and_fragments_recreate;

/**
 * Run the app but without setting up any fixtures.
 */
public class DocFragmentAppAppManifestWithFixtures extends DocFragmentAppAppManifest {

    @Override protected void overrideFixtures(final List<Class<? extends FixtureScript>> fixtureScripts) {
        fixtureScripts.add(DemoCustomer_and_DemoInvoiceWithAtPath_and_fragments_recreate.class);
    }
}
