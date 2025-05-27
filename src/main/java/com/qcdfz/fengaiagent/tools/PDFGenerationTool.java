package com.qcdfz.fengaiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.qcdfz.fengaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.UUID;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.tools
 * @author: fgh
 * @description: PDF生成工具
 * @createTime: 2025-05-17 17:58
 */
public class PDFGenerationTool {

    @Tool(description = "Generate a PDF file with given content and images (supports Markdown image format)")
    public String generatePDF(
            @ToolParam(description = "Name of the file to save the generated PDF") String fileName,
            @ToolParam(description = "Content to be included in the PDF, supports Markdown image format ![]()") String content) {
        // 过滤文件名中的非法字符，保证Windows下合法
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 创建 PdfWriter 和 PdfDocument 对象
            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {
                // 自定义字体（需要人工下载字体文件到特定目录）
//                String fontPath = Paths.get("src/main/resources/static/fonts/simsun.ttf")
//                        .toAbsolutePath().toString();
//                PdfFont font = PdfFontFactory.createFont(fontPath,
//                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                // 使用内置中文字体
                PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
                document.setFont(font);

                Pattern imagePattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
                String[] lines = content.split("\\r?\\n");
                for (String line : lines) {
                    Matcher matcher = imagePattern.matcher(line.trim());
                    if (matcher.find()) {
                        String url = matcher.group(1).trim();
                        if (StrUtil.isNotBlank(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
                            File tempFile = null;
                            try {
                                // 下载图片到本地临时文件
                                String localPath = downloadImageWithUserAgent(url);
                                tempFile = new File(localPath);
                                ImageData imageData = ImageDataFactory.create(localPath);
                                Image image = new Image(imageData);
                                image.setAutoScale(true);
                                document.add(image);
                            } catch (Exception e) {
                                document.add(new Paragraph("图片加载失败: " + url + " 错误信息: " + e.getMessage()));
                            } finally {
                                // 清理临时文件
                                if (tempFile != null && tempFile.exists()) {
                                    try {
                                        tempFile.delete();
                                    } catch (Exception ex) {
                                        document.add(new Paragraph("临时图片文件删除失败: " + tempFile.getAbsolutePath()));
                                    }
                                }
                            }
                        } else if (StrUtil.isNotBlank(url) && url.startsWith("file:")) {
                            try {
                                ImageData imageData = ImageDataFactory.create(url);
                                Image image = new Image(imageData);
                                image.setAutoScale(true);
                                document.add(image);
                            } catch (Exception e) {
                                document.add(new Paragraph("本地图片加载失败: " + url + " 错误信息: " + e.getMessage()));
                            }
                        } else {
                            document.add(new Paragraph("无效图片地址: " + url));
                        }
                    } else {
                        document.add(new Paragraph(line));
                    }
                }
            }
            return "PDF generated successfully to: " + filePath;
        } catch (IOException e) {
            return "Error generating PDF: " + e.getMessage();
        } catch (Exception e) {
            return "PDF生成过程中发生异常: " + e.getMessage();
        }
    }
    /**
     * 使用Hutool工具类下载网络图片到本地临时文件，存放在临时目录下
     * @param imageUrl 图片URL
     * @return 本地图片文件路径
     */
    private String downloadImageWithUserAgent(String imageUrl) throws IOException {
        // 只取最后一个.到?或结尾之间的内容作为后缀，过滤非法字符
        String suffix = ".jpg";
        int dotIdx = imageUrl.lastIndexOf('.');
        if (dotIdx != -1) {
            int qIdx = imageUrl.indexOf('?', dotIdx);
            if (qIdx != -1) {
                suffix = imageUrl.substring(dotIdx, qIdx);
            } else {
                suffix = imageUrl.substring(dotIdx);
            }
            suffix = suffix.replaceAll("[\\\\/:*?\"<>|]", "_");
            if (suffix.length() > 8 || suffix.length() < 2) suffix = ".jpg";
        }
        // 临时文件名直接用UUID加后缀
        String tempFileName = UUID.randomUUID().toString() + suffix;
        String tempFilePath = FileConstant.FILE_SAVE_DIR + "/" + tempFileName;
        cn.hutool.core.io.FileUtil.writeFromStream(
                cn.hutool.http.HttpUtil.createGet(imageUrl)
                        .header("User-Agent", "Mozilla/5.0")
                        .execute()
                        .bodyStream(),
                tempFilePath
        );
        return tempFilePath;
    }

}
