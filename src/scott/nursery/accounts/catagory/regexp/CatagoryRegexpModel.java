package scott.nursery.accounts.catagory.regexp;

import scott.mvc.Model;
import scott.mvc.gui.table.FTableModel;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseCatagoryRegularExpression;

public class CatagoryRegexpModel extends Model
{
    private CatagoryRegexpTableModel _tableModel = new CatagoryRegexpTableModel(this, new CatagoryRegexpTableDefinition());

    private BaseCatagory _catagory = null;
    private Long _selectedID = null;
    private String _selectedRegexp = null;
    private String _selectedNotes = null;

    public FTableModel getTableModel()
    {
        return _tableModel;
    }

    public void set_selectedRegexp(String r)
    {
        _selectedRegexp = r;
        notifyUpdate();

    }


    public String get_selectedNotes()
    {
        return _selectedNotes;
    }


    public void set_selectedNotes(String notes)
    {
        _selectedNotes = notes;
        notifyUpdate();
    }


    public String get_selectedRegexp()
    {
        return _selectedRegexp;
    }

    public BaseCatagoryRegularExpression getSelectedRegexp()
    {
        return _tableModel.getSelectedRegexp();
    }

    public void delSelected()
    {
        ApplicationModel.getInstance().delCatagoryRegexp(_selectedID);
        

        _selectedID = null;
        _selectedNotes = null;
        _selectedRegexp = null;
        
        
        notifyUpdate();        
        
    }


    public void addSelected()
    {
        BaseCatagoryRegularExpression b = new BaseCatagoryRegularExpression();
        
        if (_selectedID != null)
        {
            b.set_id(_selectedID);
        }
        b.set_notes(_selectedNotes);
        b.set_regexp(_selectedRegexp);
        b.set_catagoryId(_catagory.get_id());
        
        ApplicationModel.getInstance().addOrUpdateCatagoryRegexp(b);
        
        _selectedID = null;
        _selectedNotes = null;
        _selectedRegexp = null;
        
        notifyUpdate();        

        
    }


    public BaseCatagory get_catagory()
    {
        return _catagory;
    }


    public void set_catagory(BaseCatagory _catagory)
    {
        this._catagory = _catagory;
    }


    public Long get_selectedID()
    {
        return _selectedID;
    }


    public void set_selectedID(Long _selectedid)
    {
        _selectedID = _selectedid;
    }

    public boolean isSelectionValid()
    {
        if ((_selectedRegexp != null))
            return true;
        else
            return false;
    }


}
