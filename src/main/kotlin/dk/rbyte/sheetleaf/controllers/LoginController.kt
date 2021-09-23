package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.login.LoginDTO
import dk.rbyte.sheetleaf.data.user.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class LoginController {

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO?): ResponseEntity<UserDTO>{
        // Will check if user exists and then if credentials are correct


        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/create")
    fun create(@RequestBody loginDTO: LoginDTO?): ResponseEntity<UserDTO>{
        // Will check if user already exists, or create a new one.
        // Will generate salt with random time seed


        return ResponseEntity(HttpStatus.CONFLICT)
    }



}