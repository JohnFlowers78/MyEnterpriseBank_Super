package br.com.meusite.myenterprisebank.screen.cadastroLogin

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import br.com.meusite.myenterprisebank.firebase.FirebaseHelper
import br.com.meusite.myenterprisebank.utils.CPFUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun CadastroUsuarioScreen() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // States para os campos de texto
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }

    // State para mostrar mensagens de erro ou sucesso
    var mensagemStatus by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Campos de entrada
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("CPF") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = celular,
            onValueChange = { celular = it },
            label = { Text("Celular") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensagem de status
        if (mensagemStatus.isNotEmpty()) {
            Text(
                text = mensagemStatus,
                color = if (mensagemStatus.contains("sucesso")) Color.Green else Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = {
                // Inicia a validação do CPF
                scope.launch {
                    val isValidCPF = CPFUtils.isValidCPF(cpf)
                    if (!isValidCPF) {
                        mensagemStatus = "CPF inválido."
                        return@launch
                    }

                    // Validação concluída, inicia o cadastro
                    val firebaseAuth = FirebaseAuth.getInstance()
                    val userId = firebaseAuth.currentUser?.uid ?: "0"

                    // Chamando a função para cadastrar o usuário
                    FirebaseHelper.cadastrarUsuario(userId, nome, cpf, celular) { sucesso, erro ->
                        if (sucesso) {
                            mensagemStatus = "Usuário cadastrado com sucesso!"
                        } else {
                            mensagemStatus = "Erro ao cadastrar usuário: $erro"
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cadastrar")
        }
    }
}
