package scott.nursery.accounts.transaction;

import java.util.List;
import java.util.Observable;
import scott.hibernate.exception.TransactionException;
import scott.mvc.Model;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseCheque;
import scott.nursery.accounts.domain.bo.BaseTransaction;

public class TransactionBrowserModel extends Model
{
    private ApplicationModel _appModel = null;
    private TransactionTableModel _transactionTableModel = 
        new TransactionTableModel(this, new TransactionBrowserTableDefinition());

    public TransactionBrowserModel()
    {
        _appModel = ApplicationModel.getInstance();
        ;
        _appModel.addObserver(this);
    }

    public TransactionTableModel getTableModel()
    {
        return _transactionTableModel;
    }

    public List<BaseTransaction> getTransactions()
    {
        return _appModel.getTransactionList().getTransactionList();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        // the app model has changed .. We are only adapting the model so pass
        // the notification on.
        notifyUpdate();
    }

    public void addTransactions(List<BaseTransaction> transactionList)
            throws TransactionException
    {
        _appModel.addTransactions(transactionList);
    }

    public void addCheques(List<BaseCheque> chequeData)
            throws TransactionException
    {
        _appModel.addCheques(chequeData);
    }

    public BaseTransaction getSelectedTransaction()
    {
        return _transactionTableModel.getSelectedTransaction();
    }
}
