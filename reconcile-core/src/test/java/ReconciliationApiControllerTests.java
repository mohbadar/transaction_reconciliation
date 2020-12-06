import com.tutuka.reconciliation.Application;
import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.transactioncomapare.domain.FileUploadDTO;
import com.tutuka.reconciliation.transactioncomapare.service.CompareService;
import com.tutuka.reconciliation.transactioncomapare.util.TransactionUtility;
import com.tutuka.reconciliation.transactioncomapare.service.FileSystemStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class ReconciliationApiControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private FileSystemStorageService storageService;

	@MockBean
	private CompareService compareService;

	@LocalServerPort
	private int port;

	TestUtility testUtility= new TestUtility();

	@Test
	public void test_missingFilesCompare()
	{
	   ResponseEntity<Object> response = this.restTemplate
				.postForEntity("/app/compare-files", null,Object.class);

		assertThat(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}


	@Test
	public void test_filesCompare() throws Exception {
		File tutukaFile = new File("src/test/resources/TutukaMarkoffFile20140113 (1).csv");
		File clientFile = new File("src/test/resources/ClientMarkoffFile20140113 (1).csv");

		FileUploadDTO dto = new FileUploadDTO();
		dto.setFileOne(testUtility.fileToMultipart(tutukaFile));
		dto.setFileTwo(testUtility.fileToMultipart(clientFile));

		Map<String, Object> transactionWithScores = compareService.compareTransactions(dto);
//		ResponseEntity<Map> response = this.restTemplate
//				.postForEntity("/app/compare-files", dto,Map.class);
		System.out.println("transactions: "+ transactionWithScores.getOrDefault("tutukaRowCount", 0));

		assertThat(transactionWithScores != null);
//		assertThat(perfectMatchTransactions.get(0).getStatus().name().equalsIgnoreCase(Result.PERFECT_MATCH.getValue()));
	}

	@Test
	public void test_csvBeanRead() throws Exception {
		Iterator<Transaction> testIter = testUtility.loadFile("ValidFile.csv").iterator();
		assertThat(testIter.next().getWalletReference()).isEqualTo("P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5");
	}

	@Test
	public void test_removeduplicateTransaction() throws Exception {
		Iterator<Transaction> testIter = new TransactionUtility().removeDuplicates(testUtility.loadFile("DuplicateFile.csv"))
				.iterator();
		assertThat((testIter.next()).getTransactionID().toString()).isEqualTo("584011808649511");
	}
	
	@Test
	public void test_removeduplicatesFromList() throws Exception {
		assertThat(String.valueOf(new TransactionUtility().removeDuplicates(testUtility.loadFile("DuplicateFile.csv")).size())).isEqualTo("2");
	}

    
    //Test for handling headers not in order and extra headers
    @Test
    public void test_handleExtraHeaders() {
    	Iterator<Transaction> testIter = null;
		try {
			testIter = testUtility.loadFile("handleextraHeadersTest.csv").iterator();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(testIter.next().getTransactionID().toString()).isEqualTo("584012370494730");
    }
}