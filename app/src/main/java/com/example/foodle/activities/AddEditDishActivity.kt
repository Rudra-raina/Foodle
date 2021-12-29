package com.example.foodle.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodle.R
import com.example.foodle.adapters.CustomListDialogAdapter
import com.example.foodle.database.DishApplication
import com.example.foodle.database.DishEntity
import com.example.foodle.database.DishViewModel
import com.example.foodle.database.DishViewModelFactory
import com.example.foodle.databinding.ActivityAddEditDishBinding
import com.example.foodle.databinding.DialogCustonListBinding
import com.example.foodle.utils.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class AddEditDishActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var mBinding : ActivityAddEditDishBinding
    private lateinit var mCustomDialog: Dialog
    private  var mImagePath : String = " "
    private  var mDishDetails : DishEntity? = null

    private val mDishViewModel : DishViewModel by viewModels{
        DishViewModelFactory((application as DishApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddEditDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        window.statusBarColor= ContextCompat.getColor(this,R.color.app_bg_color)
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.app_bg_color))

        if(intent.hasExtra(Constants.dishDetails)){
            mDishDetails=intent.getParcelableExtra(Constants.dishDetails)!!
            mImagePath=mDishDetails!!.image
            val imageUri=Uri.parse(mImagePath)
            Glide.with(this).load(imageUri).centerCrop().into(mBinding.ivDishImage)

            mBinding.etTitle.setText(mDishDetails!!.title)
            mBinding.etType.setText(mDishDetails!!.type)
            mBinding.etCategory.setText(mDishDetails!!.category)
            mBinding.etIngredients.setText(mDishDetails!!.ingredients)
            mBinding.etCookingTime.setText(mDishDetails!!.CookingTime)
            mBinding.etDirectionToCook.setText(mDishDetails!!.DirectionsToCook)

            mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
            mBinding.btnAddDish.text=resources.getString(R.string.updateDIsh)

        }

        mBinding.etType.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)
        mBinding.ivAddDishImage.setOnClickListener(this)
        mBinding.btnAddDish.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.etType ->{
                customItemsListDialog("Select Dish Type",Constants.dishType(), Constants.dishType)
            }
            R.id.etCategory->{
                customItemsListDialog("Select Dish Category",Constants.dishCategory(), Constants.dishCategory)
            }
            R.id.etCookingTime ->{
                customItemsListDialog("Select Dish Cooking Time",Constants.dishCookingTime(), Constants.dishCookingTime)
            }
            R.id.ivAddDishImage ->{
                selectImageFromGallery()
            }
            R.id.btnAddDish ->{
                val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                val type = mBinding.etType.text.toString().trim { it <= ' ' }
                val category = mBinding.etCategory.text.toString().trim { it <= ' ' }
                val ingredients = mBinding.etIngredients.text.toString().trim { it <= ' ' }
                val cookingTimeInMinutes = mBinding.etCookingTime.text.toString().trim { it <= ' ' }
                val cookingDirection = mBinding.etDirectionToCook.text.toString().trim { it <= ' ' }

                when{
                    mImagePath==" "->{
                        Toast.makeText(this, "Please add image", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(title)->{
                        Toast.makeText(this, "Please add title ", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(type)->{
                        Toast.makeText(this, "Please select type of dish", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(category)->{
                        Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(ingredients)->{
                        Toast.makeText(this, "Please add ingredients", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(cookingTimeInMinutes)->{
                        Toast.makeText(this, "Please select the cooing time", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(cookingDirection)->{
                        Toast.makeText(this, "Please add cooking directions", Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        var favDish=false
                        var dishID=0
                        var imageSource=Constants.offline

                        if(mDishDetails!=null){
                            dishID=mDishDetails!!.id
                            imageSource=mDishDetails!!.imageSource
                            favDish=mDishDetails!!.favoriteDish
                        }

                        val dishEntity = DishEntity(
                        mImagePath,
                        imageSource,
                        title,
                        type,
                        category,
                        ingredients,
                        cookingTimeInMinutes,
                        cookingDirection,
                        favDish,
                        dishID
                        )

                        if(dishID==0){
                            mDishViewModel.insert(dishEntity)
                            onBackPressed()
                            Toast.makeText(this, "Dish Added Successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            mDishViewModel.update(dishEntity)
                            onBackPressed()
                            Toast.makeText(this, "Dish Updated Successfully", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }

        }
    }

    private fun customItemsListDialog(title:String, itemList:ArrayList<String>, selection:String){
        mCustomDialog=Dialog(this)
        val binding : DialogCustonListBinding = DialogCustonListBinding.inflate(layoutInflater)
        mCustomDialog.setContentView(binding.root)

        binding.tvTitle.text=title
        binding.rvList.layoutManager=LinearLayoutManager(this)

        val adapter=CustomListDialogAdapter(this,null,itemList,selection)
        binding.rvList.adapter=adapter
        mCustomDialog.show()
    }

    fun selectedItem(item:String,selection: String){
        when(selection){
            Constants.dishType -> {
                mCustomDialog.dismiss()
                mBinding.etType.setText(item)
            }
            Constants.dishCategory -> {
                mCustomDialog.dismiss()
                mBinding.etCategory.setText(item)
            }
            Constants.dishCookingTime ->{
                mCustomDialog.dismiss()
                mBinding.etCookingTime.setText(item)
            }
        }
    }

    private fun selectImageFromGallery(){
        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                   val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    @Suppress("DEPRECATION")
                    startActivityForResult(intent,Constants.gallery)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showDialogForPermission()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?){
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun showDialogForPermission(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permissions required fo this feature" +
        "It can be enabled under application settings")
            .setPositiveButton("GO TO SETTINGS"){_,_ ->
                val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri= Uri.fromParts("package",packageName,null)
                intent.data=uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK){
            if(requestCode==Constants.gallery){
                val selectedPhotoUri = data!!.data
                mImagePath=selectedPhotoUri.toString()

                Glide.with(this)
                    .load(selectedPhotoUri)
                    .centerCrop()
                    .into(mBinding.ivDishImage)

                mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                Log.e("img",mImagePath)
            }
        }
    }

}