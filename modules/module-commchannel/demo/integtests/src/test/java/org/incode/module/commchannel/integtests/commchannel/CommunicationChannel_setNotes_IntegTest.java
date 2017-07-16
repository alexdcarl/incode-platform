package org.incode.module.commchannel.integtests.commchannel;

import java.util.SortedSet;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.AbstractSubscriber;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.module.commchannel.dom.impl.channel.CommunicationChannel;
import org.incode.module.commchannel.dom.impl.channel.CommunicationChannelRepository;
import domainapp.modules.exampledom.module.commchannel.dom.demo.CommChannelDemoObject;
import domainapp.modules.exampledom.module.commchannel.dom.demo.CommChannelDemoObjectMenu;
import domainapp.modules.exampledom.module.commchannel.fixture.CommChannelDemoObjectsTearDownFixture;
import org.incode.module.commchannel.integtests.CommChannelModuleIntegTestAbstract;

import static org.assertj.core.api.Assertions.assertThat;

public class CommunicationChannel_setNotes_IntegTest extends CommChannelModuleIntegTestAbstract {

    @Inject
    CommChannelDemoObjectMenu commChannelDemoObjectMenu;
    @Inject
    CommunicationChannelRepository communicationChannelRepository;

    CommChannelDemoObject fredDemoOwner;
    SortedSet<CommunicationChannel> fredChannels;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new CommChannelDemoObjectsTearDownFixture(), null);

        fredDemoOwner = wrap(commChannelDemoObjectMenu).create("Foo");

        wrap(mixinNewEmailAddress(fredDemoOwner)).$$("fred@gmail.com", "Home Email", "Fred Smith's home email");

        fredChannels = communicationChannelRepository.findByOwner(fredDemoOwner);
    }


    public static class EditIntegrationTest extends CommunicationChannel_setNotes_IntegTest {

        @Test
        public void happy_case() throws Exception {
            final CommunicationChannel communicationChannel = fredChannels.first();
            final String newNotes = fakeDataService.lorem().paragraph();

            wrap(communicationChannel).setNotes(newNotes);

            assertThat(communicationChannel.getNotes()).isEqualTo(newNotes);
        }
    }


    public static class RaisesEventIntegrationTest extends CommunicationChannel_setNotes_IntegTest {

        @DomainService(nature = NatureOfService.DOMAIN)
        public static class TestSubscriber extends AbstractSubscriber {
            CommunicationChannel.NotesDomainEvent ev;

            @Subscribe
            public void on(CommunicationChannel.NotesDomainEvent ev) {
                this.ev = ev;
            }
        }

        @Inject
        TestSubscriber testSubscriber;

        @Test
        public void happy_case() throws Exception {
            final CommunicationChannel channel = fredChannels.first();
            final String newParagraph = fakeDataService.lorem().paragraph();
            wrap(channel).setNotes(newParagraph);

            assertThat(testSubscriber.ev.getSource()).isSameAs(channel);
            assertThat(testSubscriber.ev.getNewValue()).isEqualTo(newParagraph);
        }
    }

}