package com.yapp.muckpot.domains.board.dto

import com.querydsl.core.annotations.QueryProjection

data class RegionDto @QueryProjection constructor(
    val boardId: Long,
    val city: CityDto,
    val province: ProvinceDto
) {
    data class CityDto @QueryProjection constructor(
        val cityId: Long,
        val cityName: String
    )
    data class ProvinceDto @QueryProjection constructor(
        val provinceId: Long,
        val provinceName: String
    )
}
