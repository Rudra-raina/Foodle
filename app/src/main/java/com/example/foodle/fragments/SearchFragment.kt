package com.example.foodle.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
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
import com.example.foodle.databinding.DishDetailDialogBinding
import com.example.foodle.databinding.FragmentSearchBinding
import com.example.foodle.networks.DishApiViewModel
import com.example.foodle.networks.FilteredDishes
import com.example.foodle.utils.Constants
import java.util.*

class SearchFragment : Fragment() {

    private var mBinding: FragmentSearchBinding? = null
    private var mIngredients =" "
    private var mDirections=" "

    private lateinit var mDishApiViewModel : DishApiViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDishApiViewModel= ViewModelProvider(this)[DishApiViewModel::class.java]
        dishViewModelObserver()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.searchDishes ->{
                if(mBinding!!.et.text?.isEmpty() == true){
                    Toast.makeText(requireContext(), "Can not be empty", Toast.LENGTH_SHORT).show()
                }else{
                    apiCall(mBinding!!.et.text.toString().toLowerCase(Locale.ROOT))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun apiCall(filter:String){
        mDishApiViewModel.getSearchedDishesFromInternet(filter)
    }

    private fun dishViewModelObserver(){

        mDishApiViewModel.dishResponse.observe(viewLifecycleOwner,{
            if(it!=null){
                val size = it.recipes.size
                if(size>0){
                    mBinding!!.llEnterDish.visibility=View.GONE
                    mBinding!!.llNoDishFound.visibility=View.GONE
                    mBinding!!.llSearchedDishFromAPI.visibility=View.VISIBLE
                    mBinding!!.llNetworkError.visibility=View.GONE
                    Log.e("response",">0")
                    setUpUI(it)
                }else{
                    mBinding!!.llEnterDish.visibility=View.GONE
                    mBinding!!.llNoDishFound.visibility=View.VISIBLE
                    mBinding!!.llSearchedDishFromAPI.visibility=View.GONE
                    mBinding!!.llNetworkError.visibility=View.GONE
                    Log.e("response","=0")
                }
            }
        })

        mDishApiViewModel.dishLoadingError.observe(viewLifecycleOwner,
            {
                if(it!=null){
                    if(it==true){
                        mBinding!!.llEnterDish.visibility=View.GONE
                        mBinding!!.llNoDishFound.visibility=View.GONE
                        mBinding!!.llSearchedDishFromAPI.visibility=View.GONE
                        mBinding!!.llNetworkError.visibility=View.VISIBLE
                    }
                    Log.e("loadingErrorSearch","$it")
                }
            }

        )

        mDishApiViewModel.loadDish.observe(viewLifecycleOwner,
            {
                if(it!=null){
                    Log.e("LoadSearch","$it")
                }
            })

    }

    private fun setUpUI(list : FilteredDishes.DishesFromAPI){
        mBinding!!.tvTitle.text=list.recipes[0].title
        Glide.with(requireActivity()).load(list.recipes[0].image).centerCrop().into(mBinding!!.ivDishImage)
        mBinding!!.tvServing.text="Serving - ${list.recipes[0].servings}"
        mBinding!!.tvLikes.text= "${list.recipes[0].aggregateLikes} likes"
        mBinding!!.tvLikesRound.text= "${list.recipes[0].aggregateLikes} likes"
        mBinding!!.tvScore.text= "Score of ${list.recipes[0].spoonacularScore.toInt()}"
        mBinding!!.tvCookingTime.text=resources.getString(R.string.lbl_estimate_cooking_time,list.recipes[0].readyInMinutes.toString())

        for(value in list.recipes[0].extendedIngredients){
            if(mIngredients.isEmpty()){
                mIngredients= value.original
            }else{
                mIngredients = mIngredients + "\n" + "• " + value.original
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mDirections = Html.fromHtml(list.recipes[0].instructions, Html.FROM_HTML_MODE_COMPACT).toString()
        }else{
            @Suppress("DEPRECATION")
            mDirections = Html.fromHtml(list.recipes[0].instructions).toString()
        }

        mBinding!!.tvIngredients.text=mIngredients
        mBinding!!.tvCookingDirection.text=mDirections

        mBinding!!.llAddDish.setOnClickListener{
            val time = list.recipes[0].readyInMinutes
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


            val randomDish = DishEntity(
                list.recipes[0].image,
                Constants.online,
                list.recipes[0].title,
                "Others",
                "Others",
                mIngredients,
                dishTime,
                mDirections,
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

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}

