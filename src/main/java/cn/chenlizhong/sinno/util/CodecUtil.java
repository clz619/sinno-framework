package cn.chenlizhong.sinno.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码工具类
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午7:26
 */
public final class CodecUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将url编码
     *
     * @param source
     * @return
     */
    public static String encodeURL(String source) {
        String ret;
        try {
            ret = URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            LOG.error("encode url failure", e);
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 将url解码
     *
     * @param source
     * @return
     */
    public static String decodeURL(String source) {
        String ret;
        try {
            ret = URLDecoder.decode(source, "UTF-8");
        } catch (Exception e) {
            LOG.error("decode url failure", e);
            throw new RuntimeException(e);
        }
        return ret;
    }
}
