package scott.nursery.qif;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class QIFParser
{
    
    private static Logger _logger = Logger.getLogger(QIFParser.class);
    
    static public ArrayList<QIFTransaction> parseAccountTransactions(File file)
    {
        return parseAccountTransactions(file.getAbsolutePath());
    }


    static public ArrayList<QIFTransaction> parseAccountTransactions(String fileName)
    { 
        int lineCount = 0;
        int recordCount = 0;
        
        ReaderEx in = null;
        ArrayList<QIFTransaction> results = new ArrayList<QIFTransaction>();
        String line;
      
        try
        {
            QIFTransaction tran = new QIFTransaction();
            
            in = new ReaderEx(new FileReader(fileName));
            
            line = in.readLine();
            
            while (line != null)
            {
                lineCount++;
                if (startsWith(line, "!Type:"))
                {
                    // swallow this as this just tells us this is a bank account!
                    
                } else if (line.startsWith("D"))
                {
                    tran.set_date(QIFUtils.parseDate(line.substring(1)));
    

                } else if (line.startsWith("T"))
                {
                    tran.set_amount(QIFUtils.parseMoney(line.substring(1)));
                }  else if (line.startsWith("P"))
                {
                    tran.set_payee(line.substring(1));
                    tran.setIsCheque(QIFUtils.isCheque(tran.get_payee()));
                    
                } 
                else if (line.startsWith("^"))
                {
            
                    _logger.info("*** Added a Transaction *** [ " + tran + "]");
                    recordCount++;
                    results.add(tran);
                    tran = new QIFTransaction();
                   
                } 
                else
                {
                    _logger.error("Unknown field: " + line);
                }
                
                in.mark();
                
                line = in.readLine();
            }
        } 
        
        catch (FileNotFoundException fne)
        {
            _logger.error("Could not find file: " + fileName);

        } 
        catch (IOException ioe)
        {
            _logger.error(ioe.toString());
     
        } catch (ParseException e)
        {
            _logger.error(e.toString());
        }
        finally
        {
            try
            {
                
                if (in != null)
                {
                    in.close();
                }
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        _logger.info("RESULTS read in lines: " + lineCount + " records: " + recordCount);
        return results;
    }
    
    static private boolean startsWith(String source, String prefix)
    {
        if (prefix.length() > source.length())
        {
            return false;
        }
        return source.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    
 
    /**
     * An extended LineNumberReader to help ease the pain of parsing a QIF file
     */
    private static class ReaderEx extends LineNumberReader
    {

        public ReaderEx(Reader in)
        {
            super(in, 8192);
        }

        public void mark() throws IOException
        {
            super.mark(256);
        }

        public void reset() throws IOException
        {
            super.reset();
            _logger.info("Reset");
        }

        /**
         * Takes a peek at the next line and eats and empty line if found
         */
        public String peekLine() throws IOException
        {
            String peek;
            while (true)
            {
                mark();
                peek = readLine();
                if (peek != null)
                {
                    peek = peek.trim();
                    reset();
                    if (peek.length() == 0)
                    {
                        readLine(); // eat the empty line
                        _logger.info("*EMPTY LINE*");
                        
                    } else
                    {
                        return peek.trim();
                    }
                } else
                {
                    return null;
                }
            }
        }

        public String readLine() throws IOException
        {
            while (true)
            {
                try
                {
                    String line = super.readLine().trim();
                    _logger.info("Line " + getLineNumber() + ": "
                                + line);
                   
                    if (line.length() > 0)
                    {
                        return line;
                    }
                    _logger.info("*EMPTY LINE*");
                   
                } catch (NullPointerException e)
                {
                    return null;
                }
            }
        }
    }
}
