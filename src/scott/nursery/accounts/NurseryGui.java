package scott.nursery.accounts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import scott.nursery.accounts.transaction.TransactionBrowserController;
import scott.nursery.accounts.transaction.TransactionBrowserModel;
import scott.nursery.accounts.transaction.TransactionBrowserView;

/***
 * Main class to launch the nursery accounts gui.   Args allow it to spawn into certain
 * sub forms for diagnostics.
 * 
 * Exposes certain properties taht everyone else needs .. not terribly elegent but 
 * gets the job done.
 * 
 * @author scott
 *
 */
public class NurseryGui
{
    public static Color RED = null;
    public static Color GRAY = null;
    public static Color BLACK = null;
    
    
    public  static Shell shell = null;
    public static Display display = null;
    // ...
    
    public static void main(String[] args)
    {
        display = new Display();
        shell = new Shell(display);
         RED = display.getSystemColor(SWT.COLOR_RED);
        GRAY = display.getSystemColor(SWT.COLOR_GRAY);
        BLACK = display.getSystemColor(SWT.COLOR_BLUE);
        
        TransactionBrowserModel model = new TransactionBrowserModel();
        TransactionBrowserView view = new TransactionBrowserView(shell, model);
        TransactionBrowserController controller = new TransactionBrowserController(
                model, view);
        view.open(controller);
        
    }
    
}
