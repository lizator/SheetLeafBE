package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.login.LoginDTO
import dk.rbyte.sheetleaf.data.login.PasswordHandler
import dk.rbyte.sheetleaf.data.user.ProfileDAO
import dk.rbyte.sheetleaf.data.user.ProfileDTO
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.KeyPairGenerator
import java.security.interfaces.RSAKey
import java.util.*


@RestController
class LoginController {
    val profileDAO = ProfileDAO()
    val passHandler = PasswordHandler()

    /*
    @PostMapping("/profile/login")
    fun login(@RequestBody loginDTO: LoginDTO?): ResponseEntity<Any>{
        // Will check if user exists and then if credentials are correct

        val profile = profileDAO.getProfileByEmail(loginDTO!!.email)
            ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        //If no user with that email
        if (profile.user!!.id == -1) return ResponseEntity(HttpStatus.NOT_FOUND)

        //Here i have found the user, now to find if the password is correct

        if (!passHandler.checkPass(loginDTO.password!!, profile.pass!!, profile.salt!!)) {
            //password incorrect
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        //TODO get Secret from DB

        //password correct
        //Creating JWT

        val issuer = profile.user!!.id!!.toString()


        val jwt =  Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(SignatureAlgorithm.ES512, "secret")
            .compact()

        return ResponseEntity(jwt, HttpStatus.OK)

    }

    @PostMapping("/profile/create")
    fun create(@RequestBody profileDTO: ProfileDTO): ResponseEntity<ProfileDTO>{
        // real Password in pass of userDTO
        val pass = profileDTO.pass!!

        // Will check if user with email already exists
        val profile = profileDAO.getProfileByEmail(profileDTO.user!!.email) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        //If no user with that email
        if (profile.user!!.id != -1) return ResponseEntity(HttpStatus.CONFLICT)


        // Will generate salt with random time seed
        val pair = passHandler.encryptPassword(pass)

        profileDTO.pass = pair.first
        profileDTO.salt = pair.second

        //Creating profile in DB
        val newProfile = profileDAO.createProfile(profileDTO) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        return ResponseEntity(newProfile, HttpStatus.OK)
    }
    */

}