package com.qcdfz.fengaiagent.enums;

import lombok.Getter;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.enums
 * @author: fgh
 * @description: 腾讯云机器翻译支持的目标语言枚举
 * @createTime: 2025-05-13 12:01
 */
@Getter
public enum TencentTranslateTargetLangEnum {
    /** 简体中文 */
    ZH("zh", "简体中文"),
    /** 繁体中文 */
    ZH_TW("zh-TW", "繁体中文"),
    /** 英语 */
    EN("en", "英语"),
    /** 日语 */
    JA("ja", "日语"),
    /** 韩语 */
    KO("ko", "韩语"),
    /** 法语 */
    FR("fr", "法语"),
    /** 西班牙语 */
    ES("es", "西班牙语"),
    /** 意大利语 */
    IT("it", "意大利语"),
    /** 德语 */
    DE("de", "德语"),
    /** 土耳其语 */
    TR("tr", "土耳其语"),
    /** 俄语 */
    RU("ru", "俄语"),
    /** 葡萄牙语 */
    PT("pt", "葡萄牙语"),
    /** 越南语 */
    VI("vi", "越南语"),
    /** 印尼语 */
    ID("id", "印尼语"),
    /** 泰语 */
    TH("th", "泰语"),
    /** 马来语 */
    MS("ms", "马来语"),
    /** 阿拉伯语 */
    AR("ar", "阿拉伯语"),
    /** 印地语 */
    HI("hi", "印地语");

    private final String value;
    private final String text;

    TencentTranslateTargetLangEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

}
