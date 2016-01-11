package cn.chenlizhong.sinno.util;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

/**
 * 类操作工具
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/5 下午11:26
 */
public final class ClassUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CaseUtil.class);

    /**
     * 获取类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 加载类
     *
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, false);
    }

    /**
     * 加载类
     *
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {

        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOG.error("load class err!", e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 获取指定包名下的所有类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = Sets.newHashSet();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (null != url) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        //文件
                        String packagePath = url.getPath().replaceAll("%20", " ");
                    } else if ("jar".equals(protocol)) {
                        //jar
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("get class set err!", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 批量加载类
     *
     * @param classSet
     * @param packagePath
     * @param packageName
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return ((file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
            }
        });

        for (File file : files) {
            String fileName = file.getName();

            if (file.isFile()) {
                //是class文件
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    //加上包名
                    className = packageName + "." + className;
                }

                doAddClass(classSet, className);
            } else {
                //是文件夹
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    //加上包名的文件目录
                    subPackagePath = packagePath + File.pathSeparator + subPackagePath;
                }

                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(subPackageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }

                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 单个加载类
     *
     * @param classSet
     * @param className
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz = loadClass(className, false);
        classSet.add(clazz);
    }

}
