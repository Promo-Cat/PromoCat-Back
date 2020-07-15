package org.promocat.promocat.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.promocat.promocat.config.GeneratorConfig;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Danil Lyskin at 19:12 13.06.2020
 */
@Component
@Scope("singleton")
@Slf4j
public class MultiPartFileUtils {


    public static String PATH;

    @Value("${data.codes.files}")
    public void setPATH(final String PATH) {
        MultiPartFileUtils.PATH = PATH;
    }

    /**
     * Создание {@code png} файла из {@code pdf}. Берется только первая страница из {@code pdf}.
     * @param pdfFile {@code pdf} файл.
     * @return {@code png} файл.
     * @throws ApiFileFormatException если не получилось создать {@code pdf} файл.
     */
    public File pdfToImage(final File pdfFile) {
        try (final PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            File file = new File(PATH + Generator.generate(GeneratorConfig.FILE_NAME) + ".png");
            if (file.createNewFile()) {
                log.info("New image {} created", file.getAbsolutePath());
            }
            ImageIO.write(bim, "png", file);
            return file;
        } catch (IOException e) {
            throw new ApiFileFormatException("Exception while trying to create pdf document" + e);
        }
    }

    /**
     * Перевод картинки формата {@code png} в {@link MultiPartFileDTO}.
     * @param imageFile файл картинки.
     * @return представление картинки {@link MultiPartFileDTO}.
     * @throws ApiServerErrorException если не получилось перевести картинку.
     */
    public MultiPartFileDTO fileImageToMultipartFileDTO(final File imageFile) {
        try (FileInputStream input = new FileInputStream(imageFile)) {
            MultiPartFileDTO posterPreview = new MultiPartFileDTO();
            MultipartFile image = new MockMultipartFile("fileItem",
                    imageFile.getName(), MimeTypes.MIME_IMAGE_PNG, IOUtils.toByteArray(input));
            posterPreview.setDataType(image.getContentType());
            posterPreview.setFileName(image.getOriginalFilename());
            try {
                posterPreview.setBlob(new SerialBlob(image.getBytes()));
            } catch (SQLException | IOException e) {
                log.error(e.getLocalizedMessage());
                throw new ApiServerErrorException("Problems with setting poster");
            }
            return posterPreview;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Problems with setting poster");
        }
    }
}
