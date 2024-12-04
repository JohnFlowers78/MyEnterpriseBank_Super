package br.com.meusite.myenterprisebank.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,                  // ID gerado automaticamente pelo Room.
    val cpf: String,                  // Identificador único para o usuário.
    val email: String,                // E-mail do usuário.
    val nomeCompleto: String,         // Nome completo.
    val celular: String,              // Telefone do usuário.
    val saldo: Double = 0.0,          // Saldo inicial (0 por padrão).
    val imgPerfil: Int? = null,       // ID do recurso drawable para a imagem.
    val firebaseId: String = "",      // ID associado ao Firebase Authentication.
    val pin: String = "0000"          // PIN padrão inicial (deve ser atualizado pelo usuário posteriormente).
)