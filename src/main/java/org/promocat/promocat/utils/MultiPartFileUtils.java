package org.promocat.promocat.utils;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.xpath.operations.Mult;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Danil Lyskin at 19:12 13.06.2020
 */
public class MultiPartFileUtils {

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

    public static MultipartFile pdfToImage(final File pdfFile)  {
        try {
            PDFDocument document = new PDFDocument();
            document.load(pdfFile);

            SimpleRenderer renderer = new SimpleRenderer();
            renderer.setResolution(300);

            List<Image> images = null;
            try {
                images = renderer.render(document);
            } catch (RendererException | DocumentException e) {
                e.printStackTrace();
            }

            File file = new File(System.getProperty("java.io.tmpdir") + "hui.jpeg");
            ImageIO.write((RenderedImage) images.get(0), MimeTypes.MIME_IMAGE_JPEG, file);

            FileInputStream input = new FileInputStream(file);
            return new MockMultipartFile("fileItem",
                    file.getName(), MimeTypes.MIME_IMAGE_JPEG, IOUtils.toByteArray(input));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiFileFormatException("probs");
        }
    }
}
