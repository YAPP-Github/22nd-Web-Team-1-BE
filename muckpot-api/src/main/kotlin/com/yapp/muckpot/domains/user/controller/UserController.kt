package com.yapp.muckpot.domains.user.controller

import com.yapp.muckpot.common.ResponseDto
import com.yapp.muckpot.common.constants.ACCESS_TOKEN_KEY
import com.yapp.muckpot.common.constants.EMAIL_AUTH_REQ_RESPONSE
import com.yapp.muckpot.common.constants.LOGIN_RESPONSE
import com.yapp.muckpot.common.constants.NO_BODY_RESPONSE
import com.yapp.muckpot.common.constants.REFRESH_TOKEN_KEY
import com.yapp.muckpot.common.constants.SIGN_UP_RESPONSE
import com.yapp.muckpot.common.utils.ResponseEntityUtil
import com.yapp.muckpot.common.utils.SecurityContextHolderUtil
import com.yapp.muckpot.domains.user.controller.dto.LoginRequest
import com.yapp.muckpot.domains.user.controller.dto.SendEmailAuthRequest
import com.yapp.muckpot.domains.user.controller.dto.SignUpRequest
import com.yapp.muckpot.domains.user.controller.dto.VerifyEmailAuthRequest
import com.yapp.muckpot.domains.user.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.Example
import io.swagger.annotations.ExampleProperty
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@Api(tags = ["유저 api"], description = "유저 API")
@RequestMapping("/api")
class UserController(
    private val userService: UserService
) {

    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                examples = Example(
                    ExampleProperty(
                        value = LOGIN_RESPONSE,
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                ),
                message = "성공"
            )
        ]
    )
    @ApiOperation(value = "로그인")
    @PostMapping("/v2/users/login")
    fun login(
        @RequestBody @Valid
        request: LoginRequest
    ): ResponseEntity<ResponseDto> {
        return ResponseEntityUtil.ok(userService.login(request))
    }

    @ApiResponses(
        value = [
            ApiResponse(
                code = 201,
                examples = Example(
                    ExampleProperty(
                        value = EMAIL_AUTH_REQ_RESPONSE,
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                ),
                message = "성공"
            )
        ]
    )
    @ApiOperation(value = "이메일 인증 요청")
    @PostMapping("/v2/emails/request")
    fun sendEmailAuth(
        @RequestBody @Valid
        request: SendEmailAuthRequest
    ): ResponseEntity<ResponseDto> {
        return ResponseEntityUtil.created(userService.sendEmailAuth(request))
    }

    @ApiResponses(
        value = [
            ApiResponse(
                code = 204,
                examples = Example(
                    ExampleProperty(
                        value = NO_BODY_RESPONSE,
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                ),
                message = "성공"
            )
        ]
    )
    @ApiOperation(value = "이메일 인증 검증")
    @PostMapping("/v2/emails/verify")
    fun verifyEmailAuth(
        @RequestBody @Valid
        request: VerifyEmailAuthRequest
    ): ResponseEntity<ResponseDto> {
        userService.verifyEmailAuth(request)
        return ResponseEntityUtil.noContent()
    }

    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                examples = Example(
                    ExampleProperty(
                        value = LOGIN_RESPONSE,
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                ),
                message = "성공"
            ),
            ApiResponse(
                code = 204,
                message = "비 로그인 유저 응답"
            )
        ]
    )
    @ApiOperation(value = "유저 프로필 조회")
    @GetMapping("/v1/users/profile")
    fun findLoginUserProfile(): ResponseEntity<ResponseDto> {
        return SecurityContextHolderUtil.getCredentialOrNull()?.let {
            ResponseEntityUtil.ok(it)
        } ?: run {
            ResponseEntityUtil.noContent()
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(
                code = 201,
                examples = Example(
                    ExampleProperty(
                        value = SIGN_UP_RESPONSE,
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                ),
                message = "성공"
            )
        ]
    )
    @ApiOperation(value = "회원가입")
    @PostMapping("/v2/users")
    fun signUp(
        @RequestBody @Valid
        request: SignUpRequest
    ): ResponseEntity<ResponseDto> {
        return ResponseEntityUtil.created(userService.signUp(request))
    }

    @ApiResponses(
        value = [
            ApiResponse(
                code = 204,
                examples = Example(
                    ExampleProperty(
                        value = NO_BODY_RESPONSE,
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                ),
                message = "성공"
            )
        ]
    )
    @ApiOperation(value = "JWT 재발급")
    @PostMapping("/v1/users/refresh")
    fun reissueJwt(@CookieValue(REFRESH_TOKEN_KEY) refreshToken: String, @CookieValue(ACCESS_TOKEN_KEY) accessToken: String): ResponseEntity<ResponseDto> {
        userService.reissueJwt(refreshToken, accessToken)
        return ResponseEntityUtil.noContent()
    }
}
