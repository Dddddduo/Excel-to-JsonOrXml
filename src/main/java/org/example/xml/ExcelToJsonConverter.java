package org.example.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; // 用于处理 .xls 文件
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // 用于处理 .xlsx 文件

import java.io.*;
import java.util.*;

public class ExcelToJsonConverter {

    // 读取Excel文件并转化为JSON格式
    public static String convertExcelToJson(String excelFilePath) throws IOException {
        // 创建输入流读取Excel文件
        FileInputStream fileInputStream = new FileInputStream(excelFilePath);

        // 创建Workbook对象来处理不同格式的Excel文件
        Workbook workbook = null;
        if (excelFilePath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(fileInputStream);  // 处理 .xlsx 文件
        } else if (excelFilePath.endsWith(".xls")) {
            workbook = new HSSFWorkbook(fileInputStream);  // 处理 .xls 文件
        } else {
            throw new IllegalArgumentException("Unsupported Excel file format.");
        }

        // 获取第一个sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 获取列头
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.getStringCellValue());
        }

        // 处理Excel的每一行数据
        List<Map<String, String>> rowsData = new ArrayList<>();
        for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {  // 从1开始，因为第0行是标题
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Map<String, String> rowData = new HashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.getCell(j);
                String cellValue = getCellValue(cell);
                rowData.put(headers.get(j), cellValue);
            }
            rowsData.add(rowData);
        }

        // 将数据转化为JSON格式
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rowsData);
    }

    // 获取单元格的值
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // 将JSON写入文件
    public static void saveJsonToFile(String json, String outputFilePath) throws IOException {
        // 确保目标目录存在，如果不存在则创建
        File outputFile = new File(outputFilePath);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        // 写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(json);
        }
    }

    public static void main(String[] args) {
        try {
            // 请替换为你的Excel文件路径
            String excelFilePath = "C:\\Users\\Administrator\\Desktop\\bhuacm(1).xls";
            String jsonResult = convertExcelToJson(excelFilePath);

            // 生成输出文件路径，保存到项目的 out 文件夹
            String outputFilePath = "out/output.json";
            saveJsonToFile(jsonResult, outputFilePath);

            System.out.println("JSON 数据已保存到: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
