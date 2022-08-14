package beprogressive.airlinesclient.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import beprogressive.airlinesclient.MainActivity
import beprogressive.airlinesclient.search.ui.viewmodel.MainViewModel
import beprogressive.airlinesclient.utils.showSnackBar
import com.gmail.beprogressive.it.airlinesclient.R
import com.gmail.beprogressive.it.airlinesclient.databinding.SearchScreenBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchScreen : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: SearchScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding = SearchScreenBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = mainViewModel
                departureView.title.text = getString(R.string.from)
                destinationView.title.text = getString(R.string.to)
                departureView.root.setOnClickListener { showOriginsScreen() }
                destinationView.root.setOnClickListener {
                    showDestinationsScreen(
                        mainViewModel.getOriginCode()
                    )
                }
                departureDateView.root.setOnClickListener { showDatePicker(mainViewModel.date.value) }
                passengersView.root.setOnClickListener { showPassengerScreen() }
                searchCta.setOnClickListener { onSubmitSearch() }
            }

        return binding.root
    }

    private fun showDatePicker(time: Long? = null) {
        if (parentFragmentManager.findFragmentByTag(tag) != null) return

        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_departure_date))
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )
            .setSelection(time).build().also { datePicker ->
                datePicker.addOnPositiveButtonClickListener { updatedTime ->
                    mainViewModel.setDate(updatedTime)
                }
                datePicker.show(parentFragmentManager, DATE_PICKER_TAG)
            }
    }

    private fun showOriginsScreen(countryCode: String = "") {
        (activity as? MainActivity)?.showOriginsScreen(countryCode)
    }

    private fun showDestinationsScreen(countryCode: String = "") {
        if (mainViewModel.isOriginSet())
            (activity as? MainActivity)?.showDestinationsScreen(countryCode)
        else binding.root.showSnackBar(getString(R.string.select_departure_airport_first))
    }

    private fun showPassengerScreen() {
        (activity as? MainActivity)?.showPassengerScreen()
    }

    private fun onSubmitSearch() {
        (activity as? MainActivity)?.showResultScreen()
    }

    companion object {
        private const val DATE_PICKER_TAG = "MaterialDatePicker"
    }
}