package cn.chenlizhong.sinno.bean;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 返回视图对象
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午4:52
 */
public class View {
    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = Maps.newHashMap();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
