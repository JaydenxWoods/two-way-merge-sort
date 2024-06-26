import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.io.File;

public class xSort {
// JAYDEN WOODS

    public static void main(String[] args) {
        // Check if correct number of arguments is provided
        if (args.length != 3) {
            System.out.println("Usage: java XSort <m> <init.runs> <k> > moby.sorted");
        } else {
            // Parse command line arguments
            int m = Integer.parseInt(args[0]);
            String inputFile = args[1];
            int k = Integer.parseInt(args[2]);

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                List<String> currentRun = new ArrayList<>();
                BufferedWriter[] writers = new BufferedWriter[2];

                // Create two output files for splitting runs
                writers[0] = new BufferedWriter(new FileWriter("input_file1"));
                writers[1] = new BufferedWriter(new FileWriter("input_file2"));

                int fileIndex = 0;
                // Read lines from the input file and write them to the appropriate output file
                while ((line = reader.readLine()) != null) {
                    currentRun.add(line);
                    if (currentRun.size() == m) {
                        // Write the current run to the appropriate file
                        for (String item : currentRun) {
                            writers[fileIndex].write(item);
                            writers[fileIndex].newLine();
                        }
                        // Alternate the file index for the next run
                        fileIndex = (fileIndex + 1) % 2;
                        currentRun.clear();
                    }
                }

                 // Write the last run if it's not fully filled
                if (!currentRun.isEmpty()) {
                    for (String item : currentRun) {
                        writers[fileIndex].write(item);
                        writers[fileIndex].newLine();
                    }
                }

                // Close all writers
                for (BufferedWriter writer : writers) {
                    writer.close();
                }

                boolean finished = false;
                // When finished isnt true itll continue to merge until then
                while (!finished) {
                    m = 2 * m;

                    if (mergeRuns("input_file1", "input_file2", "output_file1", "output_file2", m) == 1) {
                        finished = true;
                    }
                }

                boolean deleteFiles = false;

                // Once finished merging call the final merge function
                if (finished) {
                    try (BufferedWriter finalWriter = new BufferedWriter(new FileWriter("moby.sorted"))) {
                        finalMerge("output_file1", "output_file2", finalWriter);
                        deleteFiles = !deleteFiles;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Delete files after finalMerge to tidy up directory
                if(deleteFiles){
                    File outputFile1 = new File("output_file1");
                    File outputFile2 = new File("output_file2");
                    File inputFile2 = new File("input_file2");
                    File inputFile1 = new File("input_file1");

                    outputFile1.delete();
                    outputFile2.delete();
                    inputFile2.delete();
                    inputFile1.delete();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Merge runs from input files into output files
    private static int mergeRuns(String inputFile1, String inputFile2, String outputFile1, String outputFile2, int m) {
        int totalMerges = 0;
        try (BufferedReader inputReader1 = new BufferedReader(new FileReader(inputFile1));
             BufferedReader inputReader2 = new BufferedReader(new FileReader(inputFile2));
             BufferedWriter outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));
             BufferedWriter outputWriter2 = new BufferedWriter(new FileWriter(outputFile2))) {

            PriorityQueue<String> pq = new PriorityQueue<>();
            String lineInput1 = inputReader1.readLine();
            String lineInput2 = inputReader2.readLine();

            boolean altFile = true;

            while (lineInput1 != null || lineInput2 != null) {
                if (lineInput1 != null)
                    pq.offer(lineInput1);
                if (lineInput2 != null)
                    pq.offer(lineInput2);

                if (pq.size() >= 2 * m || (lineInput1 == null && lineInput2 == null)) {
                    // Write merged and sorted runs alternatively to output files
                    while (!pq.isEmpty()) {
                        if (altFile) {
                            outputWriter1.write(pq.poll());
                            outputWriter1.newLine();
                        } else {
                            outputWriter2.write(pq.poll());
                            outputWriter2.newLine();
                        }
                        altFile = !altFile;
                    }
                    totalMerges++;

                    // Flush writers
                    outputWriter1.flush();
                    outputWriter2.flush();
                }

                // Read next lines if available
                if (lineInput1 != null)
                    lineInput1 = inputReader1.readLine();
                if (lineInput2 != null)
                    lineInput2 = inputReader2.readLine();
            }

            // Merge the remaining elements in the priority queue
            while (!pq.isEmpty()) {
                String element = pq.poll();
                if (altFile) {
                    outputWriter1.write(element);
                    outputWriter1.newLine();
                } else {
                    outputWriter2.write(element);
                    outputWriter2.newLine();
                }
                altFile = !altFile;
            }
            totalMerges++;

            // Flush writers
            outputWriter1.flush();
            outputWriter2.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalMerges;
    }

    // Final merge of sorted runs into a single output file
    private static void finalMerge(String outputFile1, String outputFile2, BufferedWriter finalMerge) {
        try (BufferedReader finalOutput1 = new BufferedReader(new FileReader(outputFile1));
             BufferedReader finalOutput2 = new BufferedReader(new FileReader(outputFile2))) {
            
            String line1 = finalOutput1.readLine();
            String line2 = finalOutput2.readLine();
    
            // Perform merge until both files are exhausted
            while (line1 != null || line2 != null) {
                // If both lines are not null, compare them and write the smaller one
                if (line1 != null && line2 != null) {
                    if (line1.compareTo(line2) <= 0) {
                        finalMerge.write(line1);
                        finalMerge.newLine();
                        line1 = finalOutput1.readLine();
                    } else {
                        finalMerge.write(line2);
                        finalMerge.newLine();
                        line2 = finalOutput2.readLine();
                    }
                } 
                // If one line is null, write the non-null line and read the next line from the respective file
                else if (line1 != null) {
                    finalMerge.write(line1);
                    finalMerge.newLine();
                    line1 = finalOutput1.readLine();
                } else if (line2 != null) {
                    finalMerge.write(line2);
                    finalMerge.newLine();
                    line2 = finalOutput2.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}