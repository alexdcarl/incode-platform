package org.incode.domainapp.example.dom.dom.document.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

import org.incode.domainapp.example.dom.demo.fixture.teardown.DemoModuleTearDown;
import org.incode.module.document.fixture.teardown.DocumentModuleTearDown;

public class DemoModule_and_DocTypesAndTemplates_tearDown extends FixtureScript {

    @Override
    protected void execute(final ExecutionContext executionContext) {

        // paperclip links
        isisJdoSupport.executeUpdate("delete from \"exampleDomDocument\".\"PaperclipForDemoObjectWithUrl\"");

        isisJdoSupport.executeUpdate("delete from \"exampleDomDocument\".\"PaperclipForOtherObject\"");

        // documents
        executionContext.executeChild(this, new DocumentModuleTearDown());

        // demo objects
        executionContext.executeChild(this, new DemoModuleTearDown());

    }


    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}