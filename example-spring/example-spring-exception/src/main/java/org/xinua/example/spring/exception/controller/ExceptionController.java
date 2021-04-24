package org.xinua.example.spring.exception.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xinua.example.spring.exception.exception.AuthenticationException;
import org.xinua.example.spring.exception.exception.BusinessException;
import org.xinua.example.spring.exception.exception.DataNotfoundException;
import org.xinua.example.spring.exception.util.ResultEntity;
import org.xinua.example.spring.exception.util.ResultUtil;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("")
    public ResultEntity get(@RequestParam(value = "type") String type) {
        switch (type) {
            case "0":
                return ResultUtil.success("success");
            case "1":
                throw new AuthenticationException(10001, "default msg", "zhang");
            case "2":
                throw new AuthenticationException(10002, "default msg");
            case "3":
                throw new AuthenticationException(10003, "default msg");
            case "4":
                throw new DataNotfoundException(20004, "default msg", 14);
            case "5":
                throw new DataNotfoundException(20005, "default msg", "zhangsan");
            case "6":
                throw new DataNotfoundException(20006, "default msg", 16);
            case "7":
                throw new BusinessException(30007, "default msg");
            case "8":
                throw new BusinessException(30008, "default msg");
            case "9":
                throw new BusinessException(30009, "default msg");
            case "10":
                throw new DataNotfoundException(30009, "default msg", 10);
            case "11":
                throw new AuthenticationException(10001, "default msg");
            case "12":
                throw new BusinessException(-1, "default msg");
            case "13":
                throw new RuntimeException();
        }
        throw new RuntimeException("其他异常");
    }
}