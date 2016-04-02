package scott.nursery.accounts.cheques;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import scott.mvc.Model;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableRow;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.ChequeList;
import scott.nursery.accounts.domain.bo.BaseCheque;

public class ChequeBrowserModel extends Model
{

    private ApplicationModel _appModel;
    
    public ChequeBrowserModel( )
    {
        _appModel = ApplicationModel.getInstance();
        
    }
    
    public ChequeList getChequeList()
    {
        return _appModel.getChequeList();
        
    }
    @Override
    public void update(Observable o, Object arg)
    {
        // TODO Auto-generated method stub
        
    }
    
    public List<FTableRow> getFTableRowList()
    {
        List<BaseCheque> list = _appModel.getChequeList().getChequeList();
        List<FTableRow> results = new ArrayList<FTableRow>();
        for (BaseCheque c : list)
        {
            results.add(new FTableRow(new String[]
                                                 {Utils.toString(c.get_writtenDate()),
                                                 Utils.toString(c.get_number()),
                                                 Utils.toString(c.get_payee()),
                                                 Utils.toString(c.get_amount())},c.getId()));
        }
        return results;
    }
    

    
    
}
