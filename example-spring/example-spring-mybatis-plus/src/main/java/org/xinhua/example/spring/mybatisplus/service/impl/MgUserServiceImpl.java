package org.xinhua.example.spring.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xinhua.example.spring.mybatisplus.mapper.MgUserMapper;
import org.xinhua.example.spring.mybatisplus.model.po.MgUser;
import org.xinhua.example.spring.mybatisplus.service.MgUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
@Service
public class MgUserServiceImpl extends ServiceImpl<MgUserMapper, MgUser> implements MgUserService {

}
