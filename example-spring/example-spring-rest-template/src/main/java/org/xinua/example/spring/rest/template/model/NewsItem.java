package org.xinua.example.spring.rest.template.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class NewsItem implements Serializable {

    /**
     * 图文消息的标题
     */
    private String title;

    /**
     * 图文消息的封面图片素材id（必须是永久mediaID）
     */
    private String thumb_media_id;

    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    private String show_cover_pic;

    /**
     * 作者
     */
    private String author;

    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
     */
    private String digest;

    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
     */
    private String content;

    /**
     * 图文页的URL
     */
    private String url;

    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    private String content_source_url;

    /**
     * 封面图url
     */
    private String thumb_url;

    /**
     * 是否允许评论
     */
    private String need_open_comment;

    /**
     * 是否只允许粉丝评论
     */
    private String only_fans_can_comment;

}
