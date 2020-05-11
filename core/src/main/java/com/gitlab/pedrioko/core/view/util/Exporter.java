package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Exporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class);

    public static byte[] BuildExcel(List<?> list) {
        if (list.isEmpty())
            return null;
        try (HSSFWorkbook wb = createWorkbook(list)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("ERROR on BuildExcel()", e);
            return null;
        }

    }

    public static byte[] BuildExcel(List<Object[]> list, List<String> headers) {
        if (list.isEmpty())
            return null;
        try (HSSFWorkbook wb = createWorkbook(list, headers)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("ERROR on BuildExcel()", e);
            return null;
        }

    }

    private static HSSFWorkbook createWorkbook(List<Object[]> list, List<String> headers) {
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet("sheet");
            load(headers, sheet);
            for (int r = 1; r < list.size() + 1; r++) {
                HSSFRow row = sheet.createRow(r);
                Object[] o = list.get(r - 1);
                for (int c = 0; c < headers.size(); c++) {
                    HSSFCell cell = row.createCell(c);
                    cell.setCellValue("" + o[c]);
                }
            }
            return wb;
        } catch (IOException e) {
            LOGGER.error("ERROR on createWorkbook()", e);
            return null;
        }
    }

    private static void load(List<String> headers, HSSFSheet sheet) {
        for (int r = 0; r < 1; r++) {
            HSSFRow row = sheet.createRow(r);
            for (int c = 0; c < headers.size(); c++) {
                HSSFCell cell = row.createCell(c);
                cell.setCellValue(StringUtil.getCapitalize(headers.get(c)));
            }
        }
    }

    private static HSSFWorkbook createWorkbook(List<?> list) {
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet("sheet");
            List<String> fieldTable = getListFiled(list);
            load(fieldTable, sheet);
            for (int r = 1; r < list.size() + 1; r++) {
                HSSFRow row = sheet.createRow(r);
                Object o = list.get(r - 1);
                for (int c = 0; c < fieldTable.size(); c++) {
                    Object valueFieldObject = ReflectionJavaUtil.getValueFieldObject(fieldTable.get(c), o);
                    HSSFCell cell = row.createCell(c);
                    cell.setCellValue("" + valueFieldObject);
                }
            }
            return wb;
        } catch (IOException e) {
            LOGGER.error("ERROR on createWorkbook()", e);
            return null;
        }
    }

    public static byte[] BuildCSV(List<?> list) {
        if (list.isEmpty())
            return null;
        return echoAsCSV(createWorkbook(list).getSheetAt(0)).getBytes();
    }

    public static byte[] BuildPDF(List<?> list) {
        if (list.isEmpty())
            return null;
        try {
            HSSFSheet sheet = createWorkbook(list).getSheetAt(0);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Document iText_xls_2_pdf = new Document();
            PdfWriter.getInstance(iText_xls_2_pdf, bos);
            iText_xls_2_pdf.open();
            List<String> fieldTable = getListFiled(list);
            if (fieldTable.isEmpty())
                fieldTable = ReflectionJavaUtil.getFieldsNames(list.get(0).getClass());
            PdfPTable my_table = new PdfPTable(fieldTable.size());
            PdfPCell table_cell;
            HSSFRow row = null;
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);
                    table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
                    my_table.addCell(table_cell);
                }

            }
            iText_xls_2_pdf.add(my_table);
            iText_xls_2_pdf.add(new Chunk(""));
            iText_xls_2_pdf.close();
            return bos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("ERROR on BuildPDF()", e);
            return null;
        }
    }

    private static List<String> getListFiled(List<?> list) {
        Class<?> elementType = list.get(0).getClass();
        List<String> fieldTable = ApplicationContextUtils.getBean(PropertiesUtil.class).getFieldTable(elementType);
        return fieldTable.isEmpty() ? ReflectionJavaUtil.getFieldsNames(list.get(0).getClass()) : fieldTable;
    }

    private static String echoAsCSV(HSSFSheet sheet) {
        StringBuilder sb = new StringBuilder();
        HSSFRow row = null;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                sb.append("" + row.getCell(j) + ";");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
