package org.xinhua.example.mapper;

import org.xinhua.example.model.po.MgVideoNum;

public interface MgVideoNumMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MgVideoNum row);

    int insertSelective(MgVideoNum row);

    MgVideoNum selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MgVideoNum row);

    int updateByPrimaryKey(MgVideoNum row);
}