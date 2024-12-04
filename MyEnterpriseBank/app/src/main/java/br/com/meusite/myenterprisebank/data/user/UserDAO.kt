package br.com.meusite.myenterprisebank.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.meusite.myenterprisebank.data.UserWithRelations

@Dao
interface UserDAO {
    @Query("SELECT * FROM users")
    fun listUsuarios(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertUsuario(cpf: String, email: String, senha: String): Long

    @Update
    fun updateUsuario(user: User)

    @Delete
    fun deleteUsuario(user: User)

    // Consulta o único usuário com suas transações e caixinhas
    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserWithRelations(userId: Int): LiveData<UserWithRelations>     //    fun getUserWithRelations(userId: Int ): LiveData<UserWithRelations>


    @Query("UPDATE users SET saldo = :novoSaldo WHERE userId = :userId")
    fun atualizarSaldo(novoSaldo: Double, userId: Int = 1)


    @Query("SELECT saldo FROM users WHERE userId = 1 LIMIT 1")  // Precisa de Mudanças
    fun getSaldo(): Double

    // Para pegar saldo de algum user específico  --->  quando o App evoluir
    @Query("SELECT saldo FROM users WHERE userId = :userId")
    fun getSaldoUsuario(userId: Int): Double

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Query("SELECT * FROM users LIMIT 5")
    fun getAllSavedUsers(): List<User>
}



// Retorna todas as transações associadas ao user com ID = 1
//    @Query("SELECT * FROM transacoes WHERE userId = 1")
//    suspend fun getTransacoes(): List<Transacao>   // para implementarmos uma "busca" dentro do extrato !!!!!    <---------------------  FUTURAMENTE

//    @Query("SELECT COUNT(*) FROM users")
//    fun getUserCount(): Int


//    // Métodos adicionados para verificar existência de CPF e e-mail
//    @Query("SELECT COUNT(*) > 0 FROM users WHERE cpf = :cpf")
//    fun getUserByCpf(cpf: String): Boolean


//    @Query("SELECT COUNT(*) > 0 FROM users WHERE email = :email")
//    fun getUserByEmail(email: String): Boolean