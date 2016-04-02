package scott.nursery.accounts.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import org.apache.log4j.Logger;
import scott.hibernate.HibernateTransaction;
import scott.hibernate.HibernateSessionFactory.STRATEGY;
import scott.hibernate.exception.TransactionException;
import scott.nursery.accounts.domain.bo.BasePayeeRegularExpression;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.accounts.domain.bo.PayeeRegularExpressionDAO;

public class PayeeRegexList extends Observable
{
    private static Logger _logger = Logger.getLogger(PayeeRegexList.class);
    protected List<BasePayeeRegularExpression> _payeeRegexList = new ArrayList<BasePayeeRegularExpression>();
    protected HashMap<Long, BasePayeeRegularExpression> _indexById = null;

    public PayeeRegexList()
    {
    }

    public void loadFromDB()
    {
        _logger.debug("[loadFromDB] loading payee regexp from database");
        HibernateTransaction tx = new HibernateTransaction(STRATEGY.BY_OBJECT);
        try
        {
            tx.beginTransaction();
            PayeeRegularExpressionDAO dao = new PayeeRegularExpressionDAO(tx);
            _payeeRegexList = dao.findAll();
            tx.commitTransaction();
            index();
        } catch (TransactionException e)
        {
            _logger.error("Unable to load payee regexp " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    public void addRegexp(BasePayeeRegularExpression r)
    {
        _logger.debug("[addRegexp] add regepx " + r);
        if (r.get_id() != null)
        {
            BasePayeeRegularExpression update = _indexById.get(r.get_id());
            update.set_from(r.get_from());
            update.set_to(r.get_to());
        } else
        {
            _payeeRegexList.add(r);
        }
        setChanged();
        List<BasePayeeRegularExpression> arg = new ArrayList<BasePayeeRegularExpression>();
        arg.add(r);
        notifyObservers(arg);
        persist();
        index();
    }

    public void addRegexpList(List<BasePayeeRegularExpression> list)
    {
        _logger.debug("[addTransactionList] add regexp list size "
                + list.size());
        _payeeRegexList.addAll(list);
        setChanged();
        notifyObservers(list);
        persist();
        index();
    }

    private void persist()
    {
        _logger.debug("[persist] persisting transaction list");
        HibernateTransaction tx = new HibernateTransaction();
        try
        {
            tx.beginTransaction();
            PayeeRegularExpressionDAO dao = new PayeeRegularExpressionDAO(tx);
            dao.makePersistent(_payeeRegexList);
            tx.commitTransaction();
        } catch (TransactionException e)
        {
            _logger.error("Unable to save regexp " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }
    private void delete(BasePayeeRegularExpression p)
    {
        _logger.debug("[persist] persisting transaction list");
        HibernateTransaction tx = new HibernateTransaction();
        try
        {
            tx.beginTransaction();
            PayeeRegularExpressionDAO dao = new PayeeRegularExpressionDAO(tx);
            dao.makeTransient(p);
            tx.commitTransaction();
        } catch (TransactionException e)
        {
            _logger.error("Unable to delete regexp " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }
    private void index()
    {
        _logger.debug("[index] reindexing transactions");
        _indexById = new HashMap<Long, BasePayeeRegularExpression>();
        for (BasePayeeRegularExpression t : _payeeRegexList)
        {
            _indexById.put(t.get_id(), t);
        }
        _logger.debug("[index]  index size is " + _indexById.size()
                + " transaciton list size is " + _payeeRegexList.size());
    }

    public BasePayeeRegularExpression getById(Long id)
    {
        _logger
                .debug("[getById] get regexp by id : " + id + " index size is "
                        + _indexById.size() + " tran size is "
                        + _payeeRegexList.size());
        if (_indexById == null)
        {
            _logger.error("Trying to get a payee by ID but the index is null");
            return null;
        }
        if (_indexById.size() == 0)
        {
            _logger.error("Trying to get a payee by ID but the index is empty");
            return null;
        }
        BasePayeeRegularExpression tran = _indexById.get(id);
        _logger.debug("getById] returning tran : " + tran);
        return tran;
    }

    public static void enrichTransationsWithPayeeRegexpInfo(
            BaseTransaction transaction,
            List<BasePayeeRegularExpression> expressionList)
    {
        transaction.set_enrichedPayee(null);
        for (BasePayeeRegularExpression regexp : expressionList)
        {
            String payee = transaction.get_payee();
            if (payee != null)
            {
                String enrichedPayee = payee.replaceAll(regexp.get_from(),
                        regexp.get_to());
                
                if (!payee.equals(enrichedPayee))
                {
                    transaction.set_enrichedPayee(enrichedPayee);
                    _logger.debug("Enriched PAYEE : from " + payee + " to "
                            + enrichedPayee);
                    break;
                } 
            }
        }
    }

    public void enrichTransationsWithPayeeRegexpInfo(BaseTransaction transaction)
    {
        enrichTransationsWithPayeeRegexpInfo(transaction, _payeeRegexList);
    }

    public List<BasePayeeRegularExpression> getPayeeRegexpList()
    {
        return _payeeRegexList;
    }

    public void delRegexp(Long id)
    {
        BasePayeeRegularExpression r = _indexById.get(id);
        _payeeRegexList.remove(r);
        this.delete(r);
        setChanged();
        notifyObservers();
        
        persist();
        index();
    }
}
