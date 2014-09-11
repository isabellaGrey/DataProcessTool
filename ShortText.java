import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ShortText {
	
	public void _formedText(String instream, String outstream ) throws IOException{
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream (instream), "UTF-8" ) );
		BufferedWriter output = new BufferedWriter( new OutputStreamWriter( new FileOutputStream (outstream), "UTF-8" ) );
		String line = reader.readLine(); //
		String category = null; //
		String text = null; //
		int currentFirstCommaIndex; //
		int currentFirstCurveIndex; //
		line = reader.readLine();
		while (line !=null){
			
			if (line.startsWith("Approved")){
				currentFirstCommaIndex = line.indexOf(',');
				line = line.substring(currentFirstCommaIndex+1);  //get rid of 'Approved'  -new line1
				currentFirstCommaIndex = line.indexOf(','); 
				category = line.substring(0,currentFirstCommaIndex);  //category 
				line = line.substring(currentFirstCommaIndex+1);  //get rid of category  -new line2
				
				//System.out.println(line);
				
				//begin of ten texts
				for (int i = 1; i <= 10; i++){
					
				output.write(category); //write category
				output.write("\t"); //change seperation here
				if (i != 10){
					
				
				currentFirstCurveIndex = line.indexOf('~');
				//System.out.println(currentFirstCurveIndex);
				text = line.substring(0, currentFirstCurveIndex-1);  //text
				output.write(text); // write text
				output.write("\n");
				line = line.substring(currentFirstCurveIndex+2);
				
				}else{
					text = line;
					output.write(text); // write text
					output.write("\n");
				}

				}
				
			}
			line = reader.readLine();
		}
	}
	
	public static void main(String[] args) throws IOException {
		String inputFile = "text.csv";
		String outputFile = "output";
		
		ShortText text = new ShortText();
		text._formedText(inputFile, outputFile);
	}

}
