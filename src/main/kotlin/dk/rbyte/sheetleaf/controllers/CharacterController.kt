package dk.rbyte.sheetleaf.controllers

import dk.rbyte.sheetleaf.data.character.CharacterDAO
import dk.rbyte.sheetleaf.data.character.CharacterDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CharacterController {
    val dao = CharacterDAO()

    @GetMapping("/api/character/getByProfile/{id}")
    fun getByProfile(@PathVariable id: Int): ResponseEntity<ArrayList<CharacterDTO>>{
        val array = dao.getCharactersFromProfile(id) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(array, HttpStatus.OK)
    }

    @PostMapping("/api/character/create")
    fun CreateCharacter(@RequestBody character: CharacterDTO): ResponseEntity<CharacterDTO>{
        val newCharacter = dao.CreateCharacter(character)?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity(newCharacter, HttpStatus.OK)
    }
}