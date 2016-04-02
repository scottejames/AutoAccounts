package scott.nursery.accounts.catagory;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import scott.mvc.Model;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableModel;
import scott.mvc.gui.table.FTableRow;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseCatagory.DIRECTION;

public class CatagoryTableModel extends FTableModel
{
    private static Logger _logger = Logger.getLogger(CatagoryTableModel.class);

    CatagoryTableModel(Model m, FTableDefinition d)
    {
        super(m, d);
    }

    @Override
    public void dependentModelUpdated(Model m, CONTEXT context)
    {
        _logger.debug("Updating the catagory table model using context: "
                + context);
        List<BaseCatagory> catList = ApplicationModel.getInstance()
                .getCatagoryList().getCatagoryList();
        List<FTableRow> results = new ArrayList<FTableRow>();
        for (BaseCatagory c : catList)
        {
            results.add(buildRowFromCatagory(c));
        }
        this.setModelData(results);
    }

    private FTableRow buildRowFromCatagory(BaseCatagory c)
    {
        String[] stringRow = new String[] { Utils.toString(c.get_direction()),
                c.get_name() };
        FTableRow row = new FTableRow(stringRow, c.get_id());
        return row;
    }

    private BaseCatagory buildCatagoryFromRow(FTableRow row)
    {
        BaseCatagory payee = new BaseCatagory();
        payee = ApplicationModel.getInstance().getCatagoryList()
                .getCatagoryById((Long) row.getHiddenRowData());
        String direction = row.getRowData()[0];
        String name = row.getRowData()[1];
        if (direction.equals("IN"))
            payee.set_direction(DIRECTION.IN);
        else
            payee.set_direction(DIRECTION.OUT);
        payee.set_name(name);
        return payee;
    }

    public BaseCatagory getSelectedCatagory()
    {
        if (getSelectedRow() == null)
            return null;
        else
            return buildCatagoryFromRow(getSelectedRow());
    }
}
