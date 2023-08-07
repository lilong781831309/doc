package org.xinhua.example.spring.mybatisplus.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xinhua.example.spring.mybatisplus.model.dto.PageQuery;
import org.xinhua.example.spring.mybatisplus.model.po.MgVideoNum;
import org.xinhua.example.spring.mybatisplus.model.vo.MgVideoNumVo;
import org.xinhua.example.spring.mybatisplus.model.vo.PageVo;
import org.xinhua.example.spring.mybatisplus.model.vo.ResultEntity;
import org.xinhua.example.spring.mybatisplus.service.MgVideoNumService;
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
@RequestMapping(value = "/mg-video-num/{pid}", produces = "application/json;charset=utf-8")
@RequiredArgsConstructor
public class MgVideoNumController {

    private final MgVideoNumService mgVideoNumService;

    @GetMapping("/{id}")
    public ResultEntity<MgVideoNumVo> query(@PathVariable("pid") Long pid,
                                            @PathVariable("id") Long id) {
        MgVideoNum mgVideoNum = mgVideoNumService.lambdaQuery()
                .eq(MgVideoNum::getPid, pid)
                .eq(MgVideoNum::getId, id)
                .getEntity();
        MgVideoNumVo mgVideoNumVo = BeanUtil.copyProperties(mgVideoNum, MgVideoNumVo.class);
        return ResultUtil.success(mgVideoNumVo);
    }

    @GetMapping("/page/{videoId}")
    public ResultEntity<Page<MgVideoNumVo>> pageQuery(PageQuery pageQuery,
                                                      @PathVariable("pid") Long pid,
                                                      @PathVariable("videoId") Long videoId) {
        Page<MgVideoNum> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        if (!StringUtils.isEmpty(pageQuery.getOrderBy())) {
            page.addOrder(new OrderItem(pageQuery.getOrderBy(), pageQuery.isAsc()));
        }

        LambdaQueryWrapper<MgVideoNum> queryWrapper = new LambdaQueryWrapper<MgVideoNum>()
                .eq(MgVideoNum::getPid, pid)
                .eq(MgVideoNum::getVideoId, videoId);

        mgVideoNumService.page(page, queryWrapper);

        PageVo<MgVideoNumVo> pageVo = PageVo.of(
                page.getCurrent(),
                page.getPages(),
                page.getSize(),
                page.getTotal(),
                BeanUtil.copyToList(page.getRecords(), MgVideoNumVo.class));

        return ResultUtil.success(pageVo);
    }


}
