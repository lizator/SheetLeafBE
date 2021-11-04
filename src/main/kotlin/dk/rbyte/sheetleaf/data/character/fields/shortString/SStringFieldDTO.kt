package dk.rbyte.sheetleaf.data.character.fields.shortString

import dk.rbyte.sheetleaf.data.character.fields.DataField

data class SStringFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any
) : DataField {
}