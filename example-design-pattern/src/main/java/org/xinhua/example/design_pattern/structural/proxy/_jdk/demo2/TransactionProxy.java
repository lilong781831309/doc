package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 2:22
 * @Description: 用户服务实现代理类
 * @Version: 1.0
 */
public class TransactionProxy {

    public static <T> T newProxyInstance(T target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                if (!target.getClass().isAnnotationPresent(Transaction.class) &&
                        !target.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Transaction.class)) {
                    return method.invoke(target, args);
                }

                Connection connection = null;
                boolean autoCommit = false;

                try {
                    connection = ConnectionUtil.getConnection();
                    autoCommit = connection.getAutoCommit();

                    if (autoCommit) {
                        connection.setAutoCommit(false);
                    }

                    Object result = method.invoke(target, args);

                    if (autoCommit) {
                        connection.commit();
                    }

                    return result;
                } catch (Exception e) {
                    if (connection != null) {
                        connection.rollback();
                    }
                    throw new RuntimeException(e);
                } finally {
                    if (connection != null) {
                        connection.setAutoCommit(autoCommit);
                    }
                    ConnectionUtil.release(connection);
                }
            }
        });
    }

}
