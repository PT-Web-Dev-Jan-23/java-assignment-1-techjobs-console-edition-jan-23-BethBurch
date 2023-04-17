import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        // Bonus mission: sort the results
        Collections.sort(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        // Bonus mission; normal version returns allJobs
        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column).toLowerCase();

            if (aValue.contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {
// this method(function) takes the String from the search input and then returns an array of hashmaps that in the String input type

        // load data, if not already loaded
        loadData();
//        we  will be iterating through the allJobs
//        create a variable to hold out new search results before creating the loop
        ArrayList<HashMap<String, String>> foundValues = new ArrayList<>();
        //ArrayList is a class in Java's java.util package || <HashMap<String, String>>: This is a parameterized type that specifies the type of objects that the ArrayList will hold.
        // In this case, it is a HashMap object that maps String keys to String values
        //foundValues is the variable I created
        // new ArrayList<>(): This is an instantiation of the ArrayList class. It creates a new, empty ArrayList object of the specified
        // parameterized type (HashMap<String, String> in this case)
        // and assigns it to the foundValues variable
        for (HashMap<String, String> job : allJobs) {
//            iterates over a collection of HashMap<String, String> objects named allJobs. On each iteration,
//            it assigns the current element of the collection to the variable job.
            for (Map.Entry<String, String> entry : job.entrySet()) {
//                iterates over the entrySet() of the HashMap object job. The entrySet() method returns a set of all the key-value pairs in the HashMap,
//                represented as Map.Entry objects. On each iteration, it assigns the current Map.Entry object to the variable entry
                if (entry.getValue().toLowerCase().contains(value.toLowerCase())) {
//                    checks if the value of the current entry object (which is a String) contains the value String passed to the method
                    foundValues.add(job);
//                    if statement is true, it adds the current job object to the foundValues ArrayList.
                    break;
//                   exits the inner for-each loop once a match is found, since we only want to add each job object to foundValues once.
//                   Once a match is found, there is no need to continue searching for matches in the same job.
                }
            }
        }

return foundValues;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
