package org.xinhua.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.xinhua.example.mapper.MgAppMapper;
import org.xinhua.example.model.MgApp;
import org.xinhua.example.service.MgAppService;
import org.xinhua.example.util.SqlSessionUtil;

import java.util.List;

public class MgAppServiceImpl implements MgAppService {

    @Override
    public MgApp getById(Long id) {
        MgAppMapper mapper = SqlSessionUtil.getSqlSession().getMapper(MgAppMapper.class);
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MgApp> getAll() {
        MgAppMapper mapper = SqlSessionUtil.getSqlSession().getMapper(MgAppMapper.class);
        return mapper.selectAll();
    }

    @Override
    public PageInfo<MgApp> getPage(int pageNum, int pageSize) {
        MgAppMapper mapper = SqlSessionUtil.getSqlSession().getMapper(MgAppMapper.class);
        Page<MgApp> page = PageHelper.startPage(1, 2);
        mapper.selectAll();
        return page.toPageInfo();
    }

    @Override
    public MgApp insert(MgApp mgApp) {
        MgAppMapper mapper = SqlSessionUtil.getSqlSession().getMapper(MgAppMapper.class);
        mapper.insert(mgApp);
        return mgApp;
    }

    @Override
    public int update(MgApp mgApp) {
        MgAppMapper mapper = SqlSessionUtil.getSqlSession().getMapper(MgAppMapper.class);
        return mapper.updateByPrimaryKey(mgApp);
    }

    @Override
    public int delete(Long id) {
        MgAppMapper mapper = SqlSessionUtil.getSqlSession().getMapper(MgAppMapper.class);
        return mapper.deleteByPrimaryKey(id);
    }

}
