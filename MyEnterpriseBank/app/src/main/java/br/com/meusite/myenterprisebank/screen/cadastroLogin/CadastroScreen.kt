package br.com.meusite.myenterprisebank.screen.cadastroLogin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.user.UserDAO
import br.com.meusite.myenterprisebank.utils.CPFUtils.isValidCPF
import br.com.meusite.myenterprisebank.utils.isValidCPF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CadastroScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val firebaseAuth = FirebaseAuth.getInstance()

    var cpfInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cadastro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = cpfInput,
            onValueChange = { cpfInput = it },
            label = { Text("CPF") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                if (cpfInput.isBlank() || emailInput.isBlank() || passwordInput.isBlank() || confirmPasswordInput.isBlank()) {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else if (!isValidCPF(cpfInput)) {
                    Toast.makeText(context, "CPF inválido", Toast.LENGTH_SHORT).show()
                } else if (passwordInput != confirmPasswordInput) {
                    Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                } else {
                    registerUser(
                        firebaseAuth,
                        userDao,
                        cpfInput,
                        emailInput,
                        passwordInput,
                        navController
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }
    }
}

fun registerUser(
    firebaseAuth: FirebaseAuth,
    userDao: UserDAO,
    cpf: String,
    email: String,
    password: String,
    navController: NavHostController
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        user?.updateProfile(
                            userProfileChangeRequest {
                                displayName = cpf
                            }
                        )
                        // Salvar no Room DB
                        userDao.insertUser(UserEntity(cpf = cpf, email = email))
                        withContext(Dispatchers.Main) {
                            navController.navigate("loginScreen") {
                                popUpTo("cadastroScreen") { inclusive = true }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                navController.context,
                                "Erro ao cadastrar: ${task.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    navController.context,
                    "Erro inesperado: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
