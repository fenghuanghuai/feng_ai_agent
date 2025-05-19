package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String result = fileOperationTool.readFile("恋爱助手.txt");
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "恋爱助手.txt";
        String content = "#### 如何判断相亲对象是否值得深入发展？\n" +
                "观察相亲对象的言行举止，是否尊重他人，比如对服务员的态度。交流中了解其价值观，包括对家庭、事业、生活的看法是否与你契合。关注对方的情绪稳定性，能否理性处理分歧。看其是否有明确的人生规划，对未来有清晰想法。像小钱相亲时，发现对方对生活积极向上，尊重自己的观点，且双方对未来家庭生活规划相似，于是决定深入发展。\n" +
                "推荐课程：[《相亲对象评估与恋爱决策》](https://www.codefather.cn)，课程从多个维度教你精准评估相亲对象，做出正确恋爱决策，少走弯路。";
        String result = fileOperationTool.writeFile(fileName, content);
        Assertions.assertNotNull(result);
    }
}
