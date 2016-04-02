package scott.nursery.accounts.transaction;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableController;
import scott.mvc.gui.table.FTableTextCellEditor;
import scott.mvc.gui.table.FTableView;
import scott.mvc.gui.table.IFTableCellEditor;
import scott.nursery.accounts.transaction.editors.AmountCellEditor;
import scott.nursery.accounts.transaction.editors.CatagoryCellEditor;
import scott.nursery.accounts.transaction.editors.DateCellEditor;
import scott.nursery.accounts.transaction.editors.PayeeCellEditor;

public class TransactionBrowserView extends View
{
    private FTableView _table = null;
    private Menu _menuBar = null;
    private TransactionBrowserModel _model = null;

    public TransactionBrowserView(Shell parent, TransactionBrowserModel model)
    {
        super(parent, model);
        _model = model;
        
        MigLayout layout = new MigLayout("");
        layout.setColumnConstraints("[1000,grow]");
        layout.setRowConstraints("[600,grow]");
        getShell().setLayout(layout);
        
        createTable();
        createMenuBars();
    }

    private void createTable()
    {
        _table = new FTableView(this, _model.getTableModel(),
                "grow,hmin 100,wmin 200");
        IFTableCellEditor editors[] = new IFTableCellEditor[9];

        int i = 0;
        editors[i++] = null;
        editors[i++] = new DateCellEditor(_table.getTable());
        editors[i++] = new PayeeCellEditor(_table.getTable());
        editors[i++] = new CatagoryCellEditor(_table.getTable());
        editors[i++] = null;
        editors[i++] = new FTableTextCellEditor(_table.getTable());
        editors[i++] = new AmountCellEditor(_table.getTable());
        editors[i++] = null;
        editors[i++] = null;

        
        _table.makeTableEditable(editors);
        FTableController tableCont = new FTableController(
                _model.getTableModel(), _table);
        Menu tableContextMenu = new Menu(getShell(), SWT.MENU);
        Utils.addMenuItem(tableContextMenu, "Reset Regexp", getMenuListener(),
                TransactionBrowserController.MENU_CONTEXT_RESET_PAYEE_REGEXP);
        Utils.addMenuItem(tableContextMenu, "Add Catagory Regexp", getMenuListener(),
                TransactionBrowserController.MENU_CONTEXT_ADD_CATAGORY_REGEXP);
        Utils.addMenuItem(tableContextMenu, "Add Payee Regexp", getMenuListener(),
                TransactionBrowserController.MENU_CONTEXT_ADD_PAYEE_REGEXP);
        _table.setContextMenu(tableContextMenu);
        _table.open(tableCont);
    }



    private void createMenuBars()
    {
        _menuBar = new Menu(getShell(), SWT.BAR);
        getShell().setMenuBar(_menuBar);
        Menu fileMenu = Utils.addMenu(getShell(), _menuBar, "File");
        Utils.addMenuItem(fileMenu, "Load Transactions", getMenuListener(),
                TransactionBrowserController.MENU_LOAD_TRANSACTIONS);
        Utils.addMenuItem(fileMenu, "Load Cheques", getMenuListener(),
                TransactionBrowserController.MENU_LOAD_CHEQUES);
        Utils.addMenuItem(fileMenu, "Browse Cheques", getMenuListener(),
                TransactionBrowserController.MENU_BROWSE_CHEQUES);
        Utils.addMenuItem(fileMenu, "Browse Payee Regexp", getMenuListener(),
                TransactionBrowserController.MENU_BROWSE_PAYEE_REGEXP);
        Utils.addMenuItem(fileMenu, "Browse Catagory",
                getMenuListener(),
                TransactionBrowserController.MENU_BROWSE_CATAGORY_REGEXP);
        Utils.addMenuItem(fileMenu, "Dump Accountants Report", getMenuListener(),
                TransactionBrowserController.MENU_DUMP_ACCOUNTANTS_REPORT);
        Utils.addMenuItem(fileMenu, "Exit", getMenuListener(),
                TransactionBrowserController.MENU_EXIT);
        Menu diagnosticsMenu = Utils.addMenu(getShell(), _menuBar,
                "Diagnostics");
        Utils.addMenuItem(diagnosticsMenu, "Blank Database", getMenuListener(),
                TransactionBrowserController.MENU_BLANK_DATABASE);
        Utils.addMenuItem(diagnosticsMenu, "Revert Payees", getMenuListener(),
                TransactionBrowserController.MENU_BLANK_DATABASE);
     
    }

    @Override
    public void modelUpdated(Model m)
    {
    }
}