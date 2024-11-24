package br.com.meusite.myenterprisebank.screen.cadastroLogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.data.AppDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider

data class User(val cpf: String, val pin: String)

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val firebaseAuth = FirebaseAuth.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }

    val savedUsers = remember { mutableStateListOf(User("12345678900", "1234")) }
    var cpfInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { resultTask ->
                if (task.isSuccessful) {
                    // Obter o UID do usuário autenticado
                    val userId = firebaseAuth.currentUser?.uid
                    if (userId != null) {
                        // Redirecionar para a tela principal com o userId
                        navController.navigate("mainScreen/$userId")
                    }
                } else {
                    Toast.makeText(context, "Erro no login com Google.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao obter dados do Google.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(savedUsers) { user ->
                SavedUserItem(user, onClick = {
                    cpfInput = user.cpf
                    passwordInput = user.pin
                })
            }
        }

        TextField(
            value = cpfInput,
            onValueChange = { cpfInput = it },
            label = { Text("CPF") },
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
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Verificar se o usuário existe no banco de dados com o CPF e senha
                firebaseAuth.signInWithEmailAndPassword("$cpfInput@meusite.com", passwordInput)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Obter o UID do usuário autenticado
                            val userId = firebaseAuth.currentUser?.uid
                            if (userId != null) {
                                // Redirecionar para a tela principal com o userId
                                navController.navigate("mainScreen/$userId")
                            }
                        } else {
                            Toast.makeText(context, "Erro no login.", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { signInWithGoogle(googleLauncher) }) {
                Text("Login com Google")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { signInWithFacebook(firebaseAuth, callbackManager, navController, context) }) {
                Text("Login com Facebook")
            }
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
        Text(user.cpf, modifier = Modifier.weight(1f))
        Button(onClick = onClick) {
            Text("Selecionar")
        }
    }
}

@Composable
fun signInWithGoogle(googleLauncher: ActivityResultLauncher<Intent>) {
    val context = LocalContext.current
    val googleSignInClient = Identity.getSignInClient(context)

    // Inicia a intenção de login
    val signInIntent = googleSignInClient.getSignInIntent()
    googleLauncher.launch(signInIntent)
}

fun signInWithFacebook(
    firebaseAuth: FirebaseAuth,
    callbackManager: CallbackManager,
    navController: NavHostController,
    context: Context
) {
    LoginManager.getInstance().logInWithReadPermissions(
        (context as Activity),
        listOf("email", "public_profile")
    )
    LoginManager.getInstance().registerCallback(callbackManager,
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Obter o UID do usuário autenticado
                            val userId = firebaseAuth.currentUser?.uid
                            if (userId != null) {
                                // Redirecionar para a tela principal com o userId
                                navController.navigate("mainScreen/$userId")
                            }
                        } else {
                            Toast.makeText(context, "Erro no login com Facebook.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onCancel() {
                Toast.makeText(context, "Login cancelado.", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context, "Erro no login com Facebook.", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
