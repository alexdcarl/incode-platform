package org.isisaddons.module.fakedata.dom;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.value.Blob;

public class IsisBlobs extends AbstractRandomValueGenerator{

    public IsisBlobs(final FakeDataService fakeDataService) {
        super(fakeDataService);
    }

    private final static List<String> fileNames = Arrays.asList(
            "image01-150x150.jpg",
            "image01-240x180.jpg",
            "image01-640x480.jpg",
            "image01-2048x1536.jpg",
            "image01-4000x3000.jpg",
            "image02-150x150.jpg",
            "image02-240x180.jpg",
            "image02-640x480.jpg",
            "image02-2048x1536.jpg",
            "image02-4000x3000.jpg",
            "Pawson-Naked-Objects-thesis.pdf",
            "rick-mugridge-paper.pdf");

    @Programmatic
    public Blob any() {
        final List<String> fileNames = IsisBlobs.fileNames;
        return asBlob(fileNames);
    }

    @Programmatic
    public Blob anyJpg() {
        return asBlob(fileNamesEndingWith(".jpg"));
    }

    @Programmatic
    public Blob anyPdf() {
        return asBlob(fileNamesEndingWith(".pdf"));
    }

    private static List<String> fileNamesEndingWith(final String suffix) {
        return Lists.newArrayList(Iterables.filter(IsisBlobs.fileNames, endsWith(suffix)));
    }

    private static Predicate<String> endsWith(final String suffix) {
        return new Predicate<String>() {
            @Override
            public boolean apply(final String input) {
                return input.endsWith(suffix);
            }
        };
    }


    private Blob asBlob(final List<String> fileNames) {
        final int randomIdx = fake.ints().upTo(fileNames.size());
        final String randomFileName = fileNames.get(randomIdx);
        return asBlob(randomFileName);
    }

    private static Blob asBlob(final String fileName) {
        final URL resource = Resources.getResource(IsisBlobs.class, "blobs/" + fileName);
        final ByteSource byteSource = Resources.asByteSource(resource);
        final byte[] bytes;
        try {
            bytes = byteSource.read();
            return new Blob(fileName, mimeTypeFor(fileName), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String mimeTypeFor(final String fileName) {
        if(fileName.endsWith("jpg")) {
            return "image/jpeg";
        }
        return "application/pdf";
    }

}
