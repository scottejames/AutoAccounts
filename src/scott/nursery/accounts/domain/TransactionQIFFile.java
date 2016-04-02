package scott.nursery.accounts.domain;

import java.util.ArrayList;
import java.util.List;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.qif.QIFParser;
import scott.nursery.qif.QIFTransaction;

public class TransactionQIFFile
{
    protected List<BaseTransaction> _transactionList = new ArrayList<BaseTransaction>();
    protected List<QIFTransaction> _qifTransactionList = new ArrayList<QIFTransaction>();
    protected String _fileName;
    protected int _errorCount = 0;
    protected List<String> _errors = new ArrayList<String>();

    public TransactionQIFFile(String fileName)
    {
        _fileName = fileName;
    }

    public void loadTransaction()
    {
        _qifTransactionList = QIFParser.parseAccountTransactions(_fileName);
        
        for (QIFTransaction tran : _qifTransactionList)
        {
            BaseTransaction transaction = new BaseTransaction(tran, _fileName);
            _transactionList.add(transaction);
        }
    }
    
    public int getTransactionCount()
    {
        return _transactionList.size();
    }

    public List<BaseTransaction> getTransactionData()
    {
        return _transactionList;
    }

    public int getErrorCount()
    {
        return _errors.size();
    }

    public List<String> getErrors()
    {
        return _errors;
    }

    public int getNumberOfTransactionLoaded()
    {
        return _transactionList.size();
    }
}
