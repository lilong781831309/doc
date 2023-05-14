package org.xinhua.example.design_pattern.structural.facade.demo1;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 1:59
 * @Description: 门面
 * @Version: 1.0
 */
public class ServiceFacade {

    ServiceA serviceA = new ServiceA();
    ServiceB serviceB = new ServiceB();
    ServiceC serviceC = new ServiceC();

    public void execute1() {
        serviceA.execute1();
    }

    public void execute2() {
        serviceA.execute2();
    }

    public void execute3() {
        serviceA.execute3();
    }

    public void method4() {
        serviceB.method4();
    }

    public void method5() {
        serviceB.method5();
    }

    public void method6() {
        serviceB.method6();
    }

    public void function7() {
        serviceC.function7();
    }

    public void function8() {
        serviceC.function8();
    }

    public void function9() {
        serviceC.function9();
    }

}
