package org.xinhua.example.mapper;

import java.util.List;
import org.xinhua.example.model.MgVideo;

public interface MgVideoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MgVideo row);

    MgVideo selectByPrimaryKey(Long id);

    List<MgVideo> selectAll();

    int updateByPrimaryKey(MgVideo row);
}