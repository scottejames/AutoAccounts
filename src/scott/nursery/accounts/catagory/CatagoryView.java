package scott.nursery.accounts.catagory;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.ViewEvent;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableController;
import scott.mvc.gui.table.FTableView;
import scott.nursery.accounts.catagory.CatagoryController.CATAGORY_EVENT;

public class CatagoryView extends View
{

    private FTableView _table = null;
    private Combo _comboDirection = null;
    private Text _textCatagory = null;
    private Button _okButton = null;
    private Button _addButton = null;
    private Button _delButton = null;

    public CatagoryView(Shell parentShell, CatagoryModel model)
    {
        super(parentShell, model,SWT.DIALOG_TRIM|SWT.APPLICATION_MODAL);
        getShell().setSize(500, 10000);
        MigLayout layout = new MigLayout("");
        layout.setColumnConstraints("[][grow 50][][grow 50][][]");
        layout.setRowConstraints("[200,fill][][]");
        getShell().setLayout(layout);
        Shell composite = getShell();
        
        _table = new FTableView(this, model.getTableModel(),
                "growy 0,span 6,wrap,hmin 200, wmin 200");
        FTableController tableCont = new FTableController(model
                .getTableModel(), _table);
        Menu tableContextMenu = new Menu(getShell(), SWT.MENU);
        Utils.addMenuItem(tableContextMenu, "Edit Regexp", getMenuListener(),
                CatagoryController.MENU_CONTEXT_REDIT_REGEXP);
        _table.setContextMenu(tableContextMenu);
        _table.open(tableCont);
        
        
        Utils.createLabel(composite, "Direction", "");
        _comboDirection = Utils.createCombo(composite, new String[] { "IN",
                "OUT" }, "growx");
        Utils.createLabel(composite, "Catagory", "");
        _textCatagory = Utils.createTextField(composite, null, "growx");
        _addButton = Utils.createButton(composite, "+", "");
        _delButton = Utils.createButton(composite, "-", "wrap");
        _okButton = Utils.createButton(composite, "OK", "span 6,center,wrap");
        addController();
        getShell().setDefaultButton(_addButton);
    }

    private void addController()
    {
        _okButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                notifyOKSelected();
            }
        });
        _addButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                _addButton.setFocus();
                sendEvent(new ViewEvent(CATAGORY_EVENT.ADD_CATAGORY, null));
            }
        });
        _delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                sendEvent(new ViewEvent(CATAGORY_EVENT.DELETE_CATAGORY, null));
            }
        });
        // add listeners to the text boxes
        _comboDirection.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (_comboDirection.isFocusControl())
                    sendEvent(new ViewEvent(CATAGORY_EVENT.EDIT_DIRECTION,
                            _comboDirection.getText()));
            }
        });
        _textCatagory.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (_textCatagory.isFocusControl())
                    sendEvent(new ViewEvent(CATAGORY_EVENT.EDIT_CATAGORY_NAME,
                            _textCatagory.getText()));
            }
        });
    }

    @Override
    public void modelUpdated(Model m)
    {
        CatagoryModel model = (CatagoryModel) m;
        
        // clear down existing table (if required)
        if (!_comboDirection.isFocusControl())
            _comboDirection.setText(Utils.toString(model.get_direction()));
        if (!_textCatagory.isFocusControl())
            _textCatagory.setText(Utils.toString(model.get_name()));
        
    }
}