package org.incode.platform.spi.publishmq.integtests.tests;

import java.util.List;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.platform.spi.publishmq.integtests.PublishMqModuleIntegTestAbstract;

import org.incode.domainapp.extended.module.fixtures.per_cpt.spi.publishmq.dom.demo.PublishMqDemoObject;
import org.incode.domainapp.extended.module.fixtures.per_cpt.spi.publishmq.dom.demo.PublishMqDemoObjects;
import org.incode.domainapp.extended.module.fixtures.per_cpt.spi.publishmq.fixture.PublishMqDemoObject_recreate3;


public class PublishMqDemoObjects_IntegTest extends PublishMqModuleIntegTestAbstract {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    private PublishMqDemoObjects publishmqDemoObjects;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new PublishMqDemoObject_recreate3(), null);
    }


    @Test
    public void listAll() throws Exception {

        final List<PublishMqDemoObject> all = wrap(publishmqDemoObjects).listAllPublishMqDemoObjects();
        Assertions.assertThat(all.size()).isEqualTo(3);
        
        PublishMqDemoObject publishmqDemoObject = wrap(all.get(0));
        Assertions.assertThat(publishmqDemoObject.getName()).isEqualTo("Foo");
    }
    
    @Test
    public void create() throws Exception {

        wrap(publishmqDemoObjects).createPublishMqDemoObject("Faz");
        
        final List<PublishMqDemoObject> all = wrap(publishmqDemoObjects).listAllPublishMqDemoObjects();
        Assertions.assertThat(all.size()).isEqualTo(4);
    }

}