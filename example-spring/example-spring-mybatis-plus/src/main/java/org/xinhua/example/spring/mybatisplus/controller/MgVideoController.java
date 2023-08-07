package org.xinhua.example.spring.mybatisplus.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.xinhua.example.spring.mybatisplus.model.dto.MgVideoDTO;
import org.xinhua.example.spring.mybatisplus.model.dto.PageQuery;
import org.xinhua.example.spring.mybatisplus.model.po.MgVideo;
import org.xinhua.example.spring.mybatisplus.model.vo.MgVideoVo;
import org.xinhua.example.spring.mybatisplus.model.vo.PageVo;
import org.xinhua.example.spring.mybatisplus.model.vo.ResultEntity;
import org.xinhua.example.spring.mybatisplus.service.MgVideoService;
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
@RequestMapping(value = "/mg-video/{pid}", produces = "application/json;charset=utf-8")
@RequiredArgsConstructor
public class MgVideoController {

    private final MgVideoService mgVideoService;

    @GetMapping("/{videoId}")
    public ResultEntity<MgVideoVo> query(@PathVariable("pid") Long pid,
                                         @PathVariable("videoId") Long videoId) {
        MgVideo mgVideo = mgVideoService.lambdaQuery()
                .eq(MgVideo::getPid, pid)
                .eq(MgVideo::getId, videoId)
                .getEntity();
        MgVideoVo mgVideoVo = BeanUtil.copyProperties(mgVideo, MgVideoVo.class);
        return ResultUtil.success(mgVideoVo);
    }

    @GetMapping("/page")
    public ResultEntity<Page<MgVideoVo>> pageQuery(PageQuery pageQuery,
                                                   @PathVariable("pid") Long pid) {
        Page<MgVideo> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        if (!StringUtils.isEmpty(pageQuery.getOrderBy())) {
            page.addOrder(new OrderItem(pageQuery.getOrderBy(), pageQuery.isAsc()));
        }

        LambdaQueryWrapper<MgVideo> queryWrapper = new LambdaQueryWrapper<MgVideo>()
                .eq(MgVideo::getPid, pid);

        mgVideoService.page(page, queryWrapper);

        PageVo<MgVideoVo> pageVo = PageVo.of(
                page.getCurrent(),
                page.getPages(),
                page.getSize(),
                page.getTotal(),
                BeanUtil.copyToList(page.getRecords(), MgVideoVo.class));

        return ResultUtil.success(pageVo);
    }

    @PostMapping("/")
    public ResultEntity<MgVideoVo> insert(@PathVariable("pid") Long pid,
                                          @RequestBody MgVideoDTO mgVideoDTO) {
        MgVideo mgVideo = BeanUtil.copyProperties(mgVideoDTO, MgVideo.class);
        mgVideo.setPid(pid);
        mgVideo.setVersion(0);
        if (!mgVideoService.save(mgVideo)) {
            return ResultUtil.fail();
        }
        MgVideoVo mgVideoVo = BeanUtil.copyProperties(mgVideo, MgVideoVo.class);
        return ResultUtil.success(mgVideoVo);
    }

    @PutMapping("/{videoId}")
    public ResultEntity<MgVideoVo> update(@PathVariable("pid") Long pid,
                                          @PathVariable("videoId") Long videoId,
                                          @RequestBody MgVideoDTO mgVideoDTO) {
        MgVideo mgVideo = BeanUtil.copyProperties(mgVideoDTO, MgVideo.class);
        mgVideo.setPid(pid);
        mgVideo.setId(videoId);
        if (!mgVideoService.updateById(mgVideo)) {
            return ResultUtil.fail();
        }
        mgVideo = mgVideoService.getById(videoId);
        MgVideoVo mgVideoVo = BeanUtil.copyProperties(mgVideo, MgVideoVo.class);
        return ResultUtil.success(mgVideoVo);
    }

    @DeleteMapping("/{videoId}")
    public ResultEntity delete(@PathVariable("pid") Long pid, @PathVariable("videoId") Long videoId) {
        boolean delete = mgVideoService.delete(pid, videoId);
        return ResultUtil.status(delete);
    }

    @PostMapping("/sync/{videoId}")
    public ResultEntity sync(@PathVariable("pid") Long pid, @PathVariable("videoId") Long videoId) {
        boolean sync = mgVideoService.sync(pid, videoId);
        return ResultUtil.status(sync);
    }

}
