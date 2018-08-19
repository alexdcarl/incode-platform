package org.isisaddons.module.excel.fixture.demoapp.todomodule.fixturescripts;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class DemoToDoItem_tearDown2 extends FixtureScript {

    private final String user;

    public DemoToDoItem_tearDown2() {
        this(null);
    }

    public DemoToDoItem_tearDown2(String ownedBy) {
        this.user = ownedBy;
    }

    @Override
    public void execute(ExecutionContext executionContext) {

        final String ownedBy = this.user != null ? this.user : userService.getUser().getName();

        isisJdoSupport.executeUpdate(String.format(
                "delete "
                        + "from \"exampleDemo\".\"DemoToDoItemDependencies\" "
                        + "where \"dependingId\" IN "
                        + "(select \"id\" from \"exampleDemo\".\"DemoToDoItem\" where \"ownedBy\" = '%s') ",
                ownedBy));

        isisJdoSupport.executeUpdate(String.format(
                "delete from \"exampleDemo\".\"DemoToDoItem\" "
                        + "where \"ownedBy\" = '%s'", ownedBy));
    }


    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}
