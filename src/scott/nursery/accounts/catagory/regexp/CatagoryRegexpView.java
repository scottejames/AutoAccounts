package scott.nursery.accounts.catagory.regexp;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.ViewEvent;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableController;
import scott.mvc.gui.table.FTableView;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpController.CATAGORYREGEXP_EVENT;

public class CatagoryRegexpView extends View
{


    
    private FTableView  _table = null;
    private Text _textRegexp = null;
    private Text _textNotes = null;
    private Button _okButton = null;
    private Button _addButton = null;
    private Button _delButton = null;
    
    public CatagoryRegexpView(Shell parent, CatagoryRegexpModel model)
    {
        super(parent, model,SWT.DIALOG_TRIM |SWT.APPLICATION_MODAL);
        getShell().setSize(500,10000);
        MigLayout layout = new MigLayout("");
        layout.setColumnConstraints("[][grow 50][][grow 50][][]");
        layout.setRowConstraints("[200,fill][][]");
        getShell().setLayout(layout);

        Shell composite = getShell();
        composite.setText(model.get_catagory().get_name());
       _table = new FTableView(this,model.getTableModel(),
               "growy 0,span 6,wrap,hmin 200, wmin 200");

       FTableController tableCont = 
           new FTableController(model.getTableModel(),_table); 
       
       _table.open(tableCont);
   
       Utils.createLabel(composite, "Regexp", "");
       _textRegexp = Utils.createTextField(composite, null, "growx");
       Utils.createLabel(composite, "Notes", "");
       _textNotes = Utils.createTextField(composite, null, "growx");
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
                sendEvent(new ViewEvent(CATAGORYREGEXP_EVENT.ADD_REGEXP, null));
            }
        });
        _delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                sendEvent(new ViewEvent(CATAGORYREGEXP_EVENT.DELETE_REGEX, null));
            }
        });
        // add listeners to the text boxes
        _textNotes.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (_textNotes.isFocusControl())
                    sendEvent(new ViewEvent(CATAGORYREGEXP_EVENT.EDIT_NOTES,
                            _textNotes.getText()));
            }
        });
        _textRegexp.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (_textRegexp.isFocusControl())
                    sendEvent(new ViewEvent(CATAGORYREGEXP_EVENT.EDIT_REGEX,
                            _textRegexp.getText()));
            }
        });
    }

    @Override
    public void modelUpdated(Model m)
    {
        CatagoryRegexpModel model = (CatagoryRegexpModel) m;
                
        
        if (!_textNotes.isFocusControl())
            _textNotes.setText(Utils.toString(model.get_selectedNotes()));
        if (!_textRegexp.isFocusControl())
            _textRegexp.setText(Utils.toString(model.get_selectedRegexp()));        
    }
}
