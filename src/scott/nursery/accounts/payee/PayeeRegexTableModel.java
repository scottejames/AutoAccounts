package scott.nursery.accounts.payee;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import scott.mvc.Model;
import scott.mvc.gui.Utils;
import scott.mvc.gui.table.FTableDefinition;
import scott.mvc.gui.table.FTableModel;
import scott.mvc.gui.table.FTableRow;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.bo.BasePayeeRegularExpression;

public class PayeeRegexTableModel extends FTableModel
{
    private static Logger _logger = Logger
            .getLogger(PayeeRegexTableModel.class);

    PayeeRegexTableModel(Model m, FTableDefinition t)
    {
        super(m, t);
    }

    @Override
    public void dependentModelUpdated(Model m, CONTEXT context)
    {
        _logger.debug("Updating the payee table model using context: "
                + context);
        if ((context == PayeeRegexpModel.CONTEXT_LIST)
                || (context == CONTEXT.NULL))
        {
            _logger.debug("Context suggests a table refresh is required");
            List<BasePayeeRegularExpression> regList = ApplicationModel
                    .getInstance().getPayeeRegexpList().getPayeeRegexpList();
            List<FTableRow> results = new ArrayList<FTableRow>();
            for (BasePayeeRegularExpression p : regList)
            {
                results.add(buildRowFromPayeeRegexp(p));
            }
            this.setModelData(results);
        }
    }

    private static FTableRow buildRowFromPayeeRegexp(
            BasePayeeRegularExpression p)
    {
        return new FTableRow(new String[] { p.get_from(), p.get_to() }, p
                .get_id());
    }

    private static BasePayeeRegularExpression buildPayeeRegexpFromRow(
            FTableRow row)
    {
        BasePayeeRegularExpression payee = new BasePayeeRegularExpression();
        payee = ApplicationModel.getInstance().getPayeeRegexpList().getById(
                (Long) row.getHiddenRowData());
        payee.set_from(Utils.toString(row.getRowData()[0]));
        payee.set_to(Utils.toString(row.getRowData()[1]));
        return payee;
    }

    public BasePayeeRegularExpression getSelectedRegexp()
    {
        if (getSelectedRow() == null)
            return null;
        else
            return buildPayeeRegexpFromRow(getSelectedRow());
    }
}
