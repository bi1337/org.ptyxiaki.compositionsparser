package org.ptyxiaki.compositionsparser.datamodel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.Formula;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Export parser's data to an Excel file.
 * 
 * @author hitman
 *
 */
public class ExcelOut implements DataOut {
	private Sheet sheet;
	private FileOutputStream fileOut;
	private Workbook workbook;
	private ArrayList<UnitElement> units;
	private double[][] interactionMatrix;

	/**
	 * Parameterized constructor.
	 * 
	 * @param String filename
	 * @param String wd the working directory
	 * @throws FileNotFoundException
	 */
	public ExcelOut(String filename, String wd) throws FileNotFoundException {
		// Create a Workbook
		workbook = new SXSSFWorkbook();
		System.out.println("opening workbook");
		System.out.flush();
		Path path = Paths.get(wd, filename + ".xls");
		fileOut = new FileOutputStream(path.toString());
	}

	/**
	 * Create a sheet with the metrics for each class's interactions. Problematic
	 * composites are highlighted with red font.
	 * 
	 * @param ProjectElement proj the project we are working on.
	 */
	@Override
	public void putTable(ProjectElement proj) {
		// Create a Sheet
		sheet = workbook.createSheet(proj.getProject().getName());

		int matrixSize = proj.getUnitsSize();
		interactionMatrix = new double[matrixSize][matrixSize];
		for (int i = 0; i < matrixSize; i++)
			for (int j = 0; j < matrixSize; j++)
				interactionMatrix[i][j] = -1;

		units = proj.getUnits();
		// for (PackageElement p : proj.getPackages()) {
		for (UnitElement u : units) {
			for (UnitElement component : u.getComponents()) {
				interactionMatrix[units.indexOf(component)][units.indexOf(u)] = component.getMetric("lcomhs");
			}
			// }
		}

		// Create a Font for styling header cells for bad components
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		// headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font for bad components
		CellStyle badComponentsCellStyle = workbook.createCellStyle();
		badComponentsCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		// Cell cell = headerRow.createCell(0);
		for (int i = 0; i < units.size(); i++) {
			Cell cell = headerRow.createCell(i + 1);
			cell.setCellValue(units.get(i).getElementName() + "[" + units.get(i).getMetric("lcomhs") + "]");
			if (units.get(i).hasBadComponents())
				cell.setCellStyle(badComponentsCellStyle);
		}

		for (int i = 0; i < units.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(units.get(i).getElementName());
			for (int j = 0; j < units.size(); j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(interactionMatrix[i][j]);
			}
		}

		putLcomGZ(proj);
		printIsCompositeOf(proj);
		close();
	}

	/**
	 * 
	 */
	@Override
	public void putLcomGZ(ProjectElement proj) {
		Sheet sheet = workbook.createSheet("LcomGZ");
		Row row;
		Cell cell;

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("Class");
		cell.setCellStyle(headerCellStyle);
		cell = row.createCell(1);
		cell.setCellValue("Lcom-GZ");
		cell.setCellStyle(headerCellStyle);
		cell = row.createCell(2);
		cell.setCellValue("Lcom-HS");
		cell.setCellStyle(headerCellStyle);
		cell = row.createCell(3);
		cell.setCellValue("Difference");
		cell.setCellStyle(headerCellStyle);

		int nextRow = 1;
		for (int i = 0; i < units.size(); i++) {
			if (units.get(i).hasBadComponents()) {
				row = sheet.createRow(nextRow);
				nextRow++;
				cell = row.createCell(0);
				cell.setCellValue(units.get(i).getElementName());
				cell = row.createCell(1);
				cell.setCellValue(units.get(i).getMetric("lcomgz"));
				cell = row.createCell(2);
				cell.setCellValue(units.get(i).getMetric("lcomhs"));
				cell = row.createCell(3);
			}
		}
	}

	private void printIsCompositeOf(ProjectElement project) {
		Sheet sheet = workbook.createSheet("Is Composite Of");
		Row row;
		Cell cell;
		int matrixSize = project.getUnitsSize();

		for (int i = 0; i < matrixSize; i++)
			for (int j = 0; j < matrixSize; j++)
				interactionMatrix[i][j] = -1;

		for (UnitElement component : units)
			for (UnitElement composite : component.getComposites())
				interactionMatrix[units.indexOf(composite)][units.indexOf(component)] = composite.getMetric("lcomhs");

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Font componentFont = workbook.createFont();
		componentFont.setBold(true);
		componentFont.setColor(IndexedColors.RED.getIndex());
		CellStyle componentStyle = workbook.createCellStyle();
		componentStyle.setFont(componentFont);

		row = sheet.createRow(0);

		for (int i = 0; i < units.size(); i++) {
			cell = row.createCell(i + 1);
			cell.setCellValue(units.get(i).getElementName() + "[" + units.get(i).getMetric("lcomhs") + "]");
			if (units.get(i).isComponent())
				cell.setCellStyle(componentStyle);
			else
				cell.setCellStyle(headerCellStyle);
		}

		for (int i = 0; i < units.size(); i++) {
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(units.get(i).getElementName());
			cell.setCellStyle(headerCellStyle);
			for (int j = 0; j < units.size(); j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(interactionMatrix[i][j]);
			}
		}
	}

	@Override
	public void close() {
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("haha");
		System.out.flush();
	}
	
	public void putExtremes(List<BadComponent> badComponents) {
		Font headerFont = workbook.createFont();
		Font boldFont = workbook.createFont();
		headerFont.setBold(true);
		boldFont.setBold(true);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerFont.setColor(IndexedColors.RED.getIndex());
		
		CellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);
		
		for (BadComponent component : badComponents) {
			Sheet sheet = workbook.createSheet(component.getElementName());
			/* Component name */
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue(component.getElementName());
			cell.setCellStyle(headerCellStyle);
			/* Component LCOM */
			cell = row.createCell(1);
			cell.setCellValue(component.getMetric("lcomhs"));
			cell.setCellStyle(headerCellStyle);
			
			int i = 1;
			for (UnitElement composite : component.getComposites()) {
				/* Composite name */
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(composite.getElementName());
				cell.setCellStyle(boldCellStyle);
				/* Composite LCOM */
				cell = row.createCell(1);
				cell.setCellValue(composite.getMetric("lcomhs"));
				i++;
			}
		}
		this.close();
	}

}
