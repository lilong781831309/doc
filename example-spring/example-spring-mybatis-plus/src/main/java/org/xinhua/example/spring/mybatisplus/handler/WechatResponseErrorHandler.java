package org.xinhua.example.spring.mybatisplus.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.xinhua.example.spring.mybatisplus.exception.WechatException;
import org.xinhua.example.spring.mybatisplus.util.JacksonUtil;

import java.io.IOException;

public class WechatResponseErrorHandler extends DefaultResponseErrorHandler {

    public boolean hasError(ClientHttpResponse response) throws IOException {
        checkResponse(getResponseBody(response));

        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return (statusCode != null ? hasError(statusCode) : hasError(rawStatusCode));
    }

    private void checkResponse(byte[] body) {
        JsonNode json = JacksonUtil.jsonToNode(new String(body));
        if (json != null) {
            Integer errcode = json.get("errcode").asInt();
            if (errcode != null && errcode != 0) {
                String errmsg = json.get("errmsg").asText();
                throw new WechatException(errcode, errmsg);
            }
        }
    }

}