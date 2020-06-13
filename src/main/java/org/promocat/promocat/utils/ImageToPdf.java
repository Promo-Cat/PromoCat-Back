package org.promocat.promocat.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Danil Lyskin at 19:12 13.06.2020
 */
public class ImageToPdf {

    private static final Float X = 0f;
    private static final Float Y = 0f;

    @Value("${data.template.pdf}")
    private static String PATH;

    public static void insert(Paths filePath, Paths fileName) throws Exception {
        File file = new File(PATH);
        PDDocument doc = PDDocument.load(file);
        PDPage page = doc.getPage(0);
        PDPageContentStream contents = new PDPageContentStream(doc, page);

        PDImageXObject pdImage = PDImageXObject.createFromFile(filePath.toString(), doc);
        contents.drawImage(pdImage, X, Y);

        contents.close();
        doc.save(fileName.toString());
        doc.close();
    }
}