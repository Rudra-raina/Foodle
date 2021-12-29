package com.example.foodle.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodle.R
import com.example.foodle.activities.AddEditDishActivity
import com.example.foodle.database.DishEntity
import com.example.foodle.databinding.DishRepresentationBinding
import com.example.foodle.fragments.FavoriteFragment
import com.example.foodle.fragments.HomeFragment
import com.example.foodle.utils.Constants

class DishAdapter(private val fragment: Fragment) : RecyclerView.Adapter<DishAdapter.MyViewHolder>() {

    private var dishList:List<DishEntity> = listOf()

    class MyViewHolder(val binding : DishRepresentationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DishRepresentationBinding.inflate(LayoutInflater.from(fragment.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dish=dishList[position]
        Glide.with(fragment).load(dish.image).into(holder.binding.ivItemImageDashboard)
        holder.binding.tvItemPriceDashboard.text = dish.CookingTime

        holder.binding.tvNavigateToDetails.setOnClickListener{
            if(fragment is HomeFragment){
                fragment.dishDetails(dish)
            }
            if(fragment is FavoriteFragment){
                holder.binding.ibMore.visibility= View.GONE
                fragment.dishDetails(dish)
            }
        }

        if(fragment is FavoriteFragment){
            holder.binding.ibMore.visibility= View.GONE
        }

        if(dish.title.length>17){
            holder.binding.tvItemNameDashboard.text=dish.title.substring(0,17)
        }else{
            holder.binding.tvItemNameDashboard.text=dish.title
        }

        holder.binding.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.binding.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.ActionEditDish) {
                    val intent= Intent(fragment.requireContext(),AddEditDishActivity::class.java)
                    intent.putExtra(Constants.dishDetails,dish)
                    fragment.requireContext().startActivity(intent)
                } else {
                    if (fragment is HomeFragment) {
                        fragment.deleteDish(dish)
                    }
                }
                true
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dishList(list: List<DishEntity>){
        dishList=list
        notifyDataSetChanged()
    }
}