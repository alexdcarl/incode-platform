package org.incode.module.unittestsupport.dom.matchers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Throwables;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Hamcrest {@link org.hamcrest.Matcher} implementations.
 * 
 */
public final class IsisMatchers {

    private IsisMatchers() {
    }


    @Factory
    public static <T> Matcher<T> anInstanceOf(final Class<T> expected) {
        return new TypeSafeMatcher<T>() {
            @Override
            public boolean matchesSafely(final T actual) {
                return expected.isAssignableFrom(actual.getClass());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("an instance of ").appendValue(expected);
            }
        };
    }

    @Factory
    public static Matcher<String> nonEmptyString() {
        return new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(final String str) {
                return str != null && str.length() > 0;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a non empty string");
            }

        };
    }

    @Factory
    @SuppressWarnings("unchecked")
    public static Matcher<String> nonEmptyStringOrNull() {
        return CoreMatchers.anyOf(nullValue(String.class), nonEmptyString());
    }

    @Factory
    public static Matcher<List<?>> containsElementThat(final Matcher<?> elementMatcher) {
        return new TypeSafeMatcher<List<?>>() {
            @Override
            public boolean matchesSafely(final List<?> list) {
                for (final Object o : list) {
                    if (elementMatcher.matches(o)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("contains element that ").appendDescriptionOf(elementMatcher);
            }
        };
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThan(final T c) {
        return Matchers.greaterThan(c);
    }

    @Factory
    public static Matcher<Class<?>> classEqualTo(final Class<?> operand) {

        class ClassEqualsMatcher extends TypeSafeMatcher<Class<?>> {
            private final Class<?> clazz;

            public ClassEqualsMatcher(final Class<?> clazz) {
                this.clazz = clazz;
            }

            @Override
            public boolean matchesSafely(final Class<?> arg) {
                return clazz == arg;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendValue(clazz);
            }
        }

        return new ClassEqualsMatcher(operand);
    }

    @Factory
    public static Matcher<File> existsAndNotEmpty() {

        return new TypeSafeMatcher<File>() {

            @Override
            public void describeTo(final Description arg0) {
                arg0.appendText("exists and is not empty");
            }

            @Override
            public boolean matchesSafely(final File f) {
                return f.exists() && f.length() > 0;
            }
        };
    }

    @Factory
    public static Matcher<String> matches(final String regex) {
        return new TypeSafeMatcher<String>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("string matching " + regex);
            }

            @Override
            public boolean matchesSafely(final String str) {
                return str.matches(regex);
            }
        };
    }

    @Factory
    public static <X> Matcher<Class<X>> anySubclassOf(final Class<X> cls) {
        return new TypeSafeMatcher<Class<X>>() {

            @Override
            public void describeTo(final Description arg0) {
                arg0.appendText("is subclass of ").appendText(cls.getName());
            }

            @Override
            public boolean matchesSafely(final Class<X> item) {
                return cls.isAssignableFrom(item);
            }
        };
    }

    @Factory
    public static <T> Matcher<List<T>> sameContentsAs(final List<T> expected) {
        return new TypeSafeMatcher<List<T>>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("same sequence as " + expected);
            }

            @Override
            public boolean matchesSafely(final List<T> actual) {
                return actual.containsAll(expected) && expected.containsAll(actual);
            }
        };
    }

    @Factory
    public static <T> Matcher<List<T>> listContaining(final T t) {
        return new TypeSafeMatcher<List<T>>() {
    
            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("list containing ").appendValue(t);
            }
    
            @Override
            public boolean matchesSafely(List<T> arg0) {
                return arg0.contains(t);
            }
        };
    }

    @Factory
    public static <T> Matcher<List<T>> listContainingAll(final T... items) {
        return new TypeSafeMatcher<List<T>>() {

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("has items ").appendValue(items);
                
            }

            @Override
            public boolean matchesSafely(List<T> arg0) {
                return arg0.containsAll(Arrays.asList(items));
            }
        };
    }

    @Factory
    public static Matcher<List<Object>> containsObjectOfType(final Class<?> cls) {
        return new TypeSafeMatcher<List<Object>>() {

            @Override
            public void describeTo(final Description desc) {
                desc.appendText("contains instance of type " + cls.getName());
            }

            @Override
            public boolean matchesSafely(final List<Object> items) {
                for (final Object object : items) {
                    if (cls.isAssignableFrom(object.getClass())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Factory
    public static Matcher<String> startsWith(final String expected) {
        return new TypeSafeMatcher<String>() {

            @Override
            public void describeTo(Description description) {
                description.appendText(" starts with '" + expected + "'");
            }

            @Override
            public boolean matchesSafely(String actual) {
                return actual.startsWith(expected);
            }
        };
    }

    @Factory
    public static Matcher<String> contains(final String expected) {
        return new TypeSafeMatcher<String>() {

            @Override
            public void describeTo(Description description) {
                description.appendText(" contains '" + expected + "'");
            }

            @Override
            public boolean matchesSafely(String actual) {
                return actual.contains(expected);
            }
        };
    }

    
    @Factory
    public static Matcher<File> equalsFile(final File file) throws IOException {
        final String canonicalPath = file.getCanonicalPath();
        return new TypeSafeMatcher<File>() {

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("file '" + canonicalPath + "'");
            }

            @Override
            public boolean matchesSafely(File arg0) {
                try {
                    return arg0.getCanonicalPath().equals(canonicalPath);
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }

    @Factory
    public Matcher<Throwable> causalChainHasMessageWith(final String messageFragment) {
        return new TypeSafeMatcher<Throwable>() {

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("causal chain has message with " + messageFragment);

            }

            @Override
            protected boolean matchesSafely(Throwable arg0) {
                for (Throwable ex : Throwables.getCausalChain(arg0)) {
                    if (ex.getMessage().contains(messageFragment)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }


}
