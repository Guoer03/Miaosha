package com.guoer03.controller;

import com.guoer03.domain.User;
import com.guoer03.redis.RedisService;
import com.guoer03.redis.UserKey;
import com.guoer03.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    RedisService redisService;
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Guoer03");
        return "hello";
    }
    @RequestMapping("/set")
    @ResponseBody
    public Result set(){
        User user = new User(1, "1111");
        redisService.set(UserKey.getById,user.getId()+"",user);
        return Result.success(true);
    }
    @RequestMapping("/get")
    @ResponseBody
    public Result get(){
        User user = new User(1, "1111");
        redisService.get(UserKey.getById,user.getId()+"",User.class);
        return Result.success(user);
    }
}
