package org.incode.module.base.dom.with;

import java.util.Objects;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.commons.internal.collections._Sets;

import org.incode.module.base.dom.Chained;

public interface WithIntervalContiguous<T extends WithIntervalContiguous<T>> 
        extends WithIntervalMutable<T>, Comparable<T> {

    
    /**
     * The interval that immediately precedes this one, if any.
     * 
     * <p>
     * The predecessor's {@link #getEndDate() end date} is the day before this interval's
     * {@link #getStartDate() start date}.
     * 
     * <p>
     * Implementations where successive intervals are NOT contiguous should instead implement {@link Chained}.
     */
    @Property(editing = Editing.DISABLED, optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.ALL_TABLES)
    public T getPredecessor();

    /**
     * The interval that immediately succeeds this one, if any.
     * 
     * <p>
     * The successor's {@link #getStartDate() start date} is the day after this interval's
     * {@link #getEndDate() end date}.
     * 
     * <p>
     * Implementations where successive intervals are NOT contiguous should instead implement {@link Chained}.
     */
    @Property(editing = Editing.DISABLED, optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.ALL_TABLES)
    public T getSuccessor();
    
    

    /**
     * Show this {@link WithIntervalContiguous} in context with its
     * predecessors and successors.
     * 
     * <p>
     * This will typically (always) be a derived collection obtained
     * by filtering a collection of the "parent".
     */
    @Property(editing = Editing.DISABLED)
    public SortedSet<T> getTimeline();
    

    // //////////////////////////////////////

    
    public interface Factory<T extends WithIntervalContiguous<T>> {
        T newRole(LocalDate startDate, LocalDate endDate);
    }
    
    /**
     * Helper class for implementations to delegate to.
     */
    public static class Helper<T extends WithIntervalContiguous<T>> {
        
        private final T withInterval;
        public Helper(final T withInterval) {
            this.withInterval = withInterval;
        }

        // //////////////////////////////////////

        public T succeededBy(
                final LocalDate startDate, 
                final LocalDate endDate,
                final WithIntervalContiguous.Factory<T> factory) {
            final WithInterval<?> successor = withInterval.getSuccessor();
            if(successor != null) {
                successor.setStartDate(dayAfterElseNull(endDate));
            }
            withInterval.setEndDate(dayBeforeElseNull(startDate));
            return factory.newRole(startDate, endDate);
        }

        public LocalDate default1SucceededBy() {
            return dayAfterElseNull(withInterval.getEndDate());
        }

        public String validateSucceededBy(
                final LocalDate startDate, 
                final LocalDate endDate) {
            if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
                return "End date cannot be earlier than start date";
            }
            if(withInterval.getStartDate() != null && !withInterval.getStartDate().isBefore(startDate)) {
                return "Successor must start after existing";
            }
            final WithInterval<?> successor = withInterval.getSuccessor();
            if(successor != null) {
                if (endDate == null) {
                    return "An end date is required because a successor already exists";
                }
                if(successor.getEndDate() != null && !endDate.isBefore(successor.getEndDate())) {
                    return "Successor must end prior to existing successor";
                }
            }
            return null;
        }
        
        // //////////////////////////////////////


        public T precededBy(
                final LocalDate startDate, 
                final LocalDate endDate,
                final WithIntervalContiguous.Factory<T> factory) {
            
            final WithInterval<?> predecessor = withInterval.getPredecessor();
            if(predecessor != null) {
                predecessor.setEndDate(dayBeforeElseNull(startDate));
            }
            withInterval.setStartDate(dayAfterElseNull(endDate));
            return factory.newRole(startDate, endDate);
        }
         
        public LocalDate default2PrecededBy() {
            return dayBeforeElseNull(withInterval.getStartDate());
        }

        public String validatePrecededBy(
                final LocalDate startDate, 
                final LocalDate endDate) {
            if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
                return "End date cannot be earlier than start date";
            }
            if(withInterval.getEndDate() != null && !withInterval.getEndDate().isAfter(endDate)) {
                return "Predecessor must end before existing";
            }
            final WithInterval<?> predecessor = withInterval.getPredecessor();
            if(predecessor != null) {
                if (startDate == null) {
                    return "A start date is required because a predecessor already exists";
                }
                if(predecessor.getStartDate() != null && !startDate.isAfter(predecessor.getStartDate())) {
                    return "Predecessor must start after existing predecessor";
                }
            }
            return null;
        }


        // //////////////////////////////////////

        @Programmatic
        public T getPredecessor(final SortedSet<T> siblings, final Predicate<T> filter) {
            return WithInterval.Util.firstElseNull(
                    siblings,
                    filter.and(endDatePreceding(withInterval.getStartDate())))
                    ;
        }

        @Programmatic
        public T getSuccessor(final SortedSet<T> siblings, final Predicate<T> filter) {
            return WithInterval.Util.firstElseNull(
                    siblings,
                    filter.and(startDateFollowing(withInterval.getEndDate())));

        }

        private Predicate<T> startDateFollowing(final LocalDate date) {
            return ar -> date != null && ar != null && Objects.equals(ar.getStartDate(), date.plusDays(1));
        }

        private Predicate<T> endDatePreceding(final LocalDate date) {
            return ar -> date != null && ar != null && Objects.equals(ar.getEndDate(), date.minusDays(1));
        }

        // //////////////////////////////////////

        @Programmatic
        public SortedSet<T> getTimeline(final SortedSet<T> siblings, final Predicate<? super T> filter) {
            return _Sets.newTreeSet(siblings.stream().filter(filter).collect(Collectors.toSet()));
        }
        
        // //////////////////////////////////////

        public T changeDates(
                final LocalDate startDate, 
                final LocalDate endDate) {
            
            final T predecessor = withInterval.getPredecessor();
            if(predecessor != null) {
                predecessor.setEndDate(dayBeforeElseNull(startDate));
            }
            final T successor = withInterval.getSuccessor();
            if(successor != null) {
                successor.setStartDate(dayAfterElseNull(endDate));
            }
            withInterval.setStartDate(startDate);
            withInterval.setEndDate(endDate);
            return withInterval;
        }

        public LocalDate default0ChangeDates() {
            return withInterval.getEffectiveInterval().startDate();
        }
        
        public LocalDate default1ChangeDates() {
            return withInterval.getEffectiveInterval().endDate();
        }

        public String validateChangeDates(
                final LocalDate startDate, 
                final LocalDate endDate) {

            if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
                return "End date cannot be earlier than start date";
            }
            final T predecessor = withInterval.getPredecessor();
            if (predecessor != null) {
                if(startDate == null) {
                    return "Start date cannot be set to null if there is a predecessor";
                }
                if(predecessor.getStartDate() != null && !predecessor.getStartDate().isBefore(startDate)) {
                    return "Start date cannot be on/before start of current predecessor";
                }
            }
            final T successor = withInterval.getSuccessor();
            if (successor != null) {
                if(endDate == null) {
                    return "End date cannot be set to null if there is a successor";
                }
                if(successor.getEndDate() != null && !successor.getEndDate().isAfter(endDate)) {
                    return "End date cannot be on/after end of current successor";
                }
            }
            return null;
        }
        
        // //////////////////////////////////////

        private static LocalDate dayBeforeElseNull(final LocalDate date) {
            return date!=null?date.minusDays(1):null;
        }
        private static LocalDate dayAfterElseNull(final LocalDate date) {
            return date!=null?date.plusDays(1):null;
        }

    }

}
