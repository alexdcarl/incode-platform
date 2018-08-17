package org.isisaddons.module.excel.dom;

import java.util.List;

import org.apache.isis.core.metamodel.services.ServicesInjector;
import org.apache.isis.core.metamodel.services.ServicesInjectorAware;

import org.isisaddons.module.excel.dom.util.Mode;

public class WorksheetSpec {

    /**
     * Maximum supported by Microsoft Excel UI.
     *
     * @see <a href="http://stackoverflow.com/questions/3681868/is-there-a-limit-on-an-excel-worksheets-name-length">this SO answer</a>, for example.
     */
    private static final int SHEET_NAME_MAX_LEN = 31;
    private static final String ROW_HANDLER_SUFFIX = "RowHandler";

    public interface RowFactory<Q> {
        Q create();

        Class<?> getCls();

        static class Default<T> implements RowFactory<T>, ServicesInjectorAware {
            private final Class<T> viewModelClass;
            private ServicesInjector servicesInjector;

            public Default(final Class<T> viewModelClass) {
                this.viewModelClass = viewModelClass;
            }

            @Override
            public T create() {
                try {
                    final T t = viewModelClass.newInstance();
                    servicesInjector.injectServicesInto(t);
                    return t;
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Class<?> getCls() {
                return viewModelClass;
            }

            @Override
            public void setServicesInjector(final ServicesInjector servicesInjector) {
                this.servicesInjector = servicesInjector;
            }
        }

    }

    private final RowFactory<?> factory;
    private final Mode mode;
    private final String sheetName;

    /**
     * @param viewModelClass
     * @param sheetName - must be 31 chars or less
     * @param <T>
     */
    public <T> WorksheetSpec(final Class<T> viewModelClass, String sheetName) {
        this(viewModelClass, sheetName, Mode.STRICT);
    }

    public <T> WorksheetSpec(final Class<T> viewModelClass, String sheetName, final Mode mode) {
        this(new RowFactory.Default<>(viewModelClass), sheetName, mode);
    }

    public <T> WorksheetSpec(final RowFactory<T> factory, String sheetName, final Mode mode) {
        this.factory = factory;
        this.mode = mode;
        if(sheetName == null) {
            throw new IllegalArgumentException("Sheet name must be specified");
        }
        if(isTooLong(sheetName) && hasSuffix(sheetName)) {
            sheetName = prefix(sheetName);
        }
        if(isTooLong(sheetName)) {
            throw new IllegalArgumentException(
                    String.format("Sheet name must be less than 30 characters (was '%s'", sheetName));
        }
        this.sheetName = sheetName;
    }

    public static String prefix(final String sheetName) {
        return sheetName.substring(0, sheetName.lastIndexOf(ROW_HANDLER_SUFFIX));
    }

    public <T> RowFactory<T> getFactory() { return (RowFactory<T>) factory; }

    public String getSheetName() {
        return sheetName;
    }

    public Mode getMode() {
        return mode;
    }

    public static boolean isTooLong(final String sheetName) {
        return sheetName.length() > SHEET_NAME_MAX_LEN;
    }

    public static String trim(final String sheetName) {
        return sheetName.substring(0, SHEET_NAME_MAX_LEN);
    }

    public static boolean hasSuffix(final String sheetName) {
        return sheetName.endsWith(ROW_HANDLER_SUFFIX);
    }

    public interface Matcher {
        /**
         * @return non-null to indicate how the sheet should be handled, otherwise <code>null</code> to ignore
         */
        WorksheetSpec fromSheet(String sheetName);
    }

    public interface Sequencer {
        List<WorksheetSpec> sequence(List<WorksheetSpec> specs);
    }

}
