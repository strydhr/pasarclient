package com.strydhr.thepasar.Controller

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.FileProvider
import com.fonfon.kgeohash.GeoHash
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.strydhr.thepasar.Model.User
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.AuthServices
import com.strydhr.thepasar.Utilities.googleApi
import com.strydhr.thepasar.Utilities.storage
import com.strydhr.thepasar.Utilities.userGlobal
import kotlinx.android.synthetic.main.activity_signup_location.*
import kotlinx.android.synthetic.main.popup_edit_address.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SignupLocationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var confirmBtn: ImageButton
    lateinit var address: EditText
    lateinit var unitNumber: EditText
    lateinit var welcomeNote: TextView

    lateinit var deliveryAddress:String
    var coordinate: ArrayList<Double> = ArrayList()
    lateinit var geoHash:String
    var username:String = ""
    lateinit var currentPhotoPath: String
    var photoFile: File? = null
    var photoURI: Uri? = null
    private var imageData: ByteArray? = null
    var hasUploadedImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_location)
        auth = FirebaseAuth.getInstance()
        username = intent.getStringExtra("username")

        welcomeNote = findViewById(R.id.welcome_note)
        confirmBtn = findViewById(R.id.signup_proceedBtn)
        address = findViewById(R.id.signup_address_tf)
        unitNumber = findViewById(R.id.signup_unitnumber_tf)
        welcomeNote.text = "Hi ${username}, We need your profile and delivery address to complete the registration"


        Places.initialize(this, googleApi)

        val placesClient = Places.createClient(this)

        signup_address_tf.setOnClickListener{


            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = listOf(Place.Field.ID, Place.Field.NAME)

            // Start the autocomplete intent.
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, listOf(
                    Place.Field.ADDRESS_COMPONENTS, Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS))
                .setCountry("MY")
                .build(applicationContext)
            startActivityForResult(intent, 2)
        }

        signup_profileimage.setOnClickListener {
            selectImage()
        }

        confirmBtn.setOnClickListener {
            errorHandler(address.text.toString(),signup_phone_tf.text.toString())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        println("hello")
                        val place = Autocomplete.getPlaceFromIntent(data)
                        val placeStr = place.name

                        address.setText(placeStr)
                        deliveryAddress = place.address!!
                        coordinate.add( place.latLng!!.latitude)
                        coordinate.add(place.latLng!!.longitude)
                        geoHash = GeoHash(place.latLng!!.latitude,place.latLng!!.longitude,10).toString()
                        Log.i(ContentValues.TAG, "Place: ${place.name}, ${place.address},${place.latLng?.latitude}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {

                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(ContentValues.TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }else{
            when (requestCode) {
                0 -> if (resultCode === Activity.RESULT_OK && android.R.attr.data != null) {
//                    val selectedImage = data?.extras
//                    val bmp = selectedImage?.get("data") as Bitmap
                    signup_profileimage.setImageURI(photoURI)
                    val bitmap = (signup_profileimage.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                    imageData = baos.toByteArray()
                    hasUploadedImage = true
                }
                1 -> if (resultCode === Activity.RESULT_OK && android.R.attr.data != null) {
                    signup_profileimage.setImageURI(data?.data)
                    val bitmap = (signup_profileimage.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                    imageData = baos.toByteArray()
                    hasUploadedImage = true
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun errorHandler(address:String,phone:String){
        if (address.isEmpty()){
            val toast = Toast.makeText(
                applicationContext,
                "Please fill your delivery address", Toast.LENGTH_SHORT
            )
            toast.show()
        }else if (phone.isEmpty()){
            val toast = Toast.makeText(
                applicationContext,
                "Please fill your mobile number", Toast.LENGTH_SHORT
            )
            toast.show()
        }else{
            if (!hasUploadedImage){
                val toast = Toast.makeText(
                    applicationContext,
                    "Please upload a profile image", Toast.LENGTH_SHORT
                )
                toast.show()
            }else{
                var unitStr:String
                if (unitNumber.text.isEmpty()){
                    unitStr = ""
                }else{
                    unitStr = unitNumber.text.toString()
                }
                uploadImage(imageData!!){isSuccess,url ->
                    if (isSuccess){
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(Constraints.TAG, "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = task.result

                            // Log and toast
                            AuthServices.addUserToDatabase(username,phone,deliveryAddress,unitStr,url,coordinate,geoHash,token.toString()){
                                userGlobal = User(auth.uid,username,phone,deliveryAddress,unitStr,coordinate,geoHash,url,true,true,token.toString())
                                val intent = Intent(this, MainTabActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        })

                    }
                }
            }


        }
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder =
            AlertDialog.Builder(this)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        // Create the File where the photo should go
                        photoFile = try {
                            createImageFile()
                        } catch (ex: IOException) {

                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            photoURI = FileProvider.getUriForFile(
                                this,
                                "com.strydhr.thepasar.android.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, 0)
                        }
                    }
                }
            } else if (options[item] == "Choose from Gallery") {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun uploadImage(image:ByteArray,complete:(Boolean,String)->Unit){

        val filename = "profile"

        val storageRef = storage.reference.child("UsersFiles").child(auth.uid!!).child(filename)
        var uploadTask = storageRef.putBytes(image).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                var url = downloadUri.toString()
                complete(true,url)


            } else {
                // Handle failures
                // ...
                complete(false,"")
            }
        }
    }
}
