package scott.nursery.accounts.transaction.editors;

import java.math.BigDecimal;
import org.eclipse.swt.widgets.Table;
import scott.mvc.gui.table.FTableComboCellEditor;
import scott.mvc.gui.table.FTableRow;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class CatagoryCellEditor extends FTableComboCellEditor
{

    private DIRECTION direction = null;
    public CatagoryCellEditor(Table t)
    {
        super(t);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public String[] getOptions()
    {
        return ApplicationModel.getInstance().getCatagoryList().getCatagoryNames(direction);
    }

    public void setCurrentEditedRow(FTableRow row)
    {
        BigDecimal amount = new BigDecimal(row.getRowData()[6]);
        if (amount.compareTo(new BigDecimal(0.0)) > 0)
        {
            direction = DIRECTION.IN;
        } else
        {
            direction = DIRECTION.OUT;
        }
    }

    public boolean canEdit()
    {
        // TODO Auto-generated method stub
        return true;
    }
}
