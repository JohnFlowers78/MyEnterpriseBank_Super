package br.com.meusite.myenterprisebank.data.user.login_cadastro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.user.User
import br.com.meusite.myenterprisebank.data.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val auth = FirebaseAuth.getInstance()

    val savedUsers = MutableLiveData<List<User>>()
    val mensagemErro = MutableLiveData<String>()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun loadSavedUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = repository.getAllSavedUsers()
            savedUsers.postValue(users)
        }
    }

    fun loginUser(email: String, password: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        onSuccess(it)
                    } ?: onFailure("Usuário não encontrado.")
                } else {
                    onFailure("Erro ao autenticar: ${task.exception?.message}")
                }
            }
    }
}
