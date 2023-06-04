package com.yapp.muckpot.domains.board.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.yapp.muckpot.common.AGE_MAX
import com.yapp.muckpot.common.AGE_MIN
import com.yapp.muckpot.common.CHAT_LINK_MAX
import com.yapp.muckpot.common.CONTENT_MAX
import com.yapp.muckpot.common.Location
import com.yapp.muckpot.common.MEETING_TIME
import com.yapp.muckpot.common.TITLE_MAX
import com.yapp.muckpot.common.YYYYMMDD
import com.yapp.muckpot.domains.board.entity.Board
import com.yapp.muckpot.domains.user.entity.MuckPotUser
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

@ApiModel(value = "먹팟생성 요청")
data class MuckpotCreateRequest(
    @field:ApiModelProperty(notes = "만날 날짜", required = true, example = "2023-05-21")
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = YYYYMMDD)
    val meetingDate: LocalDate,
    @field:ApiModelProperty(notes = "만날 시간", required = true, example = "오후 12:00")
    @field:Pattern(regexp = MEETING_TIME)
    val meetingTime: String,
    @field:ApiModelProperty(notes = "최대 인원", required = true, example = "5")
    @field:Min(2, message = "최대 인원은 {value}명 이상 가능합니다.")
    val maxApply: Int = 2,
    @field:ApiModelProperty(notes = "최소 나이", required = false, example = "20")
    val minAge: Int = AGE_MIN,
    @field:ApiModelProperty(notes = "최대 나이", required = false, example = "100")
    val maxAge: Int = AGE_MAX,
    @field:ApiModelProperty(notes = "주소", required = true, example = "서울 성북구 안암동5가 104-30 캐치카페 안암")
    var locationName: String,
    @field:ApiModelProperty(notes = "주소 상세", required = false, example = "6층")
    var locationDetail: String? = null,
    @field:ApiModelProperty(notes = "x 좌표", required = true, example = "127.02970799701643")
    val x: Double,
    @field:ApiModelProperty(notes = "y 좌표", required = true, example = "37.58392327180857")
    val y: Double,
    @field:ApiModelProperty(notes = "제목", required = true, example = "37.58392327180857")
    @field:Length(max = TITLE_MAX, message = "제목은 {max}(자)를 넘을 수 없습니다.")
    var title: String,
    @field:ApiModelProperty(notes = "내용", required = false, example = "같이 밥묵으실분")
    @field:Length(max = CONTENT_MAX, message = "내용은 {max}(자)를 넘을 수 없습니다.")
    var content: String? = null,
    @field:ApiModelProperty(notes = "오픈채팅방 링크", required = true, example = "https://open.kakao.com/o/gSIkvvHc")
    @field:Length(max = CHAT_LINK_MAX, message = "링크는 {max}(자)를 넘을 수 없습니다.")
    var chatLink: String
) {
    init {
        title = title.trim()
        content = content?.trim()
        chatLink = chatLink.trim()
    }

    fun toBoard(user: MuckPotUser): Board {
        return Board(
            user = user,
            title = title,
            location = Location(locationName, x, y),
            locationDetail = locationDetail,
            meetingDate = meetingDate,
            meetingTime = meetingTime,
            minAge = minAge,
            maxAge = maxAge,
            maxApply = maxApply,
            chatLink = chatLink
        )
    }
}
