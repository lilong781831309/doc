package org.xinhua.example.design_pattern.structural.composite;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 1:44
 * @Description: 测试
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        Folder root = new Folder("D盘", 1);

        Folder movieFolder = new Folder("电影", 2);
        movieFolder.addChild(new File("肖申克的救赎.mp4",3));
        movieFolder.addChild(new File("拯救大兵瑞恩.mp4",3));
        movieFolder.addChild(new File("xxx.mp4",3));

        Folder bookFolder = new Folder("电子书", 2);
        bookFolder.addChild(new File("thinking java.mp4",3));
        bookFolder.addChild(new File("锋利jquery.pdf",3));
        bookFolder.addChild(new File("深入理解java虚拟机.pdf",3));

        Folder musicFolder = new Folder("音乐", 2);
        musicFolder.addChild(new File("千千阙歌.mp3",3));
        musicFolder.addChild(new File("千年等一回.mp3",3));
        musicFolder.addChild(new File("逆流而上.mp3",3));

        root.addChild(movieFolder);
        root.addChild(bookFolder);
        root.addChild(musicFolder);

        root.show();
    }
}
