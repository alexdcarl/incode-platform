package org.incode.module.unittestsupport.dom.with;

import com.google.common.collect.ImmutableMap;

import org.incode.module.base.dom.with.WithTitleComparable;

/**
 * Automatically tests all domain objects implementing {@link WithTitleComparable}.
 * 
 * <p>
 * Any that cannot be instantiated are skipped; manually test using
 * {@link ComparableByTitleContractTester}.
 *
 * @deprecated - use superclass
 */
@Deprecated
public abstract class ComparableByTitleContractTestAbstract_compareTo extends
        org.incode.module.base.dom.with.ComparableByTitleContractTestAbstract_compareTo {

    protected ComparableByTitleContractTestAbstract_compareTo(
            final String packagePrefix,
            final ImmutableMap<Class<?>, Class<?>> noninstantiableSubstitutes) {
        super(packagePrefix, noninstantiableSubstitutes);
    }

}
