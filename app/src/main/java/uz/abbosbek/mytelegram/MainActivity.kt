package uz.abbosbek.mytelegram

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView
import uz.abbosbek.mytelegram.adapters.RvAdapter
import uz.abbosbek.mytelegram.adapters.RvClick
import uz.abbosbek.mytelegram.database.MyDbHelper
import uz.abbosbek.mytelegram.databinding.ActivityMainBinding
import uz.abbosbek.mytelegram.databinding.ItemDialogBinding
import uz.abbosbek.mytelegram.models.Users
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity(), RvClick {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var rvAdapter: RvAdapter
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var photoPath: String
    var photoSelectedState = false
    var selectedImage: Uri? = null
    var dialog: AlertDialog.Builder? = null
    var dialogView: ItemDialogBinding? = null
    //    private lateinit var list: ArrayList<Users>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        myDbHelper = MyDbHelper.newInstance(this)
        rvAdapter = RvAdapter(myDbHelper.userDao().getAllUsers() as ArrayList<Users>, this, this)

        addButton()
        toolBarMenu()
        getUserData()
    }

    //todo: Ma'lumotlarni saralash uchun
    fun toolBarMenu() {
        binding.apply {
            imageMenu.setOnClickListener {
                val popupMenu = PopupMenu(this@MainActivity, imageMenu)
                popupMenu.menuInflater.inflate(R.menu.toolber_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.A_dan_Z -> {
                            val list = myDbHelper.userDao().getSortedARight()
                            rvAdapter = RvAdapter(
                                list as ArrayList<Users>,
                                this@MainActivity,
                                this@MainActivity
                            )
                            binding.rv.adapter = rvAdapter
                            Toast.makeText(
                                this@MainActivity,
                                "A dan Z ga tartiblaydi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        R.id.Z_dan_A -> {
                            val list = myDbHelper.userDao().getSortedAReverse()
                            rvAdapter = RvAdapter(
                                list as ArrayList<Users>,
                                this@MainActivity,
                                this@MainActivity
                            )
                            binding.rv.adapter = rvAdapter

                            Toast.makeText(
                                this@MainActivity,
                                "Z dab A gacha saralaydi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        R.id.time_growth -> {
                            val timeList = myDbHelper.userDao().getSortedTimeGrowth()
                            rvAdapter = RvAdapter(
                                timeList as ArrayList<Users>,
                                this@MainActivity,
                                this@MainActivity
                            )
                            binding.rv.adapter = rvAdapter

                            Toast.makeText(
                                this@MainActivity,
                                "Vaqti o'sish tartibida",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        R.id.time_decline -> {
                            val listTime = myDbHelper.userDao().getSortTimeDecline()
                            rvAdapter = RvAdapter(
                                listTime as ArrayList<Users>,
                                this@MainActivity,
                                this@MainActivity
                            )
                            binding.rv.adapter = rvAdapter
                            Toast.makeText(
                                this@MainActivity,
                                "Vaqti kamayish tartibida",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    true
                }
                popupMenu.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialogView?.btnImage?.setImageURI(selectedImage)
    }

    //todo: Ma'lumotlarni qo'shish uchun
    fun addButton() {


        binding.apply {
            addBtn.setOnClickListener {
                dialog = AlertDialog.Builder(this@MainActivity)
                dialogView = ItemDialogBinding.inflate(layoutInflater)
                dialog?.setView(dialogView?.root)


                dialogView?.btnImage?.setOnClickListener {
                    getImageContent.launch("image/*")
//                    Toast.makeText(this@MainActivity, "The picture is selected", Toast.LENGTH_SHORT).show()
                }

                dialog?.setPositiveButton("Save") { _, _ ->

                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH) + 1 // Note: MONTH is zero-based.
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

//                    val hour = calendar.get(Calendar.HOUR)
//                    val minut = calendar.get(Calendar.MINUTE)

                    val timeDay = SimpleDateFormat("HH:mm")

                    val myNewUsers = Users(
                        dialogView?.edtName?.text.toString(),
                        dialogView?.edtAbout?.text.toString(),
                        timeDay.format(Date()),
                        "${day / month / year}",
                        photoPath
                    )

                    if (myNewUsers.name.toString().isNotEmpty() && myNewUsers.about.toString()
                            .isNotEmpty()
                    ) {
                        myDbHelper.userDao().addUsers(myNewUsers)
                        getUserData()
                        dialogView = null
                        Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Invalid user", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog?.setNegativeButton("Close") { _, _ ->
                    dialogView = null
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                }
                dialog?.show()
            }
        }
    }

    //todo: Ma'lumotlarni Databasega qo'shib Rv ga chiqarish
    private fun getUserData() {
        val userList = myDbHelper.userDao().getAllUsers() as ArrayList<Users>
        rvAdapter = RvAdapter(userList, this, this)
        binding.rv.adapter = rvAdapter
        Log.d("rrr", userList.toString())
        rvAdapter.notifyItemChanged(rvAdapter.list.size - 1)
    }

    //todo: Kiritilgan ma'lumotlarni o'zgartirish uchun ...
    override fun itemPopupMenu(users: Users, imageView: ImageView, position: Int) {
        val popupMenu = PopupMenu(this, imageView)
        popupMenu.menuInflater.inflate(R.menu.my_popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_menu -> {
                    dialogView = ItemDialogBinding.inflate(layoutInflater)
                     dialog = AlertDialog.Builder(this)
                    dialog?.setView(dialogView?.root)
                    dialogView?.edtName?.setText(users.name)
                    dialogView?.edtAbout?.setText(users.about)


                    dialogView?.btnImage?.setOnClickListener {
                        getImageContent.launch("image/*")
//                    Toast.makeText(this@MainActivity, "The picture is selected", Toast.LENGTH_SHORT).show()
                    }

                    dialog?.setPositiveButton("Save") { _, _ ->
                        val newName = dialogView?.edtName?.text.toString()
                        val newAbout = dialogView?.edtAbout?.text.toString()
                        val newPhoto = photoPath

                        users.name = newName
                        users.about = newAbout
                        users.photoPath = newPhoto

                        myDbHelper.userDao().updateUsers(users)
                        rvAdapter.notifyItemChanged(position)

                        dialogView = null

                        Toast.makeText(this, "${users.name} Edited", Toast.LENGTH_SHORT).show()
                    }
                    dialog?.setNegativeButton("Close") { _, _ ->
                        Toast.makeText(
                            this,
                            "the information has not been changed",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogView = null
                    }
                    dialog?.show()
                }

                R.id.delete_manu -> {
                    myDbHelper.userDao().deleteUsers(users)
                    rvAdapter.list.remove(users)
                    rvAdapter.notifyItemRemoved(position)
                    rvAdapter.notifyItemRangeChanged(position, rvAdapter.list.size)

                    Toast.makeText(this, "${users.name} Deleted", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun savePhotoToDir(photoUri: Uri) {
        val inputStream = contentResolver?.openInputStream(photoUri)
        val file = File(filesDir, "${Date().time}.jpg")
        val fileOutputStream = FileOutputStream(file)
        inputStream?.copyTo(fileOutputStream)
        inputStream?.close()
        fileOutputStream.close()
        photoPath = file.absolutePath
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        selectedImage = it
        savePhotoToDir(it)
        photoSelectedState = true
    }
}