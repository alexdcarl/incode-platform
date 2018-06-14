package org.incode.platform.ext.flywaydb.integtests.tests;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;

import org.incode.domainapp.example.dom.demo.dom.demo.DemoObject;
import org.incode.domainapp.example.dom.demo.dom.demo.DemoObjectMenu;
import org.incode.domainapp.example.dom.demo.fixture.teardown.sub.DemoObject_tearDown;

import static org.assertj.core.api.Assertions.assertThat;

public class Smoke_IntegTest extends DomainAppIntegTestAbstract {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    TransactionService transactionService;
    @Inject
    DemoObjectMenu menu;

    @Test
    public void create() throws Exception {

        // given
        fixtureScripts.runFixtureScript(new DemoObject_tearDown(), null);
        transactionService.nextTransaction();


        // when
        List<DemoObject> all = wrap(menu).listAllDemoObjects();

        // then
        assertThat(all).isEmpty();



        // when
        final DemoObject fred = wrap(menu).createDemoObject("Fred");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAllDemoObjects();
        assertThat(all).hasSize(1);
        assertThat(all).contains(fred);



        // when
        final DemoObject bill = wrap(menu).createDemoObject("Bill");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAllDemoObjects();
        assertThat(all).hasSize(2);
        assertThat(all).contains(fred, bill);



    }

}

