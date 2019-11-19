package com.andy.api;

import com.andy.api.web.TestController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 19:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@Slf4j
public class TestMultipleDs {
    @Resource
    TestController controller;


    @Test
    public void testDs(){
        controller.test();
    }
}
