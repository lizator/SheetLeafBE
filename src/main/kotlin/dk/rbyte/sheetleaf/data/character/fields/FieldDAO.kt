package dk.rbyte.sheetleaf.data.character.fields

import dk.rbyte.sheetleaf.data.PostgresDB
import java.sql.ResultSet
import java.sql.SQLException

class FieldDAO {

    public fun createField(field: DataField): DataField? {
        val db = PostgresDB()

        var tableName = ""
        var stringArray: Array<String>? = arrayOf(field.id, field.characterID.toString(), field.title, field.value.toString())

        when (field.type) {
            FieldTypes.SHORT_STRING_FIELD -> {
                tableName = "sstringfields"
            }
            FieldTypes.LONG_STRING_FIELD-> {
                tableName = "lstringfields"
            }
            FieldTypes.REAL_NUMBER_FIELD -> {
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
            e.printStackTrace()
            return null
        }
    }

    public fun updateField(field: DataField): DataField? {
        val db = PostgresDB()

        try {
            db.initialize()

            var tableName = ""

            when (field.type) {
                FieldTypes.SHORT_STRING_FIELD -> {
                    tableName = "sstringfields"
                }
                FieldTypes.LONG_STRING_FIELD-> {
                    tableName = "lstringfields"
                }
                FieldTypes.REAL_NUMBER_FIELD -> {
                    tableName = "intfields"
                }
                else -> {
                    //Not implemented
                    return null
                }
            }


            db.update("UPDATE $tableName SET " +
                    "title = ?, " +
                    "value = ? WHERE " +
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

            val dataField = getDataFieldFromResult(res, tableName)
            res.close()
            db.close()

            return dataField

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
        var type: FieldTypes?
        when (tableName) {
            "sstringfields" -> {
                type = FieldTypes.SHORT_STRING_FIELD
            }
            "lstringfields" -> {
                type = FieldTypes.LONG_STRING_FIELD
            }
            "intfields" -> {
                type = FieldTypes.REAL_NUMBER_FIELD
            }
            else -> return null
        }
        val dataField = DataField(
            res.getString("id"),
            res.getInt("characterid"),
            res.getString("title"),
            res.getString("value"),
            type)
        return dataField
    }

}