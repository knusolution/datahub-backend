package com.knusolution.datahub.system.controll

import com.knusolution.datahub.system.application.BaseCategoryResponse
import com.knusolution.datahub.system.application.DetailCategoryResponse
import com.knusolution.datahub.system.application.SystemService
import com.knusolution.datahub.system.domain.asDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SystemController(
    private val systemService: SystemService,
) {
    @GetMapping("/base-category")
    fun getBaseCategory(@RequestParam systemId:Long):BaseCategoryResponse?
    {
        if(!systemService.existsDbSystem(systemId)) return null
        val system = systemService.getDbSystem(systemId)
        val baseCategories = systemService.getBaseCategories(systemId).map{it.asDto()}
        return BaseCategoryResponse(systemName = system.systemName, baseCategories = baseCategories)
    }

    @GetMapping("/detail-category")
    fun getDetailCategory(@RequestParam baseCategoryId:Long):DetailCategoryResponse?
    {
        if(!systemService.existsBaseCategory(id = baseCategoryId)) return null
        val detailCategories = systemService.getDetailCategories(id = baseCategoryId).map{it.asDto()}
        return DetailCategoryResponse(detailCategories = detailCategories)
    }
}