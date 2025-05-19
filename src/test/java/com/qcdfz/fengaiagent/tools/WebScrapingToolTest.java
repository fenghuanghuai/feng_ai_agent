package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String webPage = webScrapingTool.scrapeWebPage("http://qcdfz.cn");
        Assertions.assertNotNull(webPage);
    }
}
