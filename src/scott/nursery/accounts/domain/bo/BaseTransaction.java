package scott.nursery.accounts.domain.bo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import scott.nursery.qif.QIFTransaction;

@Entity
@Table(name = "Transaction")
public class BaseTransaction
{
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long _id;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "Date")
    private Date _date;
    @Temporal(TemporalType.DATE)
    @Column(name = "ChequeWrittenDate")
    private Date _chequeWrittenDate;
    @Column(name = "Payee")
    private String _payee;
    @Column(name = "ChequeNumber")
    private String _chequeNumber;
    @Column(name = "Amount")
    private BigDecimal _amount;
    @Column(name = "IsCheque")
    private boolean _isCheque;
    @Column(name = "FileName")
    private String _fileName;
    @Column(name = "EnrichedPayee")
    private String _enrichedPayee;
    @Column(name = "Catagory")
    private Long _catagoryID;
    @Column(name = "ManualEditPayee")
    private boolean _manualEditPayee;
    @Column(name = "ManualEditCatagory")
    private boolean _manualEditCatagory = false;
    @Column(name = "ChequeNotMatched")
    private boolean _chequeNotMatched;
    @Version
    @Column(name = "Version")
    private Integer _version;

    public String toString()
    {
        return "CQ : [ID: " + _id + "][DT: " + _date + "][PY: " + _payee
                + "][AM: " + _amount + "]";
    }

    public BaseTransaction()
    {
        _date = null;
        _payee = null;
        _chequeNumber = null;
        _isCheque = false;
        _amount = null;
    }

    public BaseTransaction(Date date, String payee, String chequeNumber,
            boolean isCheque, BigDecimal amount, String fileName)
    {
        _date = date;
        _payee = payee;
        _chequeNumber = chequeNumber;
        _isCheque = isCheque;
        _amount = amount;
        _fileName = fileName;
    }

    public BaseTransaction(QIFTransaction transaction, String fileName)
    {
        _date = transaction.get_date();
        if (transaction.isCheque() == true)
            _chequeNumber = transaction.get_payee();
        else
            _payee = transaction.get_payee();
        _isCheque = transaction.isCheque();
        _amount = transaction.get_amount();
        _fileName = fileName;
    }

    public Long get_id()
    {
        return _id;
    }

    public void set_id(Long _id)
    {
        this._id = _id;
    }

    public Date get_date()
    {
        return _date;
    }

    public void set_date(Date _date)
    {
        this._date = _date;
    }

    public Date get_chequeWrittenDate()
    {
        return _chequeWrittenDate;
    }

    public void set_chequeWrittenDate(Date writtenDate)
    {
        _chequeWrittenDate = writtenDate;
    }

    public boolean show_enrichedPayee()
    {

         if ((_enrichedPayee != null) && (!_enrichedPayee.equals("")))
            return true;
        else
            return false;
    }

    public String get_payee()
    {
        if (show_enrichedPayee() == true)
        {
            return _enrichedPayee;
        } else
            return _payee;
    }

    public String get_originalPayee()
    {
        return _payee;
    }

    public void set_payee(String _payee)
    {
        if(!(_payee.equals(this._payee)))
        {
            _manualEditPayee = true;
            this._payee = _payee;
        }
    }

    public String get_chequeNumber()
    {
        return _chequeNumber;
    }

    public void set_chequeNumber(String number)
    {
        _chequeNumber = number;
    }

    public BigDecimal get_amount()
    {
        return _amount;
    }

    public void set_amount(BigDecimal a_amountmount)
    {
        this._amount = a_amountmount;
    }

    public boolean isCheque()
    {
        return _isCheque;
    }

    public void set_isCheque(boolean cheque)
    {
        _isCheque = cheque;
    }

    public String get_fileName()
    {
        return _fileName;
    }

    public void set_fileName(String name)
    {
        _fileName = name;
    }

    public String get_enrichedPayee()
    {
        return _enrichedPayee;
    }

    public void set_enrichedPayee(String payee)
    {
        _enrichedPayee = payee;
    }

    public Long get_catagoryID()
    {
        return _catagoryID;
    }

    public void set_catagoryID(Long _catagoryid)
    {
        set_manualEditCatagory(true);
        _catagoryID = _catagoryid;
    }

  

    public Boolean is_chequeNotMatched()
    {
        if (is_isCheque() == false)
            return null;
        else
            return _chequeNotMatched;
    }

    public void set_chequeNotMatched(boolean notMatched)
    {
        _chequeNotMatched = notMatched;
    }

    public Integer get_version()
    {
        return _version;
    }

    public void set_version(Integer _version)
    {
        this._version = _version;
    }

    public boolean is_isCheque()
    {
        return _isCheque;
    }

    public boolean is_manualEditPayee()
    {
        return _manualEditPayee;
    }

    public void set_manualEditPayee(boolean editPayee)
    {
        _manualEditPayee = editPayee;
    }


    public boolean is_manualEditCatagory()
    {
        return _manualEditCatagory;
    }

    public void set_manualEditCatagory(boolean editCatagory)
    {
        _manualEditCatagory = editCatagory;
    }

    @Override
    public boolean equals(Object obj)
    {
        
        if (obj instanceof BaseTransaction)
        {
            boolean result = true;
            
            BaseTransaction testTran = (BaseTransaction) obj;
            
            if (!(testTran._amount.equals(_amount)))  
                result = false;
            if (!(testTran._date.equals(_date)))
                result = false;
            if (! (testTran._payee.equals(_payee)))
                result = false;
            
            return result;
            
        } else
        {
            return super.equals(obj);
        }
       
    }
    
 
}
