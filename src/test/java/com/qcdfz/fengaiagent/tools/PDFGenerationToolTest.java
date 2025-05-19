package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "恋爱助手.pdf";
        String content = "程导航原创项目 https://www.codefather.cn";
        String result = tool.generatePDF(fileName, content);
        Assertions.assertNotNull(result);
    }
}
