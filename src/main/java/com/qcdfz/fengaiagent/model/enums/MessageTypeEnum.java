package com.qcdfz.fengaiagent.model.enums;

import lombok.Getter;

/**
 * JDos注释：
 * 消息角色类型枚举，原RoleType，现独立为MessageTypeEnum。
 * 用于区分system、user、assistant三种消息角色。
 */
@Getter
public enum MessageTypeEnum {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");
    private final String value;
    MessageTypeEnum(String value) { this.value = value; }

    public static MessageTypeEnum fromString(String str) {
        for (MessageTypeEnum type : MessageTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message role: " + str);
    }
}
