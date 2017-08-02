package domainapp.modules.exampledom.spi.security.fixture.example.tenanted;

import domainapp.modules.exampledom.spi.security.dom.demonontenanted.NonTenantedEntities;
import org.apache.isis.applib.fixturescripts.DiscoverableFixtureScript;

public class AllExampleTenantedEntities extends DiscoverableFixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {

        executionContext.executeChild(this, new FrTenantedEntity());
        executionContext.executeChild(this, new ItTenantedEntity());
        executionContext.executeChild(this, new ItMilTenantedEntity());
        executionContext.executeChild(this, new RootTenantedEntity());

    }

    // //////////////////////////////////////

    @javax.inject.Inject
    private NonTenantedEntities exampleNonTenantedEntities;

}