package dk.rbyte.sheetleaf.jwt

import dk.rbyte.sheetleaf.data.user.ProfileDAO
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class MyUserDetailsService : UserDetailsService {
    val profileDAO = ProfileDAO()

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String): UserDetails {
        val profile = profileDAO.getProfileByEmail(s) ?: throw UsernameNotFoundException("Not found")
        return User(
            profile.user!!.email, profile.pass+","+profile.salt,
            ArrayList()
        )
    }
}