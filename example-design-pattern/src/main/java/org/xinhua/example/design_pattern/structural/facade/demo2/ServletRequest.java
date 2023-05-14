package org.xinhua.example.design_pattern.structural.facade.demo2;

import java.util.Enumeration;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 2:08
 * @Description: 请求接口
 * @Version: 1.0
 */
public interface ServletRequest {

    Object getAttribute(String name);

    Enumeration<String> getAttributeNames();

    String getCharacterEncoding();

    void setCharacterEncoding(String encoding);

    int getContentLength();

    long getContentLengthLong();

    String getContentType();

}