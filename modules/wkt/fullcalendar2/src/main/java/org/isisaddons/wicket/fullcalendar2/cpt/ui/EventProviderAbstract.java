package org.isisaddons.wicket.fullcalendar2.cpt.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import org.apache.isis.core.metamodel.adapter.oid.OidMarshaller;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.services.ServicesInjector;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.core.runtime.system.persistence.PersistenceSession;
import org.apache.isis.core.runtime.system.session.IsisSessionFactory;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;

import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarableDereferencingService;

import net.ftlines.wicket.fullcalendar.Event;
import net.ftlines.wicket.fullcalendar.EventNotFoundException;
import net.ftlines.wicket.fullcalendar.EventProvider;

public abstract class EventProviderAbstract implements EventProvider {

    private static final long serialVersionUID = 1L;

    private final Map<String, Event> eventById = Maps.newLinkedHashMap();

    // //////////////////////////////////////

    public EventProviderAbstract(final EntityCollectionModel collectionModel, final String calendarName) {
        createEvents(collectionModel, calendarName);
    }

    private void createEvents(final EntityCollectionModel model, final String calendarName) {
        final Collection<ObjectAdapter> entityList = model.getObject();
        final Iterable<Event> events = Iterables.filter(
                Iterables.transform(entityList, newEvent(calendarName)), NOT_NULL);
        for (final Event event : events) {
            eventById.put(event.getId(), event);
        }
    }

    private Object dereference(final Object domainObject) {
        final List<CalendarableDereferencingService> calendarableDereferencingServices =
                getServicesInjector().lookupServices(
                        CalendarableDereferencingService.class);
        for (final CalendarableDereferencingService dereferencingService : calendarableDereferencingServices) {
            final Object dereferencedObject = dereferencingService.dereference(domainObject);
            if(dereferencedObject != null && dereferencedObject != domainObject) {
                return dereferencedObject;
            }
        }
        return domainObject;
    }

    protected abstract CalendarEvent calendarEventFor(final Object domainObject, final String calendarName);

    private Function<ObjectAdapter, Event> newEvent(final String calendarName) {
        return new Function<ObjectAdapter, Event>() {

            public Event apply(final ObjectAdapter input) {

                final Object domainObject = input.getObject();
                final CalendarEvent calendarEvent = calendarEventFor(domainObject, calendarName);
                if(calendarEvent == null) {
                    return null;
                }

                final Event event = new Event();
                
                final DateTime start = calendarEvent.getDateTime();
                final DateTime end = start;
                event.setStart(start);
                event.setEnd(end);
                event.setAllDay(true);

                final Object dereferencedObject = dereference(domainObject);
                final ObjectAdapter dereferencedAdapter = getPersistenceSession().adapterFor(dereferencedObject);

                final String oidStr = dereferencedAdapter.getOid().enString();
                event.setId(oidStr + "-" + calendarName);

                event.setClassName("fullCalendar2-event-" + calendarName);
                event.setEditable(false);
                event.setPayload(oidStr);
                event.setTitle(calendarEvent.getTitle());

                //event.setBackgroundColor(backgroundColor)
                //event.setBorderColor(borderColor)
                //event.setColor(color)
                //event.setTextColor(textColor)
                //event.setUrl(url)
                
                return event;
            }

        };
    }

    final static Predicate<Event> NOT_NULL = new Predicate<Event>() {
        public boolean apply(Event input) {
            return input != null;
        }
    };

    // //////////////////////////////////////

    public Collection<Event> getEvents(final DateTime start, final DateTime end) {
        final Interval interval = new Interval(start, end);
        final Predicate<Event> withinInterval = new Predicate<Event>() {
            public boolean apply(Event input) {
                return interval.contains(input.getStart());
            }
        };
        final Collection<Event> values = eventById.values();
        return Collections2.filter(values, withinInterval);
    }

    public Event getEventForId(String id) throws EventNotFoundException {
        return eventById.get(id);
    }


    PersistenceSession getPersistenceSession() {
        return getSessionFactory().getCurrentSession().getPersistenceSession();
    }

    ServicesInjector getServicesInjector() {
        return getSessionFactory().getServicesInjector();
    }

    IsisSessionFactory getSessionFactory() {
        return IsisContext.getSessionFactory();
    }


}
