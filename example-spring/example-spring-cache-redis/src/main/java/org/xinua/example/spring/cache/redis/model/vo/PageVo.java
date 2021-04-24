package org.xinua.example.spring.cache.redis.model.vo;

import java.io.Serializable;
import java.util.List;

public class PageVo<T> implements Serializable {
    private static final long serialVersionUID = -6141797884557648906L;

    private int totalPages;
    private long totalElements;
    private List<T> content;

    public PageVo(int totalPages, long totalElements, List<T> content) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
