package br.com.meusite.myenterprisebank.data.caixinha

import androidx.lifecycle.LiveData

class CaixinhaRepository(private val caixinhaDao: CaixinhaDAO) {

    val listCaixinhas: LiveData<List<Caixinha>> = caixinhaDao.listCaixinhas()

    suspend fun addCaixinha(caixinha: Caixinha){
        caixinhaDao.addCaixinha(caixinha)
    }

    suspend fun updateCaixinha(caixinha: Caixinha){
        caixinhaDao.updateCaixinha(caixinha)
    }

    suspend fun deleteCaixinha(caixinha: Caixinha){
        caixinhaDao.deleteCaixinha(caixinha)
    }

    fun getCaixinhaById(caixinhaId: Int): LiveData<Caixinha> {
        return caixinhaDao.getCaixinhaById(caixinhaId)
    }
}