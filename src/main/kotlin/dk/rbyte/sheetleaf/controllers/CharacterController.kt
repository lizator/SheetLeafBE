package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.character.CharacterCollectionDTO
import dk.rbyte.sheetleaf.data.character.CharacterDAO
import dk.rbyte.sheetleaf.data.character.CharacterDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CharacterController {
    val dao = CharacterDAO()

    @GetMapping("/api/character/getByProfile/{profileID}")
    fun getByProfile(@PathVariable profileID: Int): ResponseEntity<ArrayList<CharacterDTO>>{
        val array = dao.getCharactersFromProfile(profileID) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(array, HttpStatus.OK)
    }

    @GetMapping("/api/character/getByID/{characterID}")
    fun getByID(@PathVariable characterID: Int): ResponseEntity<CharacterCollectionDTO>{
        val charCollectionDTO = dao.getCharacterByID(characterID) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(charCollectionDTO, HttpStatus.OK)
    }

    @PostMapping("/api/character/create")
    fun CreateCharacter(@RequestBody collection: CharacterCollectionDTO): ResponseEntity<CharacterCollectionDTO>{
        val newCharacterCollection = dao.createCharacterCollection(collection)?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(newCharacterCollection, HttpStatus.OK)
    }

    @PostMapping("/api/character/update")
    fun updateCharacter(@RequestBody collection: CharacterCollectionDTO): ResponseEntity<CharacterCollectionDTO>{
        val newCharacter = dao.createCharacterCollection(collection)?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(newCharacter, HttpStatus.OK)
    }
}