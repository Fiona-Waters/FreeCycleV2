package ie.wit.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.myapplication.databinding.CardFreecycleBinding
import ie.wit.myapplication.models.FreecycleModel

interface FreecycleListener {
    fun onListingClick(listing: FreecycleModel)
}

class FreecycleAdapter constructor(
    private var listings: List<FreecycleModel>,
    private val listener: FreecycleListener
) : RecyclerView.Adapter<FreecycleAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFreecycleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val listing = listings[holder.adapterPosition]
        holder.bind(listing, listener)
    }

    override fun getItemCount(): Int = listings.size


    // todo start below
    inner class MainHolder(private val binding : CardFreecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listing: FreecycleModel, listener: FreecycleListener) {
            binding.listingTitle.text = listing.listingTitle
            binding.name.text = listing.name
        //TODO    Picasso.get().load(listing.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onListingClick(listing) }
        }
    }



}