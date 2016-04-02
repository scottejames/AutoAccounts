package scott.nursery.accounts.transaction.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Table;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableTextCellEditor;
import java.text.ParseException;


public class DateCellEditor extends FTableTextCellEditor
{
    private static Logger _logger = Logger.getLogger(DateCellEditor.class);

    public DateCellEditor(Table t)
    {
        super(t);
    }

    @Override
    public boolean validate(String oldValue, String newValue)
    {
      
        try
        {
            // If we can parse it then accept it
           Utils.stringToDate(newValue);
            return true;
        } catch (ParseException e)
        {
            _logger.error("Unable to parse string " + newValue
                    + " its not a date!");
        }
        return false;
        
    }
}
