package dk.rbyte.sheetleaf.data.login

import java.security.SecureRandom
import java.security.spec.KeySpec
import java.time.LocalDateTime
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class PasswordHandler {

    fun convertSalt(Ssalt:String): ByteArray {
        val salt = ByteArray(16) //converting salt to byte arr
        for (i in 0..15) {
            val first = 2 * i
            val second = first + 1
            val hex = Ssalt[first].toString() + "" + Ssalt[second] //getting the next 2 hexadecimals to convert to byte
            val x = hex.toLong(16) - 128 //Converting back to having numbers between -128 - 127
            salt[i] = x.toByte()
        }
        return salt
    }

    fun checkPass(pass: String, passHash: String, Ssalt: String): Boolean {
        val salt = convertSalt(Ssalt)
        val genP = encryptPassword(pass, salt) //Generates passhash from password getting testet and given salt: Pair<String?, String?>
        return genP.first == passHash // returns true if password is correct
    }

    /*fun generatePassHash(pass: String, salt: ByteArray?): String {
        return try {
            val passHash = hash(pass, salt)
            var genPass = ""
            for (i in 0..15) {
                var gen = Integer.toHexString(passHash[i] + 128) //adding 128 to have numbers between 0 - 255
                if (gen.length < 2) {
                    gen = "0$gen"
                }
                genPass += gen
            }
            genPass
        } catch (e: Exception) {
            e.printStackTrace()
            e.toString()
        }
    }*/

    fun encryptPassword(password: String, salt: ByteArray? = null): Pair<String?, String?> {
        val realSalt: ByteArray
        when {
            salt == null -> realSalt = createSalt()
            else -> realSalt = salt
        }
        val passHash = hash(password, realSalt)
        var genPass: String? = ""
        var genSalt: String? = ""
        for (i in 0..15) {
            var gen = Integer.toHexString(passHash[i] + 128)
            var gen2 = Integer.toHexString(realSalt[i] + 128)
            if (gen.length < 2) {
                gen = "0$gen"
            }
            if (gen2.length < 2) {
                gen2 = "0$gen2"
            }
            genPass += gen
            genSalt += gen2
        }

        return Pair(genPass, genSalt)
    }

    fun hash(pass: String, salt: ByteArray?): ByteArray {
        val spec = PBEKeySpec(pass.toCharArray(), salt, 65536, 128)
        val factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512")
        return factory.generateSecret(spec).encoded
    }

    fun createSalt(): ByteArray {
        val random = SecureRandom()
        val datetime = LocalDateTime.now().toString().toByteArray()
        random.setSeed(datetime)
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

}