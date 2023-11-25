package com.knusolution.datahub.application

import com.knusolution.datahub.domain.*
import com.knusolution.datahub.system.*
import org.springframework.stereotype.Service
import com.knusolution.datahub.domain.UserRepository
@Service
class LoginService(
    private val userRepository: UserRepository,
    private val systemRepository:SystemRepository,
    private val baseCategoryRepository: BaseCategoryRepository,
    private val detailCategoryRepository: DetailCategoryRepository,
){

    fun registerUser(req: JoinRequest){
        val system = systemRepository.save(req.asSystemDto().asEntity())
        val user = req.asUserDto().asEntity(password = req.loginId)
        user.systems.add(system)
        userRepository.save(user)

        registerCategory(system)
    }
    private fun registerCategory(system: SystemEntity)
    {
        getBaseCategory().forEach {
            val baseCategoryEntity = it.asEntity(system)
            baseCategoryRepository.save(baseCategoryEntity)
            getDetailCategory(it.baseCategoryName).forEach {
                    detail-> detailCategoryRepository.save(detail.asEntity(baseCategoryEntity))
            }
        }
    }
    fun loginUser(req: LoginRequest): LoginResponse?
    {
        val userEntity = userRepository.findByLoginId(req.loginId)
        val userDto = userEntity?.let {
            val systems = it.systems
            if(req.password == it.password) it.asUserDto() else null
        }
        return userDto?.asLoginResponse(accessToken = "")
    }
    fun exitsUserByLoginId(loginId:String) = userRepository.existsByLoginId(loginId)
}