package scott.nursery.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(value=Suite.class)
@SuiteClasses(value=
    {
        scott.nursery.test.domain.TestChequeCSVFile.class,
      scott.nursery.test.qif.TestQIFParser.class,
      scott.nursery.test.qif.TestQIFTransaction.class,
      scott.nursery.test.qif.TestQIFUtils.class })
public class AllTests
{


}
