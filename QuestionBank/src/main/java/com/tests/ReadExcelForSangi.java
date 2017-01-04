package com.tests;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class ReadExcelForSangi {
	public static void main(String[] args) {
		try {

			InputStream fileInputStream = new FileInputStream("C:\\Users\\Shasi\\Downloads\\QuestionPaper_Template.xls");
			int sheetNo = 2;


			HSSFWorkbook workBook = new HSSFWorkbook(fileInputStream);
			DataFormatter formater = new DataFormatter();

			int noOfSheets = workBook.getNumberOfSheets();
			System.out.println("Total sheets in given workbook : " + noOfSheets);

			String sheetName = workBook.getSheetName(sheetNo);
			System.out.println("\nSheet Name is : " + sheetName);

			HSSFSheet sheet = workBook.getSheetAt(sheetNo);
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String unit = formater.formatCellValue(cell);
					System.out.print(unit + "\t");
				}
				System.out.println("\n");
			}
		} catch(Exception e) {
			System.out.println("Exception reading excel file "  + e);
		}
	}
}
