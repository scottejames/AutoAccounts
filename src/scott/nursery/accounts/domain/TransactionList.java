package scott.nursery.accounts.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;
import scott.hibernate.HibernateTransaction;
import scott.hibernate.HibernateSessionFactory.STRATEGY;
import scott.hibernate.exception.TransactionException;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.accounts.domain.bo.TransactionDAO;

public class TransactionList implements Observer
{
    private static Logger _logger = Logger.getLogger(TransactionList.class);
    protected List<BaseTransaction> _transactionList = new ArrayList<BaseTransaction>();
    protected HashMap<Long, BaseTransaction> _indexById = null;
    private ChequeList _chequeList;
    private PayeeRegexList _payeeList;
    private CatagoryRegexpList _catagoryRegexpList;

    public TransactionList()
    {
    }

    public void loadFromFile(String fileName)
    {
        _logger.debug("[loadFromFile] loading transactions from " + fileName);
        TransactionQIFFile file = new TransactionQIFFile(fileName);
        file.loadTransaction();
        _transactionList = file.getTransactionData();
        persist();
        index();
    }

    public void loadFromDB()
    {
        _logger.debug("[loadFromDB] loading transactions from database");
        HibernateTransaction tx = new HibernateTransaction(STRATEGY.BY_OBJECT,
                this);
        try
        {
            tx.beginTransaction();
            TransactionDAO transactionDAO = new TransactionDAO(tx);
            _transactionList = transactionDAO.findAll();
            tx.commitTransaction();
            index();
        } catch (TransactionException e)
        {
            _logger.error("Unable to load transactions " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    public void addTransaction(BaseTransaction transaction)
    {
        _logger.debug("[addTransaction] add transaction " + transaction);
        BaseTransaction original = _indexById.get(transaction.get_id());
        // if the sign of the amount changes then the catagory is no longer
        // valid
        if ((original.get_amount().signum()) != (transaction.get_amount()
                .signum()))
        {
            if (transaction.get_catagoryID() != null)
                transaction.set_catagoryID(null);
        }
        // Enrich this transaction with cheque info
        _chequeList.enrichTransationWithChequeInfo(transaction);
        _payeeList.enrichTransationsWithPayeeRegexpInfo(transaction);
        _catagoryRegexpList
                .enrichTransationsWithCatagoryRegexpInfo(transaction);
        persist();
        index();
    }

    public void addTransactionList(List<BaseTransaction> list)
    {
        _logger.debug("[addTransactionList] add transaction list size "
                + list.size());
        for (BaseTransaction transaction : list)
        {
            _payeeList.enrichTransationsWithPayeeRegexpInfo(transaction);
            _chequeList.enrichTransationWithChequeInfo(transaction);
            _catagoryRegexpList
                    .enrichTransationsWithCatagoryRegexpInfo(transaction);
        }
        _transactionList.addAll(list);
        persist();
        index();
    }

    private void persist()
    {
        _logger.debug("[persist] persisting transaction list");
        HibernateTransaction tx = new HibernateTransaction(STRATEGY.BY_OBJECT,
                this);
        try
        {
            tx.beginTransaction();
            TransactionDAO transactionDAO = new TransactionDAO(tx);
            for (BaseTransaction transaction : _transactionList)
                transactionDAO.makePersistent(transaction);
            tx.commitTransaction();
        } catch (TransactionException e)
        {
            _logger.error("Unable to save transactions " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    private void index()
    {
        _logger.debug("[index] reindexing transactions");
        _indexById = new HashMap<Long, BaseTransaction>();
        for (BaseTransaction t : _transactionList)
        {
            _indexById.put(t.get_id(), t);
        }
        _logger.debug("[index]  index size is " + _indexById.size()
                + " transaciton list size is " + _transactionList.size());
    }

    public BaseTransaction getById(Long id)
    {
        _logger.debug("[getById] get transaction by id : " + id
                + " index size is " + _indexById.size() + " tran size is "
                + _transactionList.size());
        if (_indexById == null)
        {
            _logger
                    .error("Trying to get a transaction by ID but the index is null");
            return null;
        }
        if (_indexById.size() == 0)
        {
            _logger
                    .error("Trying to get a transaction by ID but the index is empty");
            return null;
        }
        BaseTransaction tran = _indexById.get(id);
        _logger.debug("getById] returning tran : " + tran);
        return tran;
    }

    public List<BaseTransaction> getTransactionList()
    {
        return _transactionList;
    }

    public void update(Observable o, Object arg)
    {
        if (o instanceof ChequeList)
        {
            for (BaseTransaction transaction : _transactionList)
                _chequeList.enrichTransationWithChequeInfo(transaction);
        }
        if (o instanceof PayeeRegexList)
        {
            for (BaseTransaction transaction : _transactionList)
                _payeeList.enrichTransationsWithPayeeRegexpInfo(transaction);
        }
        if (o instanceof CatagoryRegexpList)
        {
            for (BaseTransaction transaction : _transactionList)
                _catagoryRegexpList
                        .enrichTransationsWithCatagoryRegexpInfo(transaction);
        }
        index();
        persist();
    }

    public void setChequeList(ChequeList list)
    {
        _chequeList = list;
        _chequeList.addObserver(this);
    }

    public void setPayeeRegexpList(PayeeRegexList list)
    {
        _payeeList = list;
        _payeeList.addObserver(this);
    }

    public void setCatagoryRegexpList(CatagoryRegexpList list)
    {
        _catagoryRegexpList = list;
        _catagoryRegexpList.addObserver(this);
    }

    
    /***
     * Loops over the transaction list given extracting the catagory of each transaction
     *  a hash (of lists) is returned linking transactions to catagoryID
     * @param tranList
     * @return
     */
    public static HashMap<Long, List<BaseTransaction>> indexTransactionListByCatagoryId(
            List<BaseTransaction> tranList)
    {
        HashMap<Long, List<BaseTransaction>> results = new HashMap<Long, List<BaseTransaction>>();
        for (BaseTransaction tran : tranList)
        {
            if (results.get(tran.get_catagoryID()) == null)
                results.put(tran.get_catagoryID(),
                        new ArrayList<BaseTransaction>());
            List<BaseTransaction> arr = results.get(tran.get_catagoryID());
            arr.add(tran);
        }
        return results;
    }
    /***
     * Adds up the amount of all the transactions in the list
     * @param tranList
     * @return total
     */
    public static BigDecimal sumTransactionList(List<BaseTransaction> tranList)
    {
        BigDecimal total = new BigDecimal(0);
        if (tranList == null)
            return total;
        for (BaseTransaction tran : tranList)
        {
            total = total.add(tran.get_amount());
        }
        return total;
    }
    public void dumpAccountsReport(String name)
    {
        TransactionListAccountsReport report = new TransactionListAccountsReport(_transactionList);
        report.dumpAccountsReport( name);
    }
}
