import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.transactioncomapare.domain.FileUploadDTO;
import com.tutuka.reconciliation.transactioncomapare.service.CsvReaderService;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtility {


    /**
     * Get fileName and return the list of transactions in that file
     * @param filename
     * @return
     * @throws Exception
     */
    public List<Transaction> loadFile(String filename) throws Exception {
        File testFile = new File(getClass().getClassLoader().getResource(filename).getFile());
        CsvReaderService csvBeanReader = new CsvReaderService();
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        transactionsList = csvBeanReader.csvRead(testFile.getAbsolutePath());
        return transactionsList;
    }

    /**
     * Convert a Mutlipart Object of file object
     * @param file
     * @return
     */
    public MultipartFile fileToMultipart(File file) {
        FileInputStream input = null;
        MultipartFile multipartFile = null;
        try {
            input = new FileInputStream(file);
            multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
            System.out.println(" file.getName() is : " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return multipartFile;
    }

    public FileUploadDTO filesToFileLoader(File file1Name, File file2Name) {
        FileUploadDTO fileLoader = new FileUploadDTO();
        fileLoader.setFileOne(fileToMultipart(file1Name));
        fileLoader.setFileTwo(fileToMultipart(file2Name));
        return fileLoader;
    }

}
