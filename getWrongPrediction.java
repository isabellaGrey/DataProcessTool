import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class getWrongPrediction {
	
	public void _wrongPrediction(String instream, String outstream ) throws IOException{
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream (instream), "UTF-8" ) );
		BufferedWriter output = new BufferedWriter( new OutputStreamWriter( new FileOutputStream (outstream), "UTF-8" ) );
		String line = null; 
		String prediction = null; 
		String text = null;
		int currentFirstTAB; 
		int count = 7;
		
		//skip first six irrelevant lines
		for (int i = 1; i <= 7; i++){		
		line = reader.readLine();
		}
		
		
		while (line !=null){
			
			
				currentFirstTAB = line.indexOf("\t");
				prediction = line.substring(0, currentFirstTAB);
				line = line.substring(currentFirstTAB+1);
				currentFirstTAB = line.indexOf("\t");
				text = line.substring(0, currentFirstTAB);
				
				
				if (text.equals(prediction) == false){
					
					output.write(count+"--original:"+text+"$$prediction:"+prediction+"\n");
					System.out.println(text);
					System.out.println(prediction);
					System.out.println("NEXT");
				}
							
			line = reader.readLine();
			count++;
		}
	}
	
	public static void main(String[] args) throws IOException {
		String inputFile = "predict_result";
		String outputFile = "wrongPrediction";
		
		getWrongPrediction text = new getWrongPrediction();
		text._wrongPrediction(inputFile, outputFile);
	}

}
