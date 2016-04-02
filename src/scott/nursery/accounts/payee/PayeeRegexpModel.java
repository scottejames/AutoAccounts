package scott.nursery.accounts.payee;

import java.util.List;
import org.apache.log4j.Logger;
import scott.mvc.Model;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BasePayeeRegularExpression;

public class PayeeRegexpModel extends Model
{
    
    private PayeeRegexTableModel _tableModel = new PayeeRegexTableModel(this, new PayeeRegexTableDefinition());
    public static  final CONTEXT CONTEXT_FIELDS = CONTEXT.CUSTOM1;
    public static  final CONTEXT CONTEXT_LIST= CONTEXT.CUSTOM2;

    
    private static Logger _logger = Logger
    .getLogger(PayeeRegexpModel.class);
    
    ApplicationModel _appModel = null;
    
    Long _selectedRegexpId = null;
    String _selectedRegexpFrom = null;
    String _selectedRegexpTo = null;
    BasePayeeRegularExpression selectedExpression = null;
    

    public PayeeRegexTableModel getTableModel()
    {
        return _tableModel;
    }
    
    public String getSelectedFrom()
    {
        return _selectedRegexpFrom;
    }
    public String getSelectedTo()
    {
        return _selectedRegexpTo;
    }
    
    public PayeeRegexpModel()
    {
        _appModel = ApplicationModel.getInstance();
        _appModel.addObserver(this);
    }
    
    public List<BasePayeeRegularExpression> getPayeeRegexpList()
    {
        return _appModel.getPayeeRegexpList().getPayeeRegexpList();
    }

    public void set_selectedRegexpId(Long id)
    {
        _selectedRegexpId = id;
        notifyUpdate(CONTEXT_FIELDS);

    }

    public void set_selectedRegexpFrom(String regexpFrom)
    {
        _selectedRegexpFrom = regexpFrom;
        notifyUpdate(CONTEXT_FIELDS);
    }

    public void set_selectedRegexpTo(String regexpTo)
    {
        _selectedRegexpTo = regexpTo;
        notifyUpdate(CONTEXT_FIELDS);

    }
    
    public void addSelectedRegexp()
    {
       
        BasePayeeRegularExpression r = new BasePayeeRegularExpression();
     
        if (_selectedRegexpId != null)
        {
            r.set_id(_selectedRegexpId);
        }
        r.set_from(_selectedRegexpFrom);
        r.set_to(_selectedRegexpTo);
        _appModel.addOrUpdatePayeeRegexp(r);
        
        _selectedRegexpFrom = null;
        _selectedRegexpTo = null;
        _selectedRegexpId = null;
        
        notifyUpdate(CONTEXT_LIST);

        
    }
    
    public void delSelectedRegexp()
    {
        _logger.debug("DEL regexp");

        _appModel.delPayeeRegex(_selectedRegexpId);
        
        _selectedRegexpFrom = null;
        _selectedRegexpTo = null;
        _selectedRegexpId = null;
        
        notifyUpdate(CONTEXT_LIST);
                
    }
    
    public boolean isSelectionValid()
    {
        if (_selectedRegexpFrom == null || _selectedRegexpTo == null)
            return false;
        else
            return true;
    }

    
    public BasePayeeRegularExpression getSelectedRegexp()
    {
        return getTableModel().getSelectedRegexp();
     }


    
}
