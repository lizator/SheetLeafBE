package dk.rbyte.sheetleaf.data.login

import dk.rbyte.sheetleaf.data.user.UserDTO

data class LoginDTO(var email: String? = null,
                    var password: String? = null,
                    var user: UserDTO? = null) {}