package scott.nursery.accounts.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import org.apache.log4j.Logger;
import scott.hibernate.HibernateTransaction;
import scott.hibernate.HibernateSessionFactory.STRATEGY;
import scott.hibernate.exception.TransactionException;
import scott.mvc.gui.Utils;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseCatagoryRegularExpression;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.accounts.domain.bo.CatagoryRegularExpressionDAO;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class CatagoryRegexpList extends Observable
{
    private static Logger _logger = Logger.getLogger(CatagoryRegexpList.class);
    protected List<BaseCatagoryRegularExpression> _regexpList = new ArrayList<BaseCatagoryRegularExpression>();
    protected HashMap<Long, BaseCatagoryRegularExpression> _indexById = null;
    protected HashMap<Long, ArrayList<BaseCatagoryRegularExpression>> _indexByCatagoryId = null;
    protected CatagoryList _catagoryList = null;

    public void loadFromDB()
    {
        _logger.debug("[loadFromDB] loading catagory regexp from database");
        HibernateTransaction tx = new HibernateTransaction(STRATEGY.BY_OBJECT);
        try
        { 
            tx.beginTransaction();
            CatagoryRegularExpressionDAO dao = new CatagoryRegularExpressionDAO(
                    tx);
            _regexpList = dao.findAll();
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

    private void index()
    {
        _logger.debug("[index] reindexing catagory regexps");
        _indexById = new HashMap<Long, BaseCatagoryRegularExpression>();
        _indexByCatagoryId = new HashMap<Long, ArrayList<BaseCatagoryRegularExpression>>();
        for (BaseCatagoryRegularExpression c : _regexpList)
        {
            Long id = c.get_id();
            Long cat_id = c.get_catagoryId();
            _indexById.put(id, c);
            if (_indexByCatagoryId.get(cat_id) == null)
                _indexByCatagoryId.put(cat_id,
                        new ArrayList<BaseCatagoryRegularExpression>());
            ArrayList<BaseCatagoryRegularExpression> arr = _indexByCatagoryId
                    .get(cat_id);
            arr.add(c);
        }
        _logger.debug("[index]  index size is " + _indexById.size()
                + " transaciton list size is " + _regexpList.size());
    }

    private void persist()
    {
        _logger.debug("[persist] persisting catagory regexp list");
        HibernateTransaction tx = new HibernateTransaction();
        try
        {
            CatagoryRegularExpressionDAO dao = new CatagoryRegularExpressionDAO(
                    tx);
            tx.beginTransaction();
            dao.makePersistent(_regexpList);
            tx.commitTransaction();
        } catch (TransactionException e)
        {
            _logger.error("Unable to load regexp " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    private void delete(BaseCatagoryRegularExpression r)
    {
        _logger.debug("[delete] deleting catagory regexp list");
        HibernateTransaction tx = new HibernateTransaction();
        try
        {
            CatagoryRegularExpressionDAO dao = new CatagoryRegularExpressionDAO(
                    tx);
            tx.beginTransaction();
            dao.makeTransient(r);
            tx.commitTransaction();
        } catch (TransactionException e)
        {
            _logger.error("Unable to delete regexp " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    public void addRegexp(BaseCatagoryRegularExpression regexp)
    {
        if (regexp.get_id() != null)
        {
            BaseCatagoryRegularExpression update = _indexById.get(regexp
                    .get_id());
            update.set_notes(Utils.toString(regexp.get_notes()));
            update.set_regexp(regexp.get_regexp());
        } else
        {
            _regexpList.add(regexp);
        }
        setChanged();
        notifyObservers();
        index();
        persist();
    }

    public BaseCatagoryRegularExpression getRegexpById(Long id)
    {
        return _indexById.get(id);
    }

    public void delRegexpById(Long id)
    {
        BaseCatagoryRegularExpression c = _indexById.get(id);
        _regexpList.remove(c);
        delete(c);
        setChanged();
        notifyObservers();
        persist();
        index();
    }

    public void delRegexpByCatagoryId(Long id)
    {
        List<BaseCatagoryRegularExpression> list = _indexByCatagoryId.get(id);
        for (BaseCatagoryRegularExpression c : list)
        {
            delete(c);
            _regexpList.remove(c);
        }
        setChanged();
        notifyObservers();
        persist();
        index();
    }

    public List<BaseCatagoryRegularExpression> getCatagoryRegularExpression()
    {
        return _regexpList;
    }

    public List<BaseCatagoryRegularExpression> getCatagoryRegularExpressionByCatagoryId(
            Long id)
    {
        return _indexByCatagoryId.get(id);
    }

    public void enrichTransationsWithCatagoryRegexpInfo(
            BaseTransaction transaction)
    {
        int signum = transaction.get_amount().signum();
        boolean isManualEditCatagory = transaction.is_manualEditCatagory();
        for (BaseCatagoryRegularExpression regexp : _regexpList)
        {
            BaseCatagory catagory = _catagoryList.getCatagoryById(regexp
                    .get_catagoryId());
            if (((signum > 0) && (catagory.get_direction() == DIRECTION.IN))
                    || ((signum < 0) && (catagory.get_direction() == DIRECTION.OUT)))
            {
                String expression = regexp.get_regexp();
                String payee = transaction.get_payee();
                if ((payee != null) && (expression != null))
                    if (payee.matches(expression))
                    {
                        if (isManualEditCatagory == true)
                        {
                            if ((transaction.get_catagoryID() == catagory
                                    .get_id()))
                            {
                                // if the catagory was manually entered but now
                                // matches mark it as auto
                                transaction.set_manualEditCatagory(false);
                            }
                        } else
                        {
                            // Dont update unless its changed
                            if ((transaction.get_catagoryID() != catagory
                                    .get_id()))
                            {
                                System.err.println("SOMETHING CHANGED");
                                transaction.set_catagoryID(catagory.get_id());
                                transaction.set_manualEditCatagory(false);
                                break;
                            }
                        }
                    }
            }
        }
    }

    public void setCatagoryList(CatagoryList catagoryList)
    {
        _catagoryList = catagoryList;
    }
}
