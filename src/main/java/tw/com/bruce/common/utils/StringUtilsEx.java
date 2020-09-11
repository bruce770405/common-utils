package tw.com.bruce.common.utils;

import com.sun.istack.NotNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtilsEx extends StringUtils {

    private final static Charset CHARSET = Charset.forName("UTF-8");

    /**
     * 移除字串左邊的<code>0</code>字元
     *
     * <pre>
     * StringUtils.trimLeftZero(null) = ""
     * StringUtils.trimLeftZero("0012345600") = "12345600"
     * </pre>
     *
     * @param sSource 待處理之字串
     * @return 移除左側<code>0</code>字元後的字串，永不為null
     */
    public static String trimLeftZero(String sSource) {

        if (sSource == null) {
            return "";
        }

        int iLen = sSource.length();
        int index = -1;
        for (int i = 0; (i < iLen && index < 0); i++) {
            char ch = sSource.charAt(i);

            if (ch != '0') {
                index = i;
            }
        }
        String s = "";
        // 發現非0之數字
        if (index >= 0) {
            s = sSource.substring(index, iLen);
        }
        return s;
    }

    /**
     * StringUtils.trimRightZero("01234500") = "012345"
     */
    public static String trimRightZero(String sSource) {
        if (sSource == null) {
            return "";
        }

        int iLen = sSource.length();
        int index = -1;
        // 由後至前
        for (int i = (iLen - 1); (i >= 0 && index < 0); i--) {
            char ch = sSource.charAt(i);
            if (ch != '0') {
                index = i;
            }
        }

        String s = "";
        if (index >= 0) {
            s = sSource.substring(0, index + 1);
        }
        return s;
    }

    /**
     * 移除字串左右邊之半形、全形空白
     */
    public static String trimAllBigSpace(String sValue) {

        if (isEmpty(sValue)) {
            return "";
        }

        String sResult = sValue;

        while (sResult.endsWith("　") || sResult.endsWith(" ")) {
            sResult = sResult.substring(0, sResult.length() - 1);
        }

        while (sResult.startsWith("　") || sResult.startsWith(" ")) {
            sResult = sResult.substring(1);
        }

        return sResult;
    }

    /**
     * 移除字串右邊之全形空白
     */
    public static String trimRightBigSpace(String sValue) {
        String sResult = sValue;

        while (sResult.endsWith("　")) {
            sResult = sResult.substring(0, sResult.length() - 1);
        }

        return sResult;
    }

    /**
     * 是否全部為全型
     */
    public static boolean isAllFullWidth(String str) {
        if (isEmpty(str)) {
            return true;
        }
        return str.matches("^[^\\x00-\\xff]*$");
    }

    /**
     * 是否全部為半型
     */
    public static boolean isAllHalfWidth(String str) {
        if (isEmpty(str)) {
            return true;
        }
        return str.matches("^[\\x00-\\xff]*$");
    }

    /**
     * 判斷文字是否為數字
     */
    public static boolean isDigital(String str) {
        return NumberUtils.isNumber(str);
    }

    /**
     * 字串使否包含大寫
     */
    public static boolean isContainUpperCase(String str) {
        Pattern pattern = Pattern.compile(".*[A-Z].*");
        return pattern.matcher(str).matches();
    }

    /**
     * 字串是否包含小寫
     */
    public static boolean isContainLowerCase(String str) {
        Pattern pattern = Pattern.compile(".*[a-z].*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判斷文字是否為金額數字
     */
    public static boolean isMoneyNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 若字串為null時, 回傳空字串""
     */
    public static String getDefaultString(String str) {
        return StringUtilsEx.isEmpty(str) ? "" : str;
    }

    /**
     * 取得截斷長度的字串(長度)
     *
     * @param str        the check string
     * @param wordLength cut length
     * @return
     */
    public static String trim(String str, int wordLength) {
        if (StringUtilsEx.isBlank(str)) {
            return null;

        } else if (wordLength > str.length()) {
            return str;

        } else {
            return str.substring(0, wordLength);
        }
    }

    /**
     * 字元型態左靠，不足位數補傳入字元，超出截掉過長字元
     *
     * @param section the check string
     * @param sPadStr padding string
     * @param length  over length
     * @return
     */
    public static String createRightStringSection(String section, String sPadStr, int length) {

        String sSection = trimToEmpty(section);

        try {


            byte[] data = sSection.getBytes(CHARSET.name());

            if (data.length > length) {
                // 避免取到一半的中文字, 如果取到一半的中文字, 最後半個字中文字用空白取代
                String lastWord = new String(ArrayUtils.subarray(data, length - 1, length + 1), CHARSET.name());
                if (lastWord.getBytes().length == 1) {
                    return new String(ArrayUtils.subarray(data, 0, length - 1), CHARSET.name()) + " ";
                } else {
                    return new String(ArrayUtils.subarray(data, 0, length), CHARSET.name());
                }

            } else if (data.length == length) {
                return new String(ArrayUtils.subarray(data, 0, length), CHARSET.name());
            } else {
                return rightPadByByteLength(sSection, length, sPadStr);
            }
        } catch (UnsupportedEncodingException e) {
//            logger.error(e, e);
            return sSection;
        }
    }

    /**
     * 字元型態右靠，不足位數補傳入字元，超出截掉過長字元
     *
     * @param section
     * @param length
     * @return
     */
    public static String createLeftStringSection(String section, String sPadStr, int length) {

        section = StringUtilsEx.trimToEmpty(section);

        try {

            byte[] data = section.getBytes(CHARSET.name());

            if (data.length >= length) {
                ArrayUtils.subarray(data, 0, length);
                return new String(ArrayUtils.subarray(data, data.length - length, data.length), CHARSET.name());
            } else {
                return leftPadByByteLength(section, length, sPadStr);
            }
        } catch (UnsupportedEncodingException e) {
//            logger.error(e, e);
            return section;
        }
    }

    /**
     * 右靠補設定字型到設定byte長(預設編碼MS950)
     *
     * @param sData
     * @param iLength
     * @param sPadStr
     * @return
     */
    public static String leftPadByByteLength(String sData, int iLength, String sPadStr) {
        return leftPadByByteLength(sData, iLength, sPadStr, CHARSET);
    }

    /**
     * 右靠補設定字型到設定byte長
     *
     * @param sData
     * @param iLength
     * @param sPadStr
     * @param encoding
     * @return
     */
    public static String leftPadByByteLength(String sData, int iLength, String sPadStr, @NotNull Charset encoding) {

        if (sData == null) {
            sData = StringUtilsEx.EMPTY;
        }

        if (sPadStr == null || sPadStr.length() == 0) {
            sPadStr = " ";
        }

        String sResult = sData;
        int byteLen = sResult.getBytes(encoding).length;
        while (byteLen < iLength) {
            sResult = sPadStr + sResult;
            byteLen = sResult.getBytes(encoding).length;
        }

        return sResult;
    }

    /**
     * 左靠補設定字型到設定byte長(預設編碼UTF-8)
     *
     * @param sData
     * @param iLength
     * @param sPadStr
     * @return
     */
    public static String rightPadByByteLength(String sData, int iLength, String sPadStr) {
        return rightPadByByteLength(sData, iLength, sPadStr, CHARSET);
    }

    /**
     * 左靠補設定字型到設定byte長
     *
     * @param sData
     * @param iLength
     * @param padStr
     * @param encoding
     * @return
     */
    public static String rightPadByByteLength(String sData, int iLength, String padStr, @NotNull Charset encoding) {

        if (sData == null) {
            sData = StringUtilsEx.EMPTY;
        }

        if (padStr == null || padStr.length() == 0) {
            padStr = " ";
        }
        String sResult = sData;
        int bytelen = str2Bytes(sData, encoding).length;
        StringBuilder sb = new StringBuilder(sResult);
        while (bytelen < iLength) {
            sb.append(padStr);
            bytelen = str2Bytes(sb.toString(), encoding).length;
        }

        return sb.toString();
    }

    /**
     * 是否不符合Reg的Patten.
     *
     * @param patternString the pattern string.
     * @param value         the value.
     * @return boolean
     */
    public static boolean isNotFind(String patternString, String value) {
        return !isFind(patternString, value);
    }

    /**
     * 是否符合Reg的Patten.
     *
     * @param patternString the pattern string.
     * @param value         the value.
     * @return boolean
     */
    public static boolean isFind(String patternString, String value) {

        if (null == patternString || null == value) {
            return false;
        }

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * StringUtilsEx.equalsIgnoreNull(null, null) = true
     * StringUtilsEx.equalsIgnoreNull(null, "") = true
     * StringUtilsEx.equalsIgnoreNull("", "") = true
     * StringUtilsEx.equalsIgnoreNull(null, "abc") = false
     * StringUtilsEx.equalsIgnoreNull("abc", null) = false
     * StringUtilsEx.equalsIgnoreNull("abc", "abc") = true
     * StringUtilsEx.equalsIgnoreNull("abc", "ABC") = false
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreNull(String str1, String str2) {
        return StringUtilsEx.equals(StringUtilsEx.getDefaultString(str1), StringUtilsEx.getDefaultString(str2));
    }

    /**
     * convert str to bytes (不產生exception)
     *
     * @param str
     * @param encoding
     * @return
     */
    public static byte[] str2Bytes(String str, @NotNull Charset encoding) {
        byte[] data = null;
        try {
            data = str.getBytes(encoding.name());
        } catch (UnsupportedEncodingException e) {
//            logger.error(e.getMessage(), e);
        }
        return (data != null) ? data : ArrayUtils.EMPTY_BYTE_ARRAY;
    }

}
