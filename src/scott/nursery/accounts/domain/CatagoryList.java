package scott.nursery.accounts.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import scott.hibernate.HibernateTransaction;
import scott.hibernate.HibernateSessionFactory.STRATEGY;
import scott.hibernate.exception.TransactionException;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.CatagoryDAO;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class CatagoryList
{
    private static Logger _logger = Logger.getLogger(CatagoryList.class);
    protected List<BaseCatagory> _catagoryList = new ArrayList<BaseCatagory>();
    protected HashMap<Long, BaseCatagory> _indexById = null;
    protected HashMap<String, BaseCatagory> _indexByName = null;
    private ArrayList<BaseCatagory> _indexByDirectionIn;
    private ArrayList<BaseCatagory> _indexByDirectionOut;

    public CatagoryList()
    {
    }

    public void loadFromDB()
    {
        _logger.debug("[loadFromDB] loading catagory list from database");
        HibernateTransaction tx = new HibernateTransaction();
        try
        {
            tx.beginTransaction();
            CatagoryDAO dao = new CatagoryDAO(tx);
            _catagoryList = dao.findAll();
            tx.commitTransaction();
            index();
        } catch (TransactionException e)
        {
            _logger.error("Unable to load catagories " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    private void persist()
    {
        _logger.debug("[persist] persisting catagory list");
        HibernateTransaction tx = new HibernateTransaction(STRATEGY.BY_OBJECT);
        try
        {
            tx.beginTransaction();
            CatagoryDAO dao = new CatagoryDAO(tx);
            dao.makePersistent(_catagoryList);
            tx.commitTransaction();
        } catch (TransactionException e)
        {
            _logger.error("Unable to save regexp " + e.toString());
        } finally
        {
            tx.dispose();
        }
    }

    private void index()
    {
        _logger.debug("[index] reindexing transactions");
        _indexById = new HashMap<Long, BaseCatagory>();
        _indexByName = new HashMap<String, BaseCatagory>();
        _indexByDirectionIn = new ArrayList<BaseCatagory>();
        _indexByDirectionOut = new ArrayList<BaseCatagory>();
        for (BaseCatagory t : _catagoryList)
        {
            _indexById.put(t.get_id(), t);
            _indexByName.put(t.get_name(), t);
            if (t.get_direction() == DIRECTION.IN)
                _indexByDirectionIn.add(t);
            else
                _indexByDirectionOut.add(t);
        }
        _logger.debug("[index]  index size is " + _indexById.size()
                + " catagory list size is " + _catagoryList.size());
    }

    public List<BaseCatagory> getCatagoryListIN()
    {
        return _indexByDirectionIn;
    }

    public List<BaseCatagory> getCatagoryListOUT()
    {
        return _indexByDirectionOut;
    }

    public List<BaseCatagory> getCatagoryList()
    {
        return _catagoryList;
    }

    public BaseCatagory getCatagoryFromName(String name)
    {
        return _indexByName.get(name);
    }

    public BaseCatagory getCatagoryById(Long id)
    {
        _logger.debug("[getById] get regexp by id : " + id + " index size is "
                + _indexById.size() + " tran size is " + _catagoryList.size());
        if (_indexById == null)
        {
            _logger
                    .error("Trying to get a catagory by ID but the index is null");
            return null;
        }
        if (_indexById.size() == 0)
        {
            _logger
                    .error("Trying to get a catagory by ID but the index is empty");
            return null;
        }
        BaseCatagory c = _indexById.get(id);
        _logger.debug("getById] returning catagory : " + c);
        return c;
    }

    public void delCatagoryById(Long id)
    {
        BaseCatagory c = _indexById.get(id);
        _catagoryList.remove(c);
        persist();
        index();
    }

    public void addCatagory(BaseCatagory cat)
    {
        BaseCatagory existingCatagory = _indexByName.get(cat.get_name());
        if (existingCatagory != null)
        {
            existingCatagory.set_direction(cat.get_direction());
        } else if (cat.get_id() != null)
        {
            BaseCatagory update = _indexById.get(cat.get_id());
            update.set_direction(cat.get_direction());
            update.set_name(cat.get_name());
        } else
        {
            _catagoryList.add(cat);
        }
        persist();
        index();
    }

    public String[] getCatagoryNames(DIRECTION dir)
    {
        List<String> catagoryList = new ArrayList<String>();
        if (dir == null)
        {
            _logger
                    .error("ERROR! direction is not set and trying to get a  lit of catagories");
        }
        for (BaseCatagory cat : _catagoryList)
        {
            if (cat.get_direction() == dir)
                catagoryList.add(cat.get_name());
        }
        String[] results = new String[catagoryList.size()];
        return catagoryList.toArray(results);
    }
}
