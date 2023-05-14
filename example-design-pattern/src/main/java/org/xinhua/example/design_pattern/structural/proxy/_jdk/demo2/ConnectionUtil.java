package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 0:20
 * @Description: 数据库连接工具类
 * @Version: 1.0
 */
public class ConnectionUtil {

    private static Queue<Connection> pool = new LinkedList<>();

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal();

    private static Map<Connection, Integer> useageMap = new ConcurrentHashMap();

    static {
        try {
            String url = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
            String user = "root";
            String password = "123456";
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < 20; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                pool.offer(connection);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = pool.poll();
            threadLocal.set(connection);
        }
        increaseUseage(connection);
        return connection;
    }

    public static void release(Connection connection) {
        if (connection != null) {
            int num = decreaseUseage(connection);
            if (num == 0) {
                threadLocal.remove();
                pool.offer(connection);
            }
        }
    }

    private static int increaseUseage(Connection connection) {
        Integer num = useageMap.get(connection);
        num = num == null ? 1 : num + 1;
        useageMap.put(connection, num);
        return num;
    }

    private static int decreaseUseage(Connection connection) {
        Integer num = useageMap.get(connection);
        if (num == null) {
            return 0;
        }
        num--;
        useageMap.put(connection, num);
        return num;
    }

}
