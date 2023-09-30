package com.kotlin.wanted.global.util

import java.time.LocalDateTime
import java.time.ZoneId

object DateUtil {

    fun now(): LocalDateTime {
        val zoneId: ZoneId = ZoneId.of("Asia/Seoul");
        return LocalDateTime.now(zoneId);
    }

}