package dk.rbyte.sheetleaf.data

import java.sql.*
import java.util.*
import kotlin.Array
import kotlin.IllegalArgumentException
import kotlin.NumberFormatException
import kotlin.Throws


class PostgresDB {


    private val PORT = 5432
    private val HOST = "ec2-54-73-152-36.eu-west-1.compute.amazonaws.com"
    private val DBNAME = "dabt771d470ola"
    private val url = "jdbc:postgresql://$HOST:$PORT/$DBNAME?characterEncoding=latin1&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT&autoReconnect=true"
    private val props = Properties()
    private var conn: Connection? = null
    private var initialized = false
    //private var stmt: Statement? = null

    fun isInitialized(): Boolean {
        return initialized
    }

    fun initialize(){
        if (!initialized) {
            props.setProperty("user", "qgayrkwvvurdop")
            props.setProperty("password", "cf5b37f4facfb3ea55cb4ad4157cf3bd3cd37a7cf70baba456e9af8dab441eeb")
            //props.setProperty("ssl", "true")
            conn = DriverManager.getConnection(url, props)
            initialized = true
        }
    }

    fun close(){
        if (initialized) {
            conn?.close()
            initialized = false
        }
    }

    @Throws(SQLException::class)
    fun update(query: String?, strings: Array<String>) {
        if (initialized) {
            val stmt = prepareStatement(query, strings)
            stmt.executeUpdate()
        }
    }


    @Throws(SQLException::class)
    fun query(query: String?, strings: Array<String>): ResultSet? {
        var result: ResultSet? = null
        if (!initialized) {
            println("Connect to a DB first")
        } else {
            val stmt = prepareStatement(query, strings)
            result = stmt.executeQuery()
        }
        return result
    }


    private fun prepareStatement(query: String?, strings: Array<String>): PreparedStatement {
        val stmt = conn!!.prepareStatement(query) // SELECT * FROM user WHERE email = ?
        for (i in strings.indices) {
            try {
                val insert = strings[i].toInt()
                stmt.setInt(i + 1, insert)
            } catch (e: NumberFormatException) {
                try {
                    if (strings[i].split("T").toTypedArray().size == 2) {
                        val s = strings[i].split("T").toTypedArray()[0] + " " + strings[i].split("T")
                            .toTypedArray()[1] + ":00"
                        val insert = Timestamp.valueOf(s)
                        stmt.setTimestamp(i + 1, insert)
                    } else {
                        throw IllegalArgumentException()
                    }
                } catch (e2: IllegalArgumentException) {
                    if (strings[i] == "true" || strings[i] == "false") {
                        stmt.setBoolean(i + 1, strings[i].toBoolean())
                    } else stmt.setString(i + 1, strings[i])
                }
            }
        }
        return stmt
    }



}
