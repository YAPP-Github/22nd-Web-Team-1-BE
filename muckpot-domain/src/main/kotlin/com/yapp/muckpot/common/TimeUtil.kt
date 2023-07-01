package com.yapp.muckpot.common

import com.yapp.muckpot.common.constants.A_DAY_AGO
import com.yapp.muckpot.common.constants.HOUR_IN_MINUTES
import com.yapp.muckpot.common.constants.MINUTES_IN_ONE_DAY
import com.yapp.muckpot.common.constants.MINUTES_IN_TWO_DAY
import com.yapp.muckpot.common.constants.NOT_TODAY_OR_TOMORROW
import com.yapp.muckpot.common.constants.N_HOURS_AGO
import com.yapp.muckpot.common.constants.N_MINUTES_AGO
import com.yapp.muckpot.common.constants.TODAY_KR
import com.yapp.muckpot.common.constants.TOMORROW_KR
import com.yapp.muckpot.common.extension.isToday
import com.yapp.muckpot.common.extension.isTomorrow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object TimeUtil {
    /**
     * 현재 시간을 기준으로 오늘, 내일 계산
     *
     * @return 오늘, 내일, null
     */
    fun isTodayOrTomorrow(localDate: LocalDate): String? {
        var todayOrTomorrow: String? = NOT_TODAY_OR_TOMORROW
        if (localDate.isToday()) {
            todayOrTomorrow = TODAY_KR
        } else if (localDate.isTomorrow()) {
            todayOrTomorrow = TOMORROW_KR
        }
        return todayOrTomorrow
    }

    /**
     * @param localDateTime localDateTime 이 현재시간부터 얼마큼 지났는지 정책에 따라 계산
     */
    fun formatElapsedTime(localDateTime: LocalDateTime): String {
        val timeDiff = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())
        return when {
            (timeDiff < HOUR_IN_MINUTES) -> N_MINUTES_AGO.format(timeDiff)
            (timeDiff in HOUR_IN_MINUTES until MINUTES_IN_ONE_DAY) -> N_HOURS_AGO.format(timeDiff / HOUR_IN_MINUTES)
            (timeDiff in MINUTES_IN_ONE_DAY until MINUTES_IN_TWO_DAY) -> A_DAY_AGO
            else -> localDateTime.toLocalDate().toString()
        }
    }

    fun localeKoreanFormatting(localDateTime: LocalDateTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREAN)
        return localDateTime.format(formatter)
    }
}
