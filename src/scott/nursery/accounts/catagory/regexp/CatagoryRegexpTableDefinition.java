package scott.nursery.accounts.catagory.regexp;

import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableFilter.SORT_TYPE;

public class CatagoryRegexpTableDefinition extends FTableDefinition
{

    @Override
    protected Object[][] getTableDefinition()
    {
        Object[][] COLUMN_DETAILS = {
                { "Regexp", 300, SORT_TYPE.STRING , false},
                { "Notes", 300 , SORT_TYPE.STRING, false} };
        return COLUMN_DETAILS;
        
    }
}
