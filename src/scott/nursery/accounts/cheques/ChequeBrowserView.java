package scott.nursery.accounts.cheques;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableView;

public class ChequeBrowserView extends View
{
    @SuppressWarnings("unused")
	private FTableView _table = null;
    private final Object[][] COLUMN_DETAILS = {
            { "Date", 100 }, { "ChequeNumber", 100 }, { "Payee", 300 },
            { "Amount", 150 } };
    
    public ChequeBrowserView(Shell shell, Model model)
    {
        super(shell, model);
        MigLayout layout = new MigLayout("");
        layout.setColumnConstraints("[750,grow]");
        layout.setRowConstraints("[500,grow]");
        getShell().setLayout(layout);
        Composite composite = getShell();
        
        _table = Utils.createTable(composite, COLUMN_DETAILS,
                "grow,hmin 100,wmin 200");
        
     }




    @Override
    public void modelUpdated(Model m)
    {
    
        //ChequeBrowserModel model = (ChequeBrowserModel) m;

    }
}

