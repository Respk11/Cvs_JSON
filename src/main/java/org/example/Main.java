package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        createCSV("1,John,Smith,USA,25");
        addInCSV("2,Inav,Petrov,RU,23");
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee>list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
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

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static List<Employee> parseCSV(String[] strings, String fileName) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(fileName));
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(strings);
        CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                .withMappingStrategy(strategy)
                .build();
        List<Employee> list = csv.parse();
        return list;
    }

    private static void addInCSV(String string) {
        String[] employee = string.split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void createCSV(String string) {
        String[] employee = string.split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv"))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}