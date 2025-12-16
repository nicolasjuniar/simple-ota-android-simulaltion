package juniar.nicolas.pokeapp.otasimulation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import juniar.nicolas.pokeapp.otasimulation.ui.theme.OTASimulationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OTASimulationTheme {
                MainScreen()
            }
        }
    }
}
