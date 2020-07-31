package com.example.restrequets.fragments.favorites


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.restrequets.R
import com.example.restrequets.db.RealmHelper
import com.example.restrequets.model.Image
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.view.*


class FavoritesFragment : Fragment() {

    private val itemList = ArrayList<Image>()
    private var adapter = FavouritesAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        reloadImages()

        tvDeleteAll.setOnClickListener {
            RealmHelper().deleteAllImagesFromRealm()
            reloadImages()
        }

        tvSortUp.setOnClickListener {
            sortUpReload()
        }

        tvSortDown.setOnClickListener {
            sortDownReload()
        }

    }

    private fun initAdapter(view: View) {
        view.rvFavourites.layoutManager = GridLayoutManager(requireContext(), 2)
        view.rvFavourites.adapter = adapter
    }

    private fun reloadImages() {
        itemList.clear()
        itemList.addAll(RealmHelper().getImagesFromRealm())
        adapter.notifyDataSetChanged()
    }

    private fun sortUpReload() {
        itemList.clear()
        itemList.addAll(RealmHelper().sortUp())
        adapter.notifyDataSetChanged()
    }

    private fun sortDownReload() {
        itemList.clear()
        itemList.addAll(RealmHelper().sortDown())
        adapter.notifyDataSetChanged()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            reloadImages()
        }
    }

}

