package org.xinhua.example.spring.mybatisplus.service.impl;


import org.springframework.stereotype.Service;
import org.xinhua.example.spring.mybatisplus.model.dto.AccessToken;
import org.xinhua.example.spring.mybatisplus.model.dto.ListMediaResponse;
import org.xinhua.example.spring.mybatisplus.model.dto.MediaInfo;
import org.xinhua.example.spring.mybatisplus.model.dto.MediaPlaybackInfo;
import org.xinhua.example.spring.mybatisplus.model.dto.getMediaLinkResponse;
import org.xinhua.example.spring.mybatisplus.service.WechatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WechatServiceImpl2 implements WechatService {

    private Random random = new Random();
    private int n = 0;

    @Override
    public AccessToken getAccessToken(String appId, String appSecret) {
        AccessToken token = new AccessToken();
        token.setAccess_token("accessToken" + System.currentTimeMillis());
        token.setExpires_in(7200);
        return token;
    }

    @Override
    public ListMediaResponse listMedia(String accessToken, Long dramaId, String mediaName, Long startTime, Long endTime, Long limit, Long offset) {
        ListMediaResponse response = new ListMediaResponse();
        List<MediaInfo> media_info_list = new ArrayList<>();
        response.setMedia_info_list(media_info_list);

        int num = randomNum(5, 10);
        for (int i = 0; i <= num; i++) {
            media_info_list.add(newMediaInfo(dramaId));
        }
        return response;
    }

    private MediaInfo newMediaInfo(Long dramaId) {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setMedia_id(System.currentTimeMillis() * 1000 + getNextN());
        mediaInfo.setExpire_time(System.currentTimeMillis() + 2 * 60 * 60 * 1000);
        mediaInfo.setDrama_id(dramaId);
        mediaInfo.setName("dramaId 第" + n + "集");
        mediaInfo.setMp4_url("http://" + System.currentTimeMillis());
        return mediaInfo;
    }

    private int randomNum(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    private int getNextN() {
        if (n == 999) {
            n = 0;
        }
        return n++;
    }

    @Override
    public getMediaLinkResponse getMediaLink(String accessToken, Long mediaId, Integer t, String us, Integer expr, Integer rlimit, String whref, String bkref) {
        getMediaLinkResponse response = new getMediaLinkResponse();
        MediaPlaybackInfo media_info = new MediaPlaybackInfo();
        media_info.setMedia_id(mediaId);
        media_info.setMp4_url("http://" + System.currentTimeMillis() * 1000 + getNextN());
        response.setMedia_info(media_info);
        return response;
    }

}
