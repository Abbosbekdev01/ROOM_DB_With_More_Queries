package uz.abbosbek.mytelegram.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uz.abbosbek.mytelegram.models.Users

@Dao
interface UserDaoInterface {

    @Insert
    fun addUsers(userDao: Users)

    @Query("select * from Users")
    fun getAllUsers(): List<Users>

    @Delete
    fun deleteUsers(users: Users)

    @Update
    fun updateUsers(users: Users)

    @Query("select * from Users order by name ASC ")
    fun getSortedARight():List<Users>

    @Query("select * from Users order by name DESC")
    fun getSortedAReverse():List<Users>

    @Query("select * from Users order by time ASC")
    fun getSortedTimeGrowth():List<Users>

    @Query("select * from Users order by time DESC")
    fun getSortTimeDecline():List<Users>

}