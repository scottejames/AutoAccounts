package scott.nursery.test.qif;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import scott.nursery.qif.QIFParser;
import scott.nursery.qif.QIFTransaction;


public class TestQIFParser
{
    
    @Test public void loadTestData()
    {

        String datafile = new String(
                "src/scott/nursery/test/qif/QIFTestData.qif");
        ArrayList<QIFTransaction> transactions = QIFParser
                .parseAccountTransactions(datafile);
       
        assertTrue(transactions.size() == 5);
        assertTrue(transactions.get(0).get_payee().equals("PAYEE ONE"));
        assertTrue(transactions.get(1).get_payee().equals("PAYEE TWO"));
        assertTrue(transactions.get(2).get_payee().equals("PAYEE THREE"));
        assertTrue(transactions.get(3).get_payee().equals("PAYEE FOUR"));
        assertTrue(transactions.get(0).get_amount().doubleValue() == 100);
        assertTrue(transactions.get(1).get_amount().doubleValue() == -100);
        assertTrue(transactions.get(2).get_amount().doubleValue() == 10.50);
        assertTrue(transactions.get(3).get_amount().doubleValue() == -10.50);
        assertTrue(transactions.get(0).isCheque()==false);
        assertTrue(transactions.get(1).isCheque()==false);
        assertTrue(transactions.get(2).isCheque()==false);
        assertTrue(transactions.get(3).isCheque()==false);
        assertTrue(transactions.get(4).isCheque()==true);
        

    }

}
