package org.isisaddons.module.excel.fixture.demoapp.todomodule.dom;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.user.UserService;

import org.isisaddons.wicket.gmap3.cpt.applib.Location;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "libExcelFixture.ExcelDemoToDoItemMenu"
)
@DomainServiceLayout(
        named = "Dummy",
        menuOrder = "20.7"
)
public class ExcelDemoToDoItemMenu {


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<ExcelDemoToDoItem> toDoItemsNotYetComplete() {
        final List<ExcelDemoToDoItem> items = notYetCompleteNoUi();
        if(items.isEmpty()) {
            messageService.informUser("All to-do items have been completed :-)");
        }
        return items;
    }

    @Programmatic
    public List<ExcelDemoToDoItem> notYetCompleteNoUi() {
        return repositoryService.allMatches(
                new QueryDefault<>(ExcelDemoToDoItem.class,
                        "todo_notYetComplete", 
                        "ownedBy", currentUserName()));
    }

    @Programmatic
    public ExcelDemoToDoItem findToDoItemsByDescription(final String description) {
        return repositoryService.firstMatch(
                new QueryDefault<>(ExcelDemoToDoItem.class,
                        "findByDescription",
                        "description", description,
                        "ownedBy", currentUserName()));
    }



    @Action(semantics = SemanticsOf.SAFE)
    @MemberOrder(sequence = "3")
    public List<ExcelDemoToDoItem> toDoItemsComplete() {
        final List<ExcelDemoToDoItem> items = completeNoUi();
        if(items.isEmpty()) {
            messageService.informUser("No to-do items have yet been completed :-(");
        }
        return items;
    }

    @Programmatic
    public List<ExcelDemoToDoItem> completeNoUi() {
        return repositoryService.allMatches(
            new QueryDefault<>(ExcelDemoToDoItem.class,
                    "todo_complete", 
                    "ownedBy", currentUserName()));
    }



    @MemberOrder(sequence = "40")
    public ExcelDemoToDoItem newToDoItem(
            @Parameter(regexPattern = "\\w[@&:\\-\\,\\.\\+ \\w]*")
            final String description,
            final Category category,
            final Subcategory subcategory,
            @Nullable
            final LocalDate dueBy,
            @Nullable
            final BigDecimal cost) {
        final String ownedBy = currentUserName();
        return newToDoItem(description, category, subcategory, ownedBy, dueBy, cost);
    }
    public Category default1NewToDoItem() {
        return Category.Professional;
    }
    public Subcategory default2NewToDoItem() {
        return Category.Professional.subcategories().get(0);
    }
    public LocalDate default3NewToDoItem() {
        return clockService.now().plusDays(14);
    }
    public List<Subcategory> choices2NewToDoItem(
            final String description, final Category category) {
        return Subcategory.listFor(category);
    }
    public String validateNewToDoItem(
            final String description, 
            final Category category, final Subcategory subcategory, 
            final LocalDate dueBy, final BigDecimal cost) {
        return Subcategory.validate(category, subcategory);
    }


    @Action(semantics = SemanticsOf.SAFE)
    @MemberOrder(sequence = "50")
    public List<ExcelDemoToDoItem> allMyToDoItems() {
        final String currentUser = currentUserName();
        final List<ExcelDemoToDoItem> items = repositoryService.allMatches(ExcelDemoToDoItem.class, ExcelDemoToDoItem.Predicates.thoseOwnedBy(currentUser));
        Collections.sort(items);
        if(items.isEmpty()) {
            messageService.warnUser("No to-do items found.");
        }
        return items;
    }


    @Programmatic
    public List<ExcelDemoToDoItem> autoComplete(@MinLength(1) final String description) {
        return repositoryService.allMatches(
                new QueryDefault<>(ExcelDemoToDoItem.class,
                        "todo_autoComplete", 
                        "ownedBy", currentUserName(), 
                        "description", description));
    }


    @Programmatic // for use by fixtures
    public ExcelDemoToDoItem newToDoItem(
            final String description, 
            final Category category, 
            final Subcategory subcategory,
            final String userName, 
            final LocalDate dueBy, final BigDecimal cost) {
        final ExcelDemoToDoItem toDoItem = new ExcelDemoToDoItem();
        toDoItem.setDescription(description);
        toDoItem.setCategory(category);
        toDoItem.setSubcategory(subcategory);
        toDoItem.setOwnedBy(userName);
        toDoItem.setDueBy(dueBy);
        toDoItem.setCost(cost);

        toDoItem.setLocation(
                new Location(51.5172+random(-0.05, +0.05), 0.1182 + random(-0.05, +0.05)));

        LocalDate today = clockService.now();
        toDoItem.setDueBy(today.plusDays(random(10)-2));

        repositoryService.persist(toDoItem);

        return toDoItem;
    }

    @Programmatic
    public ExcelDemoToDoItem newToDoItem(final String description, final String user) {
        return newToDoItem(description, null, null, user, null, null);
    }

    private static double random(final double from, final double to) {
        return Math.random() * (to-from) + from;
    }

    private static int random(int n) {
        return (int) (Math.random() * n);
    }


    @Programmatic
    public List<ExcelDemoToDoItem> allInstances() {
        return repositoryService.allInstances(ExcelDemoToDoItem.class);
    }
    
    private String currentUserName() {
        return userService.getUser().getName();
    }





    @javax.inject.Inject
    MessageService messageService;

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    UserService userService;

    @javax.inject.Inject
    ClockService clockService;



}
