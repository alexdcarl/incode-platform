package org.isisaddons.module.docx.dom;

public class MergeException extends DocxServiceException {
    private static final long serialVersionUID = 1L;
    public MergeException(String message) {
        super(message);
    }
    public MergeException(String message, Throwable cause) {
        super(message, cause);
    }
}