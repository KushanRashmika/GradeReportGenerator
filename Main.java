import java.io.*;

public class Main {
    public static void main(String[] args) {
        String sourceFile = "data/grades.csv";
        String destinationFile = "report.txt";

        GradeReport report = new GradeReport();

        try {
            report.fileReader(sourceFile);
            report.processor();
            System.out.println(report.reportGenerator(destinationFile));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }
}
