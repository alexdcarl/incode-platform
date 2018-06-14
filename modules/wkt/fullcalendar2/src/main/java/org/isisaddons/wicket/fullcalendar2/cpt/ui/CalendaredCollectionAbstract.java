package org.isisaddons.wicket.fullcalendar2.cpt.ui;

import java.util.Collection;
import java.util.Set;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import net.ftlines.wicket.fullcalendar.Config;
import net.ftlines.wicket.fullcalendar.EventProvider;
import net.ftlines.wicket.fullcalendar.EventSource;
import net.ftlines.wicket.fullcalendar.FullCalendar;
import net.ftlines.wicket.fullcalendar.selector.EventSourceSelector;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.markup.head.IHeaderResponse;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.isis.viewer.wicket.ui.panels.PanelUtil;

/**
 * {@link PanelAbstract Panel} that represents a {@link EntityCollectionModel
 * collection of entity}s rendered using {@link AjaxFallbackDefaultDataTable}.
 */
public abstract class CalendaredCollectionAbstract extends PanelAbstract<EntityCollectionModel> {

    private static final long serialVersionUID = 1L;

    private static final String ID_SELECTOR = "selector";
    private static final String ID_FULL_CALENDAR = "fullCalendar";
    private static final String ID_FEEDBACK = "feedback";

    private final static String[] COLORS = {
        "#63BA68", "#B1ADAC", "#E6CC7F"
    };
    
    public CalendaredCollectionAbstract(final String id, final EntityCollectionModel model) {
        super(id, model);

        buildGui();
    }

    private void buildGui() {

        final EntityCollectionModel model = getModel();
        
        final NotificationPanel feedback = new NotificationPanel(ID_FEEDBACK);
        feedback.setOutputMarkupId(true);
        addOrReplace(feedback);

        final Config config = new Config();
        config.setSelectable(true);
        config.setSelectHelper(false);
        
        final Collection<ObjectAdapter> entityList = model.getObject();
        final Iterable<String> calendarNames = getCalendarNames(entityList);

        int i=0;
        for (final String calendarName: calendarNames) {
            final EventSource namedCalendar = new EventSource();
            namedCalendar.setTitle(calendarName);
            namedCalendar.setEventsProvider(newEventProvider(model, calendarName));
            namedCalendar.setEditable(true);
            String color = COLORS[i++ % COLORS.length];
            namedCalendar.setBackgroundColor(color);
            namedCalendar.setBorderColor(color);
            config.add(namedCalendar);
        }

        config.setAspectRatio(2.5f);
        
        config.getHeader().setLeft("prevYear,prev,next,nextYear, today");
        config.getHeader().setCenter("title");
        config.getHeader().setRight("");

        config.setLoading("function(bool) { if (bool) $(\"#loading\").show(); else $(\"#loading\").hide(); }");

        config.setAllDaySlot(true);
        
        final FullCalendar calendar = new FullCalendarWithEventHandling(ID_FULL_CALENDAR, config, feedback);
        addOrReplace(calendar);

        addOrReplace(new EventSourceSelector(ID_SELECTOR, calendar));
    }

    protected abstract EventProvider newEventProvider(
            final EntityCollectionModel model, final String calendarName);

    protected abstract Set<String> getCalendarNames(final Collection<ObjectAdapter> entityList);
    
    @Override
    protected void onModelChanged() {
        buildGui();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        PanelUtil.renderHead(response, getClass());
    }
}
