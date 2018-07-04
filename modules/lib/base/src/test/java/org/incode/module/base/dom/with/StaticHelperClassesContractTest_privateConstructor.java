package org.incode.module.base.dom.with;

import org.junit.Test;

import org.incode.module.base.dom.testing.PrivateConstructorTester;

public class StaticHelperClassesContractTest_privateConstructor {

    @Test
    public void cover() throws Exception {
        exercise(WithCodeGetter.ToString.class);
        exercise(WithDescriptionGetter.ToString.class);
        exercise(WithNameGetter.ToString.class);
        exercise(WithReferenceGetter.ToString.class);
        exercise(WithTitleGetter.ToString.class);
    }

    private static void exercise(final Class<?> cls) throws Exception {
        new PrivateConstructorTester(cls).exercise();
    }
}
