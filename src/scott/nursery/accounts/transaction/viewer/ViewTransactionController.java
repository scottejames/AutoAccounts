package scott.nursery.accounts.transaction.viewer;

import scott.mvc.BaseView;
import scott.mvc.Controller;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.ViewEvent;
import scott.mvc.ViewSelection;

public class ViewTransactionController extends Controller
{

    public ViewTransactionController(Model m, View v)
    {
        super(m, v);
        // TODO Auto-generated constructor stub
    }

  
    @Override
    protected void handleEvent(ViewEvent event)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void menuItemSelected(int arg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void viewClosed(BaseView v)
    {
 
        
    }

    @Override
    protected void viewOpened()
    {
   //     ((ViewTransactionModel) getModel()).loadTransaction();
        
    }

    @Override
    protected void viewSelectionChanged(ViewSelection selection)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void cancelSelected()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void okSelected()
    {
        // TODO Auto-generated method stub
        
    }

}
