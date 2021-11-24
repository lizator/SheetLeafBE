package dk.rbyte.sheetleaf.data.character

import dk.rbyte.sheetleaf.data.PostgresDB
import dk.rbyte.sheetleaf.data.character.fields.DataField
import dk.rbyte.sheetleaf.data.character.fields.FieldDAO
import dk.rbyte.sheetleaf.data.character.fields.longString.LStringFieldDTO
import dk.rbyte.sheetleaf.data.character.fields.realNumber.IntFieldDTO
import dk.rbyte.sheetleaf.data.character.fields.shortString.SStringFieldDTO
import java.sql.ResultSet
import java.sql.SQLException

class CharacterDAO {
    val fieldDAO = FieldDAO()

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

                val arr = fieldDAO.getCharacterFields(character.characterID!!)?: return null

                return CharacterCollectionDTO(character, arr.values as ArrayList<DataField>)
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

            val newCharacter = array[array.size-1]

            val sheet = newCharacter.sheet!!.split(",")

            val newArr = ArrayList<DataField>()

            for (i in 0..sheet.size-1) {
                var field: DataField
                var fieldID = sheet[i]
                var datafield = collection.fields.get(i)
                when (fieldID[0]) {
                    'S' -> {
                        //Short string field
                        field = SStringFieldDTO(fieldID, newCharacter.characterID!!, datafield.title, datafield.value)
                    }
                    'L' -> {
                        //Long string field
                        field = LStringFieldDTO(fieldID, newCharacter.characterID!!, datafield.title, datafield.value)
                    }
                    'R' -> {
                        //Real number field
                        field = IntFieldDTO(fieldID, newCharacter.characterID!!, datafield.title, datafield.value)
                    }
                    else -> {
                        //error
                        return null
                    }
                }
                val newField = fieldDAO.createField(field)?: return null
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
                "UPDATE character SET " +
                        "name = ?, " +
                        "profileid = ?, " +
                        "gameid = ?, " +
                        "sheet = ? " +
                        "WHERE characterid = ?;",
                arrayOf(
                    characterCollectionDTO.character.name,
                    characterCollectionDTO.character.profileID.toString(),
                    characterCollectionDTO.character.gameID.toString(),
                    characterCollectionDTO.character.sheet.toString()
                )
            )
            db.close()


            for (dataField in characterCollectionDTO.fields)
                fieldDAO.updateField(dataField)


            return getCharacterByID(characterCollectionDTO.character.characterID!!);
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