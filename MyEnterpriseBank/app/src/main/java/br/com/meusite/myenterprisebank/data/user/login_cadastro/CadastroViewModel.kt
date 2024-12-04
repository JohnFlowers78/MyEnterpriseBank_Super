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

class CadastroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val auth = FirebaseAuth.getInstance()

    val mensagemErro = MutableLiveData<String>()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun cadastrarUsuario(
        nomeCompleto: String,
        cpf: String,
        email: String,
        celular: String,
        password: String
    ) {
        if (nomeCompleto.isBlank() || cpf.isBlank() || email.isBlank() || celular.isBlank() || password.isBlank()) {
            mensagemErro.value = "Todos os campos devem ser preenchidos."
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val firebaseUser = result.user
                val user = User(
                    cpf = cpf,
                    email = email,
                    nomeCompleto = nomeCompleto,
                    celular = celular,
                    firebaseId = firebaseUser?.uid ?: ""
                )
                salvarUsuarioNoBanco(user)
            }
            .addOnFailureListener { exception ->
                mensagemErro.value = "Erro ao cadastrar: ${exception.message}"
            }
    }

    private fun salvarUsuarioNoBanco(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }
}