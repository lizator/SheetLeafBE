package dk.rbyte.sheetleaf.data.character

import dk.rbyte.sheetleaf.data.character.fields.DataField

data class CharacterCollectionDTO (var character:CharacterDTO, var fields: ArrayList<DataField>)