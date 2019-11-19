package com.andy.api.web;

import com.andy.api.service.IDimMenuService;
import com.andy.api.service.ISysUserService;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 19:28
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class TestController extends ApiController {
    @Resource
    ISysUserService userService;
    @Resource
    IDimMenuService menuService;

    @PostMapping("/test")
    public R test(){
        log.info(userService.getById(1).toString());
        log.info(menuService.getById(1).toString());
        return success("OK");
    }
}
