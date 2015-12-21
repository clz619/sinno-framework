package cn.chenlizhong.helper;

import cn.chenlizhong.util.PropsUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : lizhong.chen
 * @version : 1.0
 *          Descr : 数据库操作助手
 * @since : 15/12/20 下午5:44
 */
public final class DatabaseHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseHelper.class);


    private static final String DRIVER;

    private static final String URL;

    private static final String USERNAME;

    private static final String PASSWORD;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    private static final BasicDataSource DATE_SOURCE;

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    static {
        //init
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        DATE_SOURCE = new BasicDataSource();
        DATE_SOURCE.setDriverClassName(DRIVER);
        DATE_SOURCE.setUrl(URL);
        DATE_SOURCE.setUsername(USERNAME);
        DATE_SOURCE.setPassword(PASSWORD);

    }


    /**
     * 获取数据库连接
     *
     * @return Connection
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (null == conn) {
            try {
                conn = DATE_SOURCE.getConnection();
            } catch (SQLException e) {
                LOG.error("get connection failure", e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 查询实体列表
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 执行查询语句
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result = null;

        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOG.error("execute query failure", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 执行更新语句(包括update,insert,delete)
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;

        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOG.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            LOG.error("can not insert entity:field is empty");
            return false;
        }


        return true;
    }

    /**
     * 执行sql文件
     *
     * @param filePath
     */
    public static void executeSqlFile(String filePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String sql;
        try {
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOG.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    public static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }
}
