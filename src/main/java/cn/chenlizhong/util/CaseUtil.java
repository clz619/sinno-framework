package cn.chenlizhong.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author : lizhong.chen
 * @version :
 *          Descr : TODO
 * @since : 15/12/3 上午12:07
 */
public final class CaseUtil {

    /**
     * 转为String型
     *
     * @param obj
     * @return
     */
    public static String caseString(Object obj) {
        return caseString(obj, "");
    }

    /**
     * 转为String型(提供默认值)
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String caseString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    /**
     * 转为double型
     *
     * @param obj
     * @return
     */
    public static double caseDouble(Object obj) {
        return caseDouble(obj, 0);
    }

    /**
     * 转为double型(提供默认值)
     *
     * @param obj
     * @param defalutValue
     * @return
     */
    public static double caseDouble(Object obj, double defalutValue) {
        double doubleValue = defalutValue;
        if (obj != null) {
            String strVal = caseString(obj);
            if (StringUtils.isNotEmpty(strVal)) {
                try {
                    doubleValue = Double.parseDouble(strVal);
                } catch (NumberFormatException e) {
                    //ingore
                }
            }
        }
        return doubleValue;
    }

}
