package org.xinua.example.spring.mvc.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xinua.example.spring.mvc.model.vo.SimpleUserVo;
import org.xinua.example.spring.mvc.util.ResultEntity;
import org.xinua.example.spring.mvc.model.dto.Add;
import org.xinua.example.spring.mvc.model.dto.Update;
import org.xinua.example.spring.mvc.model.dto.UserDto;
import org.xinua.example.spring.mvc.model.po.User;
import org.xinua.example.spring.mvc.model.vo.PageVo;
import org.xinua.example.spring.mvc.model.vo.UserVo;
import org.xinua.example.spring.mvc.service.UserService;
import org.xinua.example.spring.mvc.util.JacksonUtil;
import org.xinua.example.spring.mvc.util.ResultUtil;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResultEntity get(@RequestParam(value = "id") @Min(value = 1, message = "id 必须大于0") Long id) {
        User user = userService.get(id);
        return ResultUtil.success(toVo(user));
    }

    @GetMapping("/simple")
    public ResultEntity getSimple(@RequestParam(value = "id") @Min(value = 1, message = "id 必须大于0") Long id) {
        User user = userService.get(id);
        return ResultUtil.success(toSimpleVo(user));
    }

    @GetMapping("/username")
    public ResultEntity get(@RequestParam(value = "username") String username) {
        User user = userService.get(username);
        return ResultUtil.success(toVo(user));
    }

    @GetMapping("/page")
    public ResultEntity page(@RequestParam(value = "pageNum", defaultValue = "0") @Min(value = 0, message = "pageNum 必须大于等于0") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "20") @Min(value = 1, message = "pageSize 必须大于0") Integer pageSize) {
        Page<User> page = userService.page(pageNum, pageSize);
        PageVo<UserVo> pageVO = new PageVo<>(page.getTotalPages(), page.getTotalElements(), toVo(page.getContent()));
        return ResultUtil.success(pageVO);
    }

    @GetMapping("/all")
    public ResultEntity all() {
        List<User> all = userService.all();
        return ResultUtil.success(toVo(all));
    }

    @PostMapping("")
    public ResultEntity add(@RequestBody @Validated(Add.class) UserDto userDto) {
        String json = JacksonUtil.objectToJson(userDto);
        User user = JacksonUtil.jsonToClass(json, User.class);
        User add = userService.add(user);
        return ResultUtil.success(toVo(add));
    }

    @PutMapping("")
    public ResultEntity update(@RequestBody @Validated(Update.class) UserDto userDto) {
        String json = JacksonUtil.objectToJson(userDto);
        User user = JacksonUtil.jsonToClass(json, User.class);
        User update = userService.update(user);
        return ResultUtil.success(toVo(update));
    }

    @DeleteMapping("")
    public ResultEntity delete(@RequestParam(value = "id") @Min(value = 1, message = "id 必须大于0") Long id) {
        userService.delete(id);
        return ResultUtil.success("删除user成功");
    }

    private List<UserVo> toVo(List<User> users) {
        List<UserVo> list = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (User user : users) {
                list.add(toVo(user));
            }
        }
        return list;
    }

    private UserVo toVo(User user) {
        if (user == null) return null;
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    private SimpleUserVo toSimpleVo(User user) {
        if (user == null) return null;
        SimpleUserVo userVo = new SimpleUserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

}
