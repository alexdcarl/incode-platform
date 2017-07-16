package org.incode.module.classification.integtests.category;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.incode.module.classification.dom.impl.applicability.ApplicabilityRepository;
import org.incode.module.classification.dom.impl.category.Category;
import org.incode.module.classification.dom.impl.category.CategoryRepository;
import org.incode.module.classification.dom.impl.category.taxonomy.Taxonomy;
import org.incode.module.classification.dom.impl.classification.ClassificationRepository;
import org.incode.module.classification.dom.spi.ApplicationTenancyService;
import domainapp.modules.exampledom.module.classification.dom.demo.DemoObjectMenu;
import domainapp.modules.exampledom.module.classification.fixture.ClassifiedDemoObjectsFixture;
import domainapp.modules.exampledom.module.classification.fixture.ClassificationDemoAppTearDownFixture;
import org.incode.module.classification.integtests.ClassificationModuleIntegTestAbstract;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryRepository_findByParentCascade_IntegTest extends ClassificationModuleIntegTestAbstract {

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
        fixtureScripts.runFixtureScript(new ClassificationDemoAppTearDownFixture(), null);
        fixtureScripts.runFixtureScript(new ClassifiedDemoObjectsFixture(), null);
    }

    @Test
    public void when_no_grandchildren() {
        // given
        Category parentLarge = categoryRepository.findByReference("LGE");

        // when
        List<Category> childrenLarge = categoryRepository.findByParentCascade(parentLarge);

        // then
        assertThat(childrenLarge).hasSize(3);
        assertThat(childrenLarge).extracting(Category::getFullyQualifiedName)
                .containsOnly(
                        "Sizes/Large/Large",
                        "Sizes/Large/Larger",
                        "Sizes/Large/Largest");
    }

    @Test
    public void when_grandchildren() {
        // given
        Taxonomy parentSizes = (Taxonomy) categoryRepository.findByReference("SIZES");

        // when
        List<Category> childrenSizes = categoryRepository.findByParentCascade(parentSizes);

        // then
        assertThat(childrenSizes).hasSize(9);
        assertThat(childrenSizes).extracting(Category::getFullyQualifiedName)
                .containsOnly(
                        "Sizes/Large",
                        "Sizes/Medium",
                        "Sizes/Small",
                        "Sizes/Large/Largest",
                        "Sizes/Large/Larger",
                        "Sizes/Large/Large",
                        "Sizes/Small/Small",
                        "Sizes/Small/Smaller",
                        "Sizes/Small/Smallest");
    }

}