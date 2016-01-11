package cn.chenlizhong.sinno.bean;

import cn.chenlizhong.sinno.util.CaseUtil;

import java.util.Map;

/**
 * 请求参数对象
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午4:46
 */
public class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名获取long型参数值
     *
     * @param name
     * @return
     */
    public long getLong(String name) {
        return CaseUtil.caseLong(paramMap.get(name));
    }

    /**
     * 获取所有字段信息
     *
     * @return
     */
    public Map<String, Object> getMap() {
        return paramMap;
    }
}
