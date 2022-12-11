package ie.wit.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.myapplication.R
import ie.wit.myapplication.databinding.CardFreecycleBinding
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.utils.customTransformation

interface FreecycleListener {
    fun onListingClick(listing: FreecycleModel)
}

class FreecycleAdapter constructor(
    private var listings: ArrayList<FreecycleModel>,
    private val listener: FreecycleListener,
    private val readOnly: Boolean
) : RecyclerView.Adapter<FreecycleAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFreecycleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding, readOnly)
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

    inner class MainHolder(val binding : CardFreecycleBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(listing: FreecycleModel, listener: FreecycleListener) {
            binding.root.tag = listing
         //   binding.listing = listing
            binding.listingTitle.text = listing.listingTitle
            binding.name.text = listing.name
        //    Picasso.get().load(listing.image).resize(200, 200).into(binding.imageIcon)
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            Picasso.get().load(listing.profilePic!!.toUri())
                .resize(200,200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onListingClick(listing) }
         //   binding.executePendingBindings()
        }
    }



}