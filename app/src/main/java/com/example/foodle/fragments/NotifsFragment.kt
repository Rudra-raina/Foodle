package com.example.foodle.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodle.R
import com.example.foodle.database.DishApplication
import com.example.foodle.database.DishEntity
import com.example.foodle.database.DishViewModel
import com.example.foodle.database.DishViewModelFactory
import com.example.foodle.databinding.FragmentNotifsBinding
import com.example.foodle.networks.DishApiViewModel
import com.example.foodle.networks.FilteredDishes
import com.example.foodle.utils.Constants


class NotifsFragment : Fragment() {

    private var mBinding : FragmentNotifsBinding? = null

    private lateinit var mRandomDishViewModel : DishApiViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentNotifsBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel= ViewModelProvider(this)[DishApiViewModel::class.java]
        mRandomDishViewModel.getRandomDishesFromInternet()
        dishViewModelObserver()

    }

    private fun dishViewModelObserver() {
        mRandomDishViewModel.dishResponse.observe(viewLifecycleOwner,{
            if(it!=null){
                Log.e("dish",it.recipes[0].title)
//                mBinding!!.llNetworkError.visibility=View.GONE
                mBinding!!.llNoNetworkError.visibility=View.VISIBLE
                setRandomDishResponseInUi(it)
            }
        })

        mRandomDishViewModel.dishLoadingError.observe(viewLifecycleOwner,{
            if(it!=null){
                if(it==true){
//                    mBinding!!.llNetworkError.visibility=View.VISIBLE
                    mBinding!!.llNoNetworkError.visibility=View.GONE
                }
                Log.e("Loading Error","$it")
            }
        })

        mRandomDishViewModel.loadDish.observe(viewLifecycleOwner,{
            if(it!=null){
                Log.e("Loading","$it")
            }
        })
    }

    private fun setRandomDishResponseInUi(dish : FilteredDishes.DishesFromAPI){
        Glide.with(requireContext()).load(dish.recipes[0].image).centerCrop().into(mBinding!!.ivDishImage)
        mBinding!!.tvTitle.text=dish.recipes[0].title
        mBinding!!.tvLikes.text=dish.recipes[0].aggregateLikes.toString()


        var ingredients = " "
        for(value in dish.recipes[0].extendedIngredients){
            if(ingredients.isEmpty()){
                ingredients= value.original
            }else{
                ingredients = ingredients + "\n" + "• " + value.original
            }
        }
        mBinding!!.tvIngredients.text=ingredients

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mBinding!!.tvCookingDirection.text = Html.fromHtml(dish.recipes[0].instructions,Html.FROM_HTML_MODE_COMPACT).toString()
        }else{
            @Suppress("DEPRECATION")
            mBinding!!.tvCookingDirection.text = Html.fromHtml(dish.recipes[0].instructions).toString()
        }

        mBinding!!.tvCookingTime.text=resources.getString(R.string.lbl_estimate_cooking_time,dish.recipes[0].readyInMinutes.toString())

        mBinding!!.tvServing.text="Serving - ${dish.recipes[0].servings}"
        mBinding!!.tvScore.text= "Score of ${dish.recipes[0].spoonacularScore.toInt()}"
        mBinding!!.tvLikesRound.text="${dish.recipes[0].aggregateLikes} likes"




        mBinding!!.llAddDish.setOnClickListener{
            val time = dish.recipes[0].readyInMinutes
            val dishTime : String
            if(time<10){
                dishTime="5-10 mins"
            }else if(time in 10..14){
                dishTime="10-15 mins"
            }else if(time in 15..19){
                dishTime="15-20 mins"
            }else if(time in 20..29){
                dishTime="20-30 mins"
            }else if(time in 30..44){
                dishTime="30-45 mins"
            }else if(time in 45..59){
                dishTime="45-60 mins"
            }else{
                dishTime="60+ mins"
            }


            val randomDish =DishEntity(
                dish.recipes[0].image,
                Constants.online,
                dish.recipes[0].title,
                "Others",
                "Others",
                ingredients,
                dishTime,
                mBinding!!.tvCookingDirection.text.toString(),
                false,
                0
            )

            val randomDishViewModel : DishViewModel by viewModels{
                DishViewModelFactory((requireActivity().application as DishApplication).repository)
            }
            randomDishViewModel.insert(randomDish)
            mBinding!!.llAddDish.visibility=View.GONE
            Toast.makeText(requireContext(), "Successfully saved", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding=null
    }

}


