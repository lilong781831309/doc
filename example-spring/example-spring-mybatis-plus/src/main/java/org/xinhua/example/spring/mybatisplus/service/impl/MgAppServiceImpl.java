package org.xinhua.example.spring.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xinhua.example.spring.mybatisplus.mapper.MgAppMapper;
import org.xinhua.example.spring.mybatisplus.model.dto.AccessToken;
import org.xinhua.example.spring.mybatisplus.model.po.MgApp;
import org.xinhua.example.spring.mybatisplus.service.MgAppService;
import org.xinhua.example.spring.mybatisplus.service.WechatService;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
@Service
@RequiredArgsConstructor
public class MgAppServiceImpl extends ServiceImpl<MgAppMapper, MgApp> implements MgAppService {

    private final WechatService wechatService;

    @Override
    public boolean deleteById(Serializable id) {
        MgApp mgApp = lambdaQuery()
                .eq(MgApp::getId, id)
                .select(MgApp::getVersion)
                .one();

        return lambdaUpdate()
                .eq(MgApp::getId, id)
                .set(MgApp::getDeleted, id)
                .update(mgApp);
    }

    @Override
    public String getAccessToken(Long id) {
        MgApp mgApp = getById(id);
        LocalDateTime expiresTime = mgApp.getExpiresTime();
        if (expiresTime.isBefore(LocalDateTime.now().minusMinutes(5))) {
            AccessToken accessToken = wechatService.getAccessToken(mgApp.getAppId(), mgApp.getAppSecret());
            mgApp.setAccessToken(accessToken.getAccess_token());
            mgApp.setExpiresTime(LocalDateTime.now().plusSeconds(accessToken.getExpires_in()));
            save(mgApp);
        }
        return mgApp.getAccessToken();
    }

}
