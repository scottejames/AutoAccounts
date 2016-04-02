package scott.nursery.accounts.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import scott.nursery.accounts.domain.bo.BaseCheque;
import scott.nursery.qif.QIFUtils;
import com.csvreader.CsvReader;

public class ChequeCSVFile
{
    protected Logger _logger = Logger.getLogger(ChequeCSVFile.class);
    protected List<BaseCheque> _loadedCheques = new ArrayList<BaseCheque>();
    protected String _fileName;
    protected List<String> _errors = new ArrayList<String>();;

    public ChequeCSVFile(String fileName)
    {
        _fileName = fileName;
    }

    public void loadCheques()
    {
        try
        {
            _logger.debug("Loading cheques from file: " + _fileName);
            CsvReader reader = new CsvReader(_fileName);
            int line = 0;
            while (reader.readRecord())
            {
                line++;
                String[] values = reader.getValues();
                _logger.debug("processing record " + values + " from record: " + reader.getRawRecord());
                String comment = new String();
                Date chequeDate;
         
                BigDecimal amount;
                String rawRecord;
                String chequeNumber;
                if  ((values.length > 3) && (values[0].length() > 0))
                    
                {
                    while(values[0].length()<6)
                        values[0] = "0" + values[0];
                    
                    chequeNumber = values[0];
                    if (!values[1].equals("VOID"))
                    {
                        chequeDate = parseDate(values[1]);
                        String payee = values[2];
                        amount = parseAmount(values[3]);
                        rawRecord = reader.getRawRecord();
                        _loadedCheques.add(new BaseCheque(
                                chequeNumber, chequeDate,
                                payee, amount, rawRecord, comment));
                    } else
                    {
                        rawRecord = reader.getRawRecord();
                        _loadedCheques.add(new BaseCheque(
                                chequeNumber, rawRecord, true));
                    }

        
                } else
                {
                    _logger.error("Unable to store record from CSV file: "
                            + reader.getRawRecord());
                }
            }
        } catch (FileNotFoundException e)
        {
            _errors.add(e.toString());
            _logger.error(e.toString());
        } catch (IOException e)
        {
            _errors.add(e.toString());
            _logger.error(e.toString());
        }
        _logger.debug("finished loading cheques have loaded " + _loadedCheques.size() + " cheques");
    }
    public Date parseWrittenDate(String strDate)
    {
        Date result = null;
        try
        {
            if (strDate.length() != 0)
            {
                result = QIFUtils.parseDate(strDate, QIFUtils.STRING_FORMAT);
            }
        } catch (NumberFormatException e)
        {
            _errors.add(e.toString());
            _logger.error(e.toString());
        }
        return result;
    }
    public Date parseDate(String strDate)
    {
        Date result = null;
        try
        {
            if (strDate.length() != 0)
            {
                result = QIFUtils.parseDate(strDate, QIFUtils.EU_FORMAT);
            }
        } catch (NumberFormatException e)
        {
            _errors.add(e.toString());
            _logger.error(e.toString());
        }
        return result;
    }

    public BigDecimal parseAmount(String strAmount)
    {
        BigDecimal result = null;
        try
        {
            if (strAmount.length() != 0)
            {
                result = QIFUtils.parseMoney(strAmount);
            }
        } catch (ParseException e)
        {
            _errors.add(e.toString());
            _logger.error(e.toString());
        }
        return result;
    }
    public List<BaseCheque> getChequeData()
    {
        return _loadedCheques;
    }
    public int getErrorCount()
    {
        return _errors.size();
    }

    public List<String> getErrors()
    {
        return _errors;
    }
    public int getNumberOfChequesLoaded()
    {
        return _loadedCheques.size();
    }
}
