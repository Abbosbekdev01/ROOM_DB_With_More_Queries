package uz.abbosbek.mytelegram.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.abbosbek.mytelegram.models.Users
import java.io.ByteArrayOutputStream

@Database(entities = [Users::class], version = 1)
abstract class MyDbHelper : RoomDatabase() {

    abstract fun userDao(): UserDaoInterface

    companion object {
        private var instance:MyDbHelper? = null

        fun newInstance(context: Context): MyDbHelper {
            if (instance == null){
                instance = Room.databaseBuilder(context,MyDbHelper::class.java, "users_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return instance!!
        }
    }


}