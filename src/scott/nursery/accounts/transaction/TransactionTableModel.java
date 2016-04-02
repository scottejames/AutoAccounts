package scott.nursery.accounts.transaction;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import scott.mvc.Model;
import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableModel;
import scott.mvc.gui.table.FTableRow;
import scott.nursery.accounts.domain.bo.BaseTransaction;

public class TransactionTableModel extends FTableModel
{
    private static Logger _logger = Logger
            .getLogger(TransactionTableModel.class);


    TransactionTableModel(Model m, FTableDefinition t)
    {
        super(m,t);
    }

    @Override
    public void dependentModelUpdated(Model m, CONTEXT context)
    {
        _logger.debug("Updating the transaction  table model using context: "
                + context);
        List<BaseTransaction> tranList = ((TransactionBrowserModel) m)
                .getTransactions();
        List<FTableRow> results = new ArrayList<FTableRow>();
        for (BaseTransaction t : tranList)
        {
            results.add(TransactionBrowserTableDefinition.buildRowFromTransaction(t));
        }
        this.setModelData(results);
     //   notifyUpdate();
    }

    public BaseTransaction getSelectedTransaction()
    {
        if (getSelectedRow() == null)
            return null;
        else
            return TransactionBrowserTableDefinition.buildTransactionFromRow(getSelectedRow());
    }
}
