package org.xinhua.example.spring.rest.template.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.xinhua.example.spring.rest.template.exception.WechatException;
import org.xinhua.example.spring.rest.template.util.JacksonUtil;

import java.io.IOException;

public class WechatResponseErrorHandler extends DefaultResponseErrorHandler {

    public boolean hasError(ClientHttpResponse response) throws IOException {
        checkResponse(getResponseBody(response));

        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return (statusCode != null ? hasError(statusCode) : hasError(rawStatusCode));
    }

    private void checkResponse(byte[] body) {
        JsonNode jsonNode = JacksonUtil.jsonToNode(body);
        if (jsonNode != null) {
            int errcode = JacksonUtil.getInt(jsonNode, "errcode");
            if (errcode != 0) {
                String errmsg = JacksonUtil.getText(jsonNode, "errmsg");
                throw new WechatException(errcode, errmsg);
            }
        }
    }

}