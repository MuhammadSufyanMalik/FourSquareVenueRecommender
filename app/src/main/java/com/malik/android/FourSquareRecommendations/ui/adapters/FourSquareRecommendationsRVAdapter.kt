package com.malik.android.FourSquareRecommendations.ui.adapters


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.malik.android.FourSquareRecommendations.api.model.Result
import com.malik.android.FourSquareRecommendations.databinding.CardViewLayoutBinding
import com.bumptech.glide.Glide


class FourSquareRecommendationsRVAdapter(var resultList: List<Result>):RecyclerView.Adapter<FourSquareRecommendationsRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CardViewLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FourSquareRecommendationsRVAdapter.ViewHolder {
       val binding = CardViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(resultList[position]){
                val model: Result = resultList[position]
                val iconUrl = iconFormattedUrl(model.categories[0].icon.prefix, 32, model.categories[0].icon.suffix)
                Glide.with(itemView.context)
                    .load(iconUrl)
                    .into(binding.icon)

                binding.nameVenue.text = model.name
                binding.categoryName.text = model.categories[0].name
                val address = model.location.address
                val country = model.location.country
                binding.formattedAddress.text = if(!model.location.address.isNullOrEmpty()) "$address - $country" else country
                binding.locality.text = if(!model.location.neighborhood.isNullOrEmpty()) model.location.neighborhood[0] else country // Most of the places other values are null

            }
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Result>)
    {
        resultList = data
        notifyDataSetChanged()
    }

    private fun iconFormattedUrl(prefix: String, size: Int, suffix:String): String {

        return ("$prefix$size$suffix")

    }
}