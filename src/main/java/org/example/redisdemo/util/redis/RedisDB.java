package org.example.redisdemo.util.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xiesir
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RedisDB {
    //以下分别代表redis 0-15库
    ZERO(0, "token 及用户认证相关"),
    ONE(1, "追踪交易数据缓存"),
    TWO(32, ""),
    THREE(33, ""),
    FOUR(34, ""),
    FIVE(35, ""),
    SIX(36, ""),
    SEVEN(37, ""),
    EIGHT(38, ""),
    NINE(39, ""),
    TEN(40, ""),
    ELEVEN(41, ""),
    TWELVE(42, ""),
    THIRTEEN(43, ""),
    FOURTEEN(44, ""),
    FIFTEEN(45, "");

    private int ordinal;
    private String description;

}