package org.incode.module.unittestsupport.dom.with;

import com.google.common.collect.ImmutableMap;

import org.incode.module.base.dom.with.WithReferenceComparable;

/**
 * Automatically tests all domain objects implementing {@link WithReferenceComparable}.
 * 
 * <p>
 * Any that cannot be instantiated are skipped; manually test using
 * {@link ComparableByReferenceContractTester}.
 *
 * @deprecated - use superclass
 */
@Deprecated
public abstract class ComparableByReferenceContractTestAbstract_compareTo extends
        org.incode.module.base.dom.with.ComparableByReferenceContractTestAbstract_compareTo {

    protected ComparableByReferenceContractTestAbstract_compareTo(
            final String packagePrefix,
            final ImmutableMap<Class<?>, Class<?>> noninstantiableSubstitutes) {
        super(packagePrefix, noninstantiableSubstitutes);
    }

}
