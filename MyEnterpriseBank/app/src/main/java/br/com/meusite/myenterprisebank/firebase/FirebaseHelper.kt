package br.com.meusite.myenterprisebank.firebase

import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {

    /**
     * Cadastra o usuário no Firebase Realtime Database.
     * @param userId ID único do usuário (Firebase UID).
     * @param nome Nome do usuário.
     * @param cpf CPF do usuário.
     * @param celular Número de celular.
     * @param callback Callback para tratar sucesso ou erro.
     */
    fun cadastrarUsuario(
        userId: String,
        nome: String,
        cpf: String,
        celular: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance().getReference("users/$userId")
        val userMap = mapOf(
            "id" to userId,
            "nome" to nome,
            "cpf" to cpf,
            "celular" to celular,
            "saldo" to 0.0
        )

        database.setValue(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }
}