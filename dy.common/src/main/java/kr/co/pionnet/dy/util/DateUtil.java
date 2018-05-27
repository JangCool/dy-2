package kr.co.pionnet.dy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.co.pionnet.dy.exception.UtilityException;

public class DateUtil {
    
    /**
     * �썝�븯�뒗 �궇吏쒗삎�떇�쑝濡� �삤�뒛 �궇吏쒕�� 由ы꽩�븳�떎.
     * 
     * @see java.text.SimpleDateFormat
     * @param pattern
     * @return string �뙣�꽩�솕�맂 �궇吏� 臾몄옄�뿴
     */
    public static String today(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 二쇱뼱吏� Date瑜� pattern�솕 �맂 臾몄옄�뿴濡� 諛섑솚�븳�떎.
     * 
     * @param date
     *            �뙣�꽩�솕�븷 �궇吏�
     * @param pattern
     *            string �뙣�꽩
     * @return string �뙣�꽩�솕�맂 �궇吏� 臾몄옄�뿴
     */
    public static String format(Date date, String pattern) {
        String str = "";
        if (date.toString().substring(0, 19).equals("0001/01/01 00:00:00")) {
            str = "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            str = simpleDateFormat.format(date);
        }
        return str;
    }

    /**
     * �쁽�옱 �궇吏쒕�� 湲곗��쑝濡� �썝�븯�뒗 �떆�젏�쓽 �궇吏쒕�� 援ы븿
     * 
     * @param amount
     *            �썝�븯�뒗 �궇吏� �떆�젏 (10�씪 �썑瑜� �썝�븯硫� 10, 10�씪 �쟾�쓣 �썝�븯硫� -10)
     * @param formatstr
     *            �궇吏� format
     * @return string
     */
    public static String format(int amount, String pattern) {
        return format(amount, pattern, Calendar.DAY_OF_MONTH);
    }

    public static String format(int amount, String pattern, int gubun) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(gubun, amount);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(calendar.getTime());
    }

    /**
     * �듅�젙�씪�옄 瑜� 湲곗��쑝濡� �썝�븯�뒗 �떆�젏�쓽 �궇吏쒕�� 援ы븿
     * formatstr(�씪�옄�룷硫�)�� fromdatestr(�듅�젙�씪�옄)�쓽 �룷硫㏐낵 �룞�씪�빐�빞 �븳�떎.
     * �궗�슜�삁 addDate(10, "yyyy-MM-dd", "2007-01-09");
     * 
     * @param amount
     *            �썝�븯�뒗 �궇吏� �떆�젏 (10�씪 �썑瑜� �썝�븯硫� 10, 10�씪 �쟾�쓣 �썝�븯硫� -10)
     * @param formatstr
     *            �듅�젙�씪�옄�쓽 �룷硫�
     * @param datestr
     *            �듅�젙�씪�옄(湲곗��씪�옄)
     * @return string
     */
    public static String format(int amount, String pattern, String datestr) {
        return format(amount, pattern, datestr, Calendar.DAY_OF_MONTH);
    }

    public static String format(int amount, String pattern, String datestr, int gubun) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(datestr, pattern));
        calendar.add(gubun, amount);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(calendar.getTime());
    }

    /**
     * pattern�삎�떇�쑝濡� �룷留룸맂 �궇吏� 臾몄옄�뿴�쓣 java.util.Date �삎�깭濡� 諛섑솚�븳�떎.
     * 
     * @param str
     *            date string you want to check.
     * @param pattern
     *            string representation of the date format. For example, "yyyy-MM-dd".
     * @return date
     */
    public static Date parse(String datestr, String pattern) {
        if (datestr == null) {
            throw new UtilityException("date string to check is null");
        }

        if (pattern == null) {
            throw new UtilityException("format string to check date is null");
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        Date date = null;

        try {
            date = formatter.parse(datestr);
        } catch (ParseException e) {
            throw new UtilityException(" wrong date:\"" + datestr + "\" with format \"" + pattern + "\"", e);
        }

        if (!format(date, pattern).equals(datestr)) {
            throw new UtilityException("Out of bound date:\"" + datestr + "\" with format \"" + pattern + "\"");
        }
        return date;
    }
    
    public static long getMinute(long date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime().getTime();
    }
    
    
    public static long format(String datestr, String pattern) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(datestr, pattern));
        
        return calendar.getTimeInMillis();
    	  
    }

}
