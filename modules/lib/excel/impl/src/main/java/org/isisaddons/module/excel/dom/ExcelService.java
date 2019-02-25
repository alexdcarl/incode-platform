package org.isisaddons.module.excel.dom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.common.collect.Lists;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.isis.applib.RecoverableException;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.applib.value.Blob;

import org.isisaddons.module.excel.dom.util.ExcelServiceImpl;
import org.isisaddons.module.excel.dom.util.Mode;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class ExcelService {

    public static class Exception extends RecoverableException {

        private static final long serialVersionUID = 1L;

        public Exception(final String msg, final Throwable ex) {
            super(msg, ex);
        }

        public Exception(final Throwable ex) {
            super(ex);
        }
    }

    public static final String XSLX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private ExcelServiceImpl excelServiceImpl;

    public ExcelService() {
    }

    @Programmatic
    @PostConstruct
    public void init(final Map<String,String> properties) {
        excelServiceImpl = new ExcelServiceImpl();
        serviceRegistry.injectServicesInto(excelServiceImpl);
    }

    // //////////////////////////////////////

    /**
     * Creates a Blob holding a spreadsheet of the domain objects.
     *
     * <p>
     *     There are no specific restrictions on the domain objects; they can be either persistable entities or
     *     view models.  Do be aware though that if imported back using {@link #fromExcel(Blob, Class, String)},
     *     then new instances are always created.  It is generally better therefore to work with view models than to
     *     work with entities.  This also makes it easier to maintain backward compatibility in the future if the
     *     persistence model changes; using view models represents a stable API for import/export.
     * </p>
     *
     * @param sheetName - must be 31 chars or less
     */
    public <T> Blob toExcel(
            final List<T> domainObjects,
            final Class<T> cls,
            final String sheetName,
            final String fileName) throws ExcelService.Exception {
        return excelServiceImpl.toExcel(domainObjects, cls, sheetName, fileName);
    }
    
    public <T> Blob toExcel(
            final List<T> domainObjects,
            final Class<T> cls,
            final String sheetName,
            final String fileName,
            final InputStream in) throws ExcelService.Exception {
        return excelServiceImpl.toExcel(domainObjects, cls, sheetName, fileName, in);
    }

    @Programmatic
    public <T> Blob toExcel(
            final WorksheetContent worksheetContent,
            final String fileName) throws ExcelService.Exception {
        return excelServiceImpl.toExcel(worksheetContent, fileName);
    }
    
    @Programmatic
    public <T> Blob toExcel(
            final WorksheetContent worksheetContent,
            final String fileName,
            final InputStream in) throws ExcelService.Exception {
        return excelServiceImpl.toExcel(worksheetContent, fileName, in);
    }

    @Programmatic
    public Blob toExcel(
            final List<WorksheetContent> worksheetContents,
            final String fileName) throws ExcelService.Exception {
        return excelServiceImpl.toExcel(worksheetContents, fileName);
    }
    
    @Programmatic
    public Blob toExcel(
            final List<WorksheetContent> worksheetContents,
            final String fileName,
            final InputStream in) throws ExcelService.Exception {
        return excelServiceImpl.toExcel(worksheetContents, fileName, in);
    }    

    @Programmatic
    public <T> Blob toExcelPivot(
            final List<T> domainObjects,
            final Class<T> cls,
            final String fileName) throws ExcelService.Exception {
        return excelServiceImpl.toExcelPivot(domainObjects, cls, fileName);
    }

    public <T> Blob toExcelPivot(
            final List<T> domainObjects,
            final Class<T> cls,
            final String sheetName,
            final String fileName) throws ExcelService.Exception {
        return excelServiceImpl.toExcelPivot(domainObjects, cls, sheetName, fileName);
    }

    @Programmatic
    public <T> Blob toExcelPivot(
            final WorksheetContent worksheetContent,
            final String fileName) throws ExcelService.Exception {
        return excelServiceImpl.toExcelPivot(worksheetContent, fileName);
    }

    @Programmatic
    public Blob toExcelPivot(
            final List<WorksheetContent> worksheetContents,
            final String fileName) throws ExcelService.Exception {

        return excelServiceImpl.toExcelPivot(worksheetContents, fileName);
    }

    /**
     * Returns a list of objects for each line in the spreadsheet, of the specified type.
     */
    @Programmatic
    public <T> List<T> fromExcel(
            final Blob excelBlob,
            final Class<T> cls,
            final String sheetName) throws ExcelService.Exception {
        return fromExcel(excelBlob, new WorksheetSpec(cls, sheetName));
    }

    @Programmatic
    public <T> List<T> fromExcel(
            final Blob excelBlob,
            final Class<T> cls,
            final String sheetName,
            final Mode mode) throws ExcelService.Exception {
        return fromExcel(excelBlob, new WorksheetSpec(cls, sheetName, mode));
    }

    @Programmatic
    public <T> List<T> fromExcel(
            final Blob excelBlob,
            final WorksheetSpec worksheetSpec) throws ExcelService.Exception {
        return excelServiceImpl.fromExcel(excelBlob, worksheetSpec);
    }

    @Programmatic
    public List<List<?>> fromExcel(
            final Blob excelBlob,
            final List<WorksheetSpec> worksheetSpecs) throws ExcelService.Exception {
        return excelServiceImpl.fromExcel(excelBlob, worksheetSpecs);
    }

    @Programmatic
    public List<List<?>> fromExcel(
            final Blob excelBlob,
            final WorksheetSpec.Matcher matcher) throws ExcelService.Exception {

        return fromExcel(excelBlob, matcher, null);
    }

    @Programmatic
    public List<List<?>> fromExcel(
            final Blob excelBlob,
            final WorksheetSpec.Matcher matcher,
            final WorksheetSpec.Sequencer sequencer) throws ExcelService.Exception {

        List<WorksheetSpec> worksheetSpecs = Lists.newArrayList();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(excelBlob.getBytes())) {
            final Workbook wb = org.apache.poi.ss.usermodel.WorkbookFactory.create(bais);
            final int numberOfSheets = wb.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                final Sheet sheet = wb.getSheetAt(i);
                WorksheetSpec worksheetSpec = matcher.fromSheet(sheet.getSheetName());
                if(worksheetSpec != null) {
                    worksheetSpecs.add(worksheetSpec);
                }
            }
        } catch (InvalidFormatException | IOException e) {
            throw new ExcelService.Exception(e);
        }

        if(sequencer != null) {
            worksheetSpecs = sequencer.sequence(worksheetSpecs);
        }

        return fromExcel(excelBlob, worksheetSpecs);
    }

    @javax.inject.Inject
    ServiceRegistry serviceRegistry;

}
