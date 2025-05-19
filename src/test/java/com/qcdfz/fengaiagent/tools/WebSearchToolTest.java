package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebSearchToolTest {

    @Value("${api-key.search-api-key}")
    private String searchApiKey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String searchWeb = webSearchTool.searchWeb("程序员鱼皮是谁");
        Assertions.assertNotNull(searchWeb);
    }
}
