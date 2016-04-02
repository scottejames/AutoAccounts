package scott.nursery.accounts.payee;

import org.apache.log4j.Logger;
import scott.mvc.Controller;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.ViewEvent;
import scott.mvc.ViewSelection;

public class PayeeRegexpController extends Controller
{

    private static Logger _logger = Logger
            .getLogger(PayeeRegexpController.class);
    private PayeeRegexpModel _model = null;
    
    public enum PAYEEREGEXP_EVENT
    {
        ADD_REGEXP, DELETE_REGEX, EDIT_FROM_REGEX, EDIT_TO_REGEX
    };

    public PayeeRegexpController(Model m, View v)
    {
        super(m, v);
        _model = (PayeeRegexpModel) m;
    }


    @Override
    protected void handleEvent(ViewEvent event)
    {
        if (!(event.event instanceof PAYEEREGEXP_EVENT))
        {
            _logger.info("[handleEvent]  unable to handle event so passing up");
            super.handleEvent(event);
        } else
        {
            switch ((PAYEEREGEXP_EVENT) event.event)
            {
            case ADD_REGEXP:
                addRegexp();
                break;
            case DELETE_REGEX:
                delRegexp();
                break;
            case EDIT_FROM_REGEX:
                editFromRegexp(event.arg);
                break;
            case EDIT_TO_REGEX:
                editToRegexp(event.arg);
                break;
            }
        }
    }

    private void editToRegexp(Object arg)
    {
        _model.set_selectedRegexpTo((String) arg);
    }

    private void editFromRegexp(Object arg)
    {
        _model.set_selectedRegexpFrom((String) arg);
    }

    private void delRegexp()
    {

        if (_model.isSelectionValid())
        {
            _logger.debug("ADD regexp");
            _model.delSelectedRegexp();
        } else
        {
            getView().displayInfo("Please select a row before deleting");
        }

     
    }

    private void addRegexp()
    {
        if (_model.isSelectionValid())
        {
            _logger.debug("ADD regexp");
            _model.addSelectedRegexp();
        } else
        {
            getView().displayInfo("Please specify both fields before adding");
        }
    }


    
    @Override
    protected void viewSelectionChanged(ViewSelection rowSelection)
    {
        PayeeRegexpModel parentModel = (PayeeRegexpModel) getModel();

        PayeeRegexTableModel tableModel = (PayeeRegexTableModel) parentModel.getTableModel();;

        // Update the parent to update the fields data 
        
        parentModel.holdUpdates();
        parentModel.set_selectedRegexpFrom(tableModel.getSelectedRegexp().get_from());
        parentModel.set_selectedRegexpTo(tableModel.getSelectedRegexp().get_to());
        parentModel.set_selectedRegexpId(tableModel.getSelectedRegexp().get_id());
        parentModel.releaseUpdates();
    }
}
