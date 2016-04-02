package scott.nursery.accounts.domain.bo;

import scott.hibernate.GenericHibernateDAO;
import scott.hibernate.HibernateTransaction;

public class CatagoryRegularExpressionDAO
    extends GenericHibernateDAO<BaseCatagoryRegularExpression, Long>

{
    public CatagoryRegularExpressionDAO(HibernateTransaction transaction)
    { 
        super(transaction);
    }

}
