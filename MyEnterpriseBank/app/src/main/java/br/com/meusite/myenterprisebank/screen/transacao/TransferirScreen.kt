package br.com.meusite.myenterprisebank.screen.transacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.transacao.Transacao
import br.com.meusite.myenterprisebank.data.user.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.meusite.myenterprisebank.data.transacao.TransacaoViewModel

@Composable
fun TransferirScreen(navController: NavHostController) {
    var chavePix by remember { mutableStateOf("") }
    var valorTransfer by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var saldoAtual by remember { mutableStateOf(0.0) }

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel() // Obtém o UserViewModel
    val transacaoViewModel: TransacaoViewModel = viewModel() // Obtém o TransacaoViewModel

    // Observe o saldo atual do usuário
    val saldoState = userViewModel.readAllData.observeAsState(listOf())

    saldoAtual = saldoState.value.firstOrNull()?.saldo ?: 0.0 // Pega o saldo do primeiro usuário registrado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TopBar com o saldo
        TopBar(saldo = "R$ ${"%.2f".format(saldoAtual)}")

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto para "Chave Pix"
            Text(
                text = "Chave Pix",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = chavePix,
                onValueChange = { chavePix = it },
                label = { Text("Chave Pix (CPF/CNPJ)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            ) // Realizar verificação de quem é o CPF e criar registro de transação entre tais Users

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Valor da Transferência",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = valorTransfer,
                onValueChange = { valorTransfer = it },
                label = { Text("Digite o valor da transferência, em Reais") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val valor = valorTransfer.toDoubleOrNull()
                    if (valor != null && valor > 0) {
                        if (saldoAtual >= valor) {
                            val novoSaldo = saldoAtual - valor
                            userViewModel.atualizarSaldo(novoSaldo, 1) // Atualiza saldo do usuário

                            val transacao = Transacao(
                                transacaoId = 0,
                                descricao = chavePix,
                                valor = valor,
                                userId = 1
                            )
                            transacaoViewModel.addTransacao(transacao) // Adiciona a transação via ViewModel

                            message = "Transferência realizada com sucesso!"
                            navController.popBackStack()
                        } else {
                            message = "Saldo insuficiente para realizar a transferência."
                        }
                    } else {
                        message = "Por favor, insira um valor válido para a transferência."
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
            ) {
                Text(text = "Transferir")
            }

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            FloatingButtons4(navController)
        }
    }
}

@Composable
fun TopBar(saldo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF820AD1))              // Cor roxa similar ao Nubank
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Saldo Disponível",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = saldo,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FloatingButtons4(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("principal")
                },
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 0.dp),
                containerColor = Color(0xFF9F30D5)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_transacao),
                    contentDescription = "Transações/Extrato",
                    modifier = Modifier.size(24.dp),    // Aumenta o ícone dentro do botão
                    tint = Color.White,
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("caixinhasList")
                },
                modifier = Modifier
                    .size(56.dp)
                    .padding(start = 0.dp),
                containerColor = Color(0xFF9F30D5)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_sifrao),
                    contentDescription = "Caixinhas",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}