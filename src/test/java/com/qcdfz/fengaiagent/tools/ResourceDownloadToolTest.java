package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://www.codefather.cn/logo.png";
        String result =  resourceDownloadTool.downloadResource(url,"logo.png");
        Assertions.assertNotNull(result);
    }
}
