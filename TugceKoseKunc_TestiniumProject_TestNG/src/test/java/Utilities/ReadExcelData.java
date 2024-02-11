package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcelData extends BaseDriver {


    private String keywordShort;
    private String keywordShirt;

    public ReadExcelData(WebDriver fileP) {
        try {
            FileInputStream file = new FileInputStream("src/main/resources/ProductList.xlsx");

            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);

            keywordShort = readCell(sheet, 0, 0);
            keywordShirt = readCell(sheet, 0, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readCell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        return cell.getStringCellValue();
    }

    public String getKeywordShort() {
        return keywordShort;
    }

    public String getKeywordShirt() {
        return keywordShirt;
    }



}

