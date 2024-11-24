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
//import br.com.meusite.myenterprisebank.data.transacao.Transacao

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    fun listUsuarios(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUsuario(user: User)

    @Delete
    suspend fun deleteUsuario(user: User)

    // Consulta o único usuário com suas transações e caixinhas
    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserWithRelations(userId: Int = 1): LiveData<UserWithRelations>     //    fun getUserWithRelations(userId: Int ): LiveData<UserWithRelations>


    @Query("UPDATE users SET saldo = :novoSaldo WHERE id = :userId")
    suspend fun atualizarSaldo(novoSaldo: Double, userId: Int = 1)

    // Retorna todas as transações associadas ao user com ID = 1
//    @Query("SELECT * FROM transacoes WHERE userId = 1")
//    suspend fun getTransacoes(): List<Transacao>   // para implementarmos uma "busca" dentro do extrato !!!!!    <---------------------  FUTURAMENTE

    @Query("SELECT saldo FROM users WHERE id = 1 LIMIT 1")
    suspend fun getSaldo(): Double

//    @Query("SELECT saldo FROM users WHERE id = :userId")    // Para pegar saldo de algum user específico  --->  quando o App evoluir
//    suspend fun getSaldoUsuario(userId: Int): Double
}