package org.isisaddons.module.poly.fixture.demoapp.demomodule.dom.fixedasset;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.poly.fixture.demoapp.demomodule.dom.commchannel.PolyDemoCommunicationChannel;
import org.isisaddons.module.poly.fixture.demoapp.poly_casecontent_module.dom.content.PolyDemoCaseContent;
import org.isisaddons.module.poly.fixture.demoapp.poly_ccowner_module.dom.PolyDemoCommunicationChannelOwner;
import org.isisaddons.module.poly.fixture.demoapp.poly_ccowner_module.dom.PolyDemoCommunicationChannelOwnerLink;
import org.isisaddons.module.poly.fixture.demoapp.poly_ccowner_module.dom.PolyDemoCommunicationChannelOwnerLinks;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "libPolyFixture"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.module.poly.fixture.demoapp.demomodule.dom.fixedasset.PolyDemoFixedAsset ")
})
@javax.jdo.annotations.Unique(name="FixedAsset_name_UNQ", members = {"name"})
@DomainObject(
        bounded = true
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class PolyDemoFixedAsset implements PolyDemoCommunicationChannelOwner, PolyDemoCaseContent, Comparable<PolyDemoFixedAsset> {

    //region > identification
    public TranslatableString title() {
        return TranslatableString.tr("{name}", "name", getName());
    }

    //endregion

    //region > name (property)

    private String name;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(
            // contributed 'title' property (from CaseContentContributions) is shown instead on tables
            hidden = Where.ALL_TABLES
    )
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // endregion

    //region > updateName (action)

    public PolyDemoFixedAsset updateName(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "New name")
            final String name) {
        setName(name);
        return this;
    }

    public String default0UpdateName() {
        return getName();
    }

    //endregion


    //region > createCommunicationChannel (contributed action)
    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(name="PolyDemoCommunicationChannel",sequence = "3")
    public PolyDemoCommunicationChannelOwner createCommunicationChannel(
            @ParameterLayout(named = "Details")
            final String details) {

        final PolyDemoCommunicationChannel communicationChannel = container.newTransientInstance(PolyDemoCommunicationChannel.class);
        communicationChannel.setDetails(details);
        container.persist(communicationChannel);
        container.flush();

        communicationChannelOwnerLinks.createLink(communicationChannel, this);
        return this;
    }

    public String disableCreateCommunicationChannel() {
        return getCommunicationChannel() != null? "Already owns a communication channel": null;
    }

    public String validateCreateCommunicationChannel(final String details) {
        return details.contains("!")? "No exclamation marks allowed in details": null;
    }
    //endregion

    //region > deleteCommunicationChannel (contributed action)
    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(name="PolyDemoCommunicationChannel", sequence = "4")
    public PolyDemoCommunicationChannelOwner deleteCommunicationChannel() {

        final PolyDemoCommunicationChannelOwnerLink ownerLink = getCommunicationChannelOwnerLink();
        final PolyDemoCommunicationChannel communicationChannel = getCommunicationChannel();

        container.removeIfNotAlready(ownerLink);
        container.removeIfNotAlready(communicationChannel);

        return this;
    }

    public String disableDeleteCommunicationChannel() {
        return getCommunicationChannelOwnerLink() == null? "Does not own a communication channel": null;
    }
    //endregion


    //region > communicationChannel (derived property)
    public PolyDemoCommunicationChannel getCommunicationChannel() {
        final PolyDemoCommunicationChannelOwnerLink ownerLink = getCommunicationChannelOwnerLink();
        return ownerLink != null? ownerLink.getCommunicationChannel(): null;
    }
    private PolyDemoCommunicationChannelOwnerLink getCommunicationChannelOwnerLink() {
        final List<PolyDemoCommunicationChannelOwnerLink> link = communicationChannelOwnerLinks.findByOwner(this);
        return link.size() == 1? link.get(0): null;
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final PolyDemoFixedAsset other) {
        return ObjectContracts.compare(this, other, "name");
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    @javax.inject.Inject
    PolyDemoCommunicationChannelOwnerLinks communicationChannelOwnerLinks;


    //endregion

}
