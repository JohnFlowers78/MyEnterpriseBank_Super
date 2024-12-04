package br.com.meusite.myenterprisebank.data

import androidx.room.Embedded
import androidx.room.Relation
import br.com.meusite.myenterprisebank.data.caixinha.Caixinha
import br.com.meusite.myenterprisebank.data.transacao.Transacao
import br.com.meusite.myenterprisebank.data.user.User

data class UserWithRelations(
    @Embedded val user: User,     // aqui "user" contem um objeto "User"  --> p/ usar algum atributo de User --> user.user.id
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId" // Mantém a relação correta com Transacao
    )
    val transacoes: List<Transacao>,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId" // Mantém a relação correta com Caixinha
    )
    val caixinhas: List<Caixinha>
)