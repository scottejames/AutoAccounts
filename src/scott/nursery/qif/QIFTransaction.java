package scott.nursery.qif;

import java.math.BigDecimal;
import java.util.Date;

public class QIFTransaction
{
    private Date _date = new Date();

    private BigDecimal _amount;
    private String _payee = null;
    private boolean _isCheque = false;
    
    public QIFTransaction()
    {
        this._amount = new BigDecimal("0");
    
    }
    
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("Payee: " + this._payee + "\n");
      

        if (this._amount != null)
        {
            buf.append("Amount:" + this._amount.toString() + "\n");
        }

        buf.append("Date: " + this._date.toString() + "\n");
        return buf.toString();
    }

    public Date get_date()
    {
        return _date;
    }

    public void set_date(Date _date)
    {
        this._date = _date;
    }

    public Date get_Date()
    {
        return _date;
    }

    public void set_Date(Date date)
    {
        _date = date;
    }

    public BigDecimal get_amount()
    {
        return _amount;
    }

    public void set_amount(BigDecimal _amount)
    {
        this._amount = _amount;
    }

    public String get_payee()
    {
        return _payee;
    }

    public void set_payee(String _payee)
    {
        this._payee = _payee;
    }

  

    public boolean isCheque()
    {
        return _isCheque;
    }

    public void setIsCheque(boolean cheque)
    {
        _isCheque = cheque;
    }


}
