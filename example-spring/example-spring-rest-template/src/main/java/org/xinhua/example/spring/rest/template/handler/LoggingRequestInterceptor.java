package org.xinhua.example.spring.rest.template.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MimeType;
import org.xinhua.example.spring.rest.template.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);
    private static final List<String> RESOURCE_TYPES;
    private static final List<MediaType> RESOURCE_MEDIA_TYPES;
    private static final List<MediaType> REQUEST_LOGGER_FILTER_TYPES;
    private static final List<MediaType> RESPONSE_LOGGER_FILTER_TYPES;

    private static final int KB = 1024;
    private static final int MB = 1024 * KB;
    private static final int REQUEST_LOGGER_MAX_SIZE = 1 * MB;
    private static final int RESPONSE_LOGGER_MAX_SIZE = 1 * MB;

    static {
        RESOURCE_TYPES = new ArrayList<>();
        RESOURCE_TYPES.add("image");
        RESOURCE_TYPES.add("video");
        RESOURCE_TYPES.add("audio");

        RESOURCE_MEDIA_TYPES = new ArrayList<>();
        RESOURCE_MEDIA_TYPES.add(MediaType.IMAGE_GIF);
        RESOURCE_MEDIA_TYPES.add(MediaType.IMAGE_JPEG);
        RESOURCE_MEDIA_TYPES.add(MediaType.IMAGE_PNG);

        REQUEST_LOGGER_FILTER_TYPES = new ArrayList<>();
        REQUEST_LOGGER_FILTER_TYPES.add(MediaType.MULTIPART_FORM_DATA);
        REQUEST_LOGGER_FILTER_TYPES.add(MediaType.APPLICATION_OCTET_STREAM);

        RESPONSE_LOGGER_FILTER_TYPES = new ArrayList<>();
        RESPONSE_LOGGER_FILTER_TYPES.add(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        logger.debug("============================request begin================================================");
        logger.debug("URI          : {}", request.getURI());
        logger.debug("Method       : {}", request.getMethod());
        logger.debug("Headers      : {}", request.getHeaders());
        logger.debug("Request body : {}", getRequestLoggerBody(request, body));
        logger.debug("============================request   end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        logger.debug("============================response begin================================================");
        logger.debug("Status code   : {}", response.getStatusCode());
        logger.debug("Status text   : {}", response.getStatusText());
        logger.debug("Headers       : {}", response.getHeaders());
        logger.debug("Response body : {}", getResponseLoggerBody(response));
        logger.debug("============================response   end================================================");
    }

    private String getRequestLoggerBody(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
        MediaType contentType = request.getHeaders().getContentType();
        if (isResourceType(contentType)) {
            return "...filtered resource type...";
        } else if (isPresentIn(contentType, RESOURCE_MEDIA_TYPES)) {
            return "...filtered request type...";
        } else if (body != null && body.length > REQUEST_LOGGER_MAX_SIZE) {
            return new String(body, 0, REQUEST_LOGGER_MAX_SIZE);
        } else {
            return new String(body, StandardCharsets.UTF_8);
        }
    }

    private String getResponseLoggerBody(ClientHttpResponse response) throws IOException {
        MediaType contentType = response.getHeaders().getContentType();
        ContentDisposition disposition = response.getHeaders().getContentDisposition();
        if (isResourceType(contentType)) {
            return "...filtered resource type...";
        } else if (isPresentIn(contentType, RESOURCE_MEDIA_TYPES)) {
            return "...filtered response type...";
        } else if (disposition != null && (StringUtil.notEmpty(disposition.getFilename()) || StringUtil.notEmpty(disposition.getType()))) {
            return "...disposition,filename:" + disposition.getFilename() + ",type:" + disposition.getType();
        } else {
            StringBuilder inputStringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    inputStringBuilder.append(line);
                    inputStringBuilder.append('\n');
                    line = bufferedReader.readLine();
                    if (inputStringBuilder.length() > RESPONSE_LOGGER_MAX_SIZE) {
                        return inputStringBuilder.substring(0, RESPONSE_LOGGER_MAX_SIZE);
                    }
                }
            }
            return inputStringBuilder.toString();
        }
    }

    //Headers       : [Connection:"keep-alive", Content-Type:"text/plain", Date:"Thu, 12 Dec 2019 12:57:27 GMT", Content-Length:"139"]
    //contentDisposition:{"type":""}
    private boolean isResourceType(MediaType contentType) {
        if (contentType == null) {
            return false;
        }
        if (isPresentIn(contentType, RESOURCE_MEDIA_TYPES)) {
            return true;
        }
        for (String resourceType : RESOURCE_TYPES) {
            if (resourceType.equals(contentType.getType())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPresentIn(MediaType contentType, Collection<? extends MimeType> mimeTypes) {
        if (contentType == null) {
            return false;
        }

        MimeType mimeType;
        Iterator<? extends MimeType> it = mimeTypes.iterator();

        while (it.hasNext()) {
            mimeType = it.next();
            if (mimeType.getType().equalsIgnoreCase(contentType.getType())
                    && mimeType.getSubtype().equalsIgnoreCase(contentType.getSubtype())) {
                return true;
            }
        }
        return false;
    }
}