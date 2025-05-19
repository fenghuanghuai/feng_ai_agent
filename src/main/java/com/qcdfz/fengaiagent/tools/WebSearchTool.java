package com.qcdfz.fengaiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.tools
 * @author: fgh
 * @description: 联网搜索工具
 * @createTime: 2025-05-17 09:54
 */

public class WebSearchTool {

    private final String apiKey;
    private static final String SEARCH_URL = "https://www.searchapi.io/api/v1/search";

    public WebSearchTool(String apiKey){
        this.apiKey = apiKey;
    }
    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(@ToolParam(description = "Search query keyword") String query){
        HashMap<String, Object> param = new HashMap<>();
        param.put("q",query);
        param.put("engine","baidu");
        param.put("api_key",apiKey);
        try {
            String response = HttpUtil.get(SEARCH_URL, param);
            // 取出返回结果的前 5 条
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取 organic_results 部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            List<Object> objects = organicResults.subList(0, 5);
            // 拼接搜索结果为字符串
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }).collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
