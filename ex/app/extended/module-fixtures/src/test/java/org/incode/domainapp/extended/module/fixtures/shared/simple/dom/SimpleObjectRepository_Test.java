package org.incode.domainapp.extended.module.fixtures.shared.simple.dom;

import java.util.List;

import com.google.common.collect.Lists;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleObjectRepository_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    ServiceRegistry2 mockServiceRegistry;
    
    @Mock
    RepositoryService mockRepositoryService;

    SimpleObjectRepository simpleObjectRepository;

    @Before
    public void setUp() throws Exception {
        simpleObjectRepository = new SimpleObjectRepository();
        simpleObjectRepository.repositoryService = mockRepositoryService;
        simpleObjectRepository.serviceRegistry = mockServiceRegistry;
    }

    public static class Create extends SimpleObjectRepository_Test {

        @Test
        public void happyCase() throws Exception {

            final String someName = "Foobar";

            // given
            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceRegistry).injectServicesInto(with(any(SimpleObject.class)));
                    inSequence(seq);

                    oneOf(mockRepositoryService).persist(with(nameOf(someName)));
                    inSequence(seq);
                }

            });

            // when
            final SimpleObject obj = simpleObjectRepository.create(someName);

            // then
            assertThat(obj).isNotNull();
            assertThat(obj.getName()).isEqualTo(someName);
        }

        private static Matcher<SimpleObject> nameOf(final String name) {
            return new TypeSafeMatcher<SimpleObject>() {
                @Override
                protected boolean matchesSafely(final SimpleObject item) {
                    return name.equals(item.getName());
                }

                @Override
                public void describeTo(final Description description) {
                    description.appendText("has name of '" + name + "'");
                }
            };
        }
    }

    public static class ListAll extends SimpleObjectRepository_Test {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<SimpleObject> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockRepositoryService).allInstances(SimpleObject.class);
                    will(returnValue(all));
                }
            });

            // when
            final List<SimpleObject> list = simpleObjectRepository.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}