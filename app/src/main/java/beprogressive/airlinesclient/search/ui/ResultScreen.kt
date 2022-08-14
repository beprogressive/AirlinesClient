package beprogressive.airlinesclient.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.beprogressive.it.airlinesclient.databinding.ResultScreenBinding
import beprogressive.airlinesclient.flights.network.model.FlightItem
import beprogressive.airlinesclient.search.ui.adapters.ResultListAdapter
import beprogressive.airlinesclient.search.ui.viewmodel.MainViewModel
import beprogressive.airlinesclient.search.ui.viewmodel.ResultViewModel
import beprogressive.airlinesclient.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultScreen : Fragment(), FlightItemInterface {

    lateinit var binding: ResultScreenBinding

    lateinit var viewModel: ResultViewModel

    private lateinit var mainViewModel: MainViewModel

    private val adapter by lazy {
        ResultListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding = ResultScreenBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@ResultScreen.viewModel
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = adapter
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewMode()
    }

    private fun subscribeToViewMode() {
        lifecycleScope.launch {
            mainViewModel.errorMessage
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    it?.let { message ->
                        binding.root.showSnackBar(message)
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.getFlights()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    adapter.submitList(it)
                }
        }
    }

    override fun onItemClick(item: FlightItem) {

    }
}