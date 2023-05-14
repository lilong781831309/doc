package org.xinhua.example.design_pattern.structural.composite;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 1:12
 * @Description: 文件
 * @Version: 1.0
 */
public class File extends Directory {

    public File(String name, int level) {
        super(name, level);
    }

    @Override
    public void show() {
        for (int i = 0; i < level; i++) {
            System.out.print("--");
        }
        System.out.println(name);
    }

}
