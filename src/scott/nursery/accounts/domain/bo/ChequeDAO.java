package scott.nursery.accounts.domain.bo;

import scott.hibernate.GenericHibernateDAO;
import scott.hibernate.HibernateTransaction;

public class ChequeDAO
    extends GenericHibernateDAO<BaseCheque, Long>
{

    public ChequeDAO(HibernateTransaction transaction)
    { 
        super(transaction);
    }


}
