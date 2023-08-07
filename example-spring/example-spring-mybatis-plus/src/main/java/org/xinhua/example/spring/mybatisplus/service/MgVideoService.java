package org.xinhua.example.spring.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xinhua.example.spring.mybatisplus.model.po.MgVideo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
public interface MgVideoService extends IService<MgVideo> {

    boolean delete(Long pid, Long videoId);

    boolean sync(Long pid, Long videoId);
}
