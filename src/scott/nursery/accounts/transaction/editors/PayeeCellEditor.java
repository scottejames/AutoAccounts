package scott.nursery.accounts.transaction.editors;

import org.eclipse.swt.widgets.Table;
import scott.mvc.gui.table.FTableRow;
import scott.mvc.gui.table.FTableTextCellEditor;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseTransaction;

public class PayeeCellEditor extends FTableTextCellEditor
{

    FTableRow _row = null;
    BaseTransaction _transaction = null;
    
    public PayeeCellEditor(Table t)
    {
        super(t);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setCurrentEditedRow(FTableRow row)
    {
        
        _row = row;
        Long id = (Long) _row.getHiddenRowData();
        _transaction = ApplicationModel.getInstance().getTransactionList().getById(id);
       
    }

    @Override
    public void setText(String s)
    {
        super.setText(_transaction.get_originalPayee());
        
    }
    @Override
    public boolean canEdit()
    {
        if (_transaction.is_isCheque()==true)
        {
            if (_transaction.get_originalPayee().equals("<BLANK>"))
                return true;
            else
                return false;
        } else
        {
            return true;
        }
        
        
    }
}
