package document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/** A class for timing the EfficientDocument and BasicDocument classes
 * 
 * @author UC San Diego Intermediate Programming MOOC team & Volfegan
 *
 */

public class DocumentBenchmarking {

	
	public static void main(String [] args) {

	    // Run each test more than once to get bigger numbers and less noise.
	    // You can try playing around with this number.
	    int trials = 100;

	    // The text to test on
	    String textfile = "data/warAndPeace.txt";
		
	    // The amount of characters to increment each step
	    // You can play around with this
		int increment = 30000;

		// The number of steps to run.  
		// You can play around with this.
		int numSteps = 100;
		
		// THe number of characters to start with. 
		// You can play around with this.
		int start = 50000;
		
		// TODO: Fill in the rest of this method so that it runs two loops
		// and prints out timing results as described in the assignment 
		// instructions and following the pseudocode below.
        System.out.println("Size \t\tBasicDocument \t\tEfficientDocument");
		for (int numToCheck = start; numToCheck < numSteps*increment + start; 
				numToCheck += increment) {
			// numToCheck holds the number of characters that you should read from the
			// file to create both a BasicDocument and an EfficientDocument.

			// 1. Print out numToCheck followed by a tab (\t) (NOT a newline)
            System.out.print(numToCheck + " \t");

			 // 2. Read numToCheck characters from the file into a
            int numChars = numToCheck;
            String bufferText = getStringFromFile(textfile, numChars);

            /* 3. Time a loop that runs trials times (trials is the variable above) that:
			 *  a. Creates a BasicDocument
			 *  b. Calls fleshScore on this document*/
            long startTime = 0;
            long endTime = 0;
            double duration = 0;
            double fleschScore = 0;

            //test many trials times
            for (int trial = 0; trial < trials; trial++) {

                startTime = System.nanoTime();
                BasicDocument test1 = new BasicDocument(bufferText);
                fleschScore = test1.getFleschScore();
                endTime = System.nanoTime();
                duration += (double)(endTime - startTime)/1000000000;
            }

			 /* 4. Print out the time it took to complete the loop in step 3
			 *     (on the same line as the first print statement) followed by a tab (\t)*/
            System.out.print(duration + " \t");

			 /* 5. Time a loop that runs trials times (trials is the variable above) that:
			 *     a. Creates an EfficientDocument 
			 *     b. Calls fleshScore on this document*/

            for (int trial = 0; trial < trials; trial++) {

                startTime = System.nanoTime();
                EfficientDocument test2 = new EfficientDocument(bufferText);
                fleschScore = test2.getFleschScore();
                endTime = System.nanoTime();
                duration = (double)(endTime - startTime)/1000000000;
            }

			 /* 6. Print out the time it took to complete the loop in step 5
			 *      (on the same line as the first print statement) followed by a newline (\n)*/
            System.out.print(duration + " \n");
            //System.out.println();
		}
		//end of benchmark
	}
	
	/** Get a specified number of characters from a text file
	 * 
	 * @param filename The file to read from
	 * @param numChars The number of characters to read
	 * @return The text string from the file with the appropriate number of characters
	 */
	public static String getStringFromFile(String filename, int numChars) {
		
		StringBuffer s = new StringBuffer();
		try {
			FileInputStream inputFile= new FileInputStream(filename);
			InputStreamReader inputStream = new InputStreamReader(inputFile);
			BufferedReader bis = new BufferedReader(inputStream);
			int val;
			int count = 0;
			while ((val = bis.read()) != -1 && count < numChars) {
				s.append((char)val);
				count++;
			}
			if (count < numChars) {
				System.out.println("Warning: End of file reached at " + count + " characters.");
			}
			bis.close();
		}
		catch(Exception e)
		{
		  System.out.println(e);
		  System.exit(0);
		}

		return s.toString();
	}
	
}
