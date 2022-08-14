package beprogressive.airlinesclient

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import beprogressive.airlinesclient.core.MessageInterface
import beprogressive.airlinesclient.core.MessageReceiver
import beprogressive.airlinesclient.core.MessageReceiver.Companion.EVENT_ERROR
import beprogressive.airlinesclient.search.ui.SearchScreenDirections
import beprogressive.airlinesclient.search.ui.viewmodel.MainViewModel
import com.gmail.beprogressive.it.airlinesclient.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MessageInterface {

    val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var navController: NavController

    private val messageReceiver: MessageReceiver = MessageReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)
    }

    fun showOriginsScreen(countryCode: String) {
        showStationSearchScreen(false, countryCode)
    }

    fun showDestinationsScreen(departureCode: String) {
        showStationSearchScreen(true, departureCode)
    }

    private fun showStationSearchScreen(showDestinations: Boolean, code: String) {
        val action = SearchScreenDirections.openSearchStationScreen(
            showDestinations = showDestinations,
            code = code
        )
        navController.navigate(action)
    }

    fun showPassengerScreen() {
        val action = SearchScreenDirections.openPassengerScreen(
            adultsCount = viewModel.adultsCount.value ?: 0,
            teensCount = viewModel.teensCount.value,
            childrenCount = viewModel.childrenCount.value
        )
        navController.navigate(action)
    }

    fun showResultScreen() {
        viewModel.getRequestItem()?.let {
            val action = SearchScreenDirections.openResultScreen(
                date = it.date,
                origin = it.origin,
                destination = it.destination,
                adult = it.adult,
                teen = it.teen,
                child = it.child
            )
            navController.navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(messageReceiver, IntentFilter(EVENT_ERROR))
    }

    override fun onPause() {
        super.onPause()
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onErrorReceived(message: String) {
        viewModel.setErrorMessage(message)
    }
}