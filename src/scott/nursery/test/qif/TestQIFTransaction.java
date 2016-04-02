package scott.nursery.test.qif;


import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import scott.nursery.qif.QIFTransaction;

public class TestQIFTransaction
{
    @Test public void testConstruction()
    {
        QIFTransaction transaction = new QIFTransaction();
        assertNotNull(transaction);
        
    }

}
