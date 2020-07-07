package za.co.entelect.challenge.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ZipUtils {

    private static final Logger LOGGER = LogManager.getLogger(ZipUtils.class);

    public static File extractZip(File zip) throws ZipException, IOException {
        LOGGER.info("Extracting Zip {}", zip.getName());
        ZipFile zipFile = new ZipFile(zip);

        String extractFilePath = String.format("%s/extracted/%s", "tournament-tmp", zip.getName().replace(".zip", ""));
        File extractedFile = new File(extractFilePath);

        zipFile.extractAll(extractedFile.getCanonicalPath());

        // Select the actual bot folder located inside the extraction folder container
        extractedFile = Objects.requireNonNull(extractedFile.listFiles())[0];

        return extractedFile;
    }

    public static File createZip(String zipName, String directory) throws ZipException {
        LOGGER.info("Creating zip: {} for directory: {}", zipName, directory);

        ZipFile zipFile = new ZipFile(new File(String.format("%s.zip", zipName)));

        File path = new File(directory);
        path = Objects.requireNonNull(path.listFiles())[0];

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        zipFile.addFolder(path, parameters);

        return zipFile.getFile();
    }
}
