package org.xinhua.example.spring.mybatisplus.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinhua.example.spring.mybatisplus.model.dto.MgAppDTO;
import org.xinhua.example.spring.mybatisplus.model.dto.PageQuery;
import org.xinhua.example.spring.mybatisplus.model.po.MgApp;
import org.xinhua.example.spring.mybatisplus.model.vo.MgAppVo;
import org.xinhua.example.spring.mybatisplus.model.vo.PageVo;
import org.xinhua.example.spring.mybatisplus.model.vo.ResultEntity;
import org.xinhua.example.spring.mybatisplus.service.MgAppService;
import org.xinhua.example.spring.mybatisplus.util.ResultUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
@RestController
@RequestMapping(value = "/mg-app", produces = "application/json;charset=utf-8")
@RequiredArgsConstructor
public class MgAppController {

    private final MgAppService mgAppService;

    @GetMapping("/{id}")
    public ResultEntity<MgAppVo> query(@PathVariable("id") Long id) {
        MgApp mgApp = mgAppService.getById(id);
        MgAppVo mgAppVo = BeanUtil.copyProperties(mgApp, MgAppVo.class);
        return ResultUtil.success(mgAppVo);
    }

    @GetMapping("/page")
    public ResultEntity<PageVo<MgAppVo>> pageQuery(PageQuery pageQuery) {
        Page<MgApp> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        if (!StringUtils.isEmpty(pageQuery.getOrderBy())) {
            page.addOrder(new OrderItem(pageQuery.getOrderBy(), pageQuery.isAsc()));
        }

        mgAppService.page(page);

        PageVo<MgAppVo> pageVo = PageVo.of(
                page.getCurrent(),
                page.getPages(),
                page.getSize(),
                page.getTotal(),
                BeanUtil.copyToList(page.getRecords(), MgAppVo.class));

        return ResultUtil.success(pageVo);
    }

    @PostMapping("/")
    public ResultEntity<MgAppVo> insert(@RequestBody MgAppDTO mgAppDTO) {
        MgApp mgApp = BeanUtil.copyProperties(mgAppDTO, MgApp.class);
        mgApp.setVersion(0);
        boolean save = mgAppService.save(mgApp);
        if (!save) {
            return ResultUtil.fail();
        }
        MgAppVo mgAppVo = BeanUtil.copyProperties(mgApp, MgAppVo.class);
        return ResultUtil.success(mgAppVo);
    }

    @PutMapping("/{id}")
    public ResultEntity<MgAppVo> update(@PathVariable("id") Long id,
                                        @RequestBody MgAppDTO mgAppDTO) {
        MgApp mgApp = BeanUtil.copyProperties(mgAppDTO, MgApp.class);
        mgApp.setId(id);
        boolean update = mgAppService.updateById(mgApp);
        if (!update) {
            return ResultUtil.fail();
        }
        mgApp = mgAppService.getById(id);
        MgAppVo mgAppVo = BeanUtil.copyProperties(mgApp, MgAppVo.class);
        return ResultUtil.success(mgAppVo);
    }

    @DeleteMapping("/{id}")
    public ResultEntity delete(@PathVariable("id") Long id) {
        boolean delete = mgAppService.deleteById(id);
        return ResultUtil.status(delete);
    }
}
