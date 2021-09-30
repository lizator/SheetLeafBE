package dk.rbyte.sheetleaf.data.user

//Profile has all fields of the Profiles table in DB. User is used to communicate userdata and JWT token to FE without password and salt
data class ProfileDTO(var user: UserDTO? = null,
                      var pass: String? = null,
                      var salt: String? = null) {


}