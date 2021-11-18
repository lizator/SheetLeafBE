package dk.rbyte.sheetleaf.data.character.fields.line

import dk.rbyte.sheetleaf.data.character.fields.DataField
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes

data class LineFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any,
    override var type: FieldTypes = FieldTypes.LINE_FIELD
) : DataField