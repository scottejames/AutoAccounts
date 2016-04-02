package scott.nursery.accounts.domain.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;







@Entity
@Table(name = "CatagoryRegexp")
public class BaseCatagoryRegularExpression
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long _id;
    
    @Column(name="regex")
    private String _regexp;

    @Column(name="notes")
    private String _notes;
    
    //@ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Long _catagoryId;

    public BaseCatagoryRegularExpression()
    {
        
    }
    public BaseCatagoryRegularExpression( String regex)
    {
        _regexp = regex;
    }
    public Long get_id()
    {
        return _id;
    }

    public void set_id(Long _id)
    {
        this._id = _id;
    }

    public String get_regexp()
    {
        return _regexp;
    }

    public void set_regexp(String _regexp)
    {
        this._regexp = _regexp;
    }

    public Long get_catagoryId()
    {
        return _catagoryId;
    }

    public void set_catagoryId(Long _catagoryId)
    {
        this._catagoryId = _catagoryId;
    }
    public String get_notes()
    {
        return _notes;
    }
    public void set_notes(String _notes)
    {
        this._notes = _notes;
    }
    
    
    

}
