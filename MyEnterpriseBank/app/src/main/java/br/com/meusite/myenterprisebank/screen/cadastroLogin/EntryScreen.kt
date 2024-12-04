package br.com.meusite.myenterprisebank.screen.cadastroLogin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EntryScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    // Verifica se há um usuário logado
    if (auth.currentUser != null) {
        navController.navigate("principal/${auth.currentUser?.uid}")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF673AB7)) // Cor de fundo
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Centraliza os botões verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente
        ) {
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp) // Espaçamento entre os botões
            ) {
                Text(
                    text = "Login",
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = { navController.navigate("cadastro") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cadastrar",
                    fontSize = 18.sp
                )
            }
        }
    }
}