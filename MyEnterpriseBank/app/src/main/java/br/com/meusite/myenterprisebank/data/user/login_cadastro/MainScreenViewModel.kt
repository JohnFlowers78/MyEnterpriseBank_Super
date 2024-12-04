package br.com.meusite.myenterprisebank.data.user.login_cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import br.com.meusite.myenterprisebank.R

class MainScreenViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _userName = mutableStateOf("Usuário")
    val userName: State<String> get() = _userName

    private val _userPhoto = mutableStateOf(R.drawable.user_default)
    val userPhoto: State<Int> get() = _userPhoto

    private val _balance = mutableStateOf("R$ 0,00")
    val balance: State<String> get() = _balance

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        document?.let {
                            _userName.value = it.getString("name") ?: "Usuário"
                            _userPhoto.value = R.drawable.jose // Substituir com recurso correto
                            _balance.value = "R$ ${it.getDouble("saldo") ?: 0.0}"
                        }
                    }
            }
        }
    }
}
