package scott.nursery.accounts.payee;

import net.miginfocom.swt.MigLayout;
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
import scott.nursery.accounts.payee.PayeeRegexpController.PAYEEREGEXP_EVENT;

public class PayeeRegexpView extends View
{
    private FTableView _table = null;
    private Text _textFrom = null;
    private Text _textTo = null;
    private Button _okButton = null;
    private Button _addButton = null;
    private Button _delButton = null;

    public PayeeRegexpView(Shell shell, PayeeRegexpModel model)
    {
        super(shell, model);
        getShell().setSize(500, 10000);
        MigLayout layout = new MigLayout("");
        layout.setColumnConstraints("[][grow 50][][grow 50][][]");
        layout.setRowConstraints("[200,fill][][]");
        getShell().setLayout(layout);
        Shell composite = getShell();
        _table = new FTableView(this, model.getTableModel(),
                "growy 0,span 6,wrap,hmin 200, wmin 200");
        FTableController tableCont = new FTableController(
                model.getTableModel(), _table);
        _table.open(tableCont);
        Utils.createLabel(composite, "From", "");
        _textFrom = Utils.createTextField(composite, null, "growx");
        Utils.createLabel(composite, "To", "");
        _textTo = Utils.createTextField(composite, null, "growx");
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
                sendEvent(new ViewEvent(PAYEEREGEXP_EVENT.ADD_REGEXP, null));
            }
        });
        _delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                sendEvent(new ViewEvent(PAYEEREGEXP_EVENT.DELETE_REGEX, null));
            }
        });
        // add listeners to the text boxes
        _textTo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (_textTo.isFocusControl())
                    sendEvent(new ViewEvent(PAYEEREGEXP_EVENT.EDIT_TO_REGEX,
                            _textTo.getText()));
            }
        });
        _textFrom.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (_textFrom.isFocusControl())
                    sendEvent(new ViewEvent(PAYEEREGEXP_EVENT.EDIT_FROM_REGEX,
                            _textFrom.getText()));
            }
        });
    }

    @Override
    public void modelUpdated(Model m)
    {
        PayeeRegexpModel model = (PayeeRegexpModel) m;
        if (!_textFrom.isFocusControl())
            _textFrom.setText(Utils.toString(model.getSelectedFrom()));
        if (!_textTo.isFocusControl())
            _textTo.setText(Utils.toString(model.getSelectedTo()));
    }
}
