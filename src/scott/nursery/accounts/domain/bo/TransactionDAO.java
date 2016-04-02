package scott.nursery.accounts.domain.bo;

import java.util.List;
import scott.hibernate.GenericHibernateDAO;
import scott.hibernate.HibernateTransaction;

public class TransactionDAO extends GenericHibernateDAO<BaseTransaction, Long>
{
    public TransactionDAO(HibernateTransaction transaction)
    {
        super(transaction);
    }

    public List<BaseTransaction> findBySourceFile(String fileName)
    {
        BaseTransaction sample = new BaseTransaction();
        sample.set_fileName(fileName);
        String[] excludes = { "_amount", "_date", "_isCheque", "_payee" };
        List<BaseTransaction> results = findByExample(sample, excludes);
        return results;
    }

    public void removeDataBySourceFile(String fileName)
    {
        List<BaseTransaction> list = findBySourceFile(fileName);
        if (list.size() > 0)
        {
            makeTransient(list);
        }
    }
}
