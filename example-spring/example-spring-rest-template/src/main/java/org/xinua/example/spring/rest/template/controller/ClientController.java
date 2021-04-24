package org.xinua.example.spring.rest.template.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.xinua.example.spring.rest.template.model.ImageMaterial;
import org.xinua.example.spring.rest.template.model.Material;
import org.xinua.example.spring.rest.template.model.MaterialListResponse;
import org.xinua.example.spring.rest.template.model.NewsMaterial;
import org.xinua.example.spring.rest.template.model.VideoMaterial;
import org.xinua.example.spring.rest.template.model.VoiceMaterial;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Qualifier("loggingRestTemplate")
    @Autowired
    private RestTemplate loggingRestTemplate;

    @Qualifier("wechatRestTemplate")
    @Autowired
    private RestTemplate wechatRestTemplate;

    @Value("${server.port}")
    private int port;

    private String serverUrl;

    @PostConstruct
    private void setServerUrl() {
        serverUrl = "http://localhost:" + port + "/server";
    }

    @GetMapping("/get/kv/string")
    public Object get_kv_string() {
        String url = serverUrl + "/get/kv/string";
        url += "?media_id=a1b2c3";
        url += "&update_time=1970-01-01";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));

        HttpEntity httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> exchange = loggingRestTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        logger.warn("code: {}", exchange.getStatusCodeValue());
        logger.warn("body: {}", exchange.getBody());

        return exchange.getBody();
    }

    @GetMapping("/get/kv/json")
    public Object get_kv_json() {
        String url = serverUrl + "/get/kv/json";
        url += "?media_id=a1b2c3";
        url += "&update_time=1970-01-01";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));

        HttpEntity httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ObjectNode> exchange = loggingRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ObjectNode.class);

        logger.warn("code: {}", exchange.getStatusCodeValue());
        logger.warn("body: {}", exchange.getBody());

        return exchange.getBody();
    }

    @GetMapping("/post/kv/object")
    public Object post_kv_object() {
        String url = serverUrl + "/post/kv/object";
        url += "?media_id=a1b2c3";
        url += "&update_time=1970-01-01";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));

        HttpEntity httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Material> exchange = loggingRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Material.class);

        logger.warn("code: {}", exchange.getStatusCodeValue());
        logger.warn("body: {}", exchange.getBody());

        return exchange.getBody();
    }

    @GetMapping("/post/body/array")
    public Object post_body_array() {
        String url = serverUrl + "/post/body/array";

        List<Material> list = new ArrayList<>();
        Material material = null;

        material = new Material();
        material.setMedia_id("11");
        material.setUpdate_time(new Date().toString());
        list.add(material);

        material = new Material();
        material.setMedia_id("22");
        material.setUpdate_time(new Date().toString());
        list.add(material);

        material = new Material();
        material.setMedia_id("33");
        material.setUpdate_time(new Date().toString());
        list.add(material);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));

        HttpEntity httpEntity = new HttpEntity<>(list.toArray(), headers);

        ResponseEntity<String[]> exchange = loggingRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String[].class);

        logger.warn("code: {}", exchange.getStatusCodeValue());
        logger.warn("body: {}", exchange.getBody());

        return exchange.getBody();
    }

    @GetMapping("/post/body/list")
    public Object post_body_list() {
        String url = serverUrl + "/post/body/list";

        List<Material> list = new ArrayList<>();
        Material material = null;

        material = new Material();
        material.setMedia_id("aa");
        material.setUpdate_time(new Date().toString());
        list.add(material);

        material = new Material();
        material.setMedia_id("bb");
        material.setUpdate_time(new Date().toString());
        list.add(material);

        material = new Material();
        material.setMedia_id("cc");
        material.setUpdate_time(new Date().toString());
        list.add(material);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));

        HttpEntity httpEntity = new HttpEntity<>(list, headers);

        ResponseEntity<List<Material>> exchange = loggingRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
                new ParameterizedTypeReference<List<Material>>() {
                });

        logger.warn("code: {}", exchange.getStatusCodeValue());
        logger.warn("body: {}", exchange.getBody());

        return exchange.getBody();
    }

    @GetMapping("/post/kv/parameterized")
    public Object post_body_parameterized(String type, @RequestParam(defaultValue = "0") int errcode, @RequestParam(defaultValue = "") String errmsg) {
        logger.warn("type:{},errcode:{},errmsg:{}", type, errcode, errmsg);

        String url = serverUrl + "/post/kv/parameterized";
        url += "?type=" + type;
        url += "&errcode=" + errcode;
        url += "&errmsg=" + errmsg;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity httpEntity = new HttpEntity<>(headers);

        MaterialListResponse<? extends Material> body = null;

        switch (type) {
            case "news":
                body = exchangeAsResponse(url, httpEntity, new ParameterizedTypeReference<MaterialListResponse<NewsMaterial>>() {});
                break;
            case "image":
                body = exchangeAsResponse(url, httpEntity, new ParameterizedTypeReference<MaterialListResponse<ImageMaterial>>() {});
                break;
            case "video":
                body = exchangeAsResponse(url, httpEntity, new ParameterizedTypeReference<MaterialListResponse<VideoMaterial>>() {});
                break;
            case "voice":
                body = exchangeAsResponse(url, httpEntity, new ParameterizedTypeReference<MaterialListResponse<VoiceMaterial>>() {});
                break;
            default:
                body = exchangeAsResponse(url, httpEntity, new ParameterizedTypeReference<MaterialListResponse<Material>>() {});
        }

        logger.warn("body:{}", body);
        return body;
    }

    private <T> MaterialListResponse<T> exchangeAsResponse(String url, HttpEntity httpEntity, ParameterizedTypeReference<MaterialListResponse<T>> responseType) {
        ResponseEntity<MaterialListResponse<T>> exchange = wechatRestTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType);
        logger.warn("code:{}", exchange.getStatusCodeValue());

        return exchange.getBody();
    }

}
