package org.xinhua.example.mapper;

import org.xinhua.example.model.po.MgVideo;

public interface MgVideoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MgVideo row);

    int insertSelective(MgVideo row);

    MgVideo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MgVideo row);

    int updateByPrimaryKey(MgVideo row);
}