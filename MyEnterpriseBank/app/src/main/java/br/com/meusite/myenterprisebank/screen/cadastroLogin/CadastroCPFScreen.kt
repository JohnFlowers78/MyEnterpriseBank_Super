package br.com.meusite.myenterprisebank.screen.cadastroLogin

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.meusite.myenterprisebank.firebase.FirebaseHelper
import br.com.meusite.myenterprisebank.utils.CPFUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun CadastroCPFScreen(
    onCpfValidated: (String) -> Unit // Callback quando o CPF é válido
) {
    var cpf by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("Digite seu CPF") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = isValid == false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                isValid = CPFUtils.isValidCPF(cpf)
                if (isValid == true) {
                    onCpfValidated(cpf)
                }
            }
        }) {
            Text("Validar CPF")
        }

        Spacer(modifier = Modifier.height(16.dp))

        isValid?.let {
            Text(
                text = if (it) "CPF válido!" else "CPF inválido!",
                color = if (it) Color.Green else Color.Red
            )
        }
    }
}

@Composable
fun CadastroUsuarioScreen() {
    val userDao =           // Obtenha sua instância de UserDAO (via Hilt ou diretamente)
    val firebaseAuth = FirebaseAuth.getInstance()

    CadastroCPFScreen { validCpf ->
        val userId = firebaseAuth.currentUser?.uid ?: "0"
        val nome = "Usuário Firebase" // Nome fictício ou proveniente do cadastro
        val celular = "11999999999"

        FirebaseHelper.cadastrarUsuario(userId, nome, validCpf, celular) { sucesso, erro ->
            if (sucesso) {
                // Salvar no Room
                userDao.addUser(
                    User(
                        id = userId.toIntOrNull() ?: 1,
                        nome = nome,
                        cpf = validCpf,
                        celular = celular,
                        saldo = 0.0,
                        imgPerfil = null
                    )
                )
                println("Usuário cadastrado com sucesso!")
            } else {
                println("Erro ao cadastrar usuário: $erro")
            }
        }
    }
}