package dk.rbyte.sheetleaf.data.user

import dk.rbyte.sheetleaf.data.PostgresDB
import java.sql.ResultSet
import java.sql.SQLException

class ProfileDAO {

    fun getProfileByEmail(email: String?): ProfileDTO?{
        if (email == null) return null
        val db = PostgresDB()
        try {
            db.initialize()
            val res = db.query("SELECT * FROM profiles WHERE email = ?", arrayOf<String>(email))
                ?: return null //

            if (!res.next()){
                //No user found
                return ProfileDTO(user = UserDTO(id = -1))
            } else {
                return getProfileFromResult(res)
            }
        } catch (e: SQLException){
            //DB error, should not happen in production
            e.printStackTrace()
            return null
        } finally {
            db.close()
        }
    }

    fun createProfile(profileDTO: ProfileDTO): ProfileDTO?{
        val db = PostgresDB()
        try {
            db.initialize()
            db.update("INSERT INTO profiles (name, email, pass, salt) " +
                    "Values (?,?,?,?)", arrayOf<String>(profileDTO.user!!.name!!, profileDTO.user!!.email!!, profileDTO.pass!!, profileDTO.salt!!))

            db.close()
            return getProfileByEmail(profileDTO.user!!.email)
        } catch (e: SQLException){
            //DB error, should not happen in production
            e.printStackTrace()
            return null
        }
    }

    fun getProfileFromResult(res: ResultSet): ProfileDTO{
        val profile = ProfileDTO()
        val id = res.getInt("profileid")
        val name = res.getString("name")
        val email = res.getString("email")
        profile.user = UserDTO(id = id, name = name, email = email)
        profile.pass = res.getString("pass")
        profile.salt = res.getString("salt")
        return profile
    }

}