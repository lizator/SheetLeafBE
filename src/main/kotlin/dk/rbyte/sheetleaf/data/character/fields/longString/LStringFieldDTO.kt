package dk.rbyte.sheetleaf.data.character.fields.longString

import dk.rbyte.sheetleaf.data.character.fields.DataField

data class LStringFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any
) : DataField {
}