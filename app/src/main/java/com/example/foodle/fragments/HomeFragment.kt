package com.example.foodle.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodle.R
import com.example.foodle.activities.MainActivity
import com.example.foodle.adapters.CustomListDialogAdapter
import com.example.foodle.adapters.DishAdapter
import com.example.foodle.database.DishApplication
import com.example.foodle.database.DishEntity
import com.example.foodle.database.DishViewModel
import com.example.foodle.database.DishViewModelFactory
import com.example.foodle.databinding.DialogCustonListBinding
import com.example.foodle.databinding.FragmentHomeBinding
import com.example.foodle.utils.Constants

class HomeFragment : Fragment() {

    private var mBinding: FragmentHomeBinding? = null
    private lateinit var mDishAdapter : DishAdapter
    private lateinit var mCustomDialog : Dialog

    private val mDishViewModel : DishViewModel by viewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvDishesList.layoutManager= GridLayoutManager(requireActivity(),2)
        mDishAdapter= DishAdapter(this)
        mBinding!!.rvDishesList.adapter=mDishAdapter

        mDishViewModel.allDishesList.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                mBinding!!.rvDishesList.visibility=View.GONE
                mBinding!!.LLNoDishFound.visibility=View.VISIBLE
            }else{
                mBinding!!.rvDishesList.visibility=View.VISIBLE
                mBinding!!.LLNoDishFound.visibility=View.GONE
                mDishAdapter.dishList(it)
            }
        }
    }

    fun deleteDish(dishEntity: DishEntity){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Dish")
        builder.setMessage("Do you really want to delete this dish")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface,_ ->
            mDishViewModel.delete(dishEntity)
            dialogInterface.dismiss()
            Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){dialogInterface,_ ->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dish_filter_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filterDishes ->{
                filterDishDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filterDishDialog() {
        mCustomDialog= Dialog(requireActivity())
        val binding= DialogCustonListBinding.inflate(layoutInflater)
        mCustomDialog.setContentView(binding.root)

        binding.tvTitle.text = resources.getString(R.string.select_item_to_filter)
        val dishType=Constants.dishType()
        dishType.add(0,"ALL ITEMS")

        binding.rvList.layoutManager=LinearLayoutManager(requireContext())
        val adapter = CustomListDialogAdapter(requireActivity(),this,dishType,Constants.filterDish)
        binding.rvList.adapter=adapter
        mCustomDialog.show()
    }

    fun filterSelection(selection : String){
        mCustomDialog.dismiss()

        if(selection=="ALL ITEMS"){
            mDishViewModel.allDishesList.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    mBinding!!.rvDishesList.visibility=View.GONE
                    mBinding!!.LLNoDishFound.visibility=View.VISIBLE
                }else{
                    mBinding!!.rvDishesList.visibility=View.VISIBLE
                    mBinding!!.LLNoDishFound.visibility=View.GONE
                    mDishAdapter.dishList(it)
                }
            }
        }else{
            mDishViewModel.getFilteredDishes(selection).observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    mBinding!!.rvDishesList.visibility=View.GONE
                    mBinding!!.LLNoDishFound.visibility=View.VISIBLE
                }else{
                    mBinding!!.rvDishesList.visibility=View.VISIBLE
                    mBinding!!.LLNoDishFound.visibility=View.GONE
                    mDishAdapter.dishList(it)
                }
            }

        }
    }

    fun dishDetails(dishEntity: DishEntity){
        findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToDishDetailFragment(
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


}