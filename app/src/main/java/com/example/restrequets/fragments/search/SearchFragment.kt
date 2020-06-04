package com.example.restrequets.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.restrequets.R
import com.example.restrequets.db.RealmHelper
import com.example.restrequets.fragments.favorites.RecyclerFavouritesAdapter
import com.example.restrequets.model.Image
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.json.JSONObject


class SearchFragment : Fragment() {

    private val itemList = ArrayList<Image>()
    private var adapter = RecyclerViewAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Realm.init(view.context)


        tvSearch.setOnClickListener {
            AndroidNetworking.get("https://api.unsplash.com/search/photos")
                .addQueryParameter("per_page", "100")
                .addQueryParameter("query", view.etSearchable.text.toString())
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
                        rvPictures.layoutManager = GridLayoutManager(view.context, 2)
                        rvPictures.adapter = RecyclerViewAdapter(imagesList)
                    }

                    override fun onError(error: ANError) {
                        Log.e("Network", "onError:$error")
                    }
                })
        }

    }
}
