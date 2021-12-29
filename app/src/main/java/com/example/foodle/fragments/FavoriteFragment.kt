package com.example.foodle.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodle.activities.MainActivity
import com.example.foodle.adapters.DishAdapter
import com.example.foodle.database.DishApplication
import com.example.foodle.database.DishEntity
import com.example.foodle.database.DishViewModel
import com.example.foodle.database.DishViewModelFactory
import com.example.foodle.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var mBinding: FragmentFavoriteBinding? = null

    private val mViewModel : DishViewModel by viewModels{
        DishViewModelFactory(((requireActivity().application) as DishApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter = DishAdapter(this)
        mBinding!!.rvDishesList.adapter = adapter

        mViewModel.favDishes.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                mBinding!!.rvDishesList.visibility = View.GONE
                mBinding!!.noDishFound.visibility = View.VISIBLE
            } else {
                mBinding!!.rvDishesList.visibility = View.VISIBLE
                mBinding!!.noDishFound.visibility = View.GONE
                adapter.dishList(it)
            }
        }
    }

    fun dishDetails(dishEntity: DishEntity){
        findNavController().navigate(FavoriteFragmentDirections.actionNavigationFavToDishDetailFragment(
            dishEntity
        ))

        if(requireActivity() is MainActivity){
            (activity as MainActivity).hideBottomNavView()
        }
    }

    override fun onResume() {
        super.onResume()

        if(requireActivity() is MainActivity){
            (activity as MainActivity).showBottomNavView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}