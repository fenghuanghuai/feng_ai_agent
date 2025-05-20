package com.qcdfz.fengaiagent.tools;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.tools
 * @author: fgh
 * @description: 邮件发送工具类
 * @createTime: 2025-05-17 18:49
 */
@Slf4j
@Component
public class EmailTool {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 邮件发送结果
     */
    @Data
    @Builder
    public static class EmailResult {
        private boolean success;
        private String message;
        private String recipient;
    }

    /**
     * 发送简单文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 发送结果
     */
    @Tool(description = "Send a simple text email")
    public String sendSimpleEmail(
            @ToolParam(description = "Recipient email address") String to,
            @ToolParam(description = "Email subject") String subject,
            @ToolParam(description = "Email content") String content) {
        EmailResult result = sendEmail(to, subject, content, false);
        return result.getMessage();
    }

    /**
     * 发送HTML格式邮件
     * @param to 收件人
     * @param subject 主题
     * @param htmlContent HTML内容
     * @return 发送结果
     */
    @Tool(description = "Send an HTML email")
    public String sendHtmlEmail(
            @ToolParam(description = "Recipient email address") String to,
            @ToolParam(description = "Email subject") String subject,
            @ToolParam(description = "HTML content") String htmlContent) {
        EmailResult result = sendEmail(to, subject, htmlContent, true);
        return result.getMessage();
    }

    /**
     * 批量发送邮件
     * @param toList 收件人列表
     * @param subject 主题
     * @param content 内容
     * @return 发送结果
     */
    @Tool(description = "Send emails to multiple recipients")
    public String sendBatchEmails(
            @ToolParam(description = "List of recipient email addresses") List<String> toList,
            @ToolParam(description = "Email subject") String subject,
            @ToolParam(description = "Email content") String content) {
        StringBuilder resultMessage = new StringBuilder();
        int successCount = 0;

        for (String to : toList) {
            EmailResult result = sendEmail(to, subject, content, false);
            if (result.isSuccess()) {
                successCount++;
            }
            resultMessage.append(result.getMessage()).append("\n");
        }

        return String.format("批量发送完成，成功：%d，失败：%d\n%s",
                successCount,
                toList.size() - successCount,
                resultMessage);
    }

    /**
     * 发送邮件的核心方法
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否为HTML格式
     * @return 发送结果
     */
    private EmailResult sendEmail(String to, String subject, String content, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // 设置邮件基本信息
            // 这个只是基本思路，后续可根据当前登录用户的信息使用用户账号发送邮件
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);

            // 发送邮件
            mailSender.send(message);

            return EmailResult.builder()
                    .success(true)
                    .message(String.format("邮件发送成功：%s", to))
                    .recipient(to)
                    .build();

        } catch (MessagingException e) {
            log.error("邮件发送失败: {}", e.getMessage(), e);
            return EmailResult.builder()
                    .success(false)
                    .message(String.format("邮件发送失败：%s，原因：%s", to, e.getMessage()))
                    .recipient(to)
                    .build();
        }
    }
}
