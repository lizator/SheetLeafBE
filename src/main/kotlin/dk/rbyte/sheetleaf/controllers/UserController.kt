package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.user.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<Any?> {
        return ResponseEntity("Hello World!", HttpStatus.OK)
    }


    @GetMapping("/hello2")
    fun hello2(): ResponseEntity<UserDTO> {
        return ResponseEntity(UserDTO(email = "Hello@World.!!"), HttpStatus.OK)
    }

}