package org.xinhua.example.design_pattern.structural.composite;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 1:11
 * @Description: 文件目录类
 * @Version: 1.0
 */
public abstract class Directory {

    protected String name;

    protected int level;

    public Directory(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public abstract void show();

}
