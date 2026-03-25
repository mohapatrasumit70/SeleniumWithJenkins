package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtil - Apache POI-based Excel reader for data-driven testing
 */
public class ExcelUtil {

    private static final Logger log = LogManager.getLogger(ExcelUtil.class);

    private ExcelUtil() {}

    /**
     * Read all data from a sheet as 2D Object array (for TestNG DataProvider)
     */
    public static Object[][] getExcelData(String filePath, String sheetName) {
        Object[][] data = null;

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in: " + filePath);
            }

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            data = new Object[rowCount][colCount];

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j] = getCellValue(cell);
                }
            }
            log.info("Read {} rows from '{}' in '{}'", rowCount, sheetName, filePath);

        } catch (IOException e) {
            log.error("Failed to read Excel: {}", e.getMessage());
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    /**
     * Read sheet as List of Maps (column header -> value)
     */
    public static List<Map<String, String>> getExcelDataAsMaps(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < colCount; j++) {
                    String header = getCellValue(headerRow.getCell(j));
                    String value = getCellValue(row.getCell(j));
                    rowData.put(header, value);
                }
                dataList.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }

        return dataList;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
