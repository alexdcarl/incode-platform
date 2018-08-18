package org.incode.domainapp.extended.webapp.togglz;

import org.isisaddons.module.togglz.glue.spi.TogglzModuleFeatureManagerProviderAbstract;

/**
 * Registered in META-INF/services, as per http://www.togglz.org/documentation/advanced-config.html
 */
public class CustomTogglzModuleFeatureManagerProvider extends TogglzModuleFeatureManagerProviderAbstract {

    public CustomTogglzModuleFeatureManagerProvider() {
        super(TogglzFeature.class);
    }

}