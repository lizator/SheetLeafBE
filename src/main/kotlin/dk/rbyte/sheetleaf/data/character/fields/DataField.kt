package dk.rbyte.sheetleaf.data.character.fields

data class DataField (
    open var id: String,
    open var characterID: Int,
    open var title: String,
    open var value: Any,
    open var type: FieldTypes
)