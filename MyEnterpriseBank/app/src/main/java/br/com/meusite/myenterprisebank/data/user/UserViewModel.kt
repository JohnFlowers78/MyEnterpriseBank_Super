package br.com.meusite.myenterprisebank.data.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.UserWithRelations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.listUsuarios
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUsuario(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUsuario(user)
        }
    }

    fun deleteUsuario(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUsuario(user)
        }
    }
    fun getUserWithRelations(userId: Int): LiveData<UserWithRelations> {
        return repository.getUserWithRelations(userId)
    }

    fun atualizarSaldo(novoSaldo: Double, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.atualizarSaldo(novoSaldo, userId)
        }
    }
    fun getSaldo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSaldo()
        }
    }
    fun getSaldoUsuario(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSaldoUsuario(userId)
        }
    }
    fun saveUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUser(user)
        }
    }
    fun getAllSavedUsers(): List<User> {
    //        viewModelScope.launch(Dispatchers.IO) {
    //            repository.getAllSavedUsers()
    //        }
        return repository.getAllSavedUsers()
    }
}
