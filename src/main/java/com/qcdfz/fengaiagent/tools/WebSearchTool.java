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
    private final String apiKeyV4;
    private static final String SEARCH_URL = "https://www.searchapi.io/api/v1/search";
    private static final String SEARCH_URL_V4 = "https://open.bigmodel.cn/api/paas/v4/web_search";

    public WebSearchTool(String apiKey){
        this.apiKey = apiKey;
        this.apiKeyV4 = apiKey;
    }
    @Tool(description = "Search for information from Zhipu Web Search API V4")
    public String searchWebV4(@ToolParam(description = "Search query keyword") String query) {
        // JDos: 构建请求头
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKeyV4);
        headers.put("Content-Type", "application/json");

        // JDos: 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("search_query", query);
        requestBody.set("search_engine", "search_std ");

        try {
            // JDos: 发送POST请求
            String response = HttpUtil.createPost(SEARCH_URL_V4)
                    .addHeaders(headers)
                    .body(requestBody.toString())
                    .execute()
                    .body();

            // JDos: 解析响应，提取search_result前5条
            JSONObject jsonObject = JSONUtil.parseObj(response);
            JSONArray resultArray = jsonObject.getJSONArray("search_result");
            if (resultArray == null || resultArray.isEmpty()) {
                return "未找到相关结果";
            }
            int limit = Math.min(5, resultArray.size());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < limit; i++) {
                JSONObject item = resultArray.getJSONObject(i);
                // JDos: 格式化每条结果
                sb.append("【").append(item.getStr("title", "无标题")).append("】")
                  .append(item.getStr("link", "无链接")).append("\n")
                  .append(item.getStr("content", "无摘要")).append("\n\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            // JDos: 异常处理
            return "智谱网页搜索异常: " + e.getMessage();
        }
    }

    @Deprecated
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
