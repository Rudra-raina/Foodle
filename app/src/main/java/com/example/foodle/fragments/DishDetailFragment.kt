package com.example.foodle.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodle.R
import com.example.foodle.database.DishApplication
import com.example.foodle.database.DishEntity
import com.example.foodle.database.DishViewModel
import com.example.foodle.database.DishViewModelFactory
import com.example.foodle.databinding.FragmentDishDetailBinding
import com.example.foodle.utils.Constants


class DishDetailFragment : Fragment() {

    private var mBinding : FragmentDishDetailBinding? = null

    private val mViewModel : DishViewModel by viewModels{
        DishViewModelFactory(((requireActivity().application) as DishApplication).repository)
    }

    private var mDishDetails : DishEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding= FragmentDishDetailBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        val args : DishDetailFragmentArgs by navArgs()
        mDishDetails=args.dishDetailsArgument
        Glide.with(requireContext()).load(Uri.parse(args.dishDetailsArgument.image)).centerCrop().into(mBinding!!.ivDishImage)
        mBinding!!.etTitle.setText(args.dishDetailsArgument.title)
        mBinding!!.etType.setText(args.dishDetailsArgument.type)
        mBinding!!.etCategory.setText(args.dishDetailsArgument.category)
        mBinding!!.etCookingTime.setText(args.dishDetailsArgument.CookingTime)
        mBinding!!.etIngredients.setText(args.dishDetailsArgument.ingredients)
        mBinding!!.etDirectionToCook.setText(args.dishDetailsArgument.DirectionsToCook)


        if(args.dishDetailsArgument.favoriteDish){
            mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_favorite_selected))
        }else{
            mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_favorite_unselected))
        }

        mBinding!!.ivFavoriteDish.setOnClickListener{
            args.dishDetailsArgument.favoriteDish=!args.dishDetailsArgument.favoriteDish
            mViewModel.update(args.dishDetailsArgument)

            if(args.dishDetailsArgument.favoriteDish){
                Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
                mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_favorite_selected))
            }else{
                Toast.makeText(requireContext(), "Removed to Favorites", Toast.LENGTH_SHORT).show()
                mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_favorite_unselected))
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share_dish->{
                val type="text/plain"
                val subject="Check out this awesome dish !!"
                val shareWith=" "

                var image=" "
                if(mDishDetails!!.imageSource==Constants.online){
                    image=mDishDetails!!.image
                }
                val extraText="Image : $image \n \n Title : ${mDishDetails!!.title} \n \n Type : ${mDishDetails!!.type}" +
                        "\n \n Category : ${mDishDetails!!.category} \n \n Ingredients : ${mDishDetails!!.ingredients}" +
                        "\n \n Instructions : ${mDishDetails!!.DirectionsToCook} \n \n Approx Time : ${mDishDetails!!.CookingTime}"

                val intent = Intent(Intent.ACTION_SEND)
                intent.type=type
                intent.putExtra(Intent.EXTRA_SUBJECT,subject)
                intent.putExtra(Intent.EXTRA_TEXT,extraText)
                startActivity(Intent.createChooser(intent,shareWith))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding=null
    }

}