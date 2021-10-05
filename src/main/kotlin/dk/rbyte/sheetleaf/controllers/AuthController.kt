package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.login.LoginDTO
import dk.rbyte.sheetleaf.data.login.PasswordHandler
import dk.rbyte.sheetleaf.data.user.ProfileDAO
import dk.rbyte.sheetleaf.data.user.ProfileDTO
import dk.rbyte.sheetleaf.jwt.JwtRequestFilter
import dk.rbyte.sheetleaf.jwt.JwtUtil
import dk.rbyte.sheetleaf.jwt.MyUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.bind.annotation.*


@RestController
class AuthController {
    val profileDAO = ProfileDAO()
    val passHandler = PasswordHandler()

    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    private val jwtTokenUtil: JwtUtil? = null

    @Autowired
    private val userDetailsService: MyUserDetailsService? = null

    @PostMapping("/open/profile/authenticate")
    @Throws(Exception::class)
    fun createAuthenticationToken(@RequestBody loginDTO: LoginDTO): ResponseEntity<Any> {
        val profile = profileDAO.getProfileByEmail(loginDTO.email) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val hashedPair = passHandler.encryptPassword(loginDTO.password!!, passHandler.convertSalt(profile.salt))

        if (profile.pass != hashedPair.first) return ResponseEntity(HttpStatus.FORBIDDEN)

        val userDetails: UserDetails = userDetailsService!!
            .loadUserByUsername(loginDTO.email!!)
        val jwt: String = jwtTokenUtil!!.generateToken(userDetails)
        profile.user.token = jwt
        return ResponseEntity.ok<Any>(profile.user)
    }

    @PostMapping("/open/profile/create")
    fun create(@RequestBody profileDTO: ProfileDTO): ResponseEntity<ProfileDTO>{
        // real Password in pass of userDTO
        val pass = profileDTO.pass

        // Will check if user with email already exists
        val profile = profileDAO.getProfileByEmail(profileDTO.user.email) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        //If no user with that email
        if (profile.user.id != -1) return ResponseEntity(HttpStatus.CONFLICT)


        // Will generate salt with random time seed
        val pair = passHandler.encryptPassword(pass)

        profileDTO.pass = pair.first
        profileDTO.salt = pair.second

        //Creating profile in DB
        val newProfile = profileDAO.createProfile(profileDTO) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        return ResponseEntity(newProfile, HttpStatus.OK)
    }
}

@EnableWebSecurity
internal class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private val myUserDetailsService: UserDetailsService? = null

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null
    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(myUserDetailsService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.csrf().disable()
            .authorizeRequests().antMatchers("/open/**").permitAll().anyRequest().authenticated().and()
            .exceptionHandling().and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}