package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.user.UserDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }


    @GetMapping("/hello2")
    fun hello2(): UserDTO {
        return UserDTO("Hello@World.!!")
    }

}