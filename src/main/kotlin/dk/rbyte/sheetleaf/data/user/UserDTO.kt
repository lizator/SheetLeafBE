package dk.rbyte.sheetleaf.data.user

data class UserDTO(var id: Int? = null,
                   var email: String? = null,
                   var name: String? = null,
                   var token: String? = null) {
}