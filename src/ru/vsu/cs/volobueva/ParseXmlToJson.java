package ru.vsu.cs.volobueva;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParseXmlToJson {
    public static void parseXmlToJson(String fileNameXml) throws ParserConfigurationException, IOException, SAXException {
        //получаем список сотрудников из xml файла
        List<Employee> list2 = parseXmlToList(fileNameXml);
        //полученный список преобразуем в строку json
        String jsonFile2 = listToJson(list2);
        //определяем json файл, в который запишем инфу
        String fileNameJson2 = "data2.json";
        //полученную информацию запишем в json-файл
        writeString(jsonFile2, fileNameJson2);
    }

    private static List<Employee> parseXmlToList(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        List<String> listElements = new ArrayList<>();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(fileName));
        //получаем из объекта Document document корневой узел Node
        Node root = document.getDocumentElement();
        //из корневого узла извлекаем список узлов nodeList
        NodeList nodeList = root.getChildNodes();
        //проходим по списку узлов и получаем элементы
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("employee")) {
                NodeList nodeList1 = node.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node1 = nodeList1.item(j);
                    if (Node.ELEMENT_NODE == node1.getNodeType()) {
                        listElements.add(node1.getTextContent());
                    }
                }
                //у элементов получили значения, с помощью которых создаем экземпляр класса Employee
                list.add(new Employee(
                        Long.parseLong(listElements.get(0)),
                        listElements.get(1),
                        listElements.get(2),
                        listElements.get(3),
                        Integer.parseInt(listElements.get(4))));
                listElements.clear();
            }
        }
        //возвращаем список сотрудников
        return list;
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
