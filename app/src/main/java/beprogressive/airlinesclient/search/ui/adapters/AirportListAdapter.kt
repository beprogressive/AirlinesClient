package beprogressive.airlinesclient.search.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import beprogressive.airlinesclient.airports.local.model.AirportItem
import beprogressive.airlinesclient.search.ui.AirportItemInterface
import com.gmail.beprogressive.it.airlinesclient.databinding.StationItemBinding


class AirportListAdapter(private val callback: AirportItemInterface) :
    ListAdapter<AirportItem, RecyclerView.ViewHolder>(StationItemDiffCallback()) {

    var items: List<AirportItem> = emptyList()
        set(value) {
            field = value
            onListOrFilterChange()
        }

    var filter: CharSequence = ""
        set(value) {
            field = value
            onListOrFilterChange()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ItemViewHolder(StationItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        val prevItem = if (position > 0) currentList[position - 1] else null
        (holder as? ItemViewHolder)?.bind(item, item.countryName != prevItem?.countryName, callback)
    }

    class ItemViewHolder(private var binding: StationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AirportItem, isCountry: Boolean, callback: AirportItemInterface) {
            binding.item = item
            binding.callback = callback
            binding.countryName.visibility = if (isCountry) View.VISIBLE else View.GONE
        }
    }

    private fun onListOrFilterChange() {
        if (filter.isEmpty()) {
            submitList(items)
            return
        }
        val pattern = filter.toString().lowercase().trim()
        val filteredList = items.filter {
            pattern in (it.name ?: "").lowercase() || pattern in (it.countryName ?: "").lowercase()
        }
        submitList(filteredList)
    }
}

class StationItemDiffCallback : DiffUtil.ItemCallback<AirportItem>() {
    override fun areItemsTheSame(oldItem: AirportItem, newItem: AirportItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AirportItem, newItem: AirportItem): Boolean {
        return oldItem.name.equals(newItem.name, ignoreCase = true) &&
                oldItem.countryName.equals(newItem.countryName, ignoreCase = true) &&
                oldItem.countryCode.equals(newItem.countryCode, ignoreCase = true)
    }
}