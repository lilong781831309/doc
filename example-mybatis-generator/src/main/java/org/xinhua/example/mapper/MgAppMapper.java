package org.xinhua.example.mapper;

import org.xinhua.example.model.po.MgApp;

public interface MgAppMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(MgApp row);

    int insertSelective(MgApp row);

    MgApp selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(MgApp row);

    int updateByPrimaryKey(MgApp row);
}