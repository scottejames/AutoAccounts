package scott.nursery.accounts.domain.bo;

import scott.hibernate.GenericHibernateDAO;
import scott.hibernate.HibernateTransaction;

public class PayeeRegularExpressionDAO
    extends GenericHibernateDAO<BasePayeeRegularExpression, Long>
{

    public PayeeRegularExpressionDAO(HibernateTransaction transaction)
    { 
        super(transaction);
    }

}
