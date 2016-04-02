package scott.nursery.accounts.catagory;

import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableFilter.SORT_TYPE;

public class CatagoryTableDefinition extends FTableDefinition
{

    @Override
    protected Object[][] getTableDefinition()
    {
        Object[][] COLUMN_DETAILS = { 
                { "Direction", 200 , SORT_TYPE.STRING,false},
                { "Catagory", 200 ,SORT_TYPE.STRING,false}, 

                };
        // TODO Auto-generated method stub
        return COLUMN_DETAILS;
    }
}
