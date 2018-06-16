package org.incode.domainapp.extended.module.fixtures.per_cpt.examples.communications.contributions;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.example.communications.dom.impl.commchannel.CommunicationChannelOwner_newChannelContributions;

@DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class DemoAppCommunicationChannelOwner_newChannelContributions extends
        CommunicationChannelOwner_newChannelContributions {

    public DemoAppCommunicationChannelOwner_newChannelContributions() {
        super(DemoAppCommunicationChannelOwner_newChannelContributions.class);
    }

}