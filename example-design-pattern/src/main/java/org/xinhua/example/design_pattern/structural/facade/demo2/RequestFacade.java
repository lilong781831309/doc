package org.xinhua.example.design_pattern.structural.facade.demo2;

import java.util.Enumeration;

/**
 * @Author: lilong
 * @createDate: 2023/4/10 2:14
 * @Description: Request 门面
 *               隐藏部分属性和方法
 * @Version: 1.0
 */
public class RequestFacade implements ServletRequest {

    private Request request;

    public RequestFacade(Request request) {
        this.request = request;
    }

    @Override
    public Object getAttribute(String name) {
        return this.request.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return this.request.getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return this.request.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String encoding) {
        this.request.setCharacterEncoding(encoding);
    }

    @Override
    public int getContentLength() {
        return this.request.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return this.request.getContentLengthLong();
    }

    @Override
    public String getContentType() {
        return this.request.getContentType();
    }
}
