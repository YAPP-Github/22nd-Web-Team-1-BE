package com.yapp.muckpot.domains.board.exception

import com.yapp.muckpot.common.BaseErrorCode
import com.yapp.muckpot.common.ResponseDto
import com.yapp.muckpot.common.enums.StatusCode

enum class BoardErrorCode(
    private val status: Int,
    private val reason: String
) : BaseErrorCode {
    BOARD_NOT_FOUND(StatusCode.BAD_REQUEST.code, "먹팟 정보를 찾을 수 없습니다."),
    MAX_APPLY_UPDATE_FAIL(StatusCode.BAD_REQUEST.code, "현재 참여인원 이상으로만 설정 가능합니다."),
    BOARD_UNAUTHORIZED(StatusCode.UNAUTHORIZED.code, "자신의 글만 수정할 수 있습니다."),
    ALREADY_JOIN(StatusCode.BAD_REQUEST.code, "이미 참여한 유저입니다.");

    override fun toResponseDto(): ResponseDto {
        return ResponseDto(this.status, this.reason, null)
    }
}
