package ie.wit.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.myapplication.databinding.CardFreecycleBinding
import ie.wit.myapplication.models.FreecycleModel

interface FreecycleListener {
    fun onListingClick(listing: FreecycleModel)
}

class FreecycleAdapter constructor(
    private var listings: ArrayList<FreecycleModel>,
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

    fun removeAt(position: Int) {
        listings.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = listings.size


    // todo start below
    inner class MainHolder(private val binding : CardFreecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listing: FreecycleModel, listener: FreecycleListener) {
            binding.root.tag = listing
            binding.listingTitle.text = listing.listingTitle
            binding.name.text = listing.name
        //    Picasso.get().load(listing.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onListingClick(listing) }
        }
    }



}