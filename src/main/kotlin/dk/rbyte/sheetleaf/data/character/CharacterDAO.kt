package dk.rbyte.sheetleaf.data.character

import dk.rbyte.sheetleaf.data.PostgresDB
import java.sql.ResultSet
import java.sql.SQLException

class CharacterDAO {

    fun getCharactersFromProfile(profileID: Int): ArrayList<CharacterDTO>? {
        val db = PostgresDB()
        try {
            db.initialize()
            val res = db.query("SELECT * FROM characters WHERE profileid = ?", arrayOf<String>(profileID.toString()))
                ?: return null

            val array = ArrayList<CharacterDTO>()

            while (res.next()){
                array.add(getCharacterFromResultset(res))
            }
            res.close()
            db.close()

            return array

        } catch (e: SQLException) {
            return null
        }
    }

    fun getCharacterByID(characterID: Int): CharacterDTO?{
        val db = PostgresDB()
        try {
            db.initialize()
            val res = db.query("SELECT * FROM characters WHERE characterid = ?", arrayOf<String>(characterID.toString()))
                ?: return null

            if (!res.next()) {
                res.close()
                db.close()
                return null
            } else {
                val character = getCharacterFromResultset(res)
                res.close()
                db.close()
                return character
            }

        } catch (e: SQLException) {
            return null
        }
    }

    fun createCharacter(character: CharacterDTO): CharacterDTO? {
        val db = PostgresDB()
        try {
            db.initialize()
            db.update(
                "INSERT INTO characters (name, profileid, gameid, sheet) VALUES (?,?,?,?)",
                arrayOf(
                    character.name,
                    character.profileID.toString(),
                    character.gameID.toString(),
                    character.sheet.toString()
                )
            )
            db.close()

            val array = getCharactersFromProfile(character.profileID)?: return null

            return array[array.size-1]

        } catch (e: SQLException) {
            return null
        }
    }

    fun updateCharacter(character: CharacterDTO): CharacterDTO? {
        val db = PostgresDB()
        try {
            db.initialize()
            db.update(
                "UPDATE character SET " +
                        "name = ?, " +
                        "profileid = ?, " +
                        "gameid = ?, " +
                        "sheet = ? " +
                        "WHERE characterid = ?;",
                arrayOf(
                    character.name,
                    character.profileID.toString(),
                    character.gameID.toString(),
                    character.sheet.toString()
                )
            )
            db.close()



            return getCharacterByID(character.characterID!!);
        } catch (e: SQLException) {
            return null
        }
    }


    private fun getCharacterFromResultset(res: ResultSet): CharacterDTO {
        return CharacterDTO(
            res.getInt("characterid"),
            res.getString("name"),
            res.getInt("profileid"),
            res.getInt("gameid"),
            res.getString("sheet")
        )

    }
}