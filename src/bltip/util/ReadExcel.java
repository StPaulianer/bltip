package bltip.util;

import com.google.common.io.PatternFilenameFilter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;

/**
 * Created by nico on 26.08.16.
 */
public class ReadExcel {
    public static void main(String[] args) throws IOException {
        File tips = new File("/Users/nico/Documents/BLTipp/BLTipp_2016_17/Tipps");
        File[] files = tips.listFiles(new PatternFilenameFilter(".*xls"));
        for (File file : files) {
            String fileName = file.getAbsolutePath();
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);

            HSSFFont defaultFont = createFont(wb, IndexedColors.BLACK.getIndex(), false);

            HSSFCellStyle jokerStyle = wb.createCellStyle();
            HSSFFont jokerFont = createFont(wb, IndexedColors.BLACK.getIndex(), true);
            jokerStyle.setFont(jokerFont);
            jokerStyle.setAlignment(ALIGN_CENTER);
            setBorder(jokerStyle);
            HSSFCellStyle deluxeJokerStyle = wb.createCellStyle();
            HSSFFont deluxeJokerFont = createFont(wb, IndexedColors.DARK_RED.getIndex(), true);
            deluxeJokerStyle.setFont(deluxeJokerFont);
            deluxeJokerStyle.setAlignment(ALIGN_CENTER);
            setBorder(deluxeJokerStyle);

            int rows = sheet.getPhysicalNumberOfRows();
            int cols = getNoOfCols(sheet, rows);
            for (int r = 0; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                if (row != null) {
                    for (int c = 0; c < cols; c++) {
                        HSSFCell cell = row.getCell(c);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            HSSFCell cellToChange = row.getCell(c - 1);
                            if (cell.getStringCellValue().equalsIgnoreCase("x")) {
                                cellToChange.setCellStyle(jokerStyle);
                                System.out.println("j: " + cellToChange.getStringCellValue());
                            } else if (cell.getStringCellValue().equalsIgnoreCase("xx")) {
                                cellToChange.setCellStyle(deluxeJokerStyle);
                                System.out.println("dj: " + cellToChange.getStringCellValue());
                            }
                        }
                    }
                }
            }

            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
        }
    }

    private static void setBorder(HSSFCellStyle jokerStyle) {
        jokerStyle.setBorderBottom((short) 1);
        jokerStyle.setBorderLeft((short) 1);
        jokerStyle.setBorderRight((short) 1);
        jokerStyle.setBorderTop((short) 1);
    }

    private static int getNoOfCols(HSSFSheet sheet, int rows) {
        HSSFRow row;
        int cols = 0; // No of columns
        int tmp;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) cols = tmp;
            }
        }
        return cols;
    }

    private static HSSFFont createFont(HSSFWorkbook wb, short color, boolean bold) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setColor(color);
        font.setBold(bold);
        font.setItalic(false);
        return font;
    }
}
