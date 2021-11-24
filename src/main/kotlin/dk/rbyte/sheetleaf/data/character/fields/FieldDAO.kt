package dk.rbyte.sheetleaf.data.character.fields

import dk.rbyte.sheetleaf.data.PostgresDB
import dk.rbyte.sheetleaf.data.character.fields.longString.LStringFieldDTO
import dk.rbyte.sheetleaf.data.character.fields.realNumber.IntFieldDTO
import dk.rbyte.sheetleaf.data.character.fields.shortString.SStringFieldDTO
import java.sql.ResultSet
import java.sql.SQLException

class FieldDAO {

    public fun createField(field: DataField): DataField? {
        val db = PostgresDB()

        var tableName = ""
        var stringArray: Array<String>? = arrayOf(field.id, field.characterID.toString(), field.title, field.value.toString())

        when (field) {
            is SStringFieldDTO -> {
                tableName = "sstringfields"
            }
            is LStringFieldDTO -> {
                tableName = "lstringfields"
            }
            is IntFieldDTO -> {
                tableName = "intfields"
            }
            else -> {
                //Not implemented
                return null
            }
        }

        try {
            db.initialize()

            db.update("INSERT INTO $tableName (id, characterid, title, value) VALUES (?,?,?,?)", stringArray!!)

            db.close()

            return getFieldByID(field.id, field.characterID)

        } catch (e: SQLException) {
            e.spliterator()
            return null
        }
    }

    public fun updateField(field: DataField): DataField? {
        val db = PostgresDB()

        try {
            db.initialize()

            var tableName = ""

            when (field) {
                is SStringFieldDTO -> {
                    tableName = "sstringfields"
                }
                is LStringFieldDTO -> {
                    tableName = "lstringfields"
                }
                is IntFieldDTO -> {
                    tableName = "intfields"
                }
                else -> {
                    //Not implemented
                    return null
                }
            }


            db.update("UPDATE $tableName SET " +
                    "title = ?, " +
                    "value = ? WHERE" +
                    "id = ? AND " +
                    "characterid = ?", arrayOf(
                    field.title,
                    field.value.toString(),
                    field.id,
                    field.characterID.toString()
            ))
            db.close()

            return getFieldByID(field.id, field.characterID)


        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    public fun getFieldByID(fieldID: String, characterID: Int): DataField? {
        val db = PostgresDB()

        try {
            db.initialize()

            var tableName = ""

            when (fieldID[0]) {
                'S' -> {
                    tableName = "sstringfields"
                }
                'L' -> {
                    tableName = "lstringfields"
                }
                'R' -> {
                    tableName = "intfields"
                }
                else -> {
                    //Not implemented
                    return null
                }
            }

            val res = db.query("SELECT * FROM $tableName WHERE " +
                    "id = ? AND " +
                    "characterid = ?", arrayOf(
                    fieldID, characterID.toString()
                    ))?: return null

            res.next()

            return getDataFieldFromResult(res, tableName)

        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }


    }

    public fun getCharacterFields(characterID: Int, tableName: String = "all"): HashMap<String, DataField>? {

        when (tableName) {
            "all" -> {
                val hashMap = HashMap<String,DataField>()
                for (name in arrayOf("sstringfields","lstringfields","intfields")) {
                    val gottenArr = getCharacterFields(characterID,name) ?: return null
                    hashMap.putAll(gottenArr)
                }
                return hashMap
            }
            else -> {
                val db = PostgresDB()

                val hashMap = HashMap<String, DataField>()

                try {
                    db.initialize()

                    val res =
                        db.query("SELECT * FROM $tableName WHERE characterid = ?", arrayOf(characterID.toString()))
                            ?: return null

                    while(res.next()) {
                        val dataField = getDataFieldFromResult(res, tableName) ?: return null
                        hashMap.put(dataField.id, dataField)
                    }

                    res.close()
                    db.close()
                    return hashMap

                } catch (e: SQLException) {
                    e.printStackTrace()
                    return null
                }

            }
        }

    }

    private fun getDataFieldFromResult(res: ResultSet, tableName: String): DataField? {
        var dataField: DataField?
        when (tableName) {
            "sstringfield" -> {
                dataField = SStringFieldDTO(
                    res.getString("id"),
                    res.getInt("characterid"),
                    res.getString("title"),
                    res.getString("value")
                )
            }
            "lstringfield" -> {
                dataField = LStringFieldDTO(
                    res.getString("id"),
                    res.getInt("characterid"),
                    res.getString("title"),
                    res.getString("value")
                )
            }
            "intfield" -> {
                dataField = SStringFieldDTO(
                    res.getString("id"),
                    res.getInt("characterid"),
                    res.getString("title"),
                    res.getInt("value")
                )
            }
            else -> return null
        }
        return dataField
    }

}