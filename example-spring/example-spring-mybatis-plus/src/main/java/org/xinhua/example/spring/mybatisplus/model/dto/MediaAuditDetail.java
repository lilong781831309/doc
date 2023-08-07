package org.xinhua.example.spring.mybatisplus.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MediaAuditDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * number	0为无效值；1为审核中；2为审核驳回；3为审核通过；4为驳回重填。需要注意可能存在单个剧集的状态为审核通过，但是剧目整体是未通过的情况，而能不能获取播放链接取决于剧目的审核状态。
     */
    private Integer status;

    /**
     * number	提审时间戳。
     */
    private Long create_time;

    /**
     * number	审核时间戳。
     */
    private Long audit_time;

    /**
     * string	审核备注。该值可能为空。
     */
    private String reason;

    /**
     * array<string>	审核证据截图id列表，截图id可以用作get_material接口的参数来获得截图内容。
     */
    private List<String> evidence_material_id_list;

}
