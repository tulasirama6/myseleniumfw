package one.tulasi.framework.dataprovider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;




public class ExcelManager
{	
	XSSFWorkbook workbook;
	XSSFSheet Sheet;
	XSSFRow row;
	XSSFCell cell;	
	HSSFWorkbook hworkbook;
	HSSFSheet hSheet;
	HSSFRow hrow;
	HSSFCell hcell;
	
	FileInputStream fis;
	int iIndex;
	ConfigManager app;
	private final Logger log = LogManager.getLogger("ExcelManager");
	//private String fileSeperator = System.getProperty("file.separator");
	
	
	/**
	 * Purpose- Constructor to pass Excel file name
	 * @param filePath
	 */
	public ExcelManager(String filePath)
	{
		try
		{
			File file = new File(filePath);
			System.setProperty("ExcelFilePath", filePath);
			if(file.exists())
			{
				if(isXLSX())
				{
					fis = new FileInputStream(filePath);
					workbook = new XSSFWorkbook(fis);
					fis.close();
				}
				else
				{
					fis = new FileInputStream(filePath);
					hworkbook = new HSSFWorkbook(fis);
					fis.close();
				}
			}
			else
			{
				log.error("File -'"+filePath+"' does not exist in Data Folder, Please Re-check given file");
				Assert.fail("File -'"+filePath+"' does not exist in Data Folder, Please Re-check given file");
			}		

		} 
		catch (Exception e)
		{			
			log.error("Exception occurred while accessing file - "+filePath+"\n"+e.getCause());
			Assert.fail("Exception occurred while accessing file - "+filePath+"\n"+e.getCause());
		} 
	}
	
	/**
	 * Purpose- To check if the sheet with given name exists or not
	 * @param sheetName- Sheet name should be provided
	 * @return - if sheet with specified name exists it returns true else it returns false
	 */
	public boolean isSheetExist(String sheetName) {
		if(isXLSX())
			iIndex = workbook.getSheetIndex(sheetName);
		else
			iIndex = hworkbook.getSheetIndex(sheetName);
		if(iIndex==-1)
			
		{
			Assert.fail("Sheet with name-'"+sheetName+"' doesn't exists in '"+app.getProperty("Excel.File")+"' excel file");
			return false;
		}
		else
			return true;
	}	
	
	/**
	 * Purpose- To get the row count of specified sheet
	 * @param sheetName- Sheet name should be provided
	 * @return- Returns value of row count
	 */
	public int getRowCount(String sheetName)
	{
		int number = 0;
		if(isSheetExist(sheetName))
		{
			if(isXLSX())
			{
				Sheet = workbook.getSheetAt(iIndex);
				number=Sheet.getLastRowNum()+1;
			}
			else
			{
				hSheet = hworkbook.getSheetAt(iIndex);
				number=hSheet.getLastRowNum()+1;
			}
		}
		return number; 
		
	}	
	
	/**
	 * Purpose- To get column count of specified sheet
	 * @param sheetName- Sheet name should be provided
	 * @return- Returns value of column count

	 */
	public int getColumnCount(String sheetName) {
		int count;
		if(isSheetExist(sheetName)) {
			if(isXLSX()) {
				Sheet = workbook.getSheet(sheetName);
				row = Sheet.getRow(0);
				if(row==null)
					return -1;
				count = row.getLastCellNum();		
			} else {
				hSheet = hworkbook.getSheet(sheetName);
				hrow = hSheet.getRow(0);
				if(hrow==null)
					return -1;
				count = hrow.getLastCellNum();		
			}
			return count;
		}
		return 0;		
	}
	
	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column name, row value
	 * @param sheetName - Sheet name should be provided
	 * @param colName- Column Name should be provided
	 * @param rowNum- Row value should be provided
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getCellDataXLSX(String sheetName,String colName,int rowNum)
	{
		try
		{	
			if(isSheetExist(sheetName))
			{			
				if(rowNum <=0)
				{
					Assert.fail("Row number should be greater than 0");
					return "";
				}
				int col_Num=-1;		
				Sheet = workbook.getSheetAt(iIndex);
				row=Sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++)
					{					
						if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())){
							col_Num=i;
							break;
						}
					}
				if(col_Num==-1)
					{
						Assert.fail("Column with specified name is not being displayed" + colName);
						return "";
					}
	
				row = Sheet.getRow(rowNum-1);
				if(row==null)
					return "";
				cell = row.getCell(col_Num);
			
				if(cell==null)
					return "";			
				if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				  return cell.getStringCellValue();
				else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA )
				{
					  String cellText  = String.valueOf(cell.getNumericCellValue());					  
					  if((cellText.contains(".")))
					  {
						  cellText  =  cell.getRawValue();
					  }
					  if (HSSFDateUtil.isCellDateFormatted(cell))
					  {
				           // format in form of D/M/YY
						  double d = cell.getNumericCellValue();
						  Calendar cal =Calendar.getInstance();
						  cal.setTime(HSSFDateUtil.getJavaDate(d));					  
						  int Year = cal.get(Calendar.YEAR);
						  int Day = cal.get(Calendar.DAY_OF_MONTH);
						  int Month = cal.get(Calendar.MONTH)+1;
				          cellText = Month  + "/" + Day + "/" + Year;
				      }			  
					  return cellText;
				  }
				else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
			      return ""; 
				else 
				  return String.valueOf(cell.getBooleanCellValue());
			}
			return "";
		}
		catch(Exception e)
		{			
			System.out.println("Exceptione is =" + e.getMessage());
			Assert.fail("row "+rowNum+" or column "+colName +" does not exist in xlsx"+e.getMessage());
			return "row "+rowNum+" or column "+colName +" does not exist in xlsx";
		}
	}
	
	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column number, row number
	 * @param sheetName - Sheet name should be provided
	 * @param colNum- Column number should be provided
	 * @param rowNum- Row number should be provided
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getCellDataXLSX(String sheetName, int colNum, int rowNum)
	{
		String cellData = null;
		try{

			if(isSheetExist(sheetName))
			{			
				if(rowNum <=0)
				{
					Assert.fail("Row number should be greater than 0");
				}
				Sheet = workbook.getSheetAt(iIndex);	
				row = Sheet.getRow(rowNum-1);
				if(row==null)
				{
					return null;
				}					
				cell = row.getCell(colNum);			
				if(cell==null)
				{
					return null;
				}								
				if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				{
					return cell.getStringCellValue();
				}
				else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA )
				{
					String cellText  = String.valueOf(cell.getNumericCellValue());
					
					if (HSSFDateUtil.isCellDateFormatted(cell))
					{
						// format in form of D/M/YY
						double d = cell.getNumericCellValue();
						Calendar cal =Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));					  
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH)+1;
						cellText = Day + "/" + Month + "/" + (String.valueOf(Year)).substring(4);
					}			  
					return cellText;
				}
				else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
				{
					return null;
				}	
				else
				{
					cell.getCellStyle().getFillBackgroundXSSFColor().getARGBHex();
					cellData = String.valueOf(cell.getBooleanCellValue());
				}
			}
		}
		catch(Exception e)
		{						
			log.error("row "+rowNum+" or column "+colNum +" does not exist in xls");
			log.error("Exception is =" + e.getMessage());
			Assert.fail("row "+rowNum+" or column "+colNum +" does not exist in xls"+"\n"+"Exception is =" + e.getMessage());
			return "row "+rowNum+" or column "+colNum +" does not exist in xls";
		}
		return cellData;
	}
	
	
	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column name, row value
	 * @param sheetName - Sheet name should be provided
	 * @param colName- Column Name should be provided
	 * @param rowNum- Row value should be provided
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getCellDataXLS(String sheetName,String colName,int rowNum)
	{
		try
		{	
			if(isSheetExist(sheetName))
			{			
				if(rowNum <=0)
				{
					Assert.fail("Row number should be greater than 0");
					return "";
				}
				int col_Num=-1;		
				hSheet = hworkbook.getSheetAt(iIndex);
				hrow=hSheet.getRow(0);
				for(int i=0;i<hrow.getLastCellNum();i++)
					{					
						if(hrow.getCell(i).getStringCellValue().trim().equals(colName.trim())){
							col_Num=i;
							break;
						}
					}
				if(col_Num==-1)
					{
						Assert.fail("Column with specified name is not being displayed"  + colName);
						return "";
					}
	
				hrow = hSheet.getRow(rowNum-1);
				if(hrow==null)
					return "";
				hcell = hrow.getCell(col_Num);
			
				if(hcell==null)
					return "";			
				if(hcell.getCellType()==Cell.CELL_TYPE_STRING)
				  return hcell.getStringCellValue();
				else if(hcell.getCellType()==Cell.CELL_TYPE_NUMERIC || hcell.getCellType()==Cell.CELL_TYPE_FORMULA )
				{
					  String cellText  = String.valueOf(hcell.getNumericCellValue());
					  if (HSSFDateUtil.isCellDateFormatted(hcell))
					  {
				           // format in form of D/M/YY
						  double d = hcell.getNumericCellValue();
						  Calendar cal =Calendar.getInstance();
						  cal.setTime(HSSFDateUtil.getJavaDate(d));					  
						  int Year = cal.get(Calendar.YEAR);
						  int Day = cal.get(Calendar.DAY_OF_MONTH);
						  int Month = cal.get(Calendar.MONTH)+1;
				          cellText = Day + "/" + Month + "/" + (String.valueOf(Year)).substring(2);
				      }			  
					  return cellText;
				  }
				else if(hcell.getCellType()==Cell.CELL_TYPE_BLANK)
			      return ""; 
				else 
				  return String.valueOf(hcell.getBooleanCellValue());
			}
			return "";
		}
		catch(Exception e)
		{			
			System.out.println("Exceptione is =" + e.getMessage());
			Assert.fail("row "+rowNum+" or column "+colName +" does not exist in xls");
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	}
	
	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column number, row number
	 * @param sheetName - Sheet name should be provided
	 * @param colNum- Column number should be provided
	 * @param rowNum- Row number should be provided
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getCellDataXLS(String sheetName, int colNum, int rowNum)
	{
		String cellData = null;
		try{

			if(isSheetExist(sheetName))
			{			
				if(rowNum <=0)
				{
					Assert.fail("Row number should be greater than 0");
				}
				hSheet = hworkbook.getSheetAt(iIndex);	
				hrow = hSheet.getRow(rowNum-1);
				
				if(hrow==null)
				{
					return null;
				}					
				hcell = hrow.getCell(colNum);			
				if(hcell==null)
				{
					return null;
				}								
				if(hcell.getCellType()==Cell.CELL_TYPE_STRING)
				{
					return hcell.getStringCellValue();
				}
				else if(hcell.getCellType()==Cell.CELL_TYPE_NUMERIC || hcell.getCellType()==Cell.CELL_TYPE_FORMULA )
				{
					String cellText  = String.valueOf(hcell.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(hcell))
					{
						// format in form of D/M/YY
						double d = hcell.getNumericCellValue();
						Calendar cal =Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));					  
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH)+1;
						cellText = Day + "/" + Month + "/" + (String.valueOf(Year)).substring(2);
					}			  
					return cellText;
				}
				else if(hcell.getCellType()==Cell.CELL_TYPE_BLANK)
				{
					return null;
				}	
				else
				{
					cellData = String.valueOf(hcell.getBooleanCellValue());
				}
			}
		}
		catch(Exception e)
		{						
			log.error("row "+rowNum+" or column "+colNum +" does not exist in xls");
			log.error("Exception is =" + e.getMessage());
			Assert.fail("row "+rowNum+" or column "+colNum +" does not exist in xls"+"\n"+"Exception is =" + e.getMessage());
			return "row "+rowNum+" or column "+colNum +" does not exist in xls";
		}
		return cellData;
	}

	/**
	 * gets cell data
	 * @param sheetName
	 * @param colNum
	 * @param rowNum
	 * @return
	 */
	public String getCellData(String sheetName, int colNum, int rowNum)
	{
		if(isXLSX())
		{
			return getCellDataXLSX(sheetName, colNum, rowNum);
		}
		else
		{
			return getCellDataXLS(sheetName, colNum, rowNum);
		}
	}

	/**
	 * gets cellData
	 * @param sheetName
	 * @param colName
	 * @param rowNum
	 * @return
	 */
	public String getCellData(String sheetName, String colName, int rowNum)
	{
		if(isXLSX())
		{
			return getCellDataXLSX(sheetName, colName, rowNum);
		}
		else
		{
			return getCellDataXLS(sheetName, colName, rowNum);
		}
	}
	
	
	// returns true if data is set successfully else false
		public boolean setCellData(String sheetName,String colName,int rowNum, String data)
		{
			String excelFilePath = System.getProperty("ExcelFilePath");
			try{
				fis = new FileInputStream(excelFilePath); 
				workbook = new XSSFWorkbook(fis);

				if(rowNum<=0)
					return false;

				int index = workbook.getSheetIndex(sheetName);
				int colNum=-1;
				if(index==-1)
					return false;

				Sheet = workbook.getSheetAt(index);

				row=Sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++)
				{
					if(row.getCell(i).getStringCellValue().trim().equals(colName)){
						colNum=i;
						break;
					}
				}
				if(colNum==-1)
					return false;

				Sheet.autoSizeColumn(colNum); 
				row = Sheet.getRow(rowNum-1);
				if (row == null)
					row = Sheet.createRow(rowNum-1);

				cell = row.getCell(colNum);	
				if (cell == null)
					cell = row.createCell(colNum);

				// cell style
				//CellStyle cs = workbook.createCellStyle();
				//cs.setWrapText(true);
				//cell.setCellStyle(cs);
				cell.setCellValue(data);
				FileOutputStream fileOut = new FileOutputStream(excelFilePath);
				workbook.write(fileOut);
				fileOut.close();	
			}
			catch(Exception e){
				log.error("Exception is =" + e.getCause());
				Assert.fail("Cannot write to Excel Sheet \n exception is =" + e.getCause());
				return false;
			}
			return true;
		}
		
	
	
	public static boolean isXLSX()
	{
		return System.getProperty("ExcelFilePath").endsWith(".xlsx");
	}
	

	@SuppressWarnings("deprecation")
	public String getCellRaw(String sheetName, String colName, int rowNum)
	{
		try
		{	
			if(isSheetExist(sheetName))
			{			
				if(rowNum <=0)
				{
					Assert.fail("Row number should be greater than 0");
					return "";
				}
				int col_Num=-1;		
				Sheet = workbook.getSheetAt(iIndex);
				row=Sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++)
					{					
						if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())){
							col_Num=i;
							break;
						}
					}
				if(col_Num==-1)
					{
						Assert.fail("Column with specified name is not being displayed");
						return "";
					}
	
				row = Sheet.getRow(rowNum-1);
				if(row==null)
					return "";
				cell = row.getCell(col_Num);
			
				if(cell==null)
					return "";			
			
				if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
			      return ""; 
				else
					return cell.getRawValue();

			}
			return "";
		}
		catch(Exception e)
		{			
			System.out.println("Exception is =" + e.getMessage());
			Assert.fail("row "+rowNum+" or column "+colName +" does not exist in xls");
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	}
	
	public String getCellHexColor(String sheetName, String colName, int rowNum) {
		try
		{	
			if(isSheetExist(sheetName))
			{			
				if(rowNum <=0)
				{
					Assert.fail("Row number should be greater than 0");
					return "";
				}
				int col_Num=-1;		
				Sheet = workbook.getSheetAt(iIndex);
				row=Sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++)
					{					
						if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
							col_Num=i;
							break;
						}
					}
				if(col_Num==-1)
					{
						Assert.fail("Column with specified name is not being displayed");
						return "";
					}
	
				row = Sheet.getRow(rowNum-1);
				if(row==null)
					return "";
				cell = row.getCell(col_Num);
			
				if(cell==null)
					return "";	
				else
					return cell.getCellStyle().getFillForegroundXSSFColor().getARGBHex().substring(2);
			}
		}catch(Exception e)
		{			
			System.out.println("Exception is =" + e.getMessage());
			Assert.fail("row "+rowNum+" or column "+colName +" does not exist in xls");
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
		return "";
	}

}

