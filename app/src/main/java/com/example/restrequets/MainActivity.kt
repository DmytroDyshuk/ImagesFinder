package com.example.restrequets

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.restrequets.fragments.search.RecyclerViewAdapter
import com.example.restrequets.model.Image
import com.example.restrequets.presenter.ViewPagerAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.llSearch
import kotlinx.android.synthetic.main.fragment_search.*
import org.json.JSONObject



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(applicationContext)
        Realm.init(this)


        AndroidNetworking.get("https://api.unsplash.com/search/photos")
            .addQueryParameter("per_page", "100")
            .addQueryParameter("query", "random")
            .addHeaders("Authorization", "Client-ID Vrz8rVInDyQw-MJSEP_M1i0_FSG5ZwjWjJHZCh4DWnE")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    val resultsArray = response.getJSONArray("results")

                    val imagesList = ArrayList<Image>()
                    for (i in 0 until resultsArray.length()){
                        val result = resultsArray.getJSONObject(i)
                        val id = result.getString("id")
                        val urls = result.getJSONObject("urls")
                        val like = result.getInt("likes")
                        val descrip = result.getString("description")

                        val image = Image()
                        image.id = id
                        image.urlFull = urls.getString("full")
                        image.urlRegular = urls.getString("regular")
                        image.likes = like
                        if (descrip != "null")
                            image.description = descrip

                        imagesList.add(image)
                    }
                    rvPictures.layoutManager = GridLayoutManager(this@MainActivity, 2)
                    rvPictures.adapter = RecyclerViewAdapter(imagesList)
                }

                override fun onError(error: ANError) {
                    Log.e("Network", "onError:$error")
                }
            })

        val adapter = ViewPagerAdapter(supportFragmentManager)
        vpPager.adapter = adapter

        llSearch.setOnClickListener {
            vpPager.currentItem = 0
            setDisableTab(tvIconFavourites, tvTitleFavourites)
            setActiveTab(tvIconSearch, tvTitleSearch)
        }

        llFavourites.setOnClickListener {
            vpPager.currentItem = 1
            setDisableTab(tvIconSearch, tvTitleSearch)
            setActiveTab(tvIconFavourites, tvTitleFavourites)

        }
    }

    private fun setActiveTab(icon: TextView, title: TextView) {
        icon.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        title.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        title.typeface = Typeface.DEFAULT_BOLD
    }

    private fun setDisableTab(icon: TextView, title: TextView) {
        icon.setTextColor(ContextCompat.getColor(this, R.color.colorLightGray))
        title.setTextColor(ContextCompat.getColor(this, R.color.colorLightGray))
        title.typeface = Typeface.DEFAULT
    }
}
