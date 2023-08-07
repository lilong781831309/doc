package org.xinhua.example.spring.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xinhua.example.spring.mybatisplus.mapper.MgVideoMapper;
import org.xinhua.example.spring.mybatisplus.model.dto.ListMediaResponse;
import org.xinhua.example.spring.mybatisplus.model.dto.MediaInfo;
import org.xinhua.example.spring.mybatisplus.model.po.MgApp;
import org.xinhua.example.spring.mybatisplus.model.po.MgVideo;
import org.xinhua.example.spring.mybatisplus.model.po.MgVideoNum;
import org.xinhua.example.spring.mybatisplus.service.MgAppService;
import org.xinhua.example.spring.mybatisplus.service.MgVideoService;
import org.xinhua.example.spring.mybatisplus.service.WechatService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lilong
 * @since 2023-08-06 01:44
 */
@Service
@RequiredArgsConstructor
public class MgVideoServiceImpl extends ServiceImpl<MgVideoMapper, MgVideo> implements MgVideoService {
    private static final Pattern pattern = Pattern.compile(".*第([0-9]+)集.*");
    private final WechatService wechatService;
    private final MgAppService mgAppService;

    @Override
    public boolean delete(Long pid, Long videoId) {
        MgVideo mgVideo = lambdaQuery()
                .eq(MgVideo::getPid, pid)
                .eq(MgVideo::getId, videoId)
                .select(MgVideo::getVersion)
                .one();

        return lambdaUpdate()
                .eq(MgVideo::getPid, pid)
                .eq(MgVideo::getId, videoId)
                .set(MgVideo::getDeleted, videoId)
                .update(mgVideo);
    }

    @Override
    public boolean sync(Long pid, Long videoId) {
        MgVideo mgVideo = lambdaQuery()
                .eq(MgVideo::getPid, pid)
                .eq(MgVideo::getId, videoId)
                .select(MgVideo::getDramaId)
                .one();

        Long dramaId = mgVideo.getDramaId();
        String accessToken = mgAppService.getAccessToken(pid);
        ListMediaResponse response = wechatService.listMedia(accessToken, dramaId, null, null, null, null, null);
        List<MediaInfo> mediaInfoList = response.getMedia_info_list();

        Map<Long, MediaInfo> mediaInfoMap = mediaInfoList.stream()
                .collect(Collectors.toMap(MediaInfo::getMedia_id, MediaInfo -> MediaInfo));

        List<Long> mediaIdList = mediaInfoList.stream()
                .map(MediaInfo::getMedia_id).collect(Collectors.toList());

        List<MgVideoNum> list = Db.list(new LambdaQueryWrapper<MgVideoNum>()
                .in(MgVideoNum::getMediaId, mediaIdList));

        list.forEach(mgVideoNum -> mgVideoNum.setMp4Url(mediaInfoMap.get(mgVideoNum.getMediaId()).getMp4_url()));

        Map<String, MgVideoNum> existMap = list.stream()
                .collect(Collectors.toMap(v -> v.getPid() + ":" + v.getMediaId(), v -> v));

        List<Long> pidList = Db.listObjs(new LambdaQueryWrapper<MgApp>()
                .select(MgApp::getId), (SFunction<MgApp, Long>) MgApp::getId);


        for (Long appId : pidList) {
            for (MediaInfo mediaInfo : mediaInfoList) {
                String k = appId + ":" + mediaInfo.getMedia_id();
                if (!existMap.containsKey(k)) {
                    MgVideoNum mgVideoNum = new MgVideoNum();
                    mgVideoNum.setPid(appId);
                    mgVideoNum.setVideoId(videoId);
                    mgVideoNum.setVideoNum(matchNum(mediaInfo.getName()));
                    mgVideoNum.setMediaId(mediaInfo.getMedia_id());
                    mgVideoNum.setMp4Url(mediaInfo.getMp4_url());
                    mgVideoNum.setExpiresTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(mediaInfo.getExpire_time()), ZoneId.systemDefault()));
                    list.add(mgVideoNum);
                }
            }
        }

        return Db.saveOrUpdateBatch(list, 1000);
    }

    private int matchNum(String name) {
        Matcher matcher = pattern.matcher(name);
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }

}
