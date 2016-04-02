package scott.nursery.accounts.catagory.regexp;

import org.apache.log4j.Logger;
import scott.mvc.Controller;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.ViewEvent;
import scott.mvc.ViewSelection;
import scott.mvc.gui.Utils;
import scott.nursery.accounts.domain.bo.BaseCatagoryRegularExpression;

public class CatagoryRegexpController extends Controller
{
    private static Logger _logger = Logger
            .getLogger(CatagoryRegexpController.class);
    private CatagoryRegexpModel _model = null;
    
    public enum CATAGORYREGEXP_EVENT
    {
        ADD_REGEXP, DELETE_REGEX, EDIT_REGEX, EDIT_NOTES
    };

    public CatagoryRegexpController(Model m, View v)
    {
        super(m, v);
        _model = (CatagoryRegexpModel) m;
    }


    @Override
    protected void handleEvent(ViewEvent event)
    {
        if (!(event.event instanceof CATAGORYREGEXP_EVENT))
        {
            _logger.info("[handleEvent]  unable to handle event so passing up");
            super.handleEvent(event);
        } else
        {
            switch ((CATAGORYREGEXP_EVENT) event.event)
            {
            case ADD_REGEXP:
                addRegexp();
                break;
            case DELETE_REGEX:
                delRegexp();
                break;
            case EDIT_REGEX:
                editRegexp(Utils.toString(event.arg));
                break;
            case EDIT_NOTES:
                editNotes(Utils.toString(event.arg));
                break;
            }
        }    }

    private void editRegexp(String r)
    {
        _model.set_selectedRegexp(r);
    }

    private void editNotes(String n)
    {
        _model.set_selectedNotes(n);
    }

    private void delRegexp()
    {
        _model.delSelected();
    }

    private void addRegexp()
    {
        if (_model.isSelectionValid())
        {
            _model.addSelected();
        } else
        {
            getView().displayInfo("Please specify regexp before adding");
        }
    }

  

    @Override
    protected void viewSelectionChanged(ViewSelection selection)
    {
        CatagoryRegexpModel model = (CatagoryRegexpModel) getModel();

        // Update the parent to update the fields data 
        
        BaseCatagoryRegularExpression regexp = model.getSelectedRegexp();
        
        model.holdUpdates();

        model.set_selectedNotes(regexp.get_notes());
        model.set_selectedRegexp(regexp.get_regexp());
        model.set_selectedID(regexp.get_id());
        
        model.releaseUpdates();               
    }
    
    public String getName()
    {
        return "CATAGORY REGEXP CONTROLLER";
    }
}
