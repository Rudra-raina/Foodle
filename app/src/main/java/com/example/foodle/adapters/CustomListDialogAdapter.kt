package com.example.foodle.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.foodle.activities.AddEditDishActivity
import com.example.foodle.databinding.RvListDesignBinding
import com.example.foodle.fragments.HomeFragment
import com.example.foodle.fragments.SearchFragment

class CustomListDialogAdapter (
    private val activity : Activity,
    private val fragment : Fragment?,
    private val list : ArrayList<String>,
    private val selection : String
    ) : RecyclerView.Adapter<CustomListDialogAdapter.MyViewHolder>(){

        class MyViewHolder(val binding : RvListDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RvListDesignBinding.inflate(LayoutInflater.from(activity),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item=list[position]
        holder.binding.tvText.text=item

        holder.itemView.setOnClickListener{
            if(activity is AddEditDishActivity){
                activity.selectedItem(item,selection)
            }
            if(fragment is HomeFragment){
                fragment.filterSelection(item)
            }
            if(fragment is SearchFragment){
                var dish = " "
                when(item){
                    "Vegetarian" ->{
                        dish="Vegetarian"
                    }
                    "Non-vegetarian" ->{
                        dish="Primal"
                    }
                    "Eggetarian" ->{
                        dish="Pescetarian"
                    }
                    "Vegan" ->{
                        dish="Vegan"
                    }
                    "Others" ->{
                        dish="Gluten Free"
                    }
                }
//                fragment.getDishesFromInternet(dish)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}