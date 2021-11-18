package dk.rbyte.sheetleaf.data.character.fields

interface DataField {
    var id: String
    var characterID: Int
    var title: String
    var value: Any
    var type: FieldTypes
}