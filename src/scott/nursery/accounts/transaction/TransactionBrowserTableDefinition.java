package scott.nursery.accounts.transaction;

import java.math.BigDecimal;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableRow;
import scott.mvc.gui.table.FTableFilter.SORT_TYPE;
import scott.nursery.accounts.NurseryGui;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.CatagoryList;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseTransaction;

public class TransactionBrowserTableDefinition extends FTableDefinition
{
    @SuppressWarnings("unused")
	private static Logger _logger = Logger
            .getLogger(TransactionBrowserTableDefinition.class);

    @Override
    protected Object[][] getTableDefinition()
    {
        Object[][] COLUMN_DETAILS = { 
                { "ID", 30, SORT_TYPE.NUMBER, false },
                { "Date", 100, SORT_TYPE.DATE, false },
                { "Payee", 300, SORT_TYPE.STRING, false },
                { "Catagory", 100, SORT_TYPE.STRING, false },
                { "EPayee", 300, SORT_TYPE.STRING, true },
                { "ChequeNumber", 150, SORT_TYPE.STRING, false },
                { "Amount", 150, SORT_TYPE.NUMBER, false },
                { "NoChequeMached", 30, SORT_TYPE.STRING, false },
                { "Enriched Payee", 30, SORT_TYPE.STRING, false } };
        return COLUMN_DETAILS;
    }

    public static FTableRow buildRowFromTransaction(BaseTransaction t)
    {
        CatagoryList catList = ApplicationModel.getInstance().getCatagoryList();
        
        Long id = t.get_catagoryID();
        String catagoryName;
        if (id != null)
        {
            catagoryName = catList.getCatagoryById(id).get_name();
        } else
        {
            catagoryName = "";
        }
        String [] rowData = new String[] { 
                Utils.toString(t.get_id()),
                Utils.dateToString(t.get_date()),
                Utils.toString(t.get_payee()), catagoryName,
                Utils.toString(t.get_enrichedPayee()),
                Utils.toString(t.get_chequeNumber()),
                Utils.toString(t.get_amount()),
                Utils.boolToString((Boolean) t.is_chequeNotMatched()),
                Utils.boolToString(t.show_enrichedPayee()) };
        
        Color [] foreColor = new Color[rowData.length];

        if (t.show_enrichedPayee())
        {
            foreColor[2]= NurseryGui.RED;
        }
        if ((t.get_catagoryID() != null) &&
                (t.is_manualEditCatagory()==false))
        {
            foreColor[3]= NurseryGui.RED;

        }
        
        return new FTableRow(rowData, foreColor,t.get_id());
    }

    public static BaseTransaction buildTransactionFromRow(FTableRow row)
    {
        CatagoryList catList = ApplicationModel.getInstance().getCatagoryList();
        BaseTransaction transaction = new BaseTransaction();
        try
        {
            int columnNo = 0;
            
            transaction = ApplicationModel.getInstance().getTransactionList().getById((Long)row.getHiddenRowData());

            // Extract fields from row
            @SuppressWarnings("unused")
			String strId = row.getRowData()[columnNo++];
            String strDate = row.getRowData()[columnNo++];
            String strPayee = row.getRowData()[columnNo++];
            String strCatagoryName = row.getRowData()[columnNo++];
            @SuppressWarnings("unused")
			String strEPayee = row.getRowData()[columnNo++];
            String strChequeNo = row.getRowData()[columnNo++];
            String strAmount = row.getRowData()[columnNo++];

            // calculate derived values
            Long catagoryId = null;
            if (strCatagoryName != null)
            {
                BaseCatagory catagory = catList
                        .getCatagoryFromName(strCatagoryName);
                if (catagory != null)
                    catagoryId = catagory.get_id();
            }
            BigDecimal amount = new BigDecimal(strAmount);
            // Store values into a transaction
            transaction.set_date(Utils.stringToDate(strDate));
            transaction.set_payee(strPayee);
            transaction.set_catagoryID(catagoryId);
            transaction.set_chequeNumber(strChequeNo);
            transaction.set_amount(amount);

        } catch (ParseException e)
        {
            e.printStackTrace();
        } 
        return transaction;
    }
}
