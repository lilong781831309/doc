package org.xinhua.example.spring.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xinhua.example.spring.mybatisplus.model.po.MgUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
@Mapper
public interface MgUserMapper extends BaseMapper<MgUser> {

}
