package org.xinhua.example.mapper;

import java.util.List;
import org.xinhua.example.model.MgApp;

public interface MgAppMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(MgApp row);

    MgApp selectByPrimaryKey(Long pid);

    List<MgApp> selectAll();

    int updateByPrimaryKey(MgApp row);
}