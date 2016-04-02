package scott.nursery.accounts.domain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import scott.mvc.gui.Utils;
import scott.nursery.ExcelFileHelper;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class TransactionListAccountsReport
{
    private HashMap<Integer, List<BaseTransaction>> _indexByMonth = null;
    private List<BaseTransaction> _transactionList = new ArrayList<BaseTransaction>();
    private List<BaseCatagory> _catIN = ApplicationModel.getInstance()
            .getCatagoryList().getCatagoryListIN();
    private List<BaseCatagory> _catOUT = ApplicationModel.getInstance()
            .getCatagoryList().getCatagoryListOUT();
    private static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    private HSSFWorkbook _workbook;
    private ExcelFileHelper _excelHelper = null;

    TransactionListAccountsReport(List<BaseTransaction> transactions)
    {
        _transactionList = transactions;
        _workbook = new HSSFWorkbook();
        _excelHelper = new ExcelFileHelper(_workbook);
        indexData();
        setupExcelStyles();
    }
    public static int FULL_BORDER_STYLE = 0;
    public static int DESCRIPTION_STYLE = 1;
    public static int ROW_TOTAL_STYLE = 2;
    public static int COLUMN_TOTAL_STYLE = 3;
    public static int NUMBER_STYLE = 4;

    private void setupExcelStyles()
    {
        _excelHelper.setStyle(FULL_BORDER_STYLE, _excelHelper.FULL_BORDER);
        
        HSSFCellStyle descriptionStyle = _workbook.createCellStyle();
        descriptionStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        descriptionStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        descriptionStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        descriptionStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        descriptionStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        descriptionStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        descriptionStyle.setRightBorderColor(HSSFColor.BLACK.index);
        descriptionStyle.setBorderTop(HSSFCellStyle.BORDER_NONE);
        descriptionStyle.setTopBorderColor(HSSFColor.BLACK.index);
        _excelHelper.setStyle(DESCRIPTION_STYLE, descriptionStyle);
        
        _excelHelper.setStyle(ROW_TOTAL_STYLE, _excelHelper.ROW);
        
        _excelHelper.setStyle(COLUMN_TOTAL_STYLE, _excelHelper.COLUMN);
        HSSFCellStyle numberStyle = _workbook.createCellStyle();
        numberStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numberStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        numberStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        numberStyle.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        numberStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        numberStyle.setBorderRight(HSSFCellStyle.BORDER_NONE);
        numberStyle.setRightBorderColor(HSSFColor.BLACK.index);
        numberStyle.setBorderTop(HSSFCellStyle.BORDER_NONE);
        numberStyle.setTopBorderColor(HSSFColor.BLACK.index);
        _excelHelper.setStyle(NUMBER_STYLE, numberStyle);
    }

    private void indexData()
    {
        _indexByMonth = new HashMap<Integer, List<BaseTransaction>>();
        Calendar cal = new GregorianCalendar();
        for (BaseTransaction t : _transactionList)
        {
            cal.setTime(t.get_date());
            Integer month = cal.get(Calendar.MONTH);
            if (_indexByMonth.get(month) == null)
                _indexByMonth.put(month, new ArrayList<BaseTransaction>());
            List<BaseTransaction> arr = _indexByMonth.get(month);
            arr.add(t);
        }
    }

    private void printHeadings(Integer month, HSSFSheet sheet)
    {
        // WRite out column headings
        HSSFRow row = sheet.createRow((short) 0);
        short column = 0;
        _excelHelper.createCell(row, column++, "Date", FULL_BORDER_STYLE);
        _excelHelper.createCell(row, column++, "Cheque No", FULL_BORDER_STYLE);
        _excelHelper
                .createCell(row, column++, "Description", FULL_BORDER_STYLE);
        for (BaseCatagory c : _catIN)
        {
            _excelHelper.createCell(row, column++, c.get_name(),
                    FULL_BORDER_STYLE);
        }
        _excelHelper.createCell(row, column++, "TOTAL", FULL_BORDER_STYLE);
        for (BaseCatagory c : _catOUT)
        {
            _excelHelper.createCell(row, column++, c.get_name(),
                    FULL_BORDER_STYLE);
        }
        _excelHelper.createCell(row, column++, "TOTAL", FULL_BORDER_STYLE);
    }

    private void printSummationRow(Integer month, HSSFSheet sheet,
            List<BaseTransaction> tranList)
    {
        HSSFRow row = sheet.createRow((short) 2);
        short column = 3;
        HashMap<Long, List<BaseTransaction>> tranByCatagory = TransactionList
                .indexTransactionListByCatagoryId(tranList);
        BigDecimal subTotal = new BigDecimal(0);
        for (BaseCatagory c : _catIN)
        {
            BigDecimal total = TransactionList
                    .sumTransactionList(tranByCatagory.get(c.get_id()));
            _excelHelper.createCell(row, column++, total, ROW_TOTAL_STYLE);
            subTotal = subTotal.add(total);
        }
        _excelHelper.createCell(row, column++, subTotal, ROW_TOTAL_STYLE);
        subTotal = new BigDecimal(0);
        for (BaseCatagory c : _catOUT)
        {
            BigDecimal total = TransactionList
                    .sumTransactionList(tranByCatagory.get(c.get_id()));
            _excelHelper.createCell(row, column++, total.abs(),
                    ROW_TOTAL_STYLE);
            subTotal = subTotal.add(total);
        }
        _excelHelper.createCell(row, column++, subTotal.abs(),
                ROW_TOTAL_STYLE);
    }

    private void printTransactionList(Integer month, HSSFSheet sheet,
            List<BaseTransaction> tranList)
    {
        short rowCount = 3;
        for (BaseTransaction tran : tranList)
        {
            short column = 0;
            HSSFRow row = sheet.createRow(rowCount++);
            BaseCatagory catagory = ApplicationModel.getInstance()
                    .getCatagoryList().getCatagoryById(tran.get_catagoryID());
            _excelHelper.createCell(row, column++, Utils.dateToString(tran
                    .get_date()), DESCRIPTION_STYLE);
            _excelHelper.createCell(row, column++, tran.get_chequeNumber(),
                    DESCRIPTION_STYLE);
            _excelHelper.createCell(row, column++, tran.get_payee(),
                    DESCRIPTION_STYLE);
            for (int count = 0; count < _catIN.size(); count++)
            {
                if (_catIN.get(count).get_name().equals(catagory.get_name()))
                {
                    _excelHelper.createCell(row, column++, tran.get_amount(),
                            NUMBER_STYLE);
                } else
                {
                    column++;
                }
            }
            if (catagory.get_direction() == DIRECTION.IN)
                _excelHelper.createCell(row, column++, tran.get_amount(),
                        COLUMN_TOTAL_STYLE);
            else
                _excelHelper.createCell(row, column++, (BigDecimal) null,
                        COLUMN_TOTAL_STYLE);
            for (int count = 0; count < _catOUT.size(); count++)
            {
                if (_catOUT.get(count).get_name().equals(catagory.get_name()))
                {
                    _excelHelper.createCell(row, column++, tran.get_amount()
                            .abs(), NUMBER_STYLE);
                } else
                {
                    column++;
                }
            }
            if (catagory.get_direction() == DIRECTION.OUT)
                _excelHelper.createCell(row, column++, tran.get_amount().abs(),
                        COLUMN_TOTAL_STYLE);
            else
                _excelHelper.createCell(row, column++, (BigDecimal) null,
                        COLUMN_TOTAL_STYLE);
        }
    }

    private void printSummaryPage(HSSFSheet sheet)
    {
        short rowNumber = 1;
        HSSFRow row = sheet.createRow(rowNumber++);
        // --
        // Setup titles
        // --
        short column = 0;
        _excelHelper.createCell(row, column++, "Month Name", ROW_TOTAL_STYLE);
        for (Integer i : _indexByMonth.keySet())
        {
            _excelHelper
                    .createCell(row, column++, MONTHS[i], ROW_TOTAL_STYLE);
        }
        rowNumber++;
        HashMap<Integer, BigDecimal> totals = new HashMap<Integer, BigDecimal>();
        for (Integer month : _indexByMonth.keySet())
        {
            totals.put(month, new BigDecimal(0));
        }
        // --
        // Cat IN
        // --
        for (BaseCatagory c : _catIN)
        {
            column = 0;
            row = sheet.createRow(rowNumber++);
            _excelHelper.createCell(row, column++, c.get_name(),
                    DESCRIPTION_STYLE);
            for (Integer month : _indexByMonth.keySet())
            {
                List<BaseTransaction> tranList = _indexByMonth.get(month);
                HashMap<Long, List<BaseTransaction>> tranByCatagory = TransactionList
                        .indexTransactionListByCatagoryId(tranList);
                BigDecimal total = TransactionList
                        .sumTransactionList(tranByCatagory.get(c.get_id()));
                _excelHelper.createCell(row, column++, total, NUMBER_STYLE);
                BigDecimal result = totals.get(month).add(total);
                totals.put(month, result);
            }
        }
        rowNumber++;
        column = 0;
        row = sheet.createRow(rowNumber++);
        _excelHelper.createCell(row, column++, "TOTAL", ROW_TOTAL_STYLE);
        for (Integer month : _indexByMonth.keySet())
        {
            _excelHelper.createCell(row, column++, totals.get(month),
                    ROW_TOTAL_STYLE);
        }
        rowNumber++;
        for (Integer month : _indexByMonth.keySet())
        {
            totals.put(month, new BigDecimal(0));
        }
        for (BaseCatagory c : _catOUT)
        {
            column = 0;
            row = sheet.createRow(rowNumber++);
            _excelHelper.createCell(row, column++, c.get_name(),
                    DESCRIPTION_STYLE);
            for (Integer month : _indexByMonth.keySet())
            {
                List<BaseTransaction> tranList = _indexByMonth.get(month);
                HashMap<Long, List<BaseTransaction>> tranByCatagory = TransactionList
                        .indexTransactionListByCatagoryId(tranList);
                BigDecimal total = TransactionList
                        .sumTransactionList(tranByCatagory.get(c.get_id()));
                _excelHelper.createCell(row, column++, total, NUMBER_STYLE);
                BigDecimal result = totals.get(month).add(total);
                totals.put(month, result);
            }
        }
        rowNumber++;
        row = sheet.createRow(rowNumber++);
        column = 0;
        _excelHelper.createCell(row, column++, "TOTAL", ROW_TOTAL_STYLE);
        for (Integer month : _indexByMonth.keySet())
        {
            _excelHelper.createCell(row, column++, totals.get(month),
                    ROW_TOTAL_STYLE);
        }
        rowNumber++;
        for (short j = 0; j < 12; j++)
            sheet.autoSizeColumn(j); // adjust width of the first column
    }

    public void dumpAccountsReport(String name)
    {
        for (Integer i : _indexByMonth.keySet())
        {
            HSSFSheet sheet = _workbook.createSheet(MONTHS[i]);
            printHeadings(i, sheet);
            printSummationRow(i, sheet, _indexByMonth.get(i));
            printTransactionList(i, sheet, _indexByMonth.get(i));
            for (short j = 0; j < 25; j++)
                sheet.autoSizeColumn(j); // adjust width of the first column
        }
        HSSFSheet sheet = _workbook.createSheet("Summary");
        printSummaryPage(sheet);
        try
        {
            FileOutputStream fileOut = new FileOutputStream(name + ".xls");
            _workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
