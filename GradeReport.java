import java.io.*;
import java.util.*;

public class GradeReport {
    //ArrayLists for storing data and information
    List<StudentData> studentData = new ArrayList<>();
    List<StudentInfo> studentInfo = new ArrayList<>();
    List<String> errors = new ArrayList<>();

    //variables
    private int columnCount;
    private double classTotalAverage;
    private double highestAverage;
    private String topStudent;

    //Reading the file
    public void fileReader(String filePath) throws FileNotFoundException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;

            while((line = reader.readLine()) != null) {
                // System.out.println("Stage 1");
                dataValidater(lineNumber, line);
                lineNumber++;
            }
        }
    }

    private void dataValidater(int lineNumber, String line) {
        // System.out.println("Stage 2");
        String[] terms = line.split(",");
        boolean hasError = false;
        

        if(lineNumber == 1) {
            columnCount = terms.length;
        }else {
            if(terms.length < columnCount) {
                errors.add("Line " + lineNumber + ": Some data has been missed!");
            }else if(terms.length > columnCount) {
                errors.add("Line " + lineNumber + ": Some redundancy data is available");
            }else {
                for(int i = 1; i < terms.length; i++) {
                    try {
                        Integer.parseInt(terms[i]);
                    } catch (NumberFormatException e) {
                        errors.add("Line " + lineNumber + ": Invalid number in \"" + terms[i] + "\"");
                        hasError = true;
                        continue;
                    }
                }
                // System.out.println("Done");
            
                if(!hasError) {
                    studentData.add(
                        new StudentData(
                            terms[0], 
                            Integer.parseInt(terms[1]),
                            Integer.parseInt(terms[2]), 
                            Integer.parseInt(terms[3])));
                }
            }
        }
    }
    public void processor() {
        highestAverage = studentData.get(0).average();
        topStudent = studentData.get(0).name();

        for(StudentData data : studentData) {
            char grade;
            double average = data.average();

            if(average > 90) {
                grade = 'A';
            }else if(average > 80) {
                grade = 'B';
            }else if(average > 70) {
                grade = 'C';
            }else if(average > 60) {
                grade = 'D';
            }else{
                grade = 'F';
            }

            if(highestAverage < average) {
                highestAverage = average;
                topStudent = data.name();
            }

            studentInfo.add(new StudentInfo(data.name(), average, grade));
            classTotalAverage += average;
        }
    }

    public String reportGenerator(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("---------- Final Report ----------\n\n");
            writer.write(String.format("%-10s | %-10s | %-10s%n", "Name", "Average", "Grade"));
            writer.write("-------------------------------\n");

            for(StudentInfo info : studentInfo) {
                writer.write(String.format("%-10s | %-10.2f | %-10c%n", info.name(), info.average(), info.grade()));
            }

            writer.write("\n");
            writer.write(String.format("** Class Average: %.2f%n", classTotalAverage / studentInfo.size()));
            writer.write(String.format("** Top Student: \"%s\"", topStudent));

            if(errorReportGenerator()) {
                writer.write("\n\nImportant!\n\t*** An error report has been generated!");
            }else{
                writer.write("\n\n** No errors have been found!");
            }

            return "Report Generation Successfully";
        }
    }

    private boolean errorReportGenerator() throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("errors/report_1.txt"))) {
            if(!errors.isEmpty()) {
                for(String error : errors) {
                    writer.write(String.format("*%s%n", error));
                }
                return true;
            }else{
                Writer.nullWriter();
                return false;
            }
        }
    }
}

