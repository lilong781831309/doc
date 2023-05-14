package org.xinhua.example.design_pattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 1:12
 * @Description: 文件夹
 * @Version: 1.0
 */
public class Folder extends Directory {

    private List<Directory> childs = new ArrayList<>();

    public Folder(String name, int level) {
        super(name, level);
    }

    public void addChild(Directory directory) {
        childs.add(directory);
    }

    @Override
    public void show() {
        for (int i = 0; i < level; i++) {
            System.out.print("--");
        }
        System.out.println(name);
        childs.forEach(child -> child.show());
    }
}
