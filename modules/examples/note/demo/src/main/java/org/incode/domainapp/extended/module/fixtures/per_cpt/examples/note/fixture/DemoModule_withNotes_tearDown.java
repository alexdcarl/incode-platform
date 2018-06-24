package org.incode.domainapp.extended.module.fixtures.per_cpt.examples.note.fixture;

import org.apache.isis.applib.fixturescripts.teardown.TeardownFixtureAbstract2;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class DemoModule_withNotes_tearDown extends TeardownFixtureAbstract2 {

    @Override
    protected void execute(final ExecutionContext executionContext) {

        deleteFrom(NotableLinkForDemoObject.class);
        deleteFrom(NotableLink.class);
        deleteFrom(Note.class);

        executionContext.executeChild(this, new DemoObject_tearDown());
    }


    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}
