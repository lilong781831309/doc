package org.xinhua.example.design_pattern.structural.proxy._cglib;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 16:43
 * @Description: 代理对象创建工厂
 * @Version: 1.0
 */
public class LogProxy {

    public static <T> T newProxyInstance(T target) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:/temp/");
        //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
        Enhancer enhancer = new Enhancer();
        //设置父类的字节码对象
        enhancer.setSuperclass(target.getClass());
        //设置回调函数
        enhancer.setCallback(new LogInterceptor());
        //创建代理对象
        return (T) enhancer.create();
    }

    static class LogInterceptor implements MethodInterceptor {

        /**
         *
         * @param obj           代理对象
         * @param method        真实对象中的方法的 Method 实例
         * @param args          实际参数
         * @param methodProxy   代理对象中的方法的 MethodProxy 实例
         * @return
         * @throws Throwable
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object result = null;
            try {
                before(args);
                result = methodProxy.invokeSuper(obj, args);
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

    }

}
