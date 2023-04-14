package org.xinhua.example.spring.rest.template.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xinhua.example.spring.rest.template.model.Content;
import org.xinhua.example.spring.rest.template.model.ImageMaterial;
import org.xinhua.example.spring.rest.template.model.Material;
import org.xinhua.example.spring.rest.template.model.MaterialListResponse;
import org.xinhua.example.spring.rest.template.model.NewsItem;
import org.xinhua.example.spring.rest.template.model.NewsMaterial;
import org.xinhua.example.spring.rest.template.model.VideoMaterial;
import org.xinhua.example.spring.rest.template.model.VoiceMaterial;
import org.xinhua.example.spring.rest.template.util.JacksonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/server")
public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    @GetMapping("/get/kv/string")
    public Object get_kv_string(String media_id, String update_time) {
        logger.warn("media_id={},update_time={}", media_id, update_time);
        return "server message";
    }

    @GetMapping("/get/kv/json")
    public Object get_kv_json(Material material) {
        logger.warn("material={}", material);

        ObjectNode objectNode = JacksonUtil.createObjectNode();
        objectNode.put("k1", 1);
        objectNode.put("k2", "v2");
        return objectNode;
    }

    @PostMapping("/post/kv/object")
    public Object post_kv_object(Material material) {
        logger.warn("material={}", material);

        material.setMedia_id("aaabbbccc");
        material.setUpdate_time(new Date().toString());

        return material;
    }

    @PostMapping("/post/body/array")
    public Object post_body_array(@RequestBody Material[] materials) {
        logger.warn("materials={}", Arrays.asList(materials));

        String[] arr = new String[3];
        arr[0] = "a";
        arr[1] = "b";
        arr[2] = "c";

        return arr;
    }

    @PostMapping("/post/body/list")
    public Object post_body_list(@RequestBody List<Material> list) {
        logger.warn("list={}", list);

        list.remove(0);
        return list;
    }

    @PostMapping("/post/kv/parameterized")
    public Object post_body_parameterized(String type, @RequestParam(defaultValue = "0") int errcode, @RequestParam(defaultValue = "") String errmsg) {
        MaterialListResponse response = new MaterialListResponse();
        response.setErrcode(errcode);
        response.setErrmsg(errmsg);
        switch (type) {
            case "news":
                setNewsMaterial(response);
                break;
            case "image":
                setImageMaterial(response);
                break;
            case "video":
                setVideoMaterial(response);
                break;
            case "voice":
                setVoiceMaterial(response);
                break;
            default:
                setMaterial(response);
        }
        return response;
    }

    private void setNewsMaterial(MaterialListResponse response) {
        List<NewsMaterial> newsMaterials = new ArrayList<>();
        newsMaterials.add(creatNewsMaterial(1));
        newsMaterials.add(creatNewsMaterial(2));
        newsMaterials.add(creatNewsMaterial(3));
        response.setItem(newsMaterials);
        response.setItem_count(newsMaterials.size());
        response.setTotal_count(10);
    }

    private NewsMaterial creatNewsMaterial(int i) {
        Random random = new Random();

        NewsMaterial newsMaterial = new NewsMaterial();

        List<NewsItem> news_item = new ArrayList<>();
        news_item.add(creatNewsItem(2 + i));
        news_item.add(creatNewsItem(1 + i));
        news_item.add(creatNewsItem(3 + i));

        Content content = new Content();
        content.setCreate_time(new Date(0).getTime() + random.nextInt(10000));
        content.setUpdate_time(new Date().getTime() - random.nextInt(10000));
        content.setNews_item(news_item);

        newsMaterial.setMedia_id("news media_id" + i);
        newsMaterial.setUpdate_time("news update_time" + i);
        newsMaterial.setContent(content);
        return newsMaterial;
    }

    private NewsItem creatNewsItem(int i) {
        NewsItem newsItem = new NewsItem();
        newsItem.setTitle("title" + i);
        newsItem.setThumb_media_id("thumb_media_id" + i);
        newsItem.setShow_cover_pic("" + i);
        newsItem.setAuthor("author" + i);
        newsItem.setDigest("" + i);
        newsItem.setContent("content" + i);
        newsItem.setUrl("url" + i);
        newsItem.setContent_source_url("content_source_url" + i);
        newsItem.setThumb_url("thumb_url" + i);
        newsItem.setNeed_open_comment("need_open_comment" + i);
        newsItem.setOnly_fans_can_comment("" + i);
        return newsItem;
    }

    private void setImageMaterial(MaterialListResponse response) {
        List<ImageMaterial> imageMaterials = new ArrayList<>();
        imageMaterials.add(creatImageMaterial(1));
        imageMaterials.add(creatImageMaterial(2));
        imageMaterials.add(creatImageMaterial(3));
        response.setItem(imageMaterials);
        response.setItem_count(imageMaterials.size());
        response.setTotal_count(20);
    }

    private ImageMaterial creatImageMaterial(int i) {
        Random random = new Random();
        List<String> tags = new ArrayList<>();
        tags.add("tag" + i + random.nextInt(100));
        tags.add("tag" + i + random.nextInt(500));
        tags.add("tag" + i + random.nextInt(1000));

        ImageMaterial imageMaterial = new ImageMaterial();
        imageMaterial.setName("name" + i);
        imageMaterial.setUrl("url" + i);
        imageMaterial.setTags(tags);

        imageMaterial.setMedia_id("image media_id" + i);
        imageMaterial.setUpdate_time("image update_time" + i);

        return imageMaterial;
    }

    private void setVideoMaterial(MaterialListResponse response) {
        List<VideoMaterial> videoMaterials = new ArrayList<>();
        videoMaterials.add(creatVideoMaterial(1));
        videoMaterials.add(creatVideoMaterial(2));
        videoMaterials.add(creatVideoMaterial(3));
        response.setItem(videoMaterials);
        response.setItem_count(videoMaterials.size());
        response.setTotal_count(100);
    }

    private VideoMaterial creatVideoMaterial(int i) {
        Random random = new Random();
        List<String> tags = new ArrayList<>();
        tags.add("tag" + i + random.nextInt(100));
        tags.add("tag" + i + random.nextInt(500));
        tags.add("tag" + i + random.nextInt(1000));

        VideoMaterial videoMaterial = new VideoMaterial();
        videoMaterial.setName("name" + i);
        videoMaterial.setCover_url("cover_url" + i);
        videoMaterial.setDescription("description" + i);
        videoMaterial.setNewcat("newcat" + i);
        videoMaterial.setVid("vid" + i);
        videoMaterial.setUrl("url" + i);
        videoMaterial.setTags(tags);

        videoMaterial.setMedia_id("video media_id" + i);
        videoMaterial.setUpdate_time("video update_time" + i);

        return videoMaterial;
    }

    private void setVoiceMaterial(MaterialListResponse response) {
        List<VoiceMaterial> voiceMaterials = new ArrayList<>();
        voiceMaterials.add(creatVoiceMaterial(1));
        voiceMaterials.add(creatVoiceMaterial(2));
        voiceMaterials.add(creatVoiceMaterial(3));
        response.setItem(voiceMaterials);
        response.setItem_count(voiceMaterials.size());
        response.setTotal_count(25);
    }

    private VoiceMaterial creatVoiceMaterial(int i) {
        Random random = new Random();
        List<String> tags = new ArrayList<>();
        tags.add("tag" + i + random.nextInt(100));
        tags.add("tag" + i + random.nextInt(500));
        tags.add("tag" + i + random.nextInt(1000));

        VoiceMaterial voiceMaterial = new VoiceMaterial();
        voiceMaterial.setName("name" + i);
        voiceMaterial.setTags(tags);

        voiceMaterial.setMedia_id("voice media_id" + i);
        voiceMaterial.setUpdate_time("voice update_time" + i);

        return voiceMaterial;
    }

    private void setMaterial(MaterialListResponse response) {
        List<Material> materials = new ArrayList<>();
        materials.add(creatMaterial(1));
        materials.add(creatMaterial(2));
        materials.add(creatMaterial(3));
        response.setItem(materials);
        response.setItem_count(materials.size());
        response.setTotal_count(500);
    }

    private Material creatMaterial(int i) {
        Material material = new Material();
        material.setMedia_id("material media_id" + i);
        material.setUpdate_time("material update_time" + i);
        return material;
    }
}
