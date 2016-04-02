package scott.nursery.qif;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QIFUtils
{
    /** US date format */
    public static final String US_FORMAT = "mm/dd/yyyy";

    /** European date format */
    public static final String EU_FORMAT = "dd/mm/yyyy";
    
    public static final String STRING_FORMAT= "dd-mmm-yy";

    public static boolean isCheque(String queryString)
    {
        Pattern pattern = Pattern.compile("^\\d\\d\\d\\d\\d\\d$");
        Matcher matcher = pattern.matcher(queryString);
        return matcher.matches();
        
    }
    
    public static Date parseDate(String sDate, String format)
    {
        String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        String[] chunks = sDate.split("/|'|\\.|-");

        if (US_FORMAT.equals(format))
        {
            
                month = Integer.parseInt(chunks[0].trim());
                day = Integer.parseInt(chunks[1].trim());
                year = Integer.parseInt(chunks[2].trim());
             
        } else if (EU_FORMAT.equals(format))
        {
         
                day = Integer.parseInt(chunks[0].trim());
                month = Integer.parseInt(chunks[1].trim());
                year = Integer.parseInt(chunks[2].trim());
             
        } else if (STRING_FORMAT.equals(format))
        {
            if (chunks.length == 3)
            {
                day =  Integer.parseInt(chunks[0].trim());
                for (int i = 0;i<12;i++)
                {
                    if (months[i].equals(chunks[1].trim()))
                    {
                        month = i+1;
                        break;
                    }
                }
               
                year = Integer.parseInt(chunks[2].trim());
            }
            
        } else
        {
            Logger.getAnonymousLogger().severe("Invalid date format specified");
            return new Date();
        }

        if (year < 100)
        {
            if (year < 29)
            {
                year += 2000;
            } else
            {
                year += 1900;
            }
        }
        cal.set(year, month - 1, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /** Use US format by default */
    public static Date parseDate(String sDate)
    {
        return parseDate(sDate, EU_FORMAT);
    }

    public static BigDecimal parseMoney(String sMoney) throws ParseException
    {
        if (sMoney != null)
        {
            BigDecimal money;
            sMoney = sMoney.trim(); // to be safe
            try
            {
                money = new BigDecimal(sMoney);
                return money;
            } catch (NumberFormatException e)
            {
                /*
                 * there must be commas, etc in the number. Need to look for
                 * them and remove them first, and then try BigDecimal again. If
                 * that fails, then give up and use NumbeberFormat and scale it
                 * down
                 */

                String[] split = sMoney.split("\\D");
                if (split.length > 2)
                {
                    StringBuffer buf = new StringBuffer();
                    if (sMoney.startsWith("-"))
                    {
                        buf.append("-");
                    }
                    for (int i = 0; i < split.length - 1; i++)
                    {
                        buf.append(split[i]);
                    }
                    buf.append('.');
                    buf.append(split[split.length - 1]);
                    try
                    {
                        money = new BigDecimal(buf.toString());
                        return money;
                    } catch (NumberFormatException e2)
                    {
                        Logger l = Logger.getAnonymousLogger();
                        l.info("second parse attempt failed");
                        l.info(buf.toString());
                        l.info("falling back to rounding");
                    }
                }
                NumberFormat formatter = NumberFormat.getNumberInstance();
                try
                {
                    Number num = formatter.parse(sMoney);
                    BigDecimal bd = new BigDecimal(num.floatValue());
                    if (bd.scale() > 6)
                    {
                        Logger l = Logger.getAnonymousLogger();
                        l.warning("-Warning-");
                        l.warning("Large scale detected in QifUtils.parseMoney");
                        l.warning("Truncating scale to 2 places");
                        l.warning(bd.toString());
                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                        l.warning(bd.toString());
                    }
                    return bd;
                } catch (ParseException pe)
                {
                    throw pe;
                }
               
            }
        }
        return new BigDecimal("0");
    }
}
