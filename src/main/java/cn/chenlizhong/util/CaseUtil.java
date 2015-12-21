package cn.chenlizhong.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : lizhong.chen
 * @version :
 *          Descr : 类型转换工具
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

    /**
     * 转为long型
     *
     * @param obj
     * @return
     */
    public static long caseLong(Object obj) {
        return caseLong(obj, 0);
    }

    /**
     * 转为long型(提供默认值)
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static long caseLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (null != obj) {
            String strValue = caseString(obj);
            if (StringUtils.isNoneEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为int型
     *
     * @param obj
     * @return
     */
    public static int caseInt(Object obj) {
        return caseInt(obj, 0);
    }

    /**
     * 转为int型(提供默认值)
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static int caseInt(Object obj, int defaultValue) {
        long intValue = defaultValue;
        if (null != obj) {
            String strValue = caseString(obj);
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.valueOf(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return defaultValue;
    }

    /**
     * 转为boolean型
     *
     * @param obj
     * @return
     */
    public static boolean caseBoolean(Object obj) {
        return caseBoolean(obj, false);
    }

    /**
     * 转为boolean型(提供默认值)
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static boolean caseBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (null != obj) {
            booleanValue = Boolean.parseBoolean(caseString(obj));
        }
        return booleanValue;
    }

}
