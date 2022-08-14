package beprogressive.airlinesclient.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gmail.beprogressive.it.airlinesclient.R
import com.gmail.beprogressive.it.airlinesclient.databinding.PassengersScreenBinding
import beprogressive.airlinesclient.search.ui.viewmodel.MainViewModel
import beprogressive.airlinesclient.search.ui.viewmodel.PassengersViewModel

class PassengersScreen : PopFragment() {

    val viewModel by lazy {
        ViewModelProvider(this)[PassengersViewModel::class.java]
    }
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val binding = PassengersScreenBinding.inflate(inflater, container, false)
            .apply {
                viewModel = this@PassengersScreen.viewModel
                lifecycleOwner = viewLifecycleOwner
                adultsView.nameTv.text = resources.getString(R.string.adult)
                teenView.nameTv.text = resources.getString(R.string.teen)
                childrenView.nameTv.text = resources.getString(R.string.child)

                adultsView.plusButton.setOnClickListener { viewModel?.updateAdultsCount(true) }
                adultsView.minusButton.setOnClickListener { viewModel?.updateAdultsCount(false) }

                teenView.plusButton.setOnClickListener { viewModel?.updateTeensCount(true) }
                teenView.minusButton.setOnClickListener { viewModel?.updateTeensCount(false) }

                childrenView.plusButton.setOnClickListener { viewModel?.updateChildrenCount(true) }
                childrenView.minusButton.setOnClickListener { viewModel?.updateChildrenCount(false) }

                okButton.setOnClickListener { applyValues() }
            }

        return binding.root
    }

    private fun applyValues() {
        mainViewModel.setAdultsCount(viewModel.getAdultsCount())
        mainViewModel.setTeensCount(viewModel.getTeensCount())
        mainViewModel.setChildren(viewModel.getChildrenCount())
        dismiss()
    }
}