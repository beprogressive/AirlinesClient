package beprogressive.airlinesclient.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.beprogressive.it.airlinesclient.databinding.FlightItemBinding
import beprogressive.airlinesclient.flights.network.model.FlightItem
import beprogressive.airlinesclient.search.ui.FlightItemInterface

class ResultListAdapter(private val callback: FlightItemInterface) :
    ListAdapter<FlightItem, RecyclerView.ViewHolder>(FlightItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ItemViewHolder(FlightItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as? ItemViewHolder)?.bind(item, callback)
    }

    class ItemViewHolder(var binding: FlightItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FlightItem, callback: FlightItemInterface) {
            binding.item = item
            binding.callback = callback
        }
    }
}

class FlightItemDiffCallback : DiffUtil.ItemCallback<FlightItem>() {
    override fun areItemsTheSame(oldItem: FlightItem, newItem: FlightItem): Boolean {
        return oldItem.name.equals(newItem.name, ignoreCase = true)
    }

    override fun areContentsTheSame(oldItem: FlightItem, newItem: FlightItem): Boolean {
        return true
    }
}