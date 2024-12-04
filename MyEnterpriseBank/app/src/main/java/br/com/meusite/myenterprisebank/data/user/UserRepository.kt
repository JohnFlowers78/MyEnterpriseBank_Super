package br.com.meusite.myenterprisebank.data.user

import androidx.lifecycle.LiveData
import br.com.meusite.myenterprisebank.data.UserWithRelations

class UserRepository(private val userDao: UserDAO) {

    val listUsuarios: LiveData<List<User>> = userDao.listUsuarios()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    //    suspend fun insertUsuario(cpf: String, email: String, senha: String): Long
    suspend fun updateUsuario(user: User){
        userDao.updateUsuario(user)
    }

    suspend fun deleteUsuario(user: User){
        userDao.deleteUsuario(user)
    }

    // Consulta o único usuário com suas transações e caixinhas
    fun getUserWithRelations(userId: Int): LiveData<UserWithRelations> {
        return userDao.getUserWithRelations(userId)
    }

    suspend fun atualizarSaldo(novoSaldo: Double, userId: Int = 1) {
        userDao.atualizarSaldo(novoSaldo, userId)
    }

    // Precisa de Mudanças ¬
//    @Query("SELECT saldo FROM users WHERE userId = 1 LIMIT 1")
    suspend fun getSaldo() {
        userDao.getSaldo()
    }

//  Para pegar saldo de algum user específico  --->  quando o App evoluir
    suspend fun getSaldoUsuario(userId: Int) {
        userDao.getSaldoUsuario(userId)
    }

    suspend fun saveUser(user: User) {
        userDao.saveUser(user)
    }

    fun getAllSavedUsers(): List<User> {
        return userDao.getAllSavedUsers()
    }
}


//  Retorna todas as transações associadas ao user com ID = 1
//    suspend fun getTransacoes(): List<Transacao>   // para implementarmos uma "busca" dentro do extrato !!!!!    <---------------------  FUTURAMENTE


//    suspend fun getUserCount(): Int