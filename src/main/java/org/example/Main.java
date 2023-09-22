package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        String fileName = "data.csv";
        createCSV("1,John,Smith,USA,25", fileName);
        addInCSV("2,Inav,Petrov,RU,23", fileName);
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee>listEmployee = parseCSV(columnMapping, fileName);
        String json = listToJson(listEmployee);
        System.out.println(json);
        writeString(json);
    }

    public static void writeString(String json) {
        try {
            FileWriter fileWriter = new FileWriter("data.json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String listToJson(List<Employee> listEmployee) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listEmployeeType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(listEmployee, listEmployeeType);
        return json;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(fileName));
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);
        CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                .withMappingStrategy(strategy)
                .build();
        List<Employee> list = csv.parse();
        return list;
    }

    private static void addInCSV(String infoEmployee, String fileName) {
        String[] employee = infoEmployee.split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName, true))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void createCSV(String infoEmployee, String fileName) {
        String[] employee = infoEmployee.split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}