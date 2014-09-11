import java.io.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.*;  

import static java.lang.System.*;  

public class dataProcessTools {
			
	void _shuffleData(File instream, File outstream) throws IOException{ //random the data 
		
		BufferedReader input = new BufferedReader( new InputStreamReader( new FileInputStream (instream), "UTF-8" ) );
		int totalLine = __getTotalLines(instream);
		String line = null;
		String currentLine;
		ArrayList <String> list = new ArrayList<String>();
		for (int i = 1; i <= totalLine; i++){
			line = input.readLine();
			list.add(line);
		}
		
		Collections.shuffle(list); //shuffle the file in the list 	
		//System.out.println(list);

		//write shuffled data according to shuffled file numbers
		BufferedWriter output = new BufferedWriter( new OutputStreamWriter( new FileOutputStream (outstream), "UTF-8" ) );
		for (int i = 0; i < totalLine; i++){
			currentLine = list.get(i);
			
			
			output.write(currentLine);
			output.write("\n");
		}
		
		input.close();
		output.close();
	}

	
	//20% as test cross validation
	void _seperateTraTes(float percentageForTrain, int totalNum, File shuffledFile, File trainData, File testData) throws IOException{ //after get random data, first 20% for test and later 80% for train
		int lineNum;
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(shuffledFile), "UTF-8"));
		BufferedWriter testOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testData), "UTF-8"));
		BufferedWriter trainOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(trainData), "UTF-8"));
				
		float n = totalNum * percentageForTrain;
		System.out.println("float-train:"+n);
		int trainNum = (int)n;
		System.out.println("int-train:"+trainNum);
		int testNum = totalNum - trainNum;
		System.out.println("int-test:"+testNum);
				
		
		// write 20% test data
		for (lineNum = 1; lineNum <= testNum; lineNum++){ //1-2299
			line = input.readLine();
			testOutput.write(line);
			testOutput.write("\n");
		}
		
		// write 80% train data
		for (lineNum = 1; lineNum <= trainNum; lineNum++){ //2300-9194
			line = input.readLine();
			trainOutput.write(line);
			trainOutput.write("\n");
		}
				
		input.close();
		trainOutput.close();
		testOutput.close();
	}
	
	void _N_Fold(int foldNum, int totalNum, String shuffledFileStr, String trainDataStr, String testDataStr) throws IOException{
		
		int testNum = totalNum/foldNum; //num of each test set
		int lineNum ;
		String line;
		
		BufferedReader input = null;
		BufferedWriter testOutput = null;		
		BufferedWriter trainOutput = null;
		
		for (int i = 1; i <= 10; i++){
			
			String testData = testDataStr + i+".txt";
			String trainData = trainDataStr + i+".txt";
			
			input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(shuffledFileStr)), "UTF-8"));
			testOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(testData)), "UTF-8"));
			trainOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(trainData)), "UTF-8"));
			
			
			//(i-1)*testNum ~ i*testNum ->test
			int beginTest = (i-1)*testNum+1;
			int endTest = i*testNum;
			
			
			System.out.println("----->begin::"+beginTest+"~~end::"+endTest);
			for (lineNum = 1; lineNum < beginTest;){
				
				line = input.readLine();
				trainOutput.write(line);
				trainOutput.write("\n");
				lineNum++;
			}
			
			for (lineNum = beginTest; lineNum <= endTest;){
				line = input.readLine();
				testOutput.write(line);
				testOutput.write("\n");
				lineNum++;
			}
			
			
		
			
			for (lineNum = endTest+1; lineNum <= totalNum;){
				line = input.readLine();
				trainOutput.write(line);
				trainOutput.write("\n");
				lineNum++;
			}
			input.close();
			trainOutput.close();
			testOutput.close();
			
		}		
	
	}

	
	void _runShell(String shellString, String outputFileName) throws IOException{ //invoke shell to do demo.sh and get accuracy to output
    	
		BufferedWriter accuracy = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFileName)), "UTF-8"));
    	String execute = shellString;
		Process process = Runtime.getRuntime().exec(execute);
        InputStreamReader ir = new InputStreamReader(process.getInputStream());  
        LineNumberReader input = new LineNumberReader(ir);  
        String line;  
        //System.out.println("###");
        
        while ((line = input.readLine()) != null)  {       	
        	accuracy.write(line);
        	accuracy.write("\n");
        System.out.println("outcome:"+line);  
        
        }
        accuracy.close();
        input.close();  
        ir.close();  

    }
	
	double _accuracyCalculation(String accPath) throws IOException{
		double totalAcc = 0; 
		String accNum = null;
		double accValue = 0;
		double count = 0;
		BufferedReader acc = new BufferedReader( new InputStreamReader( new FileInputStream (new File(accPath)), "UTF-8" ));
		String line;
		
		//line = acc.readLine();
		while((line = acc.readLine()) != null){
			//line = acc.readLine();
			if (line.startsWith("Accuracy")){
				accNum = line.substring(11,18);
				//System.out.println(accNum);
				count++;
			
			System.out.println(Double.parseDouble(accNum)/100.0);	
		totalAcc = totalAcc + Double.parseDouble(accNum)/100; //% needs to be divided by 100		
			}		
	   }
	    accValue = totalAcc/count;
	    System.out.println("averageAccuracy::"+accValue);
	    acc.close();
		return accValue;
	}
	

		  
	

	
	void _getAccuracyGraphic(){
		
	}
	
	
	// for getting probability from logistic regression decsion value. 
	//(for logistic regression only, other non-logistic R will be meaningless even could be calculated). Coz LR is based on probability 
	List _logisticFunctionNormaProb(String hardDecisionPath) throws IOException{

		BufferedReader hardDeci = new BufferedReader(new InputStreamReader(new FileInputStream(new File(hardDecisionPath)), "UTF-8"));
	
		int length; // length of a line
		List<Integer> capitalIdx = new ArrayList<Integer>(); // index for each capital character in first line
		int size; // size of an arrayList	
		String currentClass; // stores current class
		List <String>classList = new ArrayList<String>();// stores all class names
		
		List<Integer> decvalIdx = new ArrayList<Integer>();
		String currentDecval; // stores current decval
		List <String>deciList = new ArrayList<String>();
		
		float number;
		List <Double> num = new ArrayList<Double>();
		NumberFormat numFormatPercentage = NumberFormat.getPercentInstance();
		List <String>percentageList = new ArrayList<String>();
		
		
		// to get all class with original spaces(make sure the class name is capital in first character)
		String line= hardDeci.readLine(); // read first line(contains classes name)
		length = line.length();
		for (int i = 0; i < length; i++){
			//System.out.println(line.charAt(i));
			while (Character.isUpperCase(line.charAt(i))){
				capitalIdx.add(i);
				break;
			}
			}
		
		size = capitalIdx.size();
		for (int i = 0; i < size-1; i++){
			currentClass = line.substring(capitalIdx.get(i),capitalIdx.get(i+1));
			//line = line.substring(capitalIdx.get(i+1));
			classList.add(currentClass);
			
		}
		classList.add(line.substring(capitalIdx.get(capitalIdx.size()-1))); // until here, get all classes
		System.out.println(classList);
		
		// to get all decval with original spaces
		do{
			line = hardDeci.readLine();
		}while(!line.contains("**decval**"));		
		
		length = line.length();
		for (int i = 0; i < length; i++){
			while(line.charAt(i) == '.'){
				decvalIdx.add(i-2);
				break;
			}
		}
		
		size = decvalIdx.size();
		for (int i = 0; i < size-1; i++){
			currentDecval = line.substring(decvalIdx.get(i),decvalIdx.get(i+1));
			//line = line.substring(i+1);
			deciList.add(currentDecval);
		}
		
		deciList.add(line.substring(decvalIdx.get(decvalIdx.size()-1))); // until here, we get all decval		
		System.out.println(deciList);
		
		// BEGIN CALCULATION FOR NORMALIZATION TO 0 ~ 1
		for (int i = 0; i < deciList.size(); i++){
		number = Float.valueOf(deciList.get(i));
		double douNum = number;
		double prob;
		prob = 1/(1+Math.exp(-douNum));
		num.add(prob);
		
		System.out.println("LR probability::"+prob);
		
		}		
				
		return num;		
	
	}
	
	
	
	// supervisor said this method is bad, we should use inner method to get probability by logistic function: 1/(1+e^-decval). See function _logisticFunctionNormaProb
	// this method is self-crerated normalization to make decision value in the range of [0,1], as probability
	List _getDVNormalization(String hardDecisionPath) throws IOException{
		BufferedReader hardDeci = new BufferedReader(new InputStreamReader(new FileInputStream(new File(hardDecisionPath)), "UTF-8"));
		
		
		
		int length; // length of a line
		List<Integer> capitalIdx = new ArrayList<Integer>(); // index for each capital character in first line
		int size; // size of an arrayList	
		String currentClass; // stores current class
		List <String>classList = new ArrayList<String>();// stores all class names
		
		List<Integer> decvalIdx = new ArrayList<Integer>();
		String currentDecval; // stores current decval
		List <String>deciList = new ArrayList<String>();
		
		float number;
		List <Float> num = new ArrayList<Float>();
		NumberFormat numFormatPercentage = NumberFormat.getPercentInstance();
		List <String>percentageList = new ArrayList<String>();
		
		
		// to get all class with original spaces(make sure the class name is capital in first character)
		String line= hardDeci.readLine(); // read first line(contains classes name)
		length = line.length();
		for (int i = 0; i < length; i++){
			//System.out.println(line.charAt(i));
			while (Character.isUpperCase(line.charAt(i))){
				capitalIdx.add(i);
				break;
			}
			}
		
		size = capitalIdx.size();
		for (int i = 0; i < size-1; i++){
			currentClass = line.substring(capitalIdx.get(i),capitalIdx.get(i+1));
			//line = line.substring(capitalIdx.get(i+1));
			classList.add(currentClass);
			
		}
		classList.add(line.substring(capitalIdx.get(capitalIdx.size()-1))); // until here, get all classes
		System.out.println(classList);
		
		// to get all decval with original spaces
		do{
			line = hardDeci.readLine();
		}while(!line.contains("**decval**"));		
		
		length = line.length();
		for (int i = 0; i < length; i++){
			while(line.charAt(i) == '.'){
				decvalIdx.add(i-2);
				break;
			}
		}
		
		size = decvalIdx.size();
		for (int i = 0; i < size-1; i++){
			currentDecval = line.substring(decvalIdx.get(i),decvalIdx.get(i+1));
			//line = line.substring(i+1);
			deciList.add(currentDecval);
		}
		
		deciList.add(line.substring(decvalIdx.get(decvalIdx.size()-1))); // until here, we get all decval		
		System.out.println(deciList);
		
		// BEGIN CALCULATION FOR NORMALIZATION TO 0 ~ 1
		for (int i = 0; i < deciList.size(); i++){
		number = Float.valueOf(deciList.get(i));
		
		num.add(number);
		
		System.out.println("num::"+number);
		
		}
				
		float temp = 0;
		double norm = 0;
		Double denominator = 0.0;
		for (int i = 0; i < num.size(); i++){
			norm = num.get(num.size()-1)-0.00001;
			temp += (num.get(i)-norm)*(num.get(i)-norm);
		}
		denominator = Math.sqrt(temp); // get denominator
		
		double tempp;
		numFormatPercentage.setMinimumFractionDigits(3);
		for (int i = 0; i < num.size(); i++){
			tempp = (num.get(i)-norm)/denominator;			
			String percentage = numFormatPercentage.format(tempp);
			System.out.println(percentage);
			percentageList.add(percentage);
		}
				
		percentageList.addAll(classList); // put both class and percentage into one list to return
		System.out.println(percentageList);
				
		return percentageList;		
	}
	
	// read appointed line in a file  
    String __readAppointedLineNumber(File sourceFile, int lineNumber)  
            throws IOException {  
        FileReader in = new FileReader(sourceFile);  
        LineNumberReader reader = new LineNumberReader(in);  
        String s = "";  
        
        int lines = 0;  
        while (s != null) {  
            lines++;  
            s = reader.readLine();  
            if((lines - lineNumber) == 0) { 
            	break;
                //System.out.println(s);  
                //System.exit(0);  
            }  
        }  
               
        //reader.close();        
        //in.close(); 
        return s;
        
        
    } 
    
    int __getTotalLines(File file) throws IOException { 
        FileReader in = new FileReader(file);  
        LineNumberReader reader = new LineNumberReader(in);  
        String s = reader.readLine();  
        int lines = 0;  
        while (s != null) {  
            lines++;  
            s = reader.readLine();  
        } 
        reader.close();  
        in.close();  
        return lines;  
    }  

}
