package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MediaPlaybackInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long media_id;      //	number	媒资文件id。
    private Long duration;      //	number	播放时长，单位：秒。
    private String name;        //	string	媒资文件名。
    private String description; //	string	描述。
    private String cover_url;   //	string	封面图临时链接。
    private String mp4_url;     //	string	mp4格式临时链接 。
    private String hls_url;     //	string	hls格式临时链接。

}
