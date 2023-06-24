package uz.abbosbek.mytelegram.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Users {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String? = null
    var about: String? = null
    var time: String? = null
    var data: String? = null
    var photoPath: String? = null


    constructor(name: String?, about: String?) {
        this.name = name
        this.about = about
    }

    constructor()
    constructor(name: String?, about: String?, time: String?, data: String?, photoPath: String?) {
        this.name = name
        this.about = about
        this.time = time
        this.data = data
        this.photoPath = photoPath
    }
}
