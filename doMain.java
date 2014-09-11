import java.io.File;
import java.io.IOException;

public class doMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//%%ten-fold cross-validation(shuffle for one time and do ten times ten-fold)
		// use dataProcessTools Class
		dataProcessTools c = new dataProcessTools();
		
		
		/***1. reprocess data***/
		/// 0. process raw data - get rid of 'rejected' etc. 
		/// 1. shuffle the original file and get shuffled file
		String original = "data/_0_rawData";
		String shuffled = "data/shuffledData";
		//c._shuffleData(new File(original), new File(shuffled));
		
		/***2. get final model using all data***/
		/// run shell script to get final model based on all of the shuffled data
		String shell2 = "scripts/getModel.sh"; //file mentioned in getModel.sh should be users' input
		//c._runShell(shell2,"XXX"); //get 'shuffled.model' & test results
		
		
		
		/***3. validate model in step2 by N-fold cross validation***/
		/// 3.1. use shuffled file to get N_fold files (N*2 pieces)
		String trainData = "data/tenCrossValid/trainData";
		String testData = "data/tenCrossValid/testData";
		int totalNum = c.__getTotalLines(new File(shuffled));		
		//c._N_Fold(10, totalNum, shuffled, trainData, testData);
		
		/// 3.2 run shell call demo.sh to get ten models and ten according accuracies & get output for average accuracy for the model
		String shell = "./scripts/tenCV.sh";
		//c._runShell(shell,"data/tenCVaccuracy");
		
		/// 3.3 calculate average accuracy from /accuracy file/
		//c._accuracyCalculation("data/tenCVaccuracy");
		
		/***4. get topN hard decision based on model generated in step2***/
		/// 4. get top N guess for specific intent in test file
		String python = "python ./scripts/getTopNHardDeci.py";
		//c._runShell(python,"hardDecision");
		
		/***5. get normalization decision value for top N classes***/
		c._logisticFunctionNormaProb("hardDecision"); // the number "3" should be corresponding to top "N" in hardDecision file.
	}

}
