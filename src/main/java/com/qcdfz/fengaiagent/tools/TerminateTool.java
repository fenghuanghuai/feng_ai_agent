package com.qcdfz.fengaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.tools
 * @author: fgh
 * @description: 终止工具
 * @createTime: 2025-05-25 23:01
 */
public class TerminateTool {

    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.
            "When you have finished all the tasks, call this tool to end the work.
            """)
    public String doTerminate(){
        return "任务终止";
    }

}
