package beprogressive.airlinesclient.search.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.beprogressive.it.airlinesclient.R
import beprogressive.airlinesclient.airports.local.model.AirportItem
import com.gmail.beprogressive.it.airlinesclient.databinding.SearchAirportScreenBinding
import beprogressive.airlinesclient.search.ui.adapters.AirportListAdapter
import beprogressive.airlinesclient.search.ui.viewmodel.MainViewModel
import beprogressive.airlinesclient.search.ui.viewmodel.SearchViewModel
import beprogressive.airlinesclient.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchAirportScreen : PopFragment(), MenuProvider, AirportItemInterface,
    SearchView.OnQueryTextListener {

    private val args by navArgs<SearchAirportScreenArgs>()

    lateinit var binding: SearchAirportScreenBinding

    lateinit var viewModel: SearchViewModel

    private lateinit var mainViewModel: MainViewModel

    private val adapter by lazy {
        AirportListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SearchAirportScreenBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        binding.toolbar.addMenuProvider(this)

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
            if (args.showDestinations) viewModel.getDestinationCodes()?.let { destinationCodes ->
                viewModel.getDestinations(destinationCodes)
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collectLatest {
//                        adapter.submitList(it)
                        adapter.items = it
                    }
            }
            else viewModel.getOrigins()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
//                    adapter.submitList(it)
                    adapter.items = it
                }
        }
    }

    override fun onItemClick(item: AirportItem) {
        if (viewModel.isDestinationScope())
            mainViewModel.setDestination(item)
        else
            mainViewModel.setOrigin(item)
        dismiss()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_station_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.isIconified = false
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter.filter = newText
        return true
    }
}