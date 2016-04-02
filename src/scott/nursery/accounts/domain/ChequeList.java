package scott.nursery.accounts.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import org.apache.log4j.Logger;
import scott.hibernate.HibernateTransaction;
import scott.hibernate.HibernateSessionFactory.STRATEGY;
import scott.hibernate.exception.TransactionException;
import scott.nursery.accounts.domain.bo.BaseCheque;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.accounts.domain.bo.ChequeDAO;

public class ChequeList extends Observable
{
    private static Logger _logger = Logger.getLogger(ChequeList.class);
    protected List<BaseCheque> _chequeList = new ArrayList<BaseCheque>();
    protected HashMap<Long, BaseCheque> _indexById = null;
    protected HashMap<String, BaseCheque> _indexByChequeNo = null;

    public ChequeList()
    {
    }

    public BaseCheque getChequeById(Long id)
    {
        return _indexById.get(id);
    }

    public BaseCheque getChequeByChequeNo(String chequeNo)
    {
        return _indexByChequeNo.get(chequeNo);
    }

   

    public void loadFromDB()
    {
        _logger.debug("[loadFromDB] loading cheques from database");
        HibernateTransaction tx = new HibernateTransaction( STRATEGY.BY_OBJECT);
        try
        {
            tx.beginTransaction();
            ChequeDAO dao = new ChequeDAO(tx);
            _chequeList = dao.findAll();
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

    public void addCheque(BaseCheque cheque)
    {
        _logger.debug("[addChequeList] adding cheque " + cheque);
        _chequeList.add(cheque);

        index();
        persist();
        
        setChanged();
        notifyObservers();
    }
    
    public void addChequeList(List<BaseCheque> chequeList)
    {
        _logger.debug("[addChequeList] adding cheque list of size "
                + chequeList.size());
        _chequeList.addAll(chequeList);
        index();
        persist();
        
        setChanged();
        notifyObservers();
    }
    
    public void enrichTransationWithChequeInfo(BaseTransaction transaction)
    {
        if (transaction.isCheque() == true)
        {
            BaseCheque cheque = getChequeByChequeNo(transaction
                    .get_chequeNumber());
            if (cheque != null)
            {
                transaction.set_payee(cheque.get_payee());
                transaction.set_manualEditPayee(false);
                transaction.set_chequeWrittenDate(cheque.get_writtenDate());
                transaction.set_chequeNotMatched(false);
                
            } else
            {
                transaction.set_payee("NO CHEQUE FOUND");
                transaction.set_manualEditPayee(false);
                transaction.set_chequeWrittenDate(transaction.get_date());
                transaction.set_chequeNotMatched(true);

                _logger.error("Unable to find cheque : "
                        + transaction.get_chequeNumber() + " in cheque list");
            }
        }
    }


    private void persist()
    {
        _logger.debug("[persist] persisting cheque list");
        HibernateTransaction tx = new HibernateTransaction();
        try
        {
            ChequeDAO chequeDAO = new ChequeDAO(tx);
            tx.beginTransaction();
            chequeDAO.makePersistent(_chequeList);
            tx.commitTransaction();
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
        _logger.debug("[index] reindexing cheques");
        _indexById = new HashMap<Long, BaseCheque>();
        _indexByChequeNo = new HashMap<String, BaseCheque>();
        for (BaseCheque c : _chequeList)
        {
            _indexById.put(c.getId(), c);
            _indexByChequeNo.put(c.get_number(), c);
        }
        _logger.debug("[index]  index size is " + _indexById.size()
                + " transaciton list size is " + _chequeList.size());
    }

    public List<BaseCheque> getChequeList()
    {
        // TODO Auto-generated method stub
        return _chequeList;
    }
}
