import java.io.*;

public class ConcatEveryTwoColumns {
    public static void main(String[] args) {
        // Input and output file paths
        String inputFile = "C:\\Users\\dell\\Downloads\\Input.csv"; // Input file path
        String outputFile = "C:\\Users\\dell\\Downloads\\Output9.csv"; // Output file path


        try(BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile)))  {

            String line;
            boolean isHeader = true;
            line = br.readLine();
            while (line != null) {
                String[] columns = line.split(",");   // "naushad, alam,wert,asdh"

                StringBuilder outputLine = new StringBuilder();

                // Process header line to remove spaces in column names
                for (int i = 0; i < columns.length - 1; i += 2) {
                    String concatenated;
                    if (isHeader) {
                        // " sur name ".trim() => "sur name".replace(" ","")  => surname
                        concatenated = columns[i].trim().replace(" ", "") + "_"
                                + columns[i + 1].trim().replace(" ", "");
                    } else {
                        concatenated = columns[i].trim() + " " + columns[i + 1].trim();
                    }
                    outputLine.append(concatenated);
                    if (i + 2 < columns.length) {
                        outputLine.append(",");
                    }
                }
                isHeader = false;

                // Write the processed line to the output file
                bw.write(outputLine.toString());
                bw.newLine();
                // read new line
                line = br.readLine();
            }

            System.out.println("CSV file processed successfully. Output written to " + outputFile);

        } catch (IOException e) {
            System.err.println("An error occurred while processing the file: " + e.getMessage());
        }
    }
}
