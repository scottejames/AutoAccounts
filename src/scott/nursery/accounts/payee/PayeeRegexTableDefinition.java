package scott.nursery.accounts.payee;

import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableFilter.SORT_TYPE;

public class PayeeRegexTableDefinition extends FTableDefinition
{
    @Override
    protected Object[][] getTableDefinition()
    {
        Object[][] COLUMN_DETAILS = { 
                { "From", 300 , SORT_TYPE.STRING,false},
                { "To", 300 ,SORT_TYPE.STRING,false}, 

                };
        // TODO Auto-generated method stub
        return COLUMN_DETAILS;
    }
}
