package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String tt= """
                约会地点推荐及图片展示
                
                静安公园
                ![](https://images.pexels.com/photos/12370410/pexels-photo-12370410.jpeg?auto=compress&cs=tinysrgb&h=350)
                ![](https://images.pexels.com/photos/8686800/pexels-photo-8686800.png?auto=compress&cs=tinysrgb&h=350)
                
                萤七人间
                ![](https://images.pexels.com/photos/5904233/pexels-photo-5904233.jpeg?auto=compress&cs=tinysrgb&h=350)
                ![](https://images.pexels.com/photos/8784720/pexels-photo-8784720.jpeg?auto=compress&cs=tinysrgb&h=350)
                
                Kathleen's 5 Rooftop Restaurant & Bar
                ![](https://images.pexels.com/photos/32245820/pexels-photo-32245820.jpeg?auto=compress&cs=tinysrgb&h=350)
                ![](https://images.pexels.com/photos/32234888/pexels-photo-32234888.jpeg?auto=compress&cs=tinysrgb&h=350)
                
                我家餐厅
                ![](https://images.pexels.com/photos/32181831/pexels-photo-32181831.jpeg?auto=compress&cs=tinysrgb&h=350)
                ![](https://images.pexels.com/photos/6692095/pexels-photo-6692095.jpeg?auto=compress&cs=tinysrgb&h=350)
                
                约会计划
                
                上午：
                - 静安公园：开始你们的一天，在这里散步、享受自然美景，并拍照留念。
                
                午餐：
                - 我家餐厅：在这里享用一顿美味的本帮菜，体验家的味道。
                
                下午：
                - 自由活动时间：可以参观附近的美术馆或咖啡厅。
                
                晚餐：
                - 萤七人间：在这个小资的地方用餐，享受浪漫的氛围。
                
                夜生活：
                - Kathleen's 5 Rooftop Restaurant & Bar：在这家艺术气息浓厚的酒吧结束一天的行程，欣赏夜景并品尝鸡尾酒。
                """;
        String fileName = "恋爱助手.pdf";
        String content = """
                上午:
                - 地点: 静安公园
                - 活动: 漫步于法式园林风格的公园,欣赏自然美景。
                图片: [https://www.codefather.cn/logo.png]
                """;
        String result = tool.generatePDF(fileName, tt);
        Assertions.assertNotNull(result);
    }
}
