import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Map;
import java.util.HashMap;

/**
 * keywordcounter
 */
public class keywordcounter {
    public keywordcounter() {}

    /**
     * Open input and output file stream
     * Process each line from input file and extract keywords - counts, output required and terminating condition
     * create HashMap for the Keywords and Node referencing to the correspondinf frequency in Fibonnacci Heap 
     * Perform operations like insertNode, removeMax() on the Fibonnacci Heap Priority Queue
	 * write output to output_file.txt
     * 
     * @param args having name of given input file
     * 
	 * 
	 */
    public static void main(String[] args) {
        String fileName, strLine, word, outputLine;
        FileInputStream fstream;
        BufferedReader br; 
        String[] splited;
        int count, n;
        Map<String, Node> keywords;
        Node node, maxRoot;

        FibonnacciMaxPQ fibonacciHeap = new FibonnacciMaxPQ();
        
        if (args.length == 1) {
            // Fet the file name from command line argument
            fileName = args[0];
            try {
                int numOfLines =  getNumberOfLines(fileName);

                // Open the Input stream to read the file
                fstream = new FileInputStream(fileName);
                br = new BufferedReader(new InputStreamReader(fstream));

                // Open the Output stream to write to the file
                PrintWriter writer = new PrintWriter("output_file.txt", "UTF-8");

                // Initialise the Hash Map
                keywords = new HashMap();
                outputLine = "";
                
                // Read File Line By Line
                while ((strLine = br.readLine()) != null)   {
                    
                    // Classify each line as keyword, output frequency and Stop
                    if (strLine.charAt(0) == '$') {
                        // Split strLine by space
                        splited = strLine.split("\\s+");

                        // Remove $ from starting of strLine 
                        // and split it to the word and it's count
                        word = splited[0].substring(1,splited[0].length());
                        count = Integer.parseInt(splited[1]);

                        // Get hash count value for the current keyword
                        
                        if (keywords.get(word) == null) {
                            node = new Node(word, count);
                            fibonacciHeap.insertNode(node);
                            keywords.put(word, node);
                        } else {
                            node = keywords.get(word);
                            fibonacciHeap.increaseKey(count, node);
                        }

                    } else if (strLine.matches("-?(0|[1-9]\\d*)")) {

                        // Initialize a list of top n frequent keywords asked in the input file
                        n = Integer.parseInt(strLine);
                        Node[] topList = new Node[n];

                        // Remove first n Nodes from Priority Queue
                        for (int i = 0; i < n; i++) {
                            maxRoot = fibonacciHeap.removeMax(numOfLines);
                            topList[i] = maxRoot;
                            if (outputLine == "" && maxRoot != null) {
                                outputLine += maxRoot.key;
                            } else if (maxRoot != null) {
                                outputLine += "," + maxRoot.key;
                            }
                        }

                        // Write the output to the output file and reinitialize outputLine
                        writer.println(outputLine);
                        outputLine = "";

                        // Insert back in Fibonnacci Heap Priority Queue
                        for (int i = 0; i < n; i++) {
                            fibonacciHeap.insertNode(topList[i]);
                        }

                    } else if (strLine.matches("stop")) {
                        writer.close();
                        return;
                    } else {
                        System.out.println(strLine);
                        System.out.println ("Please fix the Input file content for correct processing.");
                    }
                }
                
                //Close the input stream
                br.close();
                
            } catch (FileNotFoundException e)  {
                // insert code to run when exception occurs
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter the name of input file as comman line argument.");
        }
        

    }

    // Calculate the total number of lines in the file for initializing array purpose.
    public static int getNumberOfLines(String fileName) {
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            int numOfLines = 0;
            while ((br.readLine()) != null)   {
                numOfLines++;
            }
            br.close();
            return numOfLines;
        } catch (FileNotFoundException e)  {
            // insert code to run when exception occurs
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return 100;

    }
    
}