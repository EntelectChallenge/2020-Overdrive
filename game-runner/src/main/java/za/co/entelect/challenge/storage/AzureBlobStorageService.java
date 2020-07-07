package za.co.entelect.challenge.storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class AzureBlobStorageService {

    private static final Logger LOGGER = LogManager.getLogger(AzureBlobStorageService.class);

    private final CloudBlobClient serviceClient;

    public AzureBlobStorageService(String connectionString) throws URISyntaxException, InvalidKeyException {
        serviceClient = CloudStorageAccount.parse(connectionString)
                .createCloudBlobClient();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getFile(String file, String outputFile, String container) throws URISyntaxException, StorageException, IOException {

        CloudBlobContainer bloBContainer = serviceClient.getContainerReference(container);

        LOGGER.info("Downloading {}", file);
        File f = new File(outputFile);

        File parent = f.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (!f.exists()) {
            f.createNewFile();
        }

        CloudBlockBlob blob = bloBContainer.getBlockBlobReference(file);

        blob.downloadToFile(f.getCanonicalPath());

        return f;
    }

    public void putFile(File file, String outputLocation, String container) throws URISyntaxException, StorageException, IOException {

        CloudBlobContainer bloBContainer = serviceClient.getContainerReference(container);

        LOGGER.info("Uploading {}", file);
        CloudBlockBlob blob = bloBContainer.getBlockBlobReference(outputLocation);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            blob.upload(fileInputStream, file.length());
        }
    }
}
