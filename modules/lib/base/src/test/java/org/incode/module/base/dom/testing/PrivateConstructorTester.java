package org.incode.module.base.dom.testing;

import java.lang.reflect.Constructor;

public final class PrivateConstructorTester {

    private Class<?> cls;

	public PrivateConstructorTester(Class<?> cls) {
		this.cls = cls;
	}

	public void exercise() throws Exception {
        final Constructor<?> constructor = cls.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
