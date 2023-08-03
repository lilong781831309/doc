package org.xinhua.example.mapper;

import java.util.List;
import org.xinhua.example.model.MgVideoNum;

public interface MgVideoNumMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MgVideoNum row);

    MgVideoNum selectByPrimaryKey(Long id);

    List<MgVideoNum> selectAll();

    int updateByPrimaryKey(MgVideoNum row);
}