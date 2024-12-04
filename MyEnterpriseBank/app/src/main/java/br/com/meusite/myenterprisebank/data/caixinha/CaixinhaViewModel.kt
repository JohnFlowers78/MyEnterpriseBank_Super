package br.com.meusite.myenterprisebank.data.caixinha

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.meusite.myenterprisebank.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CaixinhaViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Caixinha>>
    private val repository: CaixinhaRepository

    init {
        val caixinhaDao = AppDatabase.getDatabase(application).caixinhaDao()
        repository = CaixinhaRepository(caixinhaDao)
        readAllData = repository.listCaixinhas
    }

    fun addCaixinha(caixinha: Caixinha){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCaixinha(caixinha)
        }
    }

    fun updateCaixinha(caixinha: Caixinha){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCaixinha(caixinha)
        }
    }

    fun deleteCaixinha(caixinha: Caixinha){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCaixinha(caixinha)
        }
    }

    fun getCaixinhaById(caixinhaId: Int): LiveData<Caixinha> {
    //        viewModelScope.launch(Dispatchers.IO) {
    //            repository.getCaixinhaById(caixinhaId)
    //        }

        return repository.getCaixinhaById(caixinhaId)
    }
}
