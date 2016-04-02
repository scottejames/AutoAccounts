package scott.nursery.accounts.domain.bo;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Catagory")
public class BaseCatagory implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2966954699841995434L;

	public static enum DIRECTION {IN,OUT};
    
   
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long _id;
    
    @Column(name = "Name")
    private String _name;

    @Column(name="Direction")   
    private DIRECTION  _direction;


//    @OneToMany(mappedBy="_catagory",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
//    List<BaseCatagoryRegularExpression> _regexps;
    
    
    
    public Long get_id()
    {
        return _id;
    }

    public BaseCatagory()
    {
        
    }
    public BaseCatagory(String name, DIRECTION direction)
    {
        _name = name;
        _direction = direction;
    }
    
    public void set_id(Long _id)
    {
        this._id = _id;
    }

    public String get_name()
    {
        return _name;
    }

    public void set_name(String _name)
    {
        this._name = _name;
    }

    public DIRECTION get_direction()
    {
        return _direction;
    }

    public void set_direction(DIRECTION _direction)
    {
        this._direction = _direction;
    }
    
//    @OneToMany(cascade=CascadeType.ALL)
//    public List<BaseCatagoryRegularExpression> get_regexps()
//    {
//        return _regexps;
//    }
//
//    public void set_regexps(List<BaseCatagoryRegularExpression> _regexps)
//    {
//        this._regexps = _regexps;
//    }
//    public void addRegex(BaseCatagoryRegularExpression regex)
//    {
//        if (this._regexps == null)
//        {
//            this._regexps = new ArrayList<BaseCatagoryRegularExpression>();
//        }
//        this._regexps.add(regex);
//        regex.set_catagory(this);
//    }
    
    
}
