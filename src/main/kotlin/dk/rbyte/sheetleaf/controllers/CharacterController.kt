package dk.rbyte.sheetleaf.controllers

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
    fun getByID(@PathVariable characterID: Int): ResponseEntity<CharacterDTO>{
        val charDTO = dao.getCharacterByID(characterID) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(charDTO, HttpStatus.OK)
    }

    @PostMapping("/api/character/create")
    fun CreateCharacter(@RequestBody character: CharacterDTO): ResponseEntity<CharacterDTO>{
        val newCharacter = dao.createCharacter(character)?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(newCharacter, HttpStatus.OK)
    }

    @PostMapping("/api/character/update")
    fun updateCharacter(@RequestBody character: CharacterDTO): ResponseEntity<CharacterDTO>{
        val newCharacter = dao.createCharacter(character)?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(newCharacter, HttpStatus.OK)
    }
}