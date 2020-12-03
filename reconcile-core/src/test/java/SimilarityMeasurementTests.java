import com.tutuka.reconciliation.Application;
import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.transactioncomapare.enumeration.Result;
import com.tutuka.reconciliation.transactioncomapare.service.SimilarityMeasurementService;
import com.tutuka.reconciliation.transactioncomapare.domain.TransactionWithScoreDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "similarity.valid_increment=1" })
@ContextConfiguration(classes = Application.class)
public class SimilarityMeasurementTests {
	
	@Autowired
	SimilarityMeasurementService similarityMeasurementService;

	TestUtility testUtility = new TestUtility();

	/**
	 * Perfect Match Test
	 */
	@Test
	public void test_TransactionsPerfectMatch() {
		File tutukaFile = new File("src/test/resources/tutuka_transaction_score_match.csv");
		File clientFile = new File("src/test/resources/client_transaction_score_match.csv");
		List<TransactionWithScoreDTO> report = new ArrayList<>();
		try {
			report = similarityMeasurementService.calculateSimilarityScoreMatch(testUtility.loadFile(tutukaFile.getName()), testUtility.loadFile(clientFile.getName()),
					testUtility.filesToFileLoader(tutukaFile, clientFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(report.get(0).getStatus()).isEqualTo(Result.PERFECT_MATCH);
		System.out.println(report.get(0).getReasons().toString());
	}

	/**
	 * Similarity Score Test
	 */
	@Test
	public void test_SimilarityScore() {
		File tutukaFile = new File("src/test/resources/tutuka_transaction_score_match.csv");
		File clientFile = new File("src/test/resources/client_transaction_score_match.csv");

		TransactionWithScoreDTO dto = new TransactionWithScoreDTO();

		try {
			Transaction tutukaTransaction = testUtility.loadFile(tutukaFile.getName()).get(0);
			Transaction clientTransaction = testUtility.loadFile(clientFile.getName()).get(0);
			dto = similarityMeasurementService.getSimilarityScoreOfTwoTransactions(tutukaTransaction, tutukaTransaction);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(dto.getMatchScore() == 6.5);
		assertThat(dto.getStatus()).isEqualTo(Result.PERFECT_MATCH);
	}
	
	
	/*
	*  WalletReference Mismatch and Transaction Amount Match
	*/
	@Test
	public void test_ProbableMisMatch() {
		File tutukaFile = new File("src/test/resources/tutuka_ProbableMisMatch.csv");
		File clientFile = new File("src/test/resources/client_ProbableMisMatch.csv");
		List<TransactionWithScoreDTO> report = new ArrayList<>();
		try {
			report = similarityMeasurementService.calculateSimilarityScoreMatch(testUtility.loadFile(tutukaFile.getName()), testUtility.loadFile(clientFile.getName()),
					testUtility.filesToFileLoader(tutukaFile, clientFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(report.get(0).getStatus()).isEqualTo(Result.PROBABLE_MISMATCH);
		assertThat(report.get(0).getReasons().toString().contains("WalletReference Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("Transaction Amount match"));
		System.out.println(report.get(0).getReasons().toString());
	}

	@Test
	public void test_AmountMatch() {
	File tutukaFile = new File("src/test/resources/tutuka_PermissbleAmtMatch.csv");
	File clientFile = new File("src/test/resources/client_PermissbleAmtMatch.csv");
	List<TransactionWithScoreDTO> report = new ArrayList<>();
	try {
		report = similarityMeasurementService.calculateSimilarityScoreMatch(testUtility.loadFile(tutukaFile.getName()), testUtility.loadFile(clientFile.getName()),
				testUtility.filesToFileLoader(tutukaFile, clientFile));
	} catch (Exception e) {
		e.printStackTrace();
	}
	assertThat(report.get(0).getStatus()).isEqualTo(Result.PERMISSIBLE_MATCH);
	assertThat(report.get(0).getReasons().toString().contains("Transaction Amount match"));
	}

	@Test
	public void test_DateMatch() {
		File tutukaFile = new File("src/test/resources/tutuka_PermissbleDateMatch.csv");
		File clientFile = new File("src/test/resources/client_PermissbleDateMatch.csv");
		List<TransactionWithScoreDTO> report = new ArrayList<>();
		try {
			report = similarityMeasurementService.calculateSimilarityScoreMatch(testUtility.loadFile(tutukaFile.getName()), testUtility.loadFile(clientFile.getName()),
					testUtility.filesToFileLoader(tutukaFile, clientFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//First record contains a time difference of 2 hours
		assertThat(report.get(0).getStatus()).isEqualTo(Result.PERMISSIBLE_MATCH);
		assertThat(report.get(0).getReasons().toString().contains("TransactionDate match"));
		//Second record contains a time difference of 4 hours
		assertThat(report.get(1).getStatus()).isEqualTo(Result.PROBABLE_MATCH);
		assertThat(report.get(1).getReasons().toString().contains("TransactionDate Mismatch"));
	}
	
	@Test
	public void test_Perfect_Mismatch() {
		File tutukaFile = new File("src/test/resources/tutuka_PerfectMismatch.csv");
		File clientFile = new File("src/test/resources/client_PerfectMismatch.csv");
		List<TransactionWithScoreDTO> report = new ArrayList<>();
		try {
			report = similarityMeasurementService.calculateSimilarityScoreMatch(testUtility.loadFile(tutukaFile.getName()), testUtility.loadFile(clientFile.getName()),
					testUtility.filesToFileLoader(tutukaFile, clientFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(report.get(0).getStatus()).isEqualTo(Result.PERFECT_MISMATCH);
		assertThat(report.get(0).getReasons().toString().contains("TransactionNarrative Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("ProfileName Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("TransactionType Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("TransactionAmount Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("TransactionDate Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("WalletReference Mismatch"));
		assertThat(report.get(0).getMatchScore()==0);
		
	}
	
	//Test to catch NULL values of other columns other than ID and Descr
	@Test
	public void catchesOtherNullValues() {
		File tutukaFile = new File("src/test/resources/tutuka_OthersNull.csv");
		File clientFile = new File("src/test/resources/client_OthersNull.csv");
		List<TransactionWithScoreDTO> report = new ArrayList<>();
		try {
			report = similarityMeasurementService.calculateSimilarityScoreMatch(testUtility.loadFile(tutukaFile.getName()), testUtility.loadFile(clientFile.getName()),
					testUtility.filesToFileLoader(tutukaFile, clientFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(report.get(0).getStatus()).isEqualTo(Result.PERFECT_MISMATCH);
		assertThat(report.get(0).getReasons().toString().contains("TransactionNarrative Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("ProfileName Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("TransactionType Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("TransactionAmount Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("TransactionDate Mismatch"));
		assertThat(report.get(0).getReasons().toString().contains("WalletReference Mismatch"));
		assertThat(report.get(0).getMatchScore()==0);
	}

}
