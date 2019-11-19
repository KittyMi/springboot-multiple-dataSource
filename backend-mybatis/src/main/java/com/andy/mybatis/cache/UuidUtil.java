package com.andy.mybatis.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author ly
 * @date 2019/5/17 10:53
 * description: UUID生成器
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UuidUtil {

    /**
     * 生成uuid
     * @return uuid
     */
    static String genUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
