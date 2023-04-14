package org.xinhua.example.spring.mvc.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
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

}
