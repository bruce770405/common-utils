package tw.com.bruce.common.utils;

import jdk.internal.joptsimple.internal.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NumberUtilsEx {

    private static boolean isMatch(String regex, String orginal) {
        if (Strings.isNullOrEmpty(orginal)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 判斷是否為正整數(不包含0)
     */
    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    /**
     * 判斷是否為負整數(不包含0)
     */
    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }

    /**
     * 判斷是否為整數(包含0)
     */
    public static boolean isWholeNumber(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 判斷是否為正小數
     */
    public static boolean isPositiveDecimal(String orginal) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 判斷是否為 數字 or小數
     */
    public static boolean isRealNumber(String orginal) {
        return isWholeNumber(orginal) || isDecimal(orginal);
    }

    /**
     * 判斷是否為小數
     */
    public static boolean isDecimal(String orginal) {
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    /**
     * 判斷是否為正 數字or小數
     */
    public static boolean isPositiveRealNumber(String orginal) {
        return isPositiveInteger(orginal) || isPositiveDecimal(orginal);
    }
}
