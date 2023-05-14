package org.xinhua.example.design_pattern.creational.prototype;

/**
 * @Author: lilong
 * @createDate: 2023/4/8 19:38
 * @Description: 测试类
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) throws Exception {
        testDeepClone();
    }

    public static void testClone() {
        ArrayList<City> citys = new ArrayList<>();
        citys.add(new City("长沙"));
        citys.add(new City("湘潭"));
        citys.add(new City("衡阳"));

        Province province = new Province("湖南省", citys);
        Province province2 = (Province) province.clone();

        System.out.println("=========================");
        System.out.println(province);
        System.out.println(province2);

        province2.getCitys().get(0).setName("长沙2");
        province2.getCitys().get(1).setName("湘潭2");
        province2.getCitys().get(2).setName("衡阳2");

        System.out.println("=========================");
        System.out.println(province);
        System.out.println(province2);
    }

    public static void testDeepClone() throws Exception {
        ArrayList<City> citys = new ArrayList<>();
        citys.add(new City("长沙"));
        citys.add(new City("湘潭"));
        citys.add(new City("衡阳"));

        Province province = new Province("湖南省", citys);
        Province province2 = (Province) province.deepClone();

        System.out.println("=========================");
        System.out.println(province);
        System.out.println(province2);

        province2.getCitys().get(0).setName("长沙2");
        province2.getCitys().get(1).setName("湘潭2");
        province2.getCitys().get(2).setName("衡阳2");

        System.out.println("=========================");
        System.out.println(province);
        System.out.println(province2);
    }
}
