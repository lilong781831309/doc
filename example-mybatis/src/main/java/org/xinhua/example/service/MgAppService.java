package org.xinhua.example.service;

import com.github.pagehelper.PageInfo;
import org.xinhua.example.model.MgApp;

import java.util.List;

public interface MgAppService {

    MgApp getById(Long id);

    List<MgApp> getAll();

    PageInfo<MgApp> getPage(int pageNum, int pageSize);

    MgApp insert(MgApp mgApp);

    int update(MgApp mgApp);

    int delete(Long id);

}
