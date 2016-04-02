package scott.nursery.accounts.transaction;

import org.apache.log4j.Logger;
import scott.hibernate.HibernateTransaction;
import scott.hibernate.exception.TransactionException;
import scott.mvc.Controller;
import scott.mvc.Model;
import scott.mvc.View;
import scott.mvc.ViewEvent;
import scott.mvc.ViewSelection;
import scott.mvc.gui.table.FTableRow;
import scott.mvc.gui.table.FTableRowSelection;
import scott.nursery.accounts.catagory.CatagoryController;
import scott.nursery.accounts.catagory.CatagoryModel;
import scott.nursery.accounts.catagory.CatagoryView;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpController;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpModel;
import scott.nursery.accounts.catagory.regexp.CatagoryRegexpView;
import scott.nursery.accounts.cheques.ChequeBrowserController;
import scott.nursery.accounts.cheques.ChequeBrowserModel;
import scott.nursery.accounts.cheques.ChequeBrowserView;
import scott.nursery.accounts.domain.ApplicationModel;
import scott.nursery.accounts.domain.ChequeCSVFile;
import scott.nursery.accounts.domain.TransactionList;
import scott.nursery.accounts.domain.TransactionQIFFile;
import scott.nursery.accounts.domain.bo.BaseCatagory;
import scott.nursery.accounts.domain.bo.BaseTransaction;
import scott.nursery.accounts.domain.bo.ChequeDAO;
import scott.nursery.accounts.domain.bo.PayeeRegularExpressionDAO;
import scott.nursery.accounts.domain.bo.TransactionDAO;
import scott.nursery.accounts.payee.PayeeRegexpController;
import scott.nursery.accounts.payee.PayeeRegexpModel;
import scott.nursery.accounts.payee.PayeeRegexpView;
import scott.nursery.accounts.transaction.viewer.ViewTransactionController;
import scott.nursery.accounts.transaction.viewer.ViewTransactionView;

public class TransactionBrowserController extends Controller
{
    private static Logger _logger = Logger
            .getLogger(TransactionBrowserController.class);
    TransactionBrowserModel _model = null;
    ViewTransactionView _transactionView = null;
    ViewTransactionController _transactionController = null;
    // Identify the menu options
    public final static int MENU_LOAD_TRANSACTIONS = 1;
    public final static int MENU_LOAD_CHEQUES = 2;
    public final static int MENU_ENRICH_TRANSACTIONS = 3;
    public final static int MENU_BLANK_DATABASE = 4;
    public final static int MENU_BROWSE_PAYEE_REGEXP = 5;
    public final static int MENU_EXIT = 6;
    public static final int MENU_BROWSE_CHEQUES = 7;
    public static final int MENU_BROWSE_CATAGORY_REGEXP = 8;
    public static final int MENU_CONTEXT_RESET_PAYEE_REGEXP = 9;
    public static final int MENU_CONTEXT_ADD_CATAGORY_REGEXP = 10;
    public static final int MENU_CONTEXT_ADD_PAYEE_REGEXP = 11;
    public static final int MENU_DUMP_ACCOUNTANTS_REPORT = 12;

    public TransactionBrowserController(Model m, View v)
    {
        super(m, v);
        _model = (TransactionBrowserModel) getModel();
    }

    @Override
    protected void handleEvent(ViewEvent event)
    {
    }

    @Override
    public void viewSelectionModified(ViewSelection selection)
    {
        //TransactionBrowserModel model = (TransactionBrowserModel) getModel();
        BaseTransaction transaction = TransactionBrowserTableDefinition
                .buildTransactionFromRow(((FTableRowSelection) selection).selectedRow);
        ApplicationModel.getInstance().addOrUpdateTransaction(transaction);
    }

    @Override
    protected void menuItemSelected(int arg)
    {
        switch (arg)
        {
        case MENU_LOAD_TRANSACTIONS:
            _logger.info("menu item selected:  load transactions");
            loadTransactionsFromQIFToDB();
            break;
        case MENU_LOAD_CHEQUES:
            _logger.info("menu item selected:  load cheques");
            loadChequesFromCSVToDB();
            break;
        case MENU_ENRICH_TRANSACTIONS:
            _logger.info("menu item selected:  enrich transactions");
            break;
        case MENU_BLANK_DATABASE:
            _logger.info("menu item selected:  blank database");
            blankDatabase();
            break;
        case MENU_BROWSE_PAYEE_REGEXP:
            _logger.info("menu item selected:  manage payee regeps");
            browsePayeeRegexp();
            break;
        case MENU_BROWSE_CATAGORY_REGEXP:
            _logger.info("menu item selected:  manage catagory regeps");
            browseCatagoryRegexp();
            break;
        case MENU_EXIT:
            _logger.info("menu item selected:  exit");
            break;
        case MENU_BROWSE_CHEQUES:
            _logger.info("menu item selected: browse cheques");
            browseCheques();
            break;
        case MENU_CONTEXT_RESET_PAYEE_REGEXP:
            _logger.info("menu item selected: reset payee regexps");
            resetPayeeRegexps();
            break;
        case MENU_CONTEXT_ADD_CATAGORY_REGEXP:
            _logger.info("menu item selected: add catagory regexps");
            addCatagoryRegexp();
            break;
        case MENU_CONTEXT_ADD_PAYEE_REGEXP:
            _logger.info("menu item selected: add payee regexps");
            addPayeeRegexp();
            break;
        case MENU_DUMP_ACCOUNTANTS_REPORT:
            _logger.info("menu item selected: dump accountants report");
            dumpAccountantsReport();
            break;
        }
    }

    private void dumpAccountantsReport()
    {
        String name = getView().requestFileNameFromUser("Enter base file name to place reports",null);
        if (name == null)
            return;
        TransactionList tlist = ApplicationModel.getInstance().getTransactionList();
        tlist.dumpAccountsReport(name);
        
    }

    private void addCatagoryRegexp()
    {
        FTableRow row = _model.getTableModel().getSelectedRow();
        BaseTransaction transaction = TransactionBrowserTableDefinition
                .buildTransactionFromRow(row);
        if (transaction.get_catagoryID() == null)
        {
            getView().displayError("Please set a catagory first");
            return;
        }
        CatagoryRegexpModel regexpModel = new CatagoryRegexpModel();
        BaseCatagory catagory = ApplicationModel.getInstance()
                .getCatagoryList()
                .getCatagoryById(transaction.get_catagoryID());
        regexpModel.set_catagory(catagory);
        regexpModel.set_selectedRegexp(transaction.get_payee());
        CatagoryRegexpView regexpView = new CatagoryRegexpView(getView()
                .getShell(), regexpModel);
        CatagoryRegexpController regexpController = new CatagoryRegexpController(
                regexpModel, regexpView);
        regexpView.open(regexpController);
    }

    private void browseCatagoryRegexp()
    {
        CatagoryModel model = new CatagoryModel();
        CatagoryView view = new CatagoryView(getView().getShell(), model);
        CatagoryController controller = new CatagoryController(model, view);
        view.open(controller);
    }

    private void addPayeeRegexp()
    {
        PayeeRegexpModel model = new PayeeRegexpModel();
        FTableRow row = _model.getTableModel().getSelectedRow();
        BaseTransaction transaction = TransactionBrowserTableDefinition
                .buildTransactionFromRow(row);
        model.set_selectedRegexpFrom(transaction.get_payee());
        PayeeRegexpView view = new PayeeRegexpView(getView().getShell(), model);
        PayeeRegexpController controller = new PayeeRegexpController(model,
                view);
        view.open(controller);
    }

    private void browsePayeeRegexp()
    {
        PayeeRegexpModel model = new PayeeRegexpModel();
        PayeeRegexpView view = new PayeeRegexpView(getView().getShell(), model);
        PayeeRegexpController controller = new PayeeRegexpController(model,
                view);
        view.open(controller);
    }

    private void blankDatabase()
    {
        try
        {
            HibernateTransaction tx = new HibernateTransaction();
            tx.beginTransaction();
            TransactionDAO transactionDAO = new TransactionDAO(tx);
            ChequeDAO chequeDAO = new ChequeDAO(tx);
            PayeeRegularExpressionDAO payeeDAO = new PayeeRegularExpressionDAO(
                    tx);
            transactionDAO.makeTransient(transactionDAO.findAll());
            chequeDAO.makeTransient(chequeDAO.findAll());
            payeeDAO.makeTransient(payeeDAO.findAll());
            tx.commitTransaction();
            ApplicationModel.getInstance().loadFromDB();
        } catch (TransactionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void resetPayeeRegexps()
    {
        BaseTransaction transaction = ((TransactionBrowserModel) getModel())
                .getTableModel().getSelectedTransaction();
        transaction.set_enrichedPayee("");
        ApplicationModel.getInstance().addOrUpdateTransaction(transaction);
    }

    private void browseCheques()
    {
        ChequeBrowserModel model = new ChequeBrowserModel();
        ChequeBrowserView view = new ChequeBrowserView(getView().getShell(),
                model);
        ChequeBrowserController controller = new ChequeBrowserController(model,
                view);
        view.open(controller);
    }

    private void loadChequesFromCSVToDB()
    {
        String name = getView().requestFileNameFromUser();
        if (name == null)
            return;
        ChequeCSVFile file = new ChequeCSVFile(name);
        file.loadCheques();
        if (file.getErrorCount() != 0)
        {
            _logger.error("Unable to load CSV into DB: errorCount was "
                    + file.getErrorCount());
        }
        {
            try
            {
                _model.addCheques(file.getChequeData());
            } catch (TransactionException e)
            {
                getView().displayError(
                        "Unable to load CSV into DB: " + e.toString());
            }
        }
    }

    public void loadTransactionsFromQIFToDB()
    {
        String fileName = getView().requestFileNameFromUser();
        if (fileName == null)
            return;
        TransactionQIFFile file = new TransactionQIFFile(fileName);
        file.loadTransaction();
        if (file.getErrorCount() != 0)
        {
            _logger.error("Unable to load QIF into DB: errorCount was "
                    + file.getErrorCount());
        }
        try
        {
            _model.addTransactions(file.getTransactionData());
        } catch (TransactionException e)
        {
            getView().displayError(
                    "Unable to load QIF into DB: " + e.toString());
        }
    }
    public static class TransactionBrowserViewSelection extends ViewSelection
    {
        public Long _id = null;

        TransactionBrowserViewSelection(Long id)
        {
            _id = id;
        };
    }
}
