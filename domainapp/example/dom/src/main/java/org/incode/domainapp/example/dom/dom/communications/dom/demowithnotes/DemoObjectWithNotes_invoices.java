package org.incode.domainapp.example.dom.dom.communications.dom.demowithnotes;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import org.incode.domainapp.example.dom.demo.dom.demowithnotes.DemoObjectWithNotes;
import org.incode.domainapp.example.dom.demo.dom.invoice2.DemoInvoice2;
import org.incode.domainapp.example.dom.demo.dom.invoice2.DemoInvoice2Repository;

@Mixin(method = "coll")
public class DemoObjectWithNotes_invoices {
    private final DemoObjectWithNotes demoCustomer;

    public DemoObjectWithNotes_invoices(final DemoObjectWithNotes demoCustomer) {
        this.demoCustomer = demoCustomer;
    }

    public static class DomainEvent extends ActionDomainEvent<DemoObjectWithNotes> {
    }

    @Action(semantics = SemanticsOf.SAFE, domainEvent = DomainEvent.class)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public List<DemoInvoice2> coll() {
        return invoiceRepository.findByCustomer(demoCustomer);
    }

    @Inject
    DemoInvoice2Repository invoiceRepository;
}