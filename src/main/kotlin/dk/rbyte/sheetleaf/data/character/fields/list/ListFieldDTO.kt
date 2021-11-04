package dk.rbyte.sheetleaf.data.character.fields.list

import dk.rbyte.sheetleaf.data.character.fields.DataField

data class ListFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any
) : DataField