import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class makeRuns {
    // JAYDEN WOODS

    public static void main(String[] args) {
        // CASE: we dont have two arguments print usage for how to use application
        if (args.length != 2){
            System.out.println("Usage: java makeRuns <m> <inputFile> > init.runs");
        }else {
            // CASE: we have two arguments, we then store them into variables to further usage
            int m = Integer.parseInt(args[0]);
            String inputFile = args[1];

            try{
                // Creating a BufferedReader object to read whats inside the inputFile
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                // Creating a priority queue using the javascript inbuilt package
                PriorityQueue<String> pq = new PriorityQueue<>();
                // Storing the files line into a line variable
                String line;
                // Create a counter which will increment every line that is added
                int linesRead = 0;
                
                // While theres lines arent null in the file continue to read
                while ((line = reader.readLine()) != null){
                    // Add the line into the priority queue to sort by natural order
                    pq.offer(line);
                    // Increment the line by 1
                    linesRead++;
                    // If the linesRead variable is equal to m then output the priority queue into writer
                    if(linesRead == m){
                        // Pass our priority queue and writer into the output function
                        outputRun(pq);

                        // Reset linesRead, and priority queue to start a new run
                        linesRead = 0;
                        pq.clear();
                    }
            }
            // CASE: check if the priority queue is empty is not its not a full run
            if(!pq.isEmpty()){
                outputRun(pq);
            }

            // Close reader, and writer to then start a new run
            reader.close();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    // Function to output the priority queue run into the writer
    private static void outputRun(PriorityQueue<String> pq){
        // While the priorityQueue isnt empty
        while(!pq.isEmpty()){
            // This will poll the head item at index[0] into the file
            System.out.println(pq.poll());
        }
    }
}