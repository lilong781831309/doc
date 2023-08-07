package org.xinhua.example.spring.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xinhua.example.spring.mybatisplus.model.po.MgApp;

import java.io.Serializable;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
public interface MgAppService extends IService<MgApp> {

    boolean deleteById(Serializable id);

    String getAccessToken(Long id);

}
