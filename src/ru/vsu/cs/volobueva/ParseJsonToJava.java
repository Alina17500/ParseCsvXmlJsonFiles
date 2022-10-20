package ru.vsu.cs.volobueva;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseJsonToJava {
    public static void parseJsonToJava(String fileNameJson) {
        //получаем json из файла
        String jsonFile = readString(fileNameJson);
        //преобразовываем полученный json в список сотрудников
        List<Employee> list = jsonToList(jsonFile);
        list.forEach(System.out::println);
    }

    //инфа из файла типа json в лист
    private static List<Employee> jsonToList(String fileJson) {
        List<Employee> list = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Object object = new JSONParser().parse(fileJson);
            JSONArray array = (JSONArray) object;
            for (Object value : array) {
                list.add(gson.fromJson(value.toString(), Employee.class));
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    private static String readString(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String value;
            while ((value = reader.readLine()) != null) {
                stringBuilder.append(value).append("\n");
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return stringBuilder.toString();
    }
}
