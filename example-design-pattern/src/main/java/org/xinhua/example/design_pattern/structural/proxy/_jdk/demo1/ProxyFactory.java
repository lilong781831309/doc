package org.xinhua.example.design_pattern.structural.proxy._jdk.demo1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 16:16
 * @Description: 代理类, 记录日志
 * @Version: 1.0
 */
public class ProxyFactory {

    public static <T> T newProxyInstance(T target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result = null;
                try {
                    before(args);
                    result = method.invoke(target, args);
                    after(result);
                } catch (Exception e) {
                    afterThrowing(e);
                    throw e;
                } finally {
                    afterReturning(result);
                }
                return result;
            }

            private void before(Object[] args) {
                System.out.println("before: args = " + Arrays.toString(args));
            }

            private void after(Object result) {
                System.out.println("after: result = " + result);
            }

            private void afterThrowing(Exception e) {
                System.out.println("afterThrowing: e = " + e.getMessage());
            }

            private void afterReturning(Object result) {
                System.out.println("afterReturning: result = " + result);
            }

        });
    }

}
