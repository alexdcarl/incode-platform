package org.incode.module.classification.integtests.applicability;

import org.incode.module.classification.dom.impl.applicability.ApplicabilityRepository;
import org.incode.module.classification.dom.impl.category.CategoryRepository;
import org.incode.module.classification.dom.impl.classification.ClassificationRepository;
import org.incode.module.classification.dom.spi.ApplicationTenancyService;
import domainapp.modules.exampledom.module.classification.dom.demo.DemoObjectMenu;
import domainapp.modules.exampledom.module.classification.fixture.ClassifiedDemoObjectsFixture;
import org.incode.module.classification.integtests.ClassificationModuleIntegTestAbstract;
import org.junit.Before;
import org.junit.Ignore;

import javax.inject.Inject;

public class Applicability_UiSubscriber_IntegTest extends ClassificationModuleIntegTestAbstract {

    @Inject
    ClassificationRepository classificationRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    ApplicabilityRepository applicabilityRepository;

    @Inject
    DemoObjectMenu demoObjectMenu;
    @Inject
    ApplicationTenancyService applicationTenancyService;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new ClassifiedDemoObjectsFixture(), null);
    }


    @Ignore
    public void override_title_subscriber() {

        // given a service subscribing on Applicability.TitleUiEvent

        // then the title of an applicability should be...

    }

    @Ignore
    public void override_icon_subscriber() {

        // given a service subscribing on Applicability.IconUiEvent

        // then the icon of an applicability should be...

    }

    @Ignore
    public void override_cssClass_subscriber() {

        // given a service subscribing on Applicability.CssClassUiEvent

        // then the CSS class of an applicability should be...

    }


}