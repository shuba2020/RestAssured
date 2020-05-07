package com.src.Marqeta.utils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class CommonUtilities {

    private static XSSFSheet ExcelWSheet=null;

    private static XSSFWorkbook ExcelWBook=null;

    private static XSSFCell Cell=null;

    private static XSSFRow Row=null;

    public static Object[][] getTableArray(String FilePath, String SheetName)
            throws Exception {

        String[][] tabArray = null;

        int commentRows = 0;

        try {

            FileInputStream ExcelFile = new FileInputStream(FilePath);

            // Access the required test data sheet

            ExcelWBook = new XSSFWorkbook(ExcelFile);

            ExcelWSheet = ExcelWBook.getSheet(SheetName);

            int startRow = 1;

            int startCol = 0;

            int ci, cj;

            int totalRows = ExcelWSheet.getLastRowNum();

            // you can write a function as well to get Column count

            int totalCols = ExcelWSheet.getRow(0).getLastCellNum();

            tabArray = new String[totalRows][totalCols];

            ci = 0;

            for (int i = startRow; i <= totalRows; i++) {

                cj = 0;

                String firstRowData = getCellData(i, 0);
                if ((firstRowData != null)
                        && firstRowData.trim().startsWith("#")) {
                    commentRows++;
                    continue;
                }

                for (int j = startCol; j <= totalCols - 1; j++, cj++) {

                    //Encoding Authorization header
                    String cellData = getCellData(i, j);
                    if (cellData != null && !cellData.startsWith("processList=") && cellData.contains("Authorization,")) {

                        cellData = Base64.getEncoder().encodeToString(cellData.getBytes());
                    }

                    tabArray[ci][cj] = cellData;
                    // System.out.println(tabArray[ci][cj]);

                }

                ci++;

            }

        }

        catch (FileNotFoundException e) {

            System.out.println("Could not read the Excel sheet");

            e.printStackTrace();

        }

        catch (IOException e) {

            System.out.println("Could not read the Excel sheet");

            e.printStackTrace();

        }

        if (commentRows > 0) {
            tabArray = Arrays.copyOf(tabArray, tabArray.length - commentRows);
        }

        return (tabArray);

    }

    public static String getCellData(int RowNum, int ColNum) throws Exception {

        String CellData = null;
        if (ExcelWSheet.getRow(RowNum).getCell(ColNum) != null) {
            try {

                Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);

                int dataType = Cell.getCellType();

                if (dataType == 3) {

                    return "";

                } else if (dataType == 4) {
                    CellData = "" + Cell.getBooleanCellValue();
                    return CellData;
                } else {

                    CellData = Cell.getStringCellValue();
                    return CellData;
                }
            } catch (Exception e) {

                System.out.println(e.getMessage());

                throw (e);

            }
        } else {
            return null;
        }

    }

}
