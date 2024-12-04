package br.com.meusite.myenterprisebank.screen.cadastroLogin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.data.user.User
import br.com.meusite.myenterprisebank.data.user.login_cadastro.LoginViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()

    val savedUsers by loginViewModel.savedUsers.observeAsState(emptyList())
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    // Carregar usuários salvos ao iniciar
    LaunchedEffect(Unit) {
        loginViewModel.loadSavedUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título da tela
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de usuários salvos (max 5)
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(savedUsers) { user ->
                SavedUserItem(user, onClick = {
                    emailInput = user.email // Agora usa o email do usuário
                    passwordInput = user.pin
                })
            }
        }

        // Campo para email
        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo para senha (PIN)
        TextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botão para login
        Button(
            onClick = {
                loginViewModel.loginUser(emailInput, passwordInput,
                    onSuccess = { userId ->
                        navController.navigate("principal/$userId")
                    },
                    onFailure = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}

@Composable
fun SavedUserItem(user: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exibe o email do usuário
        Text(user.email, modifier = Modifier.weight(1f)) // Agora mostra o email
        // Botão para selecionar o usuário
        Button(onClick = onClick) {
            Text("Selecionar")
        }
    }
}
