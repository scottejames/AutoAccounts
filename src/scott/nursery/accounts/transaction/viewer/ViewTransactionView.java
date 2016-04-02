package scott.nursery.accounts.transaction.viewer;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.gui.Utils;
import scott.nursery.accounts.transaction.TransactionBrowserModel;


public class ViewTransactionView extends View
{
    
    private Text _idLabel = null;
    private Text _dateLabel = null;
    private Text _chqNoLabel = null;
    private Text _chqWrittenDateLabel = null;
    private Text _payeeLabel = null;
    private Text _enrichedPayeeLabel = null;
    private Text _amtLabel = null;
    private Text _fileNameLabel = null;
    private TransactionBrowserModel _model = null;
    
    public ViewTransactionView(Shell shell,Model model)
    {
        super(shell, model,SWT.DIALOG_TRIM);
        initalize();
        _model = (TransactionBrowserModel) model;
       
      }
   
    public void initalize()
    {
        getShell().setSize(500, 10000);
        MigLayout layout = new MigLayout("");
        layout.setColumnConstraints("[100][fill,400]");
        layout.setRowConstraints("");
        getShell().setLayout(layout);

        Composite composite = getShell();
        
        
        Utils.createLabel(composite, "ID:","");
        _idLabel = Utils.createTextField(composite, "","wrap",false);
        Utils.createLabel(composite, "Date:","");
         _dateLabel = Utils.createTextField(composite, "","wrap",false);
         Utils.createLabel(composite, "Cheque No:","");
         _chqNoLabel = Utils.createTextField(composite, "","wrap",false);
         Utils.createLabel(composite, "Date Written:","");
         _chqWrittenDateLabel = Utils.createTextField(composite, "","wrap",false);
         Utils.createLabel(composite, "Payee:","");
         _payeeLabel = Utils.createTextField(composite, "","wrap",false);
         Utils.createLabel(composite, "Enriched Payee:","");
         _enrichedPayeeLabel = Utils.createTextField(composite, "","wrap",false);
         Utils.createLabel(composite, "Amount:","");
         _amtLabel = Utils.createTextField(composite, "","wrap",false);
         Utils.createLabel(composite, "File Name:","");
         _fileNameLabel = Utils.createTextField(composite, "","wrap",false);   
    }
    @Override
    public void modelUpdated(Model m)
    {
        scott.nursery.accounts.domain.bo.BaseTransaction transaction =
         _model.getSelectedTransaction();
      
      if (transaction != null)
      {
      
          _amtLabel.setText("" + transaction.get_amount());
          _chqNoLabel.setText(transaction.get_chequeNumber()==null?"":transaction.get_chequeNumber());
          _chqWrittenDateLabel.setText(transaction.get_chequeWrittenDate()==null?"":transaction.get_chequeWrittenDate().toString());
          _dateLabel.setText(transaction.get_date()==null?"":transaction.get_date().toString());
          _enrichedPayeeLabel.setText( transaction.get_enrichedPayee()==null?"":transaction.get_enrichedPayee());
          _fileNameLabel.setText(transaction.get_fileName());
          _idLabel.setText("" + transaction.get_id());
          _payeeLabel.setText(transaction.get_payee()==null?"":transaction.get_payee());
      }
              
    }
}
