package com.example.foodle.utils

object Constants {

    fun dishType() : ArrayList<String>{
        val list=ArrayList<String>()
        list.add("Vegetarian")
        list.add("Non-vegetarian")
        list.add("Eggetarian")
        list.add("Vegan")
        list.add("Others")
        return list
    }

    fun dishCategory() : ArrayList<String>{
        val list=ArrayList<String>()
        list.add("Breakfast")
        list.add("Brunch")
        list.add("Lunch")
        list.add("Snacks")
        list.add("Dinner")
        list.add("Desert")
        list.add("Others")
        return list
    }

    fun dishCookingTime() : ArrayList<String>{
        val list=ArrayList<String>()
        list.add("5-10 mins")
        list.add("10-15 mins")
        list.add("15-20 mins")
        list.add("20-30 mins")
        list.add("30-45 mins")
        list.add("45-60 mins")
        list.add("60+ mins")
        return list
    }

    const val dishType="dishType"
    const val dishCategory="dishCategory"
    const val dishCookingTime="dishCookingTime"

    const val gallery=1

    const val offline="offline"
    const val online="online"

    const val dishDetails="dishDetails"

    const val filterDish="filterDish"

    const val apiKey="apiKey"
    const val spoonacularAPiKeyValue="1ed1e755abd04effbb41835be9161c30"
    const val spoonacularEndPoint="recipes/random"
    const val spoonacularBaseURL="https://api.spoonacular.com/"

    const val limitLicense="limitLicense"
    const val tags="tags"
    const val number="number"

    const val vegetarian="Vegetarian"
    const val nonVegetarian="Paleo"
    const val eggetarian="Pescetarian"
    const val vegan="Vegan"
    const val others="Gluten Free"

    const val NOTIFICATION_ID="DishNotificationID"
    const val NOTIFICATION_NAME="Dish"
    const val NOTIFICATION_CHANNEL="DIshChannel_01"
}