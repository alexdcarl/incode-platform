package org.incode.module.base.dom;

import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.incode.module.base.dom.with.WithInterval;
import org.incode.module.base.dom.with.WithIntervalContiguous;

public interface Chained<T extends Chained<T>> {

    
    /**
     * The object (usually an {@link WithInterval}, but not necessarily) that precedes this one, if any (not
     * necessarily contiguously)..
     * 
     * <p>
     * Implementations where successive intervals are contiguous should instead implement 
     * {@link WithIntervalContiguous}.
     */
    @Property(editing = Editing.DISABLED, optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.ALL_TABLES)
    public T getPrevious();

    /**
     * The object (usually an {@link WithInterval}, but not necessarily) that succeeds this one, if any (not 
     * necessarily contiguously).
     * 
     * <p>
     * Implementations where successive intervals are contiguous should instead implement 
     * {@link WithIntervalContiguous}.
     */
    @Property(editing = Editing.DISABLED, optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.ALL_TABLES)
    public T getNext();
    
    
}
