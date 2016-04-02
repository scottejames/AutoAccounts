package scott.nursery.test.qif;

import static org.junit.Assert.assertTrue;
import java.util.Date;

import org.junit.Test;

import scott.nursery.qif.QIFUtils;



public class TestQIFUtils
{

    @Test
    public void testChequeValidationCode()
    {
        String notChequeAtAll="SPONGE";
        String emtpyString="";
        String cheque="123456";
        String antherCheque="000111";
        assertTrue(QIFUtils.isCheque(notChequeAtAll)==false);
        assertTrue(QIFUtils.isCheque(emtpyString)==false);
        assertTrue(QIFUtils.isCheque(cheque)==true);
        assertTrue(QIFUtils.isCheque(antherCheque)==true);
        
    }
    
    @Test
    public void testParsedate()
    {
        Date date = QIFUtils.parseDate("03-Apr-01",QIFUtils.STRING_FORMAT);
         date = QIFUtils.parseDate("03/04/2006");
         System.out.println("DATE: " + date);
    }
}
