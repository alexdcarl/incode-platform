package org.incode.module.commchannel.dom.impl.emailaddress;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.module.commchannel.dom.impl.channel.CommunicationChannel;
import org.incode.module.commchannel.dom.impl.channel.CommunicationChannel_owner;
import org.incode.module.commchannel.dom.impl.ownerlink.CommunicationChannelOwnerLink;
import org.incode.module.commchannel.dom.impl.ownerlink.CommunicationChannelOwnerLinkRepository;
import org.incode.module.commchannel.dom.impl.type.CommunicationChannelType;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = EmailAddress.class
)
public class EmailAddressRepository {

    public String getId() {
        return "incodeCommChannel.EmailAddressRepository";
    }

    @Programmatic
    public EmailAddress newEmail(
            final Object owner,
            final String address,
            final String purpose,
            final String notes,
            final LocalDate startDate,
            final LocalDate endDate) {

        final EmailAddress ea = repositoryService.instantiate(EmailAddress.class);
        ea.setType(CommunicationChannelType.EMAIL_ADDRESS);
        ea.setEmailAddress(address);
        owner(ea).setOwner(owner);

        ea.setPurpose(purpose);
        ea.setNotes(notes);
        ea.setStartDate(startDate);
        ea.setEndDate(endDate);

        repositoryService.persist(ea);

        return ea;
    }

    @Programmatic
    public EmailAddress findByEmailAddress(
            final Object owner,
            final String emailAddress) {

        final List<CommunicationChannelOwnerLink> links =
                linkRepository.findByOwnerAndCommunicationChannelType(
                                                            owner, CommunicationChannelType.EMAIL_ADDRESS);
        final Iterable<EmailAddress> emailAddresses =
                Iterables.transform(
                        links,
                        CommunicationChannelOwnerLink.Functions.communicationChannel(EmailAddress.class));
        final Optional<EmailAddress> emailAddressIfFound =
                Iterables.tryFind(emailAddresses, input -> Objects.equals(emailAddress, input.getEmailAddress()));
        return emailAddressIfFound.orNull();
    }

    private CommunicationChannel_owner owner(final CommunicationChannel<?> cc) {
        return factoryService.mixin(CommunicationChannel_owner.class, cc);
    }

    //region > injected services
    @Inject
    CommunicationChannelOwnerLinkRepository linkRepository;
    @Inject
    RepositoryService repositoryService;
    @Inject
    FactoryService factoryService;
    //endregion


}
