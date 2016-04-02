package scott.nursery.accounts.catagory;

import org.apache.log4j.Logger;
import scott.mvc.BaseController;
import scott.mvc.BaseView;
import scott.mvc.Controller;
import scott.mvc.Model;
import scott.mvc.ViewEvent;
import scott.mvc.ViewSelection;
import scott.mvc.gui.Utils;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpController;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpModel;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpView;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class CatagoryController extends Controller
{
    private static Logger _logger = Logger.getLogger(BaseController.class);
    CatagoryRegexpModel _regexpModel = null;
    CatagoryRegexpView _regexpView = null;
    CatagoryRegexpController _regexpController = null;
    public static final int MENU_CONTEXT_REDIT_REGEXP = 1;
    private CatagoryModel _model = null;
    public enum CATAGORY_EVENT
    {
        ADD_CATAGORY, DELETE_CATAGORY, EDIT_DIRECTION, EDIT_CATAGORY_NAME
    };

    public CatagoryController(Model m, BaseView v)
    {
        super(m, v);
        _model = (CatagoryModel) m;
    }

    @Override
    protected void handleEvent(ViewEvent event)
    {
        switch ((CATAGORY_EVENT) event.event)
        {
        case ADD_CATAGORY:
            addCatagory();
            break;
        case DELETE_CATAGORY:
            delCatagory();
            break;
        case EDIT_DIRECTION:
            editDirection((String) event.arg);
            break;
        case EDIT_CATAGORY_NAME:
            editCatagoryName(Utils.toString(event.arg));
            break;
        }
    }

    private void editCatagoryName(String name)
    {
        _model.set_name(name);
    }

    private void editDirection(String direction)
    {
        if (direction.equals("IN"))
            _model.set_direction(DIRECTION.IN);
        else
            _model.set_direction(DIRECTION.OUT);
    }

    private void delCatagory()
    {
        _model.delCatagory();
    }

    private void addCatagory()
    {
        if (_model.isSelectionValid())
        {
            _model.addCatagory();
        } else
        {
            getView().displayInfo("Please specify direction and name before adding");
        }
    }

    @Override
    protected void menuItemSelected(int arg)
    {
        switch (arg)
        {
        case MENU_CONTEXT_REDIT_REGEXP:
            _logger.info("menu item selected: reset payee regexps");
            editRegexps();
            break;
        }
    }

    private void editRegexps()
    {
        if (_regexpView != null && !_regexpView.isDisposed())
        {
            return;
        } else
        {
            _regexpView = new CatagoryRegexpView(getView().getShell(),
                    _regexpModel);
            _regexpController = new CatagoryRegexpController(_regexpModel,
                    _regexpView);
            _regexpView.open(_regexpController);
        }
    }

    @Override
    protected void viewClosed(BaseView v)
    {
        
    }

    @Override
    protected void viewOpened()
    {
        _regexpModel = new CatagoryRegexpModel();
    }

    @Override
    protected void viewSelectionChanged(ViewSelection selection)
    {
        CatagoryModel model = (CatagoryModel) getModel();
        // Update the parent to update the fields data
        BaseCatagory catagory = model.getSelectedCatagory();
        if (catagory != null)
        {
            model.holdUpdates();
            model.set_name(catagory.get_name());
            model.set_direction(catagory.get_direction());
            model.set_id(catagory.get_id());
            model.releaseUpdates();
        }
        _regexpModel.set_catagory(catagory);
    }
}