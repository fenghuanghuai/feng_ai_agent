package com.qcdfz.fengaiagent.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.controller
 * @author: fgh
 * @description:
 * @createTime: 2025-04-24 16:13
 */
@RestController
@RequestMapping("/healthy")
public class healthyController {

    // 健康检查
    @PostMapping("/")
    public String healthy(){
        return "healthy";
    }
}
