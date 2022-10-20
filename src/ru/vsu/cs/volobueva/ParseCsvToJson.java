package ru.vsu.cs.volobueva;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ParseCsvToJson {
    public static void parseCsvToJson(String[] columnMapping, String fileNameCsv) {
        //получаем список сотрудников из csv файла
        List<Employee> list1 = parseCsvToList(columnMapping,fileNameCsv);
        //полученный список преобразуем в строку json
        String jsonFile1 = listToJson(list1);
        //определяем json файл, в который запишем инфу
        String fileNameJson1 = "data1.json";
        //полученную информацию запишем в json-файл
        writeString(jsonFile1, fileNameJson1);
    }

    private static List<Employee> parseCsvToList(String[] columnDataBase, String fileName) {
        //создадим конструктор CSVReader, в который передадим передадим файловый ридер (FileReader) файла fileName(т.е. созданный нами файл data.csv)
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> mappingStrategy = new ColumnPositionMappingStrategy<>();
            //укажем тип
            mappingStrategy.setType(Employee.class);
            //укажем тип столбцов
            mappingStrategy.setColumnMapping(columnDataBase);
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(mappingStrategy).build();
            //csvToBean.parse() вернем список сотрудников
            return csvToBean.parse();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        //определим тип списка объектов, которые будем преобразовывать в Json
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        //получаем json, передав в качестве аргументов список сотрудников и тип списка
        return gson.toJson(list, listType);
    }

    private static void writeString(String jsonFile, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(jsonFile);
            writer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
