package org.incode.example.classification.integtests.tests.category;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.incode.domainapp.module.fixtures.per_cpt.examples.classification.fixture.DemoObjectWithAtPath_and_OtherObjectWithAtPath_recreate3;
import org.incode.domainapp.module.fixtures.shared.demowithatpath.dom.DemoObjectWithAtPathMenu;
import org.incode.example.classification.dom.impl.applicability.ApplicabilityRepository;
import org.incode.example.classification.dom.impl.category.Category;
import org.incode.example.classification.dom.impl.category.CategoryRepository;
import org.incode.example.classification.dom.impl.category.taxonomy.Taxonomy;
import org.incode.example.classification.dom.impl.classification.ClassificationRepository;
import org.incode.example.classification.dom.spi.ApplicationTenancyService;
import org.incode.example.classification.integtests.ClassificationModuleIntegTestAbstract;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryRepository_findByParentAndName_IntegTest extends ClassificationModuleIntegTestAbstract {

    @Inject
    ClassificationRepository classificationRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    ApplicabilityRepository applicabilityRepository;

    @Inject
    DemoObjectWithAtPathMenu demoObjectMenu;
    @Inject
    ApplicationTenancyService applicationTenancyService;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new DemoObjectWithAtPath_and_OtherObjectWithAtPath_recreate3(), null);
    }

    @Test
    public void happy_case() {
        // given
        Category parentLarge = categoryRepository.findByReference("LGE");

        // when
        Category childLarge = categoryRepository.findByParentAndName(parentLarge, "Larger");

        // then
        assertThat(childLarge.getReference()).isEqualTo("XL");
        assertThat(childLarge.getParent()).isEqualTo(parentLarge);
    }

    @Test
    public void when_none() {
        // given
        Taxonomy parentFrenchColours = (Taxonomy) categoryRepository.findByReference("FRCOL");

        // when
        Category childFrenchColours = categoryRepository.findByParentAndName(parentFrenchColours, "Nonexisting");

        // then
        assertThat(childFrenchColours).isNull();
    }

}