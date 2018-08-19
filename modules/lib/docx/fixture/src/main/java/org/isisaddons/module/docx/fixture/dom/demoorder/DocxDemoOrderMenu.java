package org.isisaddons.module.docx.fixture.dom.demoorder;

import java.util.List;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

/**
 * As used by DocX.
 */
@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        repositoryFor = DocxDemoOrder.class,
        objectType = "libDocxFixture.DocxDemoOrderMenu"
)
@DomainServiceLayout(
        named = "Dummy",
        menuOrder = "20.3"
)
public class DocxDemoOrderMenu {


    //region > listAll (action)
    // //////////////////////////////////////

    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<DocxDemoOrder> listAllDemoOrders() {
        return container.allInstances(DocxDemoOrder.class);
    }

    //endregion

    //region > create (action)
    // //////////////////////////////////////
    
    @MemberOrder(sequence = "2")
    public DocxDemoOrder createDemoOrder(
            final String orderNumber,
            final String customerName,
            final LocalDate orderDate,
            @Nullable
            final String preferences) {
        final DocxDemoOrder obj = new DocxDemoOrder(orderNumber, customerName, orderDate, preferences);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services
    // //////////////////////////////////////

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion

}
