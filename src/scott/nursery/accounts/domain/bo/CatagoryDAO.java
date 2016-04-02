package scott.nursery.accounts.domain.bo;

import scott.hibernate.GenericHibernateDAO;
import scott.hibernate.HibernateTransaction;

public class CatagoryDAO
    extends GenericHibernateDAO<BaseCatagory, Long>
{


    public CatagoryDAO(HibernateTransaction transaction)
    { 
        super(transaction);
    }


}
