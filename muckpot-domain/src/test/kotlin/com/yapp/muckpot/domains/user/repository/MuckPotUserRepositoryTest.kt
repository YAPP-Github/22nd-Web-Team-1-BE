package com.yapp.muckpot.domains.user.repository

import com.yapp.muckpot.common.Location
import com.yapp.muckpot.common.enums.Gender
import com.yapp.muckpot.config.CustomDataJpaTest
import com.yapp.muckpot.domains.user.entity.MuckPotUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback

@CustomDataJpaTest
@Rollback(false)
class MuckPotUserRepositoryTest(
    @Autowired val muckPotUserRepository: MuckPotUserRepository
) : StringSpec({
    "Point 타입 저장 성공" {
        // given
        val muckPotUser = MuckPotUser(
            null, "user@naver.com", "abcd1234", "nickname2", Gender.MEN,
            2000, "main", "sub", Location("location", 40.7128, -74.0060), "url"
        )
        // when
        val saveUser = muckPotUserRepository.save(muckPotUser)
        // then
        saveUser.id shouldNotBe null
    }

    "findByEmail 호출 성공" {
        val user = muckPotUserRepository.findByEmail("user@samsung.com")

        user shouldBe null
    }
})
