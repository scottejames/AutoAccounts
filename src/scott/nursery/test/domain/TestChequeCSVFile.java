package scott.nursery.test.domain;

import static org.junit.Assert.*;
import org.junit.Test;
import scott.nursery.accounts.domain.ChequeCSVFile;

public class TestChequeCSVFile
{
    String rootDirectory="/Users/scott/Projects/eclipse workspace/Nursery/src/scott/nursery/test/domain";
    
    @Test
    public void loadGoodData()
    {
        ChequeCSVFile file = new ChequeCSVFile(rootDirectory + "/good-data.csv");
        file.loadCheques();
        assertEquals(0,file.getErrorCount());
        assertEquals(5,file.getChequeData().size());
        
    }
    
    @Test
    public void loadEmptyData()
    {
        ChequeCSVFile file = new ChequeCSVFile(rootDirectory + "/empty.csv");
        file.loadCheques();
        assertEquals(0,file.getErrorCount());
        assertEquals(0,file.getChequeData().size());
    }
    @Test
    public void loadHardData()
    {
        ChequeCSVFile file = new ChequeCSVFile(rootDirectory + "/hard-data.csv");
        file.loadCheques();
        assertEquals(0,file.getErrorCount());
        assertEquals(2,file.getChequeData().size());
    }
    @Test
    public void loadBadData()
    {

            ChequeCSVFile file = new ChequeCSVFile(rootDirectory + "/bad-data.csv");
            file.loadCheques();
            assertEquals(2,file.getErrorCount());
            assertEquals(2,file.getChequeData().size());
        
    }
}
