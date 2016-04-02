package scott.nursery.accounts.catagory.regexp;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import scott.mvc.Model;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableModel;
import scott.mvc.gui.table.FTableRow;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BaseCatagoryRegularExpression;

public class CatagoryRegexpTableModel extends FTableModel
{
    private static Logger _logger = Logger
            .getLogger(CatagoryRegexpTableModel.class);

    public CatagoryRegexpTableModel(CatagoryRegexpModel catagoryRegexpModel,
            FTableDefinition d)
    {
        super(catagoryRegexpModel, d);
    }

    public BaseCatagoryRegularExpression getSelectedRegexp()
    {
        if (getSelectedRow() == null)
            return null;
        else
            return buildCatagoryRegexpFromRow(getSelectedRow());
    }

    private BaseCatagoryRegularExpression buildCatagoryRegexpFromRow(
            FTableRow row)
    {
        CatagoryRegexpModel parentModel = (CatagoryRegexpModel) getParentModel();
        BaseCatagoryRegularExpression regexp = new BaseCatagoryRegularExpression();
        regexp = ApplicationModel.getInstance().getCatagoryRegexpList()
                .getRegexpById((Long) row.getHiddenRowData());
        String str = row.getRowData()[0];
        String notes = row.getRowData()[1];
        regexp.set_catagoryId(parentModel.get_catagory().get_id());
        regexp.set_regexp(str);
        regexp.set_notes(notes);
        return regexp;
    }

    @Override
    public void dependentModelUpdated(Model m, CONTEXT context)
    {
        _logger
                .debug("Updating the catagory regexp table model using context: "
                        + context);
        CatagoryRegexpModel model = (CatagoryRegexpModel) getParentModel();
        Long catagoryID = model.get_catagory().get_id();
        List<BaseCatagoryRegularExpression> catList = ApplicationModel
                .getInstance().getCatagoryRegexpList()
                .getCatagoryRegularExpressionByCatagoryId(catagoryID);
        List<FTableRow> results = new ArrayList<FTableRow>();
        if (catList != null)
        {
            for (BaseCatagoryRegularExpression c : catList)
            {
                results.add(buildRowFromCatagoryRegexp(c));
            }
        }
        this.setModelData(results);
    }

    private FTableRow buildRowFromCatagoryRegexp(BaseCatagoryRegularExpression c)
    {
        String[] stringRow = new String[] { Utils.toString(c.get_regexp()),
                Utils.toString(c.get_notes()) };
        FTableRow row = new FTableRow(stringRow, c.get_id());
        return row;
    }
}
