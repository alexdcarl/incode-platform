package org.incode.module.base.dom.with;

import org.apache.isis.core.unittestsupport.bidir.Instantiator;

public class InstantiatorForComparableByName implements Instantiator {
    public final Class<? extends WithNameComparable<?>> cls;
    private int i;

    public InstantiatorForComparableByName(Class<? extends WithNameComparable<?>> cls) {
        this.cls = cls;
    }

    @Override
    public Object instantiate() {
        WithNameComparable<?> newInstance;
        try {
            newInstance = cls.newInstance();
            newInstance.setName(""+(++i));
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
