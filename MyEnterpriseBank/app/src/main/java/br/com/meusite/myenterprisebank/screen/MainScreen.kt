package br.com.meusite.myenterprisebank.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.R
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.meusite.myenterprisebank.data.user.login_cadastro.MainScreenViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(navController: NavHostController) {
    // Obtenha o ViewModel da MainScreen
    val viewModel: MainScreenViewModel = viewModel()

    // Obtenha os estados do ViewModel
    val userName by viewModel.userName
    val userPhoto by viewModel.userPhoto
    val balance by viewModel.balance

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: "" // Obtém o userId diretamente do FirebaseAuth

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 16.dp)
    ) {
        TopBar(userName = userName, userPhoto = userPhoto)
        BalanceSection(balance = balance) {
            navController.navigate("extratoList?userId=$userId")
        }
        Spacer(modifier = Modifier.height(100.dp))
        ActionButtons(navController)
        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            thickness = 1.dp
        )
        FloatingButtons(navController)
    }
}

@Composable
fun TopBar(userName: String, userPhoto: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF820AD1))
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = userPhoto),
            contentDescription = "Imagem do usuário",
            modifier = Modifier.size(54.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Olá, $userName",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BalanceSection(balance: String, onExtratoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Saldo",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = balance,
                color = Color.Black,
                fontSize = 18.sp
            )
        }
        Button(
            onClick = onExtratoClick,
            modifier = Modifier.width(120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9868B6))
        ) {
            Text(
                text = "Extrato →",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ActionButtons(navController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: "" // Obtém o userId diretamente do FirebaseAuth

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButton(
            text = "Área Pix",
            iconResId = R.drawable.icon_pix,
            onClick = { navController.navigate("transferir?userId=$userId") }
        )
        ActionButton(
            text = "Transferir",
            iconResId = R.drawable.icon_transferir,
            onClick = { navController.navigate("transferir?userId=$userId") }
        )
        ActionButton(
            text = "Depositar",
            iconResId = R.drawable.icon_depositar,
            onClick = { navController.navigate("depositar?userId=$userId") }
        )
    }
}

@Composable
fun ActionButton(text: String, iconResId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                modifier = Modifier.size(42.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FloatingButtons(navController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: "" // Obtém o userId diretamente do FirebaseAuth

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 44.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("principal?userId=$userId")
                },
                modifier = Modifier.size(56.dp),
                containerColor = Color(0xFF9F30D5)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_transacao),
                    contentDescription = "Transações/Extrato",
                    modifier = Modifier.size(24.dp)
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("caixinhasList?userId=$userId")
                },
                modifier = Modifier.size(56.dp),
                containerColor = Color(0xFF9F30D5)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_sifrao),
                    contentDescription = "Caixinhas",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}