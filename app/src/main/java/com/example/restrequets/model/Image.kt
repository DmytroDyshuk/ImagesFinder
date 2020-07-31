package com.example.restrequets.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Image : RealmObject() {
    @PrimaryKey
    var id: String? = null
    var urlFull: String? = null
    var urlRegular: String? = null
    var likes: Int? = null
    var description: String? = null
}