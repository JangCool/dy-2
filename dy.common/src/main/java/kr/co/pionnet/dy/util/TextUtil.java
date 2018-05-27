package kr.co.pionnet.dy.util;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.pionnet.dy.exception.UtilityException;

public class TextUtil {

    public static final int CHARSET_UTF8 = 1;
    public static final int CHARSET_NON_UTF8 = 0;

    /**
     * 문자열의 바이트수를 리턴한다.
     * TextUtil.CHARSET_UTF8 : 한글을 3바이트로 처리
     * TextUtil.CHARSET_NON_UTF8 : 한글을 2바이트로 처리
     * 
     * @param value
     * @return
     */
    public static long getLength(String value) {
        return getLength(value, TextUtil.CHARSET_UTF8);
    }

    public static long getLength(String value, int charset) {

        long length = 0;

        for (int i = 0; i < value.length(); i++) {
            char chr = value.charAt(i);

            if (chr >= 0x0001 && chr <= 0x007F) {
                length++;
            } else if (chr > 0x07FF) {
                if (charset == TextUtil.CHARSET_UTF8) {	// 한글 UTF8에서 3바이트
                    length += 3;
                } else {
                    length += 2;
                }
            } else {
                length += 2;
            }
        }

        return length;
    }

    /**
     * 주어진 문자열을 처음위치부터 주어진 바이트 수만큼 자른다.
     * 
     * @param valuie
     * @param length
     * @return
     */
    public static String substring(String value, long length) {

        CharArrayWriter writer = null;

        long count = 0;

        try {

            writer = new CharArrayWriter();

            for (int i = 0; i < value.length(); i++) {

                char chr = value.charAt(i);

                if (chr >= 0x0001 && chr <= 0x007F) {
                    count++;
                } else if (chr > 0x07FF) {	// 한글 UTF에서 3바이트이므로 2바이트로 계산
                    count += 2;
                } else {
                    count += 2;
                }

                if (count > length) {
                    break;
                }

                writer.append(chr);
            }

            return writer.toString();

        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }

    }

    /**
     * 원본 문자열의 앞에 패딩 문자열을 반복하여 붇여 전체 크기가 totalLength인 문자열로 반환한다. 패딩 문자열은 전체 크기에
     * 맞게 앞에서 부터 잘려서 들어가고 원본 문자열보다 전체 크기가 작을 수 없다.
     * 예) lpad("abcd", 8, "XY") --> XYXYabcd,
     * lpad("abcd", 7, "XY") --> XYXabcd
     * 
     * @param originalString
     *            원본문자열
     * @param totalLength
     *            전체크기
     * @param padString
     *            패딩문자열
     * @return string
     * @throws UtilException
     */
    public static String lpad(String originalString, int totalLength, String padString) throws UtilityException {
        if (originalString == null || padString == null || totalLength <= 0) {
            throw new UtilityException("Target string and padding String can't be null and totalLength must be positive number");
        }

        int originalLength = originalString.length();
        int padLength = padString.length();

        if (totalLength == originalLength) {
            return originalString;
        } else if (totalLength < originalLength) {
            throw new UtilityException("Total length must not be smaller than original string length");
        }

        int moduloLength = (totalLength - originalLength) % padLength;
        StringBuffer pad = new StringBuffer("");

        for (int i = originalLength; i < totalLength - moduloLength; i += padLength) {
            pad.append(padString);
        }

        if (moduloLength != 0) {
            pad.append(padString.substring(0, moduloLength));
        }

        return pad.append(originalString).toString();
    }

    /**
     * 원본 문자열의 뒤에 패딩 문자열을 반복하여 붇여 전체 크기가 totalLength인 문자열로 반환한다. 패딩 문자열은 전체 크기에
     * 맞게 앞에서 부터 잘려서 들어가고 원본 문자열보다 전체 크기가 작을 수 없다.
     * 예) rpad("abcd", 8, "XY") --> abcdXYXY,
     * rpad("abcd", 7, "XY") --> abcdXYX
     * 
     * @param originalString
     * @param totalLength
     * @param padString
     * @return string
     * @throws UtilException
     */
    public static String rpad(String originalString, int totalLength, String padString) throws UtilityException {
        if (originalString == null || padString == null || totalLength <= 0) {
            throw new UtilityException("Target string and padding String can't be null and totalLength must be positive number");
        }

        int originalLength = originalString.length();
        int padLength = padString.length();

        if (totalLength == originalLength) {
            return originalString;
        } else if (totalLength < originalLength) {
            throw new UtilityException("Total length must not be smaller than original string length");
        }

        int moduloLength = (totalLength - originalLength) % padLength;
        StringBuffer pad = new StringBuffer("");

        for (int i = originalLength; i < totalLength - moduloLength; i += padLength) {
            pad.append(padString);
        }
        if (moduloLength != 0) {
            pad.append(padString.substring(0, moduloLength));
        }

        return originalString + pad.toString();
    }

    /**
     * 작성자:박현진
     * 개요:전문등의 통신용이나 DB저장시 사용. 바이트수 기준으로 오른쪽을 fillerh 채운다. filler가 없으면 " "로 채워진다.
     */
    public static byte[] rpadbyte(String value, int length, String filler) {

        if (filler == null) {
            filler = " ";	// 기본값 스페이스
        }

        int count = 0;

        for (int i = 0; i < value.length(); i++) {
            char chr = value.charAt(i);
            if (chr >= 0x0001 && chr <= 0x007F) {
                count++;
            } else if (chr > 0x07FF) {	// 한글 UTF에서 3바이트이므로 2바이트로 계산
                count += 2;
            } else {
                count += 2;
            }
        }

        StringBuffer result = new StringBuffer(value);
        for (int i = count; i < length; i++) {
            result.append(filler);
        }
        return result.toString().getBytes();
    }

    /**
     * 작성자:박현진
     * 개요:전문등의 통신용이나 DB저장시 사용. 바이트수 기준으로 왼쪽을 fillerh 채운다. filler가 없으면 " "로 채워진다.
     */
    public static byte[] lpadbyte(String value, int length, String filler) {

        if (filler == null) {
            filler = " ";	// 기본값 스페이스
        }

        int count = 0;

        for (int i = 0; i < value.length(); i++) {
            char chr = value.charAt(i);
            if (chr >= 0x0001 && chr <= 0x007F) {
                count++;
            } else if (chr > 0x07FF) {	// 한글 UTF에서 3바이트이므로 2바이트로 계산
                count += 2;
            } else {
                count += 2;
            }
        }

        StringBuffer result = new StringBuffer();
        for (int i = count; i < length; i++) {
            result.append(filler);
        }
        result.append(value);
        return result.toString().getBytes();
    }

    /**
     * 주어진 문자열을 구분자(delimiter)로 나눈 문자열 배열(<code>String[]</code>)을 반환한다.
     * 
     * @return string배열
     */
    public static String[] split(String str, String delimiter) {
        return split(str, delimiter, true);
    }

    public static String[] split(String str, String delimiter, boolean bEmpty) {
        if (str == null) {
            return new String[0];
        }

        if (delimiter == null || delimiter.equals("")) {
            String[] ret = new String[1];
            ret[0] = str;
            return ret;
        }

        String token = null;
        if (bEmpty) {
            List<String> list = new ArrayList<String>();

            int start = 0;
            int end = 0;
            while ((end = str.indexOf(delimiter, start)) != -1) {
                token = str.substring(start, end);
                list.add(token);
                // start = end + 1; // delimiter가 "#;"인 경우는?
                start = end + delimiter.length();
            }
            list.add(str.substring(start)); // !!!

            return (String[]) list.toArray(new String[list.size()]);
        } else { // 공백 무시
            if (str == null || str.length() < 1) {
                return new String[0];
            }

            List<String> list = new ArrayList<String>();

            int start = 0;
            int end = 0;
            token = null;
            while ((end = str.indexOf(delimiter, start)) != -1) {
                token = str.substring(start, end);
                if (token != null && !"".equals(token)) {
                    list.add(token);
                }
                // start = end + 1; // delimiter가 "#;"인 경우는?
                start = end + delimiter.length();
            }
            token = str.substring(start);
            if (token != null && !"".equals(token)) {
                list.add(token);
            }

            return (String[]) list.toArray(new String[list.size()]);
        }
    }

    /**
     * 문자열 중의 특정 문자열을 원하는 문자열로 바꾼다.
     * 대소문자를 구분하지 않고 변환한다.
     * 
     * @param word
     * @param regex
     *            자바 정규식 표현
     * @param replacement
     * @return string
     */
    public static String replaceString(String word, String regex, String replacement) {
        return replaceString(word, regex, replacement, true);
    }

    /**
     * 문자열 중의 특정 문자열을 원하는 문자열로 바꾼다.
     * caseSensitive:true -> 대소문자를 구분하지 않는다.
     * caseSensitive:false -> 대소문자를 구분한다.
     * 
     * @param word
     * @param regex
     *            자바 정규식 표현
     * @param replacement
     * @param caseSensitive
     * @return string
     */
    public static String replaceString(String word, String regex, String replacement, boolean caseSensitive) throws UtilityException {

        if (word == null) {
            throw new UtilityException("Parameter 'word' can not be null.");
        }

        if ("".equals(word)) {
            return "";
        }

        if (regex == null) {
            throw new UtilityException("Parameter 'regex' can not be null.");
        }

        if (replacement == null) {
            throw new UtilityException("Parameter 'replacement' can not be null.");
        }

        Pattern pattern = null;
        if (caseSensitive) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regex);
        }

        return pattern.matcher(word).replaceAll(replacement);
    }

    /**
     * 문자열을 지정된 갯수를 원하는 문자열로 변환한다.
     * 예) replaceString("supermam", -3, "*") -> super***
     * replaceString("supermam", 3, "*") -> ***erman
     * 실제 문자열 길이를 초과하면 모든 문자를 치환한다.
     * 
     * @param word
     * @param length
     *            치환할 문자갯수(양수이면 앞에서 부터, 음수이면 뒤에서부터 치환함)
     * @param replacement
     *            치환할 문자열
     * @return string
     */
    public static String replaceString(String word, int length, String replacement) {

        String regexp = null;
        int real_length = (Math.abs(length) > word.length() ? word.length() : Math.abs(length));
        if (length >= 0) {
            regexp = "^.{" + real_length + "}";
        } else {
            regexp = ".{" + real_length + "}$";
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < real_length; i++) {
            buf.append(replacement);
        }
        Pattern pattern = Pattern.compile(regexp);

        return pattern.matcher(word).replaceAll(buf.toString());
    }

 

    public static boolean isEmpty(String word) {
        if (word == null) {
            return true;
        }
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isWhitespace(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static String concat(String... args) {
        if (args == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            if (s == null) {
                continue;
            }
            builder.append(s);
        }
        return builder.toString();
    }
    
    public static String nvl(String str, String defaultValue) {

        if (str == null || "".equals(str)) {
            return defaultValue;
        } else {
            return str;
        }

    }

}
