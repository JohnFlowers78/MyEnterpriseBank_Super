package br.com.meusite.myenterprisebank.data.transacao

import androidx.lifecycle.LiveData

class TransacaoRepository(private val transacaoDao: TransacaoDAO) {

    val listTransacoes: LiveData<List<Transacao>> = transacaoDao.listTransacoes()

    suspend fun addTransacao(transacao: Transacao){
        transacaoDao.addTransacao(transacao)
    }


}