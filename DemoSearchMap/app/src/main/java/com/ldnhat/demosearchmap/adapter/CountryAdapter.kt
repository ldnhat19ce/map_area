package com.ldnhat.demosearchmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldnhat.demosearchmap.databinding.ItemCountryBinding
import com.ldnhat.demosearchmap.model.CountryDetail

class CountryAdapter(val clickListener : CountryListener) : ListAdapter<CountryDetail, CountryAdapter.CountryViewHolder>(CountryDetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {

        return CountryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.bind(getItem(position), clickListener)
    }

    class CountryViewHolder(private val binding : ItemCountryBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : CountryDetail, clickListener: CountryListener){
            binding.countryDetail = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) :CountryViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCountryBinding.inflate(layoutInflater, parent, false)

                return CountryViewHolder(binding)
            }
        }
    }

    class CountryDetailDiffCallback : DiffUtil.ItemCallback<CountryDetail>(){
        override fun areItemsTheSame(oldItem: CountryDetail, newItem: CountryDetail): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: CountryDetail, newItem: CountryDetail): Boolean {
            return oldItem == newItem
        }
    }


}

class CountryListener(val clickListener : (countryDetail : CountryDetail) -> Unit){
    fun onClick(countryDetail: CountryDetail) = clickListener(countryDetail)
}