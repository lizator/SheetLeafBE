package dk.rbyte.sheetleaf.data.character

import dk.rbyte.sheetleaf.data.PostgresDB
import dk.rbyte.sheetleaf.data.character.fields.DataField
import dk.rbyte.sheetleaf.data.character.fields.FieldDAO
import java.sql.ResultSet
import java.sql.SQLException

class CharacterDAO {
    val fieldDAO = FieldDAO()

    fun getCharactersFromProfile(profileID: Int): ArrayList<CharacterDTO>? {
        val db = PostgresDB()
        try {
            db.initialize()
            val res = db.query("SELECT * FROM characters WHERE profileid = ? ORDER BY characterid DESC", arrayOf<String>(profileID.toString()))
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

    fun getCharacterByID(characterID: Int): CharacterCollectionDTO?{
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

                val map = fieldDAO.getCharacterFields(character.characterID!!)?: return null

                val arr = ArrayList<DataField>()
                for (key in map.keys) {
                    val dto = map[key]?: return null
                    arr.add(dto)
                }

                return CharacterCollectionDTO(character, arr)
            }

        } catch (e: SQLException) {
            return null
        }
    }

    fun createCharacterCollection(collection: CharacterCollectionDTO): CharacterCollectionDTO? {
        val db = PostgresDB()
        val character = collection.character
        try {
            db.initialize()
            db.update(
                "INSERT INTO characters (name, profileid, sheet) VALUES (?,?,?)",
                arrayOf(
                    character.name,
                    character.profileID.toString(),
                    character.sheet.toString()
                )
            )

            db.close()


            val array = getCharactersFromProfile(character.profileID)?: return null

            val newCharacter = array[array.size-1]

            val sheet = newCharacter.sheet!!.split(",")

            val newArr = ArrayList<DataField>()

            for (i in 0..sheet.size-1) {
                var datafield = collection.fields[i]
                datafield.characterID = newCharacter.characterID!!
                val newField = fieldDAO.createField(datafield)?: return null
                newArr.add(newField)

            }
            return CharacterCollectionDTO(newCharacter, newArr)


        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }

    }

    fun updateCharacter(characterCollectionDTO: CharacterCollectionDTO): CharacterCollectionDTO? {
        val db = PostgresDB()
        try {
            db.initialize()
            db.update(
                "UPDATE characters SET " +
                        "name = ?, " +
                        "profileid = ?, " +
                        "sheet = ? " +
                        "WHERE characterid = ?;",
                arrayOf(
                    characterCollectionDTO.character.name,
                    characterCollectionDTO.character.profileID.toString(),
                    characterCollectionDTO.character.sheet.toString(),
                    characterCollectionDTO.character.characterID.toString()
                )
            )
            db.close()


            for (dataField in characterCollectionDTO.fields)
                fieldDAO.updateField(dataField)


            return getCharacterByID(characterCollectionDTO.character.characterID!!);
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }


    private fun getCharacterFromResultset(res: ResultSet): CharacterDTO {
        return CharacterDTO(
            res.getInt("characterid"),
            res.getString("name"),
            res.getInt("profileid"),
            0, //Not implemented games yet
            res.getString("sheet")
        )

    }
}