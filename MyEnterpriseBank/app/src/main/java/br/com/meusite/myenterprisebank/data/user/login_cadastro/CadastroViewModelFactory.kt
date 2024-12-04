package br.com.meusite.myenterprisebank.data.user.login_cadastro

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CadastroViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CadastroViewModel::class.java)) {
            return CadastroViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
