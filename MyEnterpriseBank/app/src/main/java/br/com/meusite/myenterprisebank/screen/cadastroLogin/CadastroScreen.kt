package br.com.meusite.myenterprisebank.screen.cadastroLogin

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.meusite.myenterprisebank.data.user.login_cadastro.CadastroViewModel
import br.com.meusite.myenterprisebank.data.user.login_cadastro.CadastroViewModelFactory

@Composable
fun CadastroScreen(navController: NavHostController) {
    val context = LocalContext.current
    val cadastroViewModel: CadastroViewModel = viewModel(factory = CadastroViewModelFactory(context.applicationContext as Application))

    var nomeCompleto by remember { mutableStateOf("") }
    var cpfInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var celularInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }

    val mensagemErro by cadastroViewModel.mensagemErro.observeAsState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Cadastro de Usuário", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nomeCompleto,
            onValueChange = { nomeCompleto = it },
            label = { Text("Nome Completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cpfInput,
            onValueChange = { cpfInput = it },
            label = { Text("CPF") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = celularInput,
            onValueChange = { celularInput = it },
            label = { Text("Celular") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPasswordInput,
            onValueChange = { confirmPasswordInput = it },
            label = { Text("Confirmar Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (passwordInput == confirmPasswordInput) {
                    cadastroViewModel.cadastrarUsuario(nomeCompleto, cpfInput, emailInput, celularInput, passwordInput)
                } else {
                    cadastroViewModel.mensagemErro.value = "As senhas não coincidem."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }

        if (mensagemErro.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(mensagemErro, color = MaterialTheme.colorScheme.error)
        }
    }
}
