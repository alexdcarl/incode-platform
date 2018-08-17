package org.isisaddons.module.togglz.glue.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;

/**
 * Simply access the feature toggles console.
 */
@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "isistogglz.FeatureToggleConsoleAccessor"
)
@DomainServiceLayout(
        menuBar = DomainServiceLayout.MenuBar.TERTIARY,
        menuOrder = "750"
)
public class FeatureToggleConsoleAccessor {

    public static final String KEY_CONSOLE_URL = "isis.services.togglz.FeatureToggleConsoleAccessor.consoleUrl";
    public static final String KEY_CONSOLE_URL_DEFAULT = "http:///togglz";

    public static final String KEY_HIDE_CONSOLE_ACCESSOR_ACTION = "isis.services.togglz.FeatureToggleConsoleAccessor.hideAction";

    private boolean consoleDisable;
    private String consoleUrl;

    @PostConstruct
    public void init(final Map<String, String> properties) {
        this.consoleDisable = getElseFalse(properties, KEY_HIDE_CONSOLE_ACCESSOR_ACTION);
        this.consoleUrl = getElse(properties.get(KEY_CONSOLE_URL), KEY_CONSOLE_URL_DEFAULT);
    }

    private static boolean getElseFalse(final Map<String, String> properties, final String key) {
        final String value = properties.get(key);
        return !Strings.isNullOrEmpty(value) && Boolean.parseBoolean(value);
    }

    private static String getElse(final String value, final String dflt) {
        return Strings.isNullOrEmpty(value) ? dflt : value.trim();
    }

    @Action(
            restrictTo = RestrictTo.PROTOTYPING,
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
        cssClassFa="fa fa-toggle-on"
    )
    @MemberOrder(sequence = "500.10.2")
    public URL featureToggles() {
        try {
            return new URL(consoleUrl);
        } catch (MalformedURLException e) {
            container.warnUser(TranslatableString.tr("Unable to access the Feature Toggle Console"), FeatureToggleConsoleAccessor.class, "featureToggles");
            return null;
        }
    }

    public boolean hideFeatureToggles() {
        return consoleDisable;
    }

    @Inject
    private DomainObjectContainer container;
}
