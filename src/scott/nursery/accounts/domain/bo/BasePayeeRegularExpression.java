package scott.nursery.accounts.domain.bo;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PayeeRegExp")
public class BasePayeeRegularExpression
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long _id;
    
    @Column(name="FromRegex")
    private String _from;
    
    @Column(name="ToRegexp")
    private String _to;

    public BasePayeeRegularExpression()
    {
        
    }
    public BasePayeeRegularExpression(String from, String to)
    {
        _from=from;
        _to=to;
    }
    public BasePayeeRegularExpression(String from, String to,Long id)
    {
        _from=from;
        _to=to;
        _id=id;
    }
    public Pattern getFromPattern()
    {
        Pattern myPattern = Pattern.compile(_from);
        return myPattern;
        
    }
    public Pattern getToPattern()
    {
        Pattern myPattern = Pattern.compile(_to);
        return myPattern;
        
    }
    public Long get_id()
    {
        return _id;
    }

    public void set_id(Long _id)
    {
        this._id = _id;
    }

    public String get_from()
    {
        return _from;
    }

    public void set_from(String _from)
    {
        this._from = _from;
    }

    public String get_to()
    {
        return _to;
    }

    public void set_to(String _to)
    {
        this._to = _to;
    }
    public String toString()
    {
        return "(" + get_from() + "/" +  get_to() + ")";
    }
    
}
