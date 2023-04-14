package org.xinhua.example.spring.lock.service;

public interface TokenService {

    AccessToken getToken();

    AccessToken newToken();

}
