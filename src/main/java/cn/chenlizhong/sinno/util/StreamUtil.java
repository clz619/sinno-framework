package cn.chenlizhong.sinno.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流操作工具类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午7:18
 */
public final class StreamUtil {
    private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);

    public static String getString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            LOG.error("get string failure", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
