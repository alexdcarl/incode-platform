package org.incode.module.classification.integtests.category;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.isis.applib.services.wrapper.InvalidException;

import org.incode.module.classification.dom.impl.applicability.ApplicabilityRepository;
import org.incode.module.classification.dom.impl.category.Category;
import org.incode.module.classification.dom.impl.category.CategoryRepository;
import org.incode.module.classification.dom.impl.classification.ClassificationRepository;
import org.incode.module.classification.dom.spi.ApplicationTenancyService;
import domainapp.modules.exampledom.module.classification.dom.demo.DemoObjectMenu;
import domainapp.modules.exampledom.module.classification.fixture.ClassifiedDemoObjectsFixture;
import domainapp.modules.exampledom.module.classification.fixture.ClassificationDemoAppTearDownFixture;
import org.incode.module.classification.integtests.ClassificationModuleIntegTestAbstract;

import static org.assertj.core.api.Assertions.assertThat;

public class Category_addChild_IntegTest extends ClassificationModuleIntegTestAbstract {

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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    Category italianColours;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new ClassificationDemoAppTearDownFixture(), null);
        fixtureScripts.runFixtureScript(new ClassifiedDemoObjectsFixture(), null);
        italianColours = categoryRepository.findByReference("ITACOL");
    }

    @Test
    public void happy_case() {
        // given
        assertThat(italianColours.getChildren()).hasSize(3);

        // when
        wrap(italianColours).addChild("Orange", "ORANGE", null);

        // then
        assertThat(italianColours.getChildren()).hasSize(4);
    }

    @Test
    public void cannot_create_a_child_with_same_name_as_some_other_child() {
        // given
        assertThat(italianColours.getChildren()).extracting(Category::getName)
                .contains("Red");

        // then
        expectedException.expect(InvalidException.class);
        expectedException.expectMessage("There is already a child classification with the name of \'Red\'");

        // when
        wrap(italianColours).addChild("Red", "NEWRED", null);
    }

    @Test
    public void cannot_create_a_child_with_same_reference_as_some_other_child() {
        // given
        assertThat(italianColours.getChildren()).extracting(Category::getReference)
                .contains("GREEN");

        // then
        expectedException.expect(InvalidException.class);
        expectedException.expectMessage("There is already a child classification with the reference of \'GREEN\'");

        //when
        wrap(italianColours).addChild("New Green", "GREEN", null);
    }

}