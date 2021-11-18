package dk.rbyte.sheetleaf.data.character.fields.longString

import dk.rbyte.sheetleaf.data.character.fields.DataField
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes

data class LStringFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any,
    override var type: FieldTypes = FieldTypes.LONG_STRING_FIELD
) : DataField {
}