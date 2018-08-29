package com.custom;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 



import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
/**
 * ???????????
 * 
 */
public class StringUtil {
    private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
    private static Pattern numericStringPattern = Pattern.compile("^[0-9\\-\\-]+$");
    private static Pattern floatNumericPattern = Pattern.compile("^[0-9\\-\\.]+$");
    private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
    public static final String splitStrPattern = ",|?|;|?|?|\\.|?|-|_|\\(|\\)|\\[|\\]|\\{|\\}|\\\\|/| |?|\"";
    private static Log logger = LogFactory.getLog(StringUtil.class);
 
    /**
     * ??????????<br/>
     * ????10?0?batchCreateString("0", 5)<br/>????00000
     * 5?ab??????, batchCreateString("ab", 5)????ababababab
     * @param string    ???
     * @param times     ????????
     * @return
     */
    public static String batchCreateString(String string, int times) {
        if (StringUtils.isEmpty(string)) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(string);
        }
        return sb.toString();
    }
 
    /**
     * ?????????
     *  ?????
     * @param srcArray
     * @return ???????
     */
    public static String arrayToString(String[] srcArray) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < srcArray.length; i++) {
            result.append(srcArray[i] + ",");
        }
        return deleteComma(result.toString());
    }
 
    /**
     * ?????????
     *  ?????
     * @param srcArray      ????
     * @param addToPre      ?????????????
     * @param addToBehind   ?????????????
     * @return ???????
     */
    public static String arrayToString(String[] srcArray, String addToPre, String addToBehind) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < srcArray.length; i++) {
            result.append(StringUtils.defaultString(addToPre));
            result.append(srcArray[i]);
            result.append(StringUtils.defaultString(addToBehind));
            result.append(",");
        }
        return deleteComma(result.toString());
    }
 
    /**
     * ??????????
     * @param src   ?????
     * @return  ?????????
     * @see StringUtil#deleteSymbol(String, String)
     */
    public static String deleteComma(String src) {
        return deleteSymbol(src, ",");
    }
 
    /**
     * ??????????
     * @param src   ?????
     * @return  ?????????
     * @see StringUtil#deleteSymbol(String, String)
     */
    public static String deleteSemicolon(String src) {
        return deleteSymbol(src, ";");
    }
 
    /**
     * ????????????
     * @param src   ?????
     * @param symbol    ??????
     * @return  ???<b>symbol</b>????
     */
    public static String deleteSymbol(String src, String symbol) {
        src = StringUtils.defaultString(src);
        if (!src.endsWith(symbol)) {
            return src;
        }
        src = src.substring(0, src.length() - 1);
        return src;
    }
 
    /**
     * ????????
     * 
     * @param src
     *            ????
     * @return ???????
     */
    public static boolean isNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
 
    /**
     * ????????
     * 
     * @param src
     *            ????
     * @return ???????
     */
    public static boolean isNumericString(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericStringPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
 
    /**
     * ?????????
     * 
     * @param src
     *            ????
     * @return ??????????
     */
    public static boolean isABC(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
 
    /**
     * ??????????
     * 
     * @param src
     *            ????
     * @return ???????
     */
    public static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
 
    /**
     * ?string array or list??????symbol????????
     * 
     * @param array
     * @param symbol
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String joinString(Collection array, String symbol) {
        return joinString(array, symbol, false);
    }
 
    public static String joinMark(int lens) {
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < lens; i++) {
            buf.append('?');
            if (i != lens - 1) {
                buf.append(',');
            }
        }
        return buf.toString();
    }
 
    public static String joinString(Collection array, String symbol, boolean flag) {
        String result = "";
        if (array != null) {
            for (Object val : array) {
                String temp = val.toString();
                if (temp != null && temp.trim().length() > 0) {
                    if (flag) {
                        result += "'";
                    }
                    result += temp;
                    if (flag) {
                        result += "'";
                    }
                    result += symbol;
                }
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }
 
    public static String subStringNotEncode(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }
 
    /**
     * ????????????symbol?? ??
     * 
     * @param len
     *            ????????????????GBK???????????????????
     * @param str
     * @param symbol
     * @return
     */
    public static String getLimitLengthString(String str, int len, String symbol) {
        int iLen = len * 2;
        int counterOfDoubleByte = 0;
        String strRet = "";
        try {
            if (str != null) {
                byte[] b = str.getBytes("GBK");
                if (b.length <= iLen) {
                    return str;
                }
                for (int i = 0; i < iLen; i++) {
                    if (b[i] < 0) {
                        counterOfDoubleByte++;
                    }
                }
                if (counterOfDoubleByte % 2 == 0) {
                    strRet = new String(b, 0, iLen, "GBK") + symbol;
                    return strRet;
                } else {
                    strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
                    return strRet;
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            return str.substring(0, len);
        } finally {
            strRet = null;
        }
    }
 
    /**
     * ????????????symbol?? ??
     * 
     * @param len
     *            ????????????????GBK???????????????????
     * @param str
     * @return12
     */
    public static String getLimitLengthString(String str, int len) {
        return getLimitLengthString(str, len, "...");
    }
 
    /**
     * 
     * ????????
     * 
     * @param subject
     * @param size
     * @return
     */
    public static String subStrNotEncode(String subject, int size) {
        if (subject.length() > size) {
            subject = subject.substring(0, size);
        }
        return subject;
    }
 
    /**
     * ?string array or list??????symbol????????
     * 
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(String[] array, String symbol) {
        return joinString(array, symbol, false);
    }
 
    public static String joinString(String[] array, String symbol, boolean flag) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String temp = array[i];
                if (temp != null && temp.trim().length() > 0) {
                    if (flag) {
                        result += "'";
                    }
                    result += temp;
                    if (flag) {
                        result += "'";
                    }
                    result += symbol;
                }
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }
 
    /**
     * ????????????????????
     * 
     * @param SrcStr
     *            ????
     * @return ????????
     */
    public static int getStringLen(String SrcStr) {
        int return_value = 0;
        if (SrcStr != null) {
            char[] theChars = SrcStr.toCharArray();
            for (int i = 0; i < theChars.length; i++) {
                return_value += (theChars[i] <= 255) ? 1 : 2;
            }
        }
        return return_value;
    }
 
    /**
     * ???????????????
     * 
     * @param str
     * @return [true]|[false] ??|???
     */
    public static boolean check(String str) {
        String sIllegal = "'\"";
        int len = sIllegal.length();
        if (null == str)
            return false;
        for (int i = 0; i < len; i++) {
            if (str.indexOf(sIllegal.charAt(i)) != -1)
                return true;
        }
 
        return false;
    }
 
    /***************************************************************************
     * getHideEmailPrefix - ?????????
     * 
     * @param email
     *            - EMail???? ??: linwenguo@koubei.com ??...
     * @return ???????????, ? *********@koubei.com.
     * @version 1.0 (2006.11.27) Wilson Lin
     **************************************************************************/
    public static String getHideEmailPrefix(String email) {
        if (null != email) {
            int index = email.lastIndexOf('@');
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }
        return email;
    }
 
    /***************************************************************************
     * repeat - ??????????N?????????
     * 
     * @param src
     *            - ???? ??: ??(" "), ??("*"), "??" ??...
     * @param num
     *            - ??????
     * @return ???????????
     * @version 1.0 (2006.10.10) Wilson Lin
     **************************************************************************/
    public static String repeat(String src, int num) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < num; i++)
            s.append(src);
        return s.toString();
    }
 
    /**
     * ???????????????????
     * 
     * @param src
     * @return
     */
    public static List<String> parseString2ListByCustomerPattern(String pattern, String src) {
 
        if (src == null)
            return null;
        List<String> list = new ArrayList<String>();
        String[] result = src.split(pattern);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }
 
    /**
     * ???????????????????
     * 
     * @param src
     * @return
     */
    public static List<String> parseString2ListByPattern(String src) {
        String pattern = "?|,|?|?";
        return parseString2ListByCustomerPattern(pattern, src);
    }
 
    /**
     * ?????float
     * 
     * @param format
     *            ???????? such as #.00, #.#
     */
 
    public static String formatFloat(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }
 
    /**
     * ????????? null?"" ??? true
     * 
     * @author Robin Chang
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s != null && !s.equals("") && !s.equals("null")) {
            return false;
        }
        return true;
    }
 
    /**
     * ??????????? ??: 1,2,3 =>[1,2,3] 3??? ,2,3=>[,2,3] 3??? ,2,3,=>[,2,3,] 4??? ,,,=>[,,,] 4???
     * 
     * 5.22????????????????? ?????,,??""??
     * 
     * @param split
     *            ???? ??,
     * @param src
     *            ?????
     * @return ????list
     * @author Robin
     */
    public static List<String> splitToList(String split, String src) {
        // ??,
        String sp = ",";
        if (split != null && split.length() == 1) {
            sp = split;
        }
        List<String> r = new ArrayList<String>();
        int lastIndex = -1;
        int index = src.indexOf(sp);
        if (-1 == index && src != null) {
            r.add(src);
            return r;
        }
        while (index >= 0) {
            if (index > lastIndex) {
                r.add(src.substring(lastIndex + 1, index));
            } else {
                r.add("");
            }
 
            lastIndex = index;
            index = src.indexOf(sp, index + 1);
            if (index == -1) {
                r.add(src.substring(lastIndex + 1, src.length()));
            }
        }
        return r;
    }
 
    /**
     * ? ?=? ????????? (a=1,b=2 =>a=1&b=2)
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String linkedHashMapToString(LinkedHashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            String result = "";
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                String value = (String) map.get(name);
                result += (result.equals("")) ? "" : "&";
                result += String.format("%s=%s", name, value);
            }
            return result;
        }
        return null;
    }
 
    /**
     * ??????? ??=????? (a=1&b=2 => a=1,b=2)
     *
     * @param str
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static LinkedHashMap<String, String> toLinkedHashMap(String str) {
        if (str != null && !str.equals("") && str.indexOf("=") > 0) {
            LinkedHashMap result = new LinkedHashMap();
 
            String name = null;
            String value = null;
            int i = 0;
            while (i < str.length()) {
                char c = str.charAt(i);
                switch (c) {
                case 61: // =
                    value = "";
                    break;
                case 38: // &
                    if (name != null && value != null && !name.equals("")) {
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                    break;
                default:
                    if (value != null) {
                        value = (value != null) ? (value + c) : "" + c;
                    } else {
                        name = (name != null) ? (name + c) : "" + c;
                    }
                }
                i++;
 
            }
 
            if (name != null && value != null && !name.equals("")) {
                result.put(name, value);
            }
 
            return result;
 
        }
        return null;
    }
 
    /**
     * ?????????????????
     * 
     * @param captions
     *            ??:"?,???,??,???"
     * @param index
     *            1
     * @return ??
     */
    public static String getCaption(String captions, int index) {
        if (index > 0 && captions != null && !captions.equals("")) {
            String[] ss = captions.split(",");
            if (ss != null && ss.length > 0 && index < ss.length) {
                return ss[index];
            }
        }
        return null;
    }
 
    /**
     * ??????,??num<=0 ???"";
     * 
     * @param num
     * @return
     */
    public static String numberToString(Object num) {
        if (num == null) {
            return null;
        } else if (num instanceof Integer && (Integer) num > 0) {
            return Integer.toString((Integer) num);
        } else if (num instanceof Long && (Long) num > 0) {
            return Long.toString((Long) num);
        } else if (num instanceof Float && (Float) num > 0) {
            return Float.toString((Float) num);
        } else if (num instanceof Double && (Double) num > 0) {
            return Double.toString((Double) num);
        } else {
            return "";
        }
    }
 
    /**
     * ??????
     * 
     * @param money
     * @param style
     *            ?? [default]???????? such as #.00, #.#
     * @return
     */
 
    public static String moneyToString(Object money, String style) {
        if (money != null && style != null && (money instanceof Double || money instanceof Float)) {
            Double num = (Double) money;
 
            if (style.equalsIgnoreCase("default")) {
                // ???? 0 ??? ,?????????????.0
                if (num == 0) {
                    // ???0
                    return "";
                } else if ((num * 10 % 10) == 0) {
                    // ????
                    return Integer.toString((int) num.intValue());
                } else {
                    // ???
                    return num.toString();
                }
 
            } else {
                DecimalFormat df = new DecimalFormat(style);
                return df.format(num);
            }
        }
        return null;
    }
 
    /**
     * ?sou?????finds ?????finds???????sou???,??true;
     * 
     * @param sou
     * @param finds
     * @return
     */
    public static boolean strPos(String sou, String... finds) {
        if (sou != null && finds != null && finds.length > 0) {
            for (int i = 0; i < finds.length; i++) {
                if (sou.indexOf(finds[i]) > -1)
                    return true;
            }
        }
        return false;
    }
 
    public static boolean strPos(String sou, List<String> finds) {
        if (sou != null && finds != null && finds.size() > 0) {
            for (String s : finds) {
                if (sou.indexOf(s) > -1)
                    return true;
            }
        }
        return false;
    }
 
    public static boolean strPos(String sou, String finds) {
        List<String> t = splitToList(",", finds);
        return strPos(sou, t);
    }
 
    /**
     * ??????????? ????null??????,???null???not null?????? ????s1=s2???
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(String s1, String s2) {
        if (StringUtil.isEmpty(s1) && StringUtil.isEmpty(s2)) {
            return true;
        } else if (!StringUtil.isEmpty(s1) && !StringUtil.isEmpty(s2)) {
            return s1.equals(s2);
        }
        return false;
    }
 
    public static int toInt(String s) {
        if (s != null && !"".equals(s.trim())) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
 
    public static double toDouble(String s) {
        if (s != null && !"".equals(s.trim())) {
            return Double.parseDouble(s);
        }
        return 0;
    }
 
    /**
     * ?xml ??object
     * 
     * @param xml
     * @return
     */
    public static Object xmlToObject(String xml) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF8"));
            return new XMLDecoder(new BufferedInputStream(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public static long toLong(String s) {
        try {
            if (s != null && !"".equals(s.trim()))
                return Long.parseLong(s);
        } catch (Exception exception) {
        }
        return 0L;
    }
 
    public static String simpleEncrypt(String str) {
        if (str != null && str.length() > 0) {
            // str = str.replaceAll("0","a");
            str = str.replaceAll("1", "b");
            // str = str.replaceAll("2","c");
            str = str.replaceAll("3", "d");
            // str = str.replaceAll("4","e");
            str = str.replaceAll("5", "f");
            str = str.replaceAll("6", "g");
            str = str.replaceAll("7", "h");
            str = str.replaceAll("8", "i");
            str = str.replaceAll("9", "j");
        }
        return str;
 
    }
 
    /**
     * ???????URL?????????? ??????http?www???URL?? ???????????????????????????:???list???
     *
     * @param str
     *            ????????
     * @return ?????????
     */
    public static String removeURL(String str) {
        if (str != null)
            str = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
        return str;
    }
 
    /**
     * ??????????????????
     * @param bit
     *            ?????????
     * @return String
     */
    public static String numRandom(int bit) {
        if (bit == 0)
            bit = 6; // ??6?
        String str = "";
        str = "0123456789";// ?????
        return RandomStringUtils.random(bit, str);// ??6?????
    }
 
    /**
     * ????????????????
     *
     * @param bit
     *            ?????????
     * @return String
     */
    public static String random(int bit) {
        if (bit == 0)
            bit = 6; // ??6?
        // ??o?0,l?1????,??,??????o?l
        String str = "";
        str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";// ?????
        return RandomStringUtils.random(bit, str);// ??6?????
    }
 
    /**
     * Wap?????????
     * @param str
     * @return
     */
    public static String replaceWapStr(String str) {
        if (str != null) {
            str = str.replaceAll("<span class=\"keyword\">", "");
            str = str.replaceAll("</span>", "");
            str = str.replaceAll("<strong class=\"keyword\">", "");
            str = str.replaceAll("<strong>", "");
            str = str.replaceAll("</strong>", "");
 
            str = str.replace('$', '?');
 
            str = str.replaceAll("&amp;", "?");
            str = str.replace('&', '?');
 
            str = str.replace('<', '?');
 
            str = str.replace('>', '?');
 
        }
        return str;
    }
 
    /**
     * ????float ??????0.00
     * 
     * @param s
     *            ??????
     * @return ????float
     */
    public static Float toFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return new Float(0);
        }
    }
 
    /**
     * ???????????????????????
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }
 
    /**
     * ??????
     * @return
     */
    public static String Q2B(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }
            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException ex) {
                    if (logger.isErrorEnabled()) {
                        logger.error(ex);
                    }
                }
            } else {
                outStr = outStr + Tstr;
            }
        }
        return outStr;
    }
 
    /**
     * 
     * ????
     * 
     * @param s
     *            ????
     * @param fencode
     *            ?????
     * @param bencode
     *            ??????
     * @return ????
     */
    public static String changCoding(String s, String fencode, String bencode) {
        String str;
        try {
            if (StringUtil.isNotEmpty(s)) {
                str = new String(s.getBytes(fencode), bencode);
            } else {
                str = "";
            }
            return str;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
 
    /**
     * @param str
     * @return
     ************************************************************************* 
     */
    public static String removeHTMLLableExe(String str) {
        str = stringReplace(str, ">\\s*<", "><");
        str = stringReplace(str, "&nbsp;", " ");// ????
        str = stringReplace(str, "<br ?/?>", "\n");// ?<br><br />
        str = stringReplace(str, "<([^<>]+)>", "");// ??<>????
        str = stringReplace(str, "\\s\\s\\s*", " ");// ???????????
        str = stringReplace(str, "^\\s*", "");// ??????
        str = stringReplace(str, "\\s*$", "");// ??????
        str = stringReplace(str, " +", " ");
        return str;
    }
 
    /**
     * ??html??
     * 
     * @param str
     *            ????
     * @return ?????
     */
    public static String removeHTMLLable(String str) {
        str = stringReplace(str, "\\s", "");// ???????????
        str = stringReplace(str, "<br ?/?>", "\n");// ?<br><br />
        str = stringReplace(str, "<([^<>]+)>", "");// ??<>????
        str = stringReplace(str, "&nbsp;", " ");// ????
        str = stringReplace(str, "&(\\S)(\\S?)(\\S?)(\\S?);", "");// ?<br><br />
        return str;
    }
 
    /**
     * ??HTML????????
     * 
     * @param str
     *            ????
     * @return ?????
     */
    public static String removeOutHTMLLable(String str) {
        str = stringReplace(str, ">([^<>]+)<", "><");
        str = stringReplace(str, "^([^<>]+)<", "<");
        str = stringReplace(str, ">([^<>]+)$", ">");
        return str;
    }
 
    /**
     * 
     * ?????
     * 
     * @param str
     *            ????
     * @param sr
     *            ???????
     * @param sd
     *            ????
     * @return ???
     */
    public static String stringReplace(String str, String sr, String sd) {
        String regEx = sr;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        str = m.replaceAll(sd);
        return str;
    }
 
    /**
     * 
     * ?html?????????????
     * 
     * @param str
     *            html???
     * @param pt
     *            ???table
     * @return ???
     */
    public static String fomateToFullForm(String str, String pt) {
        String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        String[] sa = null;
        String sf = "";
        String sf2 = "";
        String sf3 = "";
        for (; m.find();) {
            sa = p.split(str);
            if (sa == null) {
                break;
            }
            sf = str.substring(sa[0].length(), str.indexOf("/>", sa[0].length()));
            sf2 = sf + "></" + pt + ">";
            sf3 = str.substring(sa[0].length() + sf.length() + 2);
            str = sa[0] + sf2 + sf3;
            sa = null;
        }
        return str;
    }
 
    /**
     * 
     * ????????????
     * 
     * @param str
     *            ???
     * @param sub
     *            ??
     * @param b
     *            true????,false????
     * @return ??????????
     */
    public static int[] getSubStringPos(String str, String sub, boolean b) {
        // int[] i = new int[(new Integer((str.length()-stringReplace( str , sub
        // , "" ).length())/sub.length())).intValue()] ;
        String[] sp = null;
        int l = sub.length();
        sp = splitString(str, sub);
        if (sp == null) {
            return null;
        }
        int[] ip = new int[sp.length - 1];
        for (int i = 0; i < sp.length - 1; i++) {
            ip[i] = sp[i].length() + l;
            if (i != 0) {
                ip[i] += ip[i - 1];
            }
        }
        if (b) {
            for (int j = 0; j < ip.length; j++) {
                ip[j] = ip[j] - l;
            }
        }
        return ip;
    }
 
    /**
     * 
     * ????????????
     * 
     * @param str
     *            ????
     * @param ms
     *            ?????
     * @return ??????
     */
    public static String[] splitString(String str, String ms) {
        String regEx = ms;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        String[] sp = p.split(str);
        return sp;
    }
 
    /**
     * Improved version of java.lang.String.split() that supports escape.
     * Example: if you want to split a string with comma "," as separator and
     * with double quotes as escape characters, use
     * <code>split("one, two, \"a,b,c\"", ",", "\"");</code>. Result is a list
     * of 3 strings "one", "two", "a,b,c".
     * <p>
     * <b>Note:</b> keep in mind to escape the chars [b]\()[]{^$|?*+.[/b] that
     * are special regular expression operators!
     * 
     * @param string
     *            String to split up by the given <i>separator</i>.
     * @param separator
     *            Split separator.
     * @param escape
     *            Optional escape character to enclose substrings that can
     *            contain separators.
     * @return Separated substrings of <i>string</i>.
     * @since 2.6.0
     */
    static public String[] split(final String string, final String separator, final String escape) {
        List<String> result = new ArrayList<String>();
 
        if (string != null && separator != null) {
            if (escape == null || "".equals(escape)) {
                result = Arrays.asList(string.split(separator));
            } else {
                final StringBuilder sb = new StringBuilder();
                sb.append("\\s*"); // all matches with optional leading white
                // spaces
                sb.append(escape); // enclosed in escape character
                sb.append("(.*?)"); // with any character
                sb.append(escape);
                sb.append("\\s*"); // and optional trailing white spaces
                sb.append("|"); // or
                sb.append("(?<=^|"); // beginning of line (via zero-width
                // positive lookbehind) or
                sb.append(separator); // separator
                sb.append(")");
                sb.append("[^"); // any character except
                sb.append(separator); // separator
                sb.append("]*"); // zero or more times
                final String regEx = sb.toString();
 
                final Pattern p = Pattern.compile(regEx);
                final Matcher m = p.matcher(string);
                while (m.find()) {
                    // strip off quotes:
                    result.add(m.group(1) != null ? m.group(1) : m.group());
                }// next sequence
            }
        }// else: input unavailable
 
        return result.toArray(new String[0]);
    }// split()
 
    public static final String[] splitMulti(final String string, final String escape, final String... separators) {
        Collection<String> strColl = new ArrayList<String>();
        strColl.add(string);
 
        strColl = splitMulti0(strColl, 0, escape, separators);
        return strColl.toArray(new String[strColl.size()]);
    }
 
    private static final Collection<String> splitMulti0(final Collection<String> strColl, final int separatorIdx, final String escape,
            final String[] separators) {
        if (null == separators || separators.length == 0)
            return strColl;
        if (separatorIdx >= separators.length)
            return strColl;
        final String separator = separators[separatorIdx];
        final Collection<String> result = new ArrayList<String>();
        String tmpArr[];
        for (final String tmpStr : strColl) {
            tmpArr = split(tmpStr, separator, escape);
            for (final String tmpItm : tmpArr) {
                if (!result.contains(tmpItm))
                    result.add(tmpItm);
            }
        }
        return splitMulti0(result, separatorIdx + 1, escape, separators);
    }
 
    /**
     * ????????????,???????????
     * 
     * @param str ????
     * @param pattern
     *            ?????
     * @return ????????
     ************************************************************************* 
     */
 
    // ????????????pattern?????????????
    // java.util.regex??????????????????????????????
    public static String[] getStringArrayByPattern(String str, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(str);
        // ??
        Set<String> result = new HashSet<String>();// ?????????????????? ?????
        // boolean find() ???????????????????
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) { // int groupCount()
                                                                // ?????????????????
                // System.out.println(matcher.group(i));
                result.add(matcher.group(i));
 
            }
        }
        String[] resultStr = null;
        if (result.size() > 0) {
            resultStr = new String[result.size()];
            return result.toArray(resultStr);// ?Set result???String[] resultStr
        }
        return resultStr;
 
    }
 
    /**
     * ?????b,e??????,???e????
     * 
     * @param s
     *            ????
     * @param b
     *            ????
     * @param e
     *            ????
     * @return b,e??????
     */
 
    /*
     * String aaa="abcdefghijklmn"; String[] bbb=StringProcessor.midString(aaa, "b","l"); System.out.println("bbb[0]:"+bbb[0]);//cdefghijk System.out.println("bbb[1]:"+bbb[1]);//lmn ?????????????????????????,????0;?????0?????????,????1
     */
 
    /*
     * String aaa="abcdefgllhijklmn5465"; String[] bbb=StringProcessor.midString(aaa, "b","l"); //ab cdefg llhijklmn5465 // ??0 ??1
     */
    public static String[] midString(String s, String b, String e) {
        int i = s.indexOf(b) + b.length();
        int j = s.indexOf(e, i);
        String[] sa = new String[2];
        if (i < b.length() || j < i + 1 || i > j) {
            sa[1] = s;
            sa[0] = null;
            return sa;
        } else {
            sa[0] = s.substring(i, j);
            sa[1] = s.substring(j);
            return sa;
        }
    }
 
    /**
     * ?????????????????
     * 
     * @param s
     * @param pf
     * @param pb
     * @param start
     * @return
     */
    public static String stringReplace(String s, String pf, String pb, int start) {
        Pattern pattern_hand = Pattern.compile(pf);
        Matcher matcher_hand = pattern_hand.matcher(s);
        int gc = matcher_hand.groupCount();
        int pos = start;
        String sf1 = "";
        String sf2 = "";
        String sf3 = "";
        int if1 = 0;
        String strr = "";
        while (matcher_hand.find(pos)) {
            sf1 = matcher_hand.group();
            if1 = s.indexOf(sf1, pos);
            if (if1 >= pos) {
                strr += s.substring(pos, if1);
                pos = if1 + sf1.length();
                sf2 = pb;
                for (int i = 1; i <= gc; i++) {
                    sf3 = "\\" + i;
                    sf2 = replaceAll(sf2, sf3, matcher_hand.group(i));
                }
                strr += sf2;
            } else {
                return s;
            }
        }
        strr = s.substring(0, start) + strr;
        return strr;
    }
 
    /**
     * ?????
     * 
     * @param s
     *            ????
     * @param sf
     *            ????
     * @param sb
     *            ?????
     * @return ???????
     */
    public static String replaceAll(String s, String sf, String sb) {
        int i = 0, j = 0;
        int l = sf.length();
        boolean b = true;
        boolean o = true;
        String str = "";
        do {
            j = i;
            i = s.indexOf(sf, j);
            if (i > j) {
                str += s.substring(j, i);
                str += sb;
                i += l;
                o = false;
            } else {
                str += s.substring(j);
                b = false;
            }
        } while (b);
        if (o) {
            str = s;
        }
        return str;
    }
 
    /**
     * ??????????????
     * 
     * @param str
     *            ???
     * @param pattern
     *            ???????
     * @return ?????true,?false
     */
    public static boolean isMatch(String str, String pattern) {
        Pattern pattern_hand = Pattern.compile(pattern);
        Matcher matcher_hand = pattern_hand.matcher(str);
        boolean b = matcher_hand.matches();
        return b;
    }
 
    /**
     * ?????
     * 
     * @param s
     *            ????
     * @param jmp
     *            ??jmp
     * @param sb
     *            ??sb
     * @param se
     *            ?se
     * @return ??????
     */
    public static String subStringExe(String s, String jmp, String sb, String se) {
        if (isEmpty(s)) {
            return "";
        }
        int i = s.indexOf(jmp);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        i = s.indexOf(sb);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        if (se == "") {
            return s;
        } else {
            i = s.indexOf(se);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }
            return s;
        }
    }
 
    /**
     * ************************************************************************* ????URL?????????
     * 
     * @param src ????
     * @return ???????
     ************************************************************************* 
     */
    public static String URLEncode(String src) {
        String return_value = "";
        try {
            if (src != null) {
                return_value = URLEncoder.encode(src, "GBK");
 
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return_value = src;
        }
 
        return return_value;
    }
 
    /**
     * *************************************************************************
     * @param str ??
     *            &#31119;test&#29031;&#27004;&#65288;&#21271;&#22823;&#38376;&# 24635 ;&#24215;&#65289;&#31119;
     * @return ???????
     ************************************************************************* 
     */
    public static String getGBK(String str) {
 
        return transfer(str);
    }
 
    public static String transfer(String str) {
        Pattern p = Pattern.compile("&#\\d+;");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String old = m.group();
            str = str.replaceAll(old, getChar(old));
        }
        return str;
    }
 
    public static String getChar(String str) {
        String dest = str.substring(2, str.length() - 1);
        char ch = (char) Integer.parseInt(dest);
        return "" + ch;
    }
 
    /**
     * yahoo????????.
     * @param subject
     * @return
     */
    public static String subYhooString(String subject, int size) {
        subject = subject.substring(1, size);
        return subject;
    }
 
    public static String subYhooStringDot(String subject, int size) {
        subject = subject.substring(1, size) + "...";
        return subject;
    }
 
    /**
     * ????(??)??list????“,”?????? ???????????????? ??List<Integer> intList = new ArrayList<Integer>(); ?????StringUtil.listTtoString(intList); ???list?4????1000000??????850ms??
     *
     * @param <T>
     *            ??
     * @param list
     *            list??
     * @return ?“,”??????
     */
    public static <T> String listTtoString(List<T> list) {
        if (list == null || list.size() < 1)
            return "";
        Iterator<T> i = list.iterator();
        if (!i.hasNext())
            return "";
        StringBuilder sb = new StringBuilder();
        for (;;) {
            T e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.toString();
            sb.append(",");
        }
    }
 
    /**
     * ?????????“,”??????
     *
     * @param a
     *            ??a
     * @return ?“,”??????
     */
    public static String intArraytoString(int[] a) {
        if (a == null)
            return "";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }
 
    /**
     * ????????
     *
     */
    public static boolean isContentRepeat(String content) {
        int similarNum = 0;
        int forNum = 0;
        int subNum = 0;
        int thousandNum = 0;
        String startStr = "";
        String nextStr = "";
        boolean result = false;
        float endNum = (float) 0.0;
        if (content != null && content.length() > 0) {
            if (content.length() % 1000 > 0)
                thousandNum = (int) Math.floor(content.length() / 1000) + 1;
            else
                thousandNum = (int) Math.floor(content.length() / 1000);
            if (thousandNum < 3)
                subNum = 100 * thousandNum;
            else if (thousandNum < 6)
                subNum = 200 * thousandNum;
            else if (thousandNum < 9)
                subNum = 300 * thousandNum;
            else
                subNum = 3000;
            for (int j = 1; j < subNum; j++) {
                if (content.length() % j > 0)
                    forNum = (int) Math.floor(content.length() / j) + 1;
                else
                    forNum = (int) Math.floor(content.length() / j);
                if (result || j >= content.length())
                    break;
                else {
                    for (int m = 0; m < forNum; m++) {
                        if (m * j > content.length() || (m + 1) * j > content.length() || (m + 2) * j > content.length())
                            break;
                        startStr = content.substring(m * j, (m + 1) * j);
                        nextStr = content.substring((m + 1) * j, (m + 2) * j);
                        if (startStr.equals(nextStr)) {
                            similarNum = similarNum + 1;
                            endNum = (float) similarNum / forNum;
                            if (endNum > 0.4) {
                                result = true;
                                break;
                            }
                        } else
                            similarNum = 0;
                    }
                }
            }
        }
        return result;
    }
 
    /**
     * ????????? null?"" null??result,???????
     * 
     * @param s
     * @return
     */
    public static String isEmpty(String s, String result) {
        if (s != null && !s.equals("")) {
            return s;
        }
        return result;
    }
 
    /**
     * ????????
     * 
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        boolean flag = true;
        if (str != null && !str.equals("") && !str.equals("null")) {
            if (str.toString().length() > 0) {
                flag = true;
            }
        } else {
            flag = false;
        }
        return flag;
    }
 
    /**
     * ?????????
     *
     * @param str
     * @return
     */
    public static String full2Half(String str) {
        if (str == null || "".equals(str))
            return "";
        StringBuffer sb = new StringBuffer();
 
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
 
            if (c >= 65281 && c < 65373)
                sb.append((char) (c - 65248));
            else
                sb.append(str.charAt(i));
        }
 
        return sb.toString();
 
    }
 
    /**
     * ????????
     *
     * @param str
     * @return
     */
    public static String replaceBracketStr(String str) {
        if (str != null && str.length() > 0) {
            str = str.replaceAll("?", "(");
            str = str.replaceAll("?", ")");
        }
        return str;
    }
 
    /**
     * ???????map???(??a=1&b=2 => a=1,b=2)
     * 
     * @param query
     *            ??????
     * @param split1
     *            ????????????&?
     * @param split2
     *            key?value?????????=?
     * @param dupLink
     *            ???????????????????????????????????null null??????????????????????????????
     * @return map
     * @author sky
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map<String, String> parseQuery(String query, char split1, char split2, String dupLink) {
        if (!isEmpty(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap();
 
            String name = null;
            String value = null;
            String tempValue = "";
            int len = query.length();
            for (int i = 0; i < len; i++) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!isEmpty(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = result.get(name);
                            if (tempValue != null) {
                                value += dupLink + tempValue;
                            }
                        }
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }
 
            if (!isEmpty(name) && value != null) {
                if (dupLink != null) {
                    tempValue = result.get(name);
                    if (tempValue != null) {
                        value += dupLink + tempValue;
                    }
                }
                result.put(name, value);
            }
 
            return result;
        }
        return null;
    }
 
    /**
     * ?list ??????????String
     * 
     * @param list
     * @param slipStr
     * @return String
     */
    @SuppressWarnings("rawtypes")
    public static String listToStringSlipStr(List list, String slipStr) {
        StringBuffer returnStr = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                returnStr.append(list.get(i)).append(slipStr);
            }
        }
        if (returnStr.toString().length() > 0)
            return returnStr.toString().substring(0, returnStr.toString().lastIndexOf(slipStr));
        else
            return "";
    }
 
    /**
     * ???start???*??len????????
     * 
     * @param str
     *            ???????
     * @param start
     *            ????
     * @param len
     *            ??
     * @return ???????
     */
    public static String getMaskStr(String str, int start, int len) {
        if (StringUtil.isEmpty(str)) {
            return str;
        }
        if (str.length() < start) {
            return str;
        }
 
        // ??*??????
        String ret = str.substring(0, start);
 
        // ???????*??
        int strLen = str.length();
        if (strLen < start + len) {
            len = strLen - start;
        }
 
        // ???*
        for (int i = 0; i < len; i++) {
            ret += "*";
        }
 
        // ??*??????
        if (strLen > start + len) {
            ret += str.substring(start + len);
        }
 
        return ret;
    }
 
    /**
     * ?????????,??????????List???
     * 
     * @param slipStr
     *            ??????
     * @param src
     *            ???
     * @return ??
     */
    public static List<String> stringToStringListBySlipStr(String slipStr, String src) {
 
        if (src == null)
            return null;
        List<String> list = new ArrayList<String>();
        String[] result = src.split(slipStr);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }
 
    /**
     * ?????
     * 
     * @param str
     *            ?????
     * @param len
     *            ??????
     * @param tail
     *            ???????
     * @return ???????
     */
    public static String getHtmlSubString(String str, int len, String tail) {
        if (str == null || str.length() <= len) {
            return str;
        }
        int length = str.length();
        char c = ' ';
        String tag = null;
        String name = null;
        int size = 0;
        String result = "";
        boolean isTag = false;
        List<String> tags = new ArrayList<String>();
        int i = 0;
        for (int end = 0, spanEnd = 0; i < length && len > 0; i++) {
            c = str.charAt(i);
            if (c == '<') {
                end = str.indexOf('>', i);
            }
 
            if (end > 0) {
                // ????
                tag = str.substring(i, end + 1);
                int n = tag.length();
                if (tag.endsWith("/>")) {
                    isTag = true;
                } else if (tag.startsWith("</")) { // ???
                    name = tag.substring(2, end - i);
                    size = tags.size() - 1;
                    // ????html????
                    if (size >= 0 && name.equals(tags.get(size))) {
                        isTag = true;
                        tags.remove(size);
                    }
                } else { // ???
                    spanEnd = tag.indexOf(' ', 0);
                    spanEnd = spanEnd > 0 ? spanEnd : n;
                    name = tag.substring(1, spanEnd);
                    if (name.trim().length() > 0) {
                        // ????????html??
                        spanEnd = str.indexOf("</" + name + ">", end);
                        if (spanEnd > 0) {
                            isTag = true;
                            tags.add(name);
                        }
                    }
                }
                // ?html????
                if (!isTag) {
                    if (n >= len) {
                        result += tag.substring(0, len);
                        break;
                    } else {
                        len -= n;
                    }
                }
 
                result += tag;
                isTag = false;
                i = end;
                end = 0;
            } else { // ?html????
                len--;
                result += c;
            }
        }
        // ??????html??
        for (String endTag : tags) {
            result += "</" + endTag + ">";
        }
        if (i < length) {
            result += tail;
        }
        return result;
    }
 
    public static String getProperty(String property) {
        if (property.contains("_")) {
            return property.replaceAll("_", "\\.");
        }
        return property;
    }
 
    /**
     * ????encodeURIComponent??????
     * 
     * @param property
     * @return
     */
    public static String getEncodePra(String property) {
        String trem = "";
        if (isNotEmpty(property)) {
            try {
                trem = URLDecoder.decode(property, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return trem;
    }
 
    // ?????????????
    public boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }
 
    // ????
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
 
    // ?????
    public String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
 
    /**
     * ???????????????
     * 
     * @param stringArray
     *            ???
     * @param source
     *            ??????
     * @return ????
     */
    public static boolean contains(String[] stringArray, String source) {
        // ???list
        List<String> tempList = Arrays.asList(stringArray);
 
        // ??list?????,????
        if (tempList.contains(source)) {
            return true;
        } else {
            return false;
        }
    }
 
    /**
     * ??????
     * @param str
     * @return String
     */
    public static String StringFilter(String str) {
        // ????????
        // String regEx = "[^a-zA-Z0-9]";
        // ?????????
        String regEx = "[`~!@#$%^&*()+=|{}':;',[*\\\\?]//[//].<>/?~?@#?%……&*??——+|{}??‘??”“’????]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
 
    /**
     * ????
     * @param source
     * @return String
     */
    public static String deleteCNChar(String source) {
        char[] cs = source.toCharArray();
        int length = cs.length;
        char[] buf = new char[length];
        for (int i = 0; i < length; i++) {
            char c = cs[i];
            if (!checkCNChar(c)) {
                buf[i] = c;
            }
        }
        String ret = new String(buf);
        return ret.trim();
    }
 
    public static boolean checkCNChar(char oneChar) {
        if ((oneChar >= '\u4e00' && oneChar <= '\u9fa5') || (oneChar >= '\uf900' && oneChar <= '\ufa2d'))
            return true;
        return false;
    }
 
    /**
     * ???????????"/"
     * @param filepath
     * @return
     */
    public static String urlPath(String filepath) {
        if (null != filepath) {
            return filepath.replace(File.separator, "/");
        }
        return null;
    }
 
    /**
     * ???????????"\"
     * @param filepath
     * @return
     */
    public static String filePath(String filepath) {
        if (null != filepath) {
            return filepath.replace("/", File.separator);
        }
        return null;
    }
 
    public static boolean containsKeyString(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (str.contains("'") || str.contains("\"") || str.contains("\r") || str.contains("\n") || str.contains("\t") || str.contains("\b")
                || str.contains("\f")) {
            return true;
        }
        return false;
    }
 
    // ?""?'??
    public static String replaceKeyString(String str) {
        if (containsKeyString(str)) {
            return str.replace("'", "\\'").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t").replace("\b", "\\b")
                    .replace("\f", "\\f");
        } else {
            return str;
        }
    }
 
    /**
     * ???????B,KB,MB,GB?
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        long SIZE_KB = 1024;
        long SIZE_MB = SIZE_KB * 1024;
        long SIZE_GB = SIZE_MB * 1024;
 
        if (size < SIZE_KB) {
            return String.format("%d B", (int) size);
        } else if (size < SIZE_MB) {
            return String.format("%.2f KB", (float) size / SIZE_KB);
        } else if (size < SIZE_GB) {
            return String.format("%.2f MB", (float) size / SIZE_MB);
        } else {
            return String.format("%.2f GB", (float) size / SIZE_GB);
        }
    }
 
    /**
     * ??????????????????
     *
     * @param srcString ????
     * @param flag     ??????ture???false??
     * @return ????????
     */
    public static String toLowerCaseInitial(String srcString, boolean flag) {
        StringBuilder sb = new StringBuilder();
        if (flag) {
            sb.append(Character.toLowerCase(srcString.charAt(0)));
        } else {
            sb.append(Character.toUpperCase(srcString.charAt(0)));
        }
        sb.append(srcString.substring(1));
        return sb.toString();
    }
 
    public static final String toString(final Object obj, final String defauleValue) {
        if (obj == null || "".equals(obj)) {
            return defauleValue;
        }
        return String.valueOf(obj);
    }
 
    /**
    * ???????????.??????????
    *
    * @param clazzName ????
    * @return ???.????????????
    */
    public static String getLastName(String clazzName) {
        String[] ls = clazzName.split("\\.");
        return ls[ls.length - 1];
    }
 
    /**
    * ??????????????????????????,???????"/"???
    *
    * @param path ????
    * @return ?????????
    */
    public static String formatPath(String path) {
        String reg0 = "\\\\?";
        String reg = "\\\\?|/?";
        String temp = path.trim().replaceAll(reg0, "/");
        temp = temp.replaceAll(reg, "/");
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        if (System.getProperty("file.separator").equals("\\")) {
            temp = temp.replace('/', '\\');
        }
        return temp;
    }
 
    /**
    * ??????????????????????????,???????"/"??(???FTP????????Web???????)?
    *
    * @param path ????
    * @return ?????????
    */
    public static String formatPath4Ftp(String path) {
        String reg0 = "\\\\?";
        String reg = "\\\\?|/?";
        String temp = path.trim().replaceAll(reg0, "/");
        temp = temp.replaceAll(reg, "/");
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp;
    }
 
    /**
    * ???????
    *
    * @param path ????
    * @return ?????
    */
    public static String getParentPath(String path) {
        return new File(path).getParent();
    }
 
    /**
    * ??????
    *
    * @param fullPath ???
    * @param rootPath ???
    * @return ??????????
    */
    public static String getRelativeRootPath(String fullPath, String rootPath) {
        String relativeRootPath = null;
        String _fullPath = formatPath(fullPath);
        String _rootPath = formatPath(rootPath);
 
        if (_fullPath.startsWith(_rootPath)) {
            relativeRootPath = fullPath.substring(_rootPath.length());
        } else {
            throw new RuntimeException("?????????????????????");
        }
        if (relativeRootPath == null)
            return null;
        else
            return formatPath(relativeRootPath);
    }
 
    /**
    * ?????????
    *
    * @return ?????
    */
    public static String getSystemLineSeparator() {
        return System.getProperty("line.separator");
    }
 
    /**
    * ??“|”????????????????????????????????
    *
    * @param series ??“|”??????
    * @return ???????
    */
    public static List<String> series2List(String series) {
        return series2List(series, "\\|");
    }
 
    /**
    * ???????regex????????????????????????????????
    *
    * @param series ????????????
    * @param regex ???????????
    * @return ???????
    */
    private static List<String> series2List(String series, String regex) {
        List<String> result = new ArrayList<String>();
        if (series != null && regex != null) {
            for (String s : series.split(regex)) {
                if (s.trim() != null && !s.trim().equals(""))
                    result.add(s.trim());
            }
        }
        return result;
    }
 
    /**
    * @param strList ???????
    * @return ??“|”????????
    */
    public static String list2series(List<String> strList) {
        StringBuffer series = new StringBuffer();
        for (String s : strList) {
            series.append(s).append("|");
        }
        return series.toString();
    }
 
    /**
    * ????????????
    *
    * @param resStr ????
    * @return ????????????
    */
    public static String firstToLowerCase(String resStr) {
        if (resStr == null) {
            return null;
        } else if ("".equals(resStr.trim())) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            Character c = resStr.charAt(0);
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c))
                    c = Character.toLowerCase(c);
                sb.append(resStr);
                sb.setCharAt(0, c);
                return sb.toString();
            }
        }
        return resStr;
    }
 
    /**
    * ????????????
    *
    * @param resStr ????
    * @return ????????????
    */
    public static String firstToUpperCase(String resStr) {
        if (resStr == null) {
            return null;
        } else if ("".equals(resStr.trim())) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            Character c = resStr.charAt(0);
            if (Character.isLetter(c)) {
                if (Character.isLowerCase(c))
                    c = Character.toUpperCase(c);
                sb.append(resStr);
                sb.setCharAt(0, c);
                return sb.toString();
            }
        }
        return resStr;
    }
 
    public static final String u(final Object... args) {
        if (null == args)
            return "";
        if (args.length == 1)
            return String.valueOf(args[0]);
        final StringBuilder buf = new StringBuilder();
        for (final Object obj : args) {
            buf.append(String.valueOf(obj));
        }
        return buf.toString();
    }
 
    public static String html(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }
 
    public static void main(String[] args) {
        System.out.println(formatSize(1546513));
 
        /*System.out.println(System.getProperty("file.separator"));
        Properties p = System.getProperties();
        System.out.println(formatPath("C:///\\xxxx\\\\\\\\\\///\\\\R5555555.txt"));
 
         List<String> result = series2List("asdf | sdf|siii|sapp|aaat| ", "\\|");
         System.out.println(result.size());
         for (String s : result) {
             System.out.println(s);
         }*/
    }
 
}