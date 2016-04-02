package scott.nursery.accounts.transaction.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Table;
import scott.mvc.gui.table.FTableTextCellEditor;

public class AmountCellEditor extends FTableTextCellEditor
{
    private static Logger _logger = Logger.getLogger(AmountCellEditor.class);
    
    public AmountCellEditor(Table t)
    {
      super(t);
    }

    @Override
    public boolean validate(String oldValue, String newValue)
    {
      
        try
        {
            // If we can parse it then accept it
            Double.parseDouble(newValue);
            return true;
        } catch (NumberFormatException e)
        {
            _logger.error("Unable to parse string " + newValue
                    + " its not a number!");
        }
        return false;
        
    }
   
}
