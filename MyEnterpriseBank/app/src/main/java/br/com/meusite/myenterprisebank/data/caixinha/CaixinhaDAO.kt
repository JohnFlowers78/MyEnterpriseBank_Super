package br.com.meusite.myenterprisebank.data.caixinha

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CaixinhaDAO {

    @Query("SELECT * FROM caixinhas")
    fun listCaixinhas(): LiveData<List<Caixinha>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCaixinha(caixinha: Caixinha)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(caixinhas: List<Caixinha>)    // Para popular uma base de dados ja pronta (facilitar testes)

    @Update
    fun updateCaixinha(caixinha: Caixinha)

    @Delete
    fun deleteCaixinha(caixinha: Caixinha)

    @Query("SELECT * FROM caixinhas WHERE caixinhaId = :caixinhaId")
    fun getCaixinhaById(caixinhaId: Int): LiveData<Caixinha>
}