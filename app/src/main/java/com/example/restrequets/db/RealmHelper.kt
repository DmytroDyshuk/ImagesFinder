package com.example.restrequets.db

import com.example.restrequets.model.Image
import io.realm.Realm
import io.realm.Sort
import kotlin.collections.ArrayList

class RealmHelper {

    private val realm = Realm.getDefaultInstance()

    fun saveImageToRealm(image: Image) {
        realm.executeTransactionAsync { bgRealm ->
            bgRealm.copyToRealmOrUpdate(image)
        }
    }

    fun getImagesFromRealm() : ArrayList<Image> {
        val result = ArrayList<Image>()
        result.addAll(realm.copyFromRealm(realm.where(Image::class.java).findAllAsync()))
        return result
    }

    fun deleteImageFromRealm(id: String?) {
        realm.executeTransaction { bgRealm ->
            bgRealm.where(Image::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
        }
    }

    fun deleteAllImagesFromRealm() {
        realm.executeTransaction { bgRealm ->
            bgRealm.deleteAll()
        }
    }

    fun sortUp() : ArrayList<Image> {
        val result = ArrayList<Image>()
        result.addAll(realm.copyFromRealm(realm.where(Image::class.java).sort("likes", Sort.ASCENDING).findAllAsync()))
        return result
    }

    fun sortDown() : ArrayList<Image> {
        val result = ArrayList<Image>()
        result.addAll(realm.copyFromRealm(realm.where(Image::class.java).sort("likes", Sort.DESCENDING).findAllAsync()))
        return result
    }
}