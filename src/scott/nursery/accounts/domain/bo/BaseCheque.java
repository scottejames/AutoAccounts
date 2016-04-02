package scott.nursery.accounts.domain.bo;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Cheque")
public class BaseCheque
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long _id;
    
    @Column(name = "ChequeNumber")
    private String _number;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CashedDate")
    private Date _cashedDate;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "WrittenDate")
    private Date _writtenDate;
    
    @Column(name = "Payee")
    private String _payee;
    
    @Column(name = "Amount")
    private BigDecimal _amount;
    
    @Column(name = "RawData")
    private String _rawData;
    
    @Column(name = "Comment")
    private String _comment;
    
    @Column(name = "Void")
    private boolean _void;
    
    
    public BaseCheque()
    {
        
    }
    public BaseCheque(String number,Date date, String payee, BigDecimal amount, String rawData, String comment)    
    {
        _number = number;
        _writtenDate = date;
        _payee = payee;
      
        _amount = amount;
        _rawData = rawData;
        _comment = comment;
        _void = false;
    }
    public BaseCheque(String number, String rawData,boolean isVoid)
    {
        _number = number;
        _rawData = rawData;
        _void = isVoid;
    }
    public BigDecimal get_amount()
    {
        return _amount;
    }

    public String get_payee()
    {
        return _payee;
    }
    public Long getId()
    {
        return _id;
    }
    public void set_amount(BigDecimal _amount)
    {
        this._amount = _amount;
    }

    public void set_payee(String _payee)
    {
        this._payee = _payee;
    }
    @SuppressWarnings("unused")
    private void setId(Long id)
    {
        this._id = id;
    }

    public String get_rawData()
    {
        return _rawData;
    }

    public void set_rawData(String data)
    {
        _rawData = data;
    }
    public String get_comment()
    {
        return _comment;
    }
    public void set_comment(String _comment)
    {
        this._comment = _comment;
    }
    public boolean is_void()
    {
        return _void;
    }
    public void set_void(boolean _void)
    {
        this._void = _void;
    }
    public String get_number()
    {
        return _number;
    }
    public Date get_cashedDate()
    {
        return _cashedDate;
    }
    public void set_cashedDate(Date date)
    {
        _cashedDate = date;
    }
    public Date get_writtenDate()
    {
        return  _writtenDate;
    }
    public void set_writtenDate(Date date)
    {
        _writtenDate = date;
    }


    
}
