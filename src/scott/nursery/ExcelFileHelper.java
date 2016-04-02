package scott.nursery;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class ExcelFileHelper
{
    public HSSFCellStyle FULL_BORDER;
    public HSSFCellStyle COLUMN;
    public HSSFCellStyle ROW;
    public HSSFCellStyle EMPTY;
    private HSSFWorkbook _workBook;
    private HashMap<Integer,HSSFCellStyle> _styleList;

    public void writeWorkBook(String file)
    {
        FileOutputStream fileOut;
        try
        {
            fileOut = new FileOutputStream(file + ".xls");
            _workBook.write(fileOut);
            fileOut.close();
            
        } 
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public ExcelFileHelper(HSSFWorkbook workbook)
    {
        _styleList = new HashMap<Integer,HSSFCellStyle>();
  
        _workBook = workbook;
        
        FULL_BORDER = _workBook.createCellStyle();
        FULL_BORDER.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        FULL_BORDER.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        FULL_BORDER.setBottomBorderColor(HSSFColor.BLACK.index);
        FULL_BORDER.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        FULL_BORDER.setLeftBorderColor(HSSFColor.BLACK.index);
        FULL_BORDER.setBorderRight(HSSFCellStyle.BORDER_THIN);
        FULL_BORDER.setRightBorderColor(HSSFColor.BLACK.index);
        FULL_BORDER.setBorderTop(HSSFCellStyle.BORDER_THIN);
        FULL_BORDER.setTopBorderColor(HSSFColor.BLACK.index);
        
        
        COLUMN = _workBook.createCellStyle();
        COLUMN.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        COLUMN.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        COLUMN.setBottomBorderColor(HSSFColor.BLACK.index);
        COLUMN.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        COLUMN.setLeftBorderColor(HSSFColor.BLACK.index);
        COLUMN.setBorderRight(HSSFCellStyle.BORDER_THIN);
        COLUMN.setRightBorderColor(HSSFColor.BLACK.index);
        COLUMN.setBorderTop(HSSFCellStyle.BORDER_NONE);
        COLUMN.setTopBorderColor(HSSFColor.BLACK.index);
        
        ROW = _workBook.createCellStyle();
        ROW.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        ROW.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        ROW.setBottomBorderColor(HSSFColor.BLACK.index);
        ROW.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        ROW.setLeftBorderColor(HSSFColor.BLACK.index);
        ROW.setBorderRight(HSSFCellStyle.BORDER_NONE);
        ROW.setRightBorderColor(HSSFColor.BLACK.index);
        ROW.setBorderTop(HSSFCellStyle.BORDER_THIN);
        ROW.setTopBorderColor(HSSFColor.BLACK.index);
        
        EMPTY = _workBook.createCellStyle();
        EMPTY.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    }
    
    public void setStyle(int name,HSSFCellStyle style) 
    {
      _styleList.put(name, style);
    }
    
    public void createCell(HSSFRow row, short column, String heading, int styleID)
    {
        HSSFCell cell = row.createCell((short) column);
        HSSFCellStyle style = _styleList.get(styleID);
        cell.setCellValue(new HSSFRichTextString(heading));
        if (style!=null)
            cell.setCellStyle(style);
    }
    public void createCell(HSSFRow row, short column, BigDecimal value, int styleID)
    {
        HSSFCell cell = row.createCell((short) column);
        HSSFCellStyle style = _styleList.get(styleID);
        if (value==null)
            cell.setCellValue(0);
        else
            cell.setCellValue(value.doubleValue());
        if (style!=null)
            cell.setCellStyle(style);
    }
    
    public HSSFCellStyle cloneStyle(HSSFCellStyle original)
    {
        HSSFCellStyle results = _workBook.createCellStyle();
        original.setAlignment(original.getAlignment());
        original.setBorderBottom(original.getBorderBottom());
        original.setBottomBorderColor(original.getBottomBorderColor());
        original.setBorderLeft(original.getBorderLeft());
        original.setLeftBorderColor(original.getLeftBorderColor());
        original.setBorderRight(original.getBorderRight());
        original.setRightBorderColor(original.getRightBorderColor());
        original.setBorderTop(original.getBorderTop());
        original.setTopBorderColor(original.getTopBorderColor());
        
        return results;
    }

}
