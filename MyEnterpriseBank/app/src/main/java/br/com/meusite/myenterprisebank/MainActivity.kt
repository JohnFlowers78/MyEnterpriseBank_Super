package br.com.meusite.myenterprisebank

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.meusite.myenterprisebank.screen.CaixinhasGridScreen
import br.com.meusite.myenterprisebank.screen.ExtratoListScreen
import br.com.meusite.myenterprisebank.screen.MainScreen
import br.com.meusite.myenterprisebank.screen.caixinha.AddCaixinhaScreen
import br.com.meusite.myenterprisebank.screen.caixinha.DetalhesCaixinhaScreen
import br.com.meusite.myenterprisebank.screen.caixinha.UpdateCaixinhaScreen
import br.com.meusite.myenterprisebank.screen.cadastroLogin.CadastroScreen
import br.com.meusite.myenterprisebank.screen.cadastroLogin.EntryScreen
import br.com.meusite.myenterprisebank.screen.cadastroLogin.LoginScreen
import br.com.meusite.myenterprisebank.screen.transacao.DepositarScreen
import br.com.meusite.myenterprisebank.screen.transacao.TransferirScreen
import br.com.meusite.myenterprisebank.ui.theme.BasicNuBankTheme
import com.facebook.appevents.AppEventsLogger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    // Inicialize o AppEventsLogger com a instância da Application
        AppEventsLogger.activateApp(application)

        enableEdgeToEdge()
        setContent {
            BasicNuBankTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NuBankApp()
                }
            }
        }
    }
}

@Composable
fun NuBankApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "entrada") {
        composable("entrada") { EntryScreen(navController) }
        composable("cadastro") { CadastroScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("principal") { MainScreen(navController) }

        composable("depositar") { DepositarScreen(navController) }
        composable("transferir") { TransferirScreen(navController) }
        composable("extratoList") { ExtratoListScreen(navController) }
        composable("caixinhasList") { CaixinhasGridScreen(navController) }
        composable("addCaixinha") { AddCaixinhaScreen(navController) }
        composable("detalhesCaixinha/{caixinhaId}") { backStackEntry ->
            val caixinhaId = backStackEntry.arguments?.getString("caixinhaId")
            Log.d("caixinha 1-- ", "$caixinhaId")
            DetalhesCaixinhaScreen(navController, caixinhaId)
        }
        composable("updateCaixinha/{caixinhaId}") { backStackEntry ->
            val caixinhaId = backStackEntry.arguments?.getString("caixinhaId")?.toInt() ?: -1
            UpdateCaixinhaScreen(navController, caixinhaId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicNuBankTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            NuBankApp()
        }
    }
}



//________________________________________________________________

// Como passar o userId para a tela principal:
//Você pode configurar o NavHostController para receber o userId como parâmetro na sua tela principal e usá-lo para fazer a consulta ao saldo do usuário. Aqui está um exemplo de como isso pode ser feito na sua navegação:
//


//navController.navigate("mainScreen/$userId")
//E, no seu NavGraph, defina o parâmetro na tela principal:
//


//composable("mainScreen/{userId}") { backStackEntry ->
//    val userId = backStackEntry.arguments?.getString("userId")
//    MainScreen(userId = userId)
//}

//Agora, na MainScreen, você pode utilizar o userId para buscar os dados necessários.
//________________________________________________________________
