package org.incode.module.base.dom.titled;

import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;

import org.incode.module.base.dom.TitledEnum;
import org.incode.module.unittestsupport.dom.titled.TitledEnumContractTester;

public class TitledEnumContractTestAbstract_title {
    protected final String prefix;

    public TitledEnumContractTestAbstract_title(final String prefix) {
        this.prefix = prefix;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void searchAndTest() {
        Reflections reflections = new Reflections(prefix);

        Set<Class<? extends TitledEnum>> subtypes =
                reflections.getSubTypesOf(TitledEnum.class);
        for (Class<? extends TitledEnum> subtype : subtypes) {
            if(!Enum.class.isAssignableFrom(subtype)) {
                continue; // ignore non-enums
            }
            Class<? extends Enum> enumType = (Class<? extends Enum>) subtype;
            new TitledEnumContractTester(enumType).test();
        }
    }
}
