package com.yapp.muckpot.domains.board.controller.dto

import com.yapp.muckpot.common.CHAT_LINK_MAX
import com.yapp.muckpot.common.CONTENT_MAX
import com.yapp.muckpot.common.TITLE_MAX
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class MuckpotCreateRequestTest : StringSpec({
    lateinit var validator: Validator
    lateinit var request: MuckpotCreateRequest

    beforeSpec {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    beforeEach {
        request = MuckpotCreateRequest(
            meetingDate = LocalDate.now(),
            meetingTime = "오전 12:00",
            maxApply = 10,
            minAge = 20,
            maxAge = 100,
            locationName = "location",
            locationDetail = null,
            x = 0.0,
            y = 0.0,
            title = "title",
            content = null,
            chatLink = ""
        )
    }

    "제목은 최대 100자" {
        request.title = "X".repeat(TITLE_MAX + 1)

        val violations: MutableSet<ConstraintViolation<MuckpotCreateRequest>> = validator.validate(request)
        violations.size shouldBe 1
        for (violation in violations) {
            violation.message shouldBe "제목은 $TITLE_MAX(자)를 넘을 수 없습니다."
        }
    }

    "내용은 최대 2000자" {
        request.content = "X".repeat(CONTENT_MAX + 1)

        val violations: MutableSet<ConstraintViolation<MuckpotCreateRequest>> = validator.validate(request)
        violations.size shouldBe 1
        for (violation in violations) {
            violation.message shouldBe "내용은 $CONTENT_MAX(자)를 넘을 수 없습니다."
        }
    }

    "링크는 최대 300자" {
        request.chatLink = "X".repeat(CHAT_LINK_MAX + 1)

        val violations: MutableSet<ConstraintViolation<MuckpotCreateRequest>> = validator.validate(request)
        violations.size shouldBe 1
        for (violation in violations) {
            violation.message shouldBe "링크는 $CHAT_LINK_MAX(자)를 넘을 수 없습니다."
        }
    }
})
