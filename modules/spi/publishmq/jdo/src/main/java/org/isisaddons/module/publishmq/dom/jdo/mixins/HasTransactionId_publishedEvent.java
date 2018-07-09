package org.isisaddons.module.publishmq.dom.jdo.mixins;

import java.util.UUID;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.HasTransactionId;

import org.isisaddons.module.publishmq.PublishMqModule;
import org.isisaddons.module.publishmq.dom.jdo.events.PublishedEvent;
import org.isisaddons.module.publishmq.dom.jdo.events.PublishedEventRepository;


/**
 * This mixin contributes a <tt>publishedEvent</tt> action to any other implementations of
 * {@link HasTransactionId}; that is: audit entries, and commands.  If there is more than
 * one {@link PublishedEvent} then only the first (ie top-most) is returned.
 */
@Mixin
public class HasTransactionId_publishedEvent {

    public static class ActionDomainEvent
            extends PublishMqModule.ActionDomainEvent<HasTransactionId_publishedEvent> { }


    private final HasTransactionId hasTransactionId;
    public HasTransactionId_publishedEvent(final HasTransactionId hasTransactionId) {
        this.hasTransactionId = hasTransactionId;
    }


    @Action(
            semantics = SemanticsOf.SAFE,
            domainEvent = ActionDomainEvent.class
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(name="transactionId", sequence="1")
    public PublishedEvent $$() {
        return findFirstEvent();
    }
    /**
     * Hide if the contributee is a {@link PublishedEvent}
     */
    public boolean hide$$() {
        return (hasTransactionId instanceof PublishedEvent);
    }
    public String disable$$() {
        return findFirstEvent() == null ? "No published event found for transaction Id": null;
    }

    private PublishedEvent findFirstEvent() {
        final UUID transactionId = hasTransactionId.getTransactionId();
        final int sequenceForFirstEvent = 0;
        return publishedEventRepository.findByTransactionIdAndSequence(transactionId, sequenceForFirstEvent);
    }


    @javax.inject.Inject
    PublishedEventRepository publishedEventRepository;


}
