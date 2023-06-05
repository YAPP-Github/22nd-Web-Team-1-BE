package com.yapp.muckpot.domains.board.entity

import com.yapp.muckpot.common.Location
import com.yapp.muckpot.common.MAX_APPLY_MIN
import com.yapp.muckpot.domains.user.entity.MuckPotUser
import com.yapp.muckpot.domains.user.enums.MuckPotStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.lang.IllegalArgumentException
import java.time.LocalDate

class BoardTest : FunSpec({
    val user: MuckPotUser = mockk()
    val location: Location = mockk()

    context("Board 유효성 검사") {
        test("minAge는 maxAge 보다 작아야 한다.") {
            shouldThrow<IllegalArgumentException> {
                Board(
                    id = null,
                    user = user,
                    title = "title",
                    location = location,
                    locationDetail = null,
                    meetingDate = LocalDate.now(),
                    meetingTime = "오전 12:00",
                    content = "content",
                    views = 0,
                    currentApply = 0,
                    maxApply = 3,
                    chatLink = "link",
                    status = MuckPotStatus.IN_PROGRESS,
                    minAge = 25,
                    maxAge = 21
                )
            }.message shouldBe "최소나이는 최대나이보다 작아야 합니다."
        }

        test("최대 참여인원은 2명 이상이어야 한다.") {
            shouldThrow<IllegalArgumentException> {
                Board(
                    id = null,
                    user = user,
                    title = "title",
                    location = location,
                    locationDetail = null,
                    meetingDate = LocalDate.now(),
                    meetingTime = "오전 12:00",
                    content = "content",
                    views = 0,
                    currentApply = 0,
                    maxApply = 1,
                    chatLink = "link",
                    status = MuckPotStatus.IN_PROGRESS,
                    minAge = 21,
                    maxAge = 25
                )
            }.message shouldBe "최대 인원은 ${MAX_APPLY_MIN}명 이상 가능합니다."
        }

        test("정원이 초과된 경우 참여할 수 없다.") {
            val board = Board(
                id = null,
                user = user,
                title = "title",
                location = location,
                locationDetail = null,
                meetingDate = LocalDate.now(),
                meetingTime = "오전 12:00",
                content = "content",
                views = 0,
                currentApply = 0,
                maxApply = 2,
                chatLink = "link",
                status = MuckPotStatus.IN_PROGRESS,
                minAge = 21,
                maxAge = 25
            )
            board.participate()
            shouldThrow<IllegalArgumentException> {
                board.participate()
            }.message shouldBe "정원이 초과되었습니다."
        }
    }
})
