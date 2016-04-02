package scott.nursery.accounts.catagory;

import org.apache.log4j.Logger;
import scott.mvc.Model;
import scott.mvc.gui.table.FTableModel;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class CatagoryModel extends Model
{
    private static Logger _logger = Logger.getLogger(CatagoryModel.class);
    private Long _id;
    private DIRECTION _direction;
    private String _name;
    private CatagoryTableModel _tableModel = new CatagoryTableModel(this, new CatagoryTableDefinition());

    public FTableModel getTableModel()
    {
        return _tableModel;
    }

    public DIRECTION get_direction()
    {
        return _direction;
    }

    public void set_direction(DIRECTION _direction)
    {
        this._direction = _direction;
        notifyUpdate();
    }

    public String get_name()
    {
        return _name;
    }

    public void set_name(String _catagory)
    {
        this._name = _catagory;
        notifyUpdate();
    }

    public void addCatagory()
    {
        BaseCatagory c = new BaseCatagory();
        if (_id != null)
        {
            c.set_id(_id);
        }
        c.set_direction(_direction);
        c.set_name(_name);
        ApplicationModel.getInstance().addOrUpdateCatagory(c);
        _id = null;
        _direction = null;
        _name = null;
        notifyUpdate();
    }

    public void delCatagory()
    {
        _logger.debug("DEL regexp");
        ApplicationModel.getInstance().delCatagory(_id);
        _id = null;
        _direction = null;
        _name = null;
        notifyUpdate();
    }
    public BaseCatagory getSelectedCatagory()
    {
        return _tableModel.getSelectedCatagory();
    }
    public Long get_id()
    {
        return _id;
    }

    public void set_id(Long _id)
    {
        this._id = _id;
        notifyUpdate();
    }

    public boolean isSelectionValid()
    {
        if ((_direction != null) && (_name != null))
            return true;
        else
            return false;
    }
}