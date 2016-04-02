package scott.nursery.accounts.domain;

import java.util.List;
import org.apache.log4j.Logger;
import scott.hibernate.exception.TransactionException;
import scott.mvc.Model;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseCatagoryRegularExpression;
import scott.nursery.accounts.domain.bo.BaseCheque;
import scott.nursery.accounts.domain.bo.BasePayeeRegularExpression;
import scott.nursery.accounts.domain.bo.BaseTransaction;

public class ApplicationModel extends Model
{
    private static Logger _logger = Logger
    .getLogger(ApplicationModel.class);
    private final TransactionList _transactionList = new TransactionList();
    private final ChequeList _chequeList = new ChequeList();
    private final PayeeRegexList _payeeRegexpList = new PayeeRegexList();
    private final CatagoryList _catagoryList = new CatagoryList();
    private static ApplicationModel _instance = null;
    private final CatagoryRegexpList _catagoryRegexpList = new CatagoryRegexpList();
    
    public static ApplicationModel getInstance()
    {
        if (_instance == null)
        {
            _instance = new ApplicationModel();
        }  
        return _instance;
    }

    private ApplicationModel()
    {
      loadFromDB();
      
      _transactionList.setChequeList(_chequeList);
      _transactionList.setPayeeRegexpList(_payeeRegexpList);
      _transactionList.setCatagoryRegexpList(_catagoryRegexpList);
      _catagoryRegexpList.setCatagoryList(_catagoryList);
    }

    public void loadFromDB()
    {
        _transactionList.loadFromDB();
        _chequeList.loadFromDB();
        _payeeRegexpList.loadFromDB();
        _catagoryList.loadFromDB();
        _catagoryRegexpList.loadFromDB();
        
        notifyUpdate();
    }
    
    public void addTransactions(List<BaseTransaction> transactionList)
            throws TransactionException
    {
        _transactionList.addTransactionList(transactionList);

        notifyUpdate();

    }

    /***
     * Adds or updates a transaction,  if a transaction with the specificed ID is already held in the 
     * model the transaction is replaced with the param else the param is just added
     * 
     * @param transaction
     */
    public void addOrUpdateTransaction(BaseTransaction transaction)
     {        
        _transactionList.addTransaction(transaction);
        notifyUpdate();
    }

    public void addCheques(List<BaseCheque> chequeList)
            throws TransactionException
    {
        _chequeList.addChequeList(chequeList);
        notifyUpdate();

    }

    public void addOrUpdatePayeeRegexp(BasePayeeRegularExpression payee)
    {

        _payeeRegexpList.addRegexp(payee);
        notifyUpdate();
    }
 

    public void delPayeeRegex(Long id)
    {
        _logger.info(">> DEL PAYEE REGEXP");
        _payeeRegexpList.delRegexp(id);
        
        notifyUpdate();
    }

    public void addOrUpdateCatagory(BaseCatagory c)
    {
        _logger.info(">> ADD CATAGORY");

        _catagoryList.addCatagory(c);
        
        notifyUpdate();

    }
    
    public void addOrUpdateCatagoryRegexp(
                BaseCatagoryRegularExpression regexp)
    {
        _catagoryRegexpList.addRegexp(regexp);
        notifyUpdate();

        
    }
    
    public void delCatagoryRegexp(Long id)
    {
        _catagoryRegexpList.delRegexpById(id);
        notifyUpdate();
    }

    public void delCatagory(Long id)
    {
        _logger.info(">> DEL CATAGORY");

        _catagoryList.delCatagoryById(id);
        notifyUpdate();

    }
    


    // --------- accessors
    public TransactionList getTransactionList()
    {
        return _transactionList;
    }

    public ChequeList getChequeList()
    {
        return _chequeList;
    }

    public PayeeRegexList getPayeeRegexpList()
    {
        return _payeeRegexpList;
    }

    public CatagoryList getCatagoryList()
    {
        return _catagoryList;
    }
    public CatagoryRegexpList getCatagoryRegexpList()
    {
        return _catagoryRegexpList;
    }



}
