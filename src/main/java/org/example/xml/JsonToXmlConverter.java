package org.example.xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonToXmlConverter {

    public static void main(String[] args) {
        // JSON文件路径
        String jsonFilePath = "out/output.json";

        // 输出XML文件路径
        String outputXmlPath = "out/output.xml";

        try {
            // 读取JSON文件内容
            FileReader fileReader = new FileReader(jsonFilePath);
            StringBuilder jsonContent = new StringBuilder();
            int ch;
            while ((ch = fileReader.read()) != -1) {
                jsonContent.append((char) ch);
            }
            fileReader.close();

            // 将JSON字符串转换为JSONArray
            JSONArray jsonArray = new JSONArray(jsonContent.toString());

            // 将JSONArray转换为XML字符串
            String xmlContent = XML.toString(jsonArray);

            // 创建out文件夹（如果不存在的话）
            File outDir = new File("out");
            if (!outDir.exists()) {
                outDir.mkdir();
            }

            // 将XML字符串写入到输出文件
            FileWriter fileWriter = new FileWriter(outputXmlPath);
            // 添加XML声明
//            fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fileWriter.write("<root>\n" + xmlContent + "\n</root>"); // 包裹XML内容
            fileWriter.close();

            System.out.println("JSON文件已成功转换为XML并保存到 " + outputXmlPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
