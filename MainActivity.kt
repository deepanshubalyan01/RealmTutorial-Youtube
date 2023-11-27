package com.example.realmsetup

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.realmsetup.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import org.bson.Document
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    lateinit var app : App
    lateinit  var binding: ActivityMainBinding
    lateinit var gsc : GoogleSignInClient
    lateinit var  signInRequest : BeginSignInRequest
    lateinit var  oneTapClient : SignInClient
    val GOOGLE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //realm init...
        Realm.init(this)
        app = App("myfirstapp-kwawk")


//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.clientId))
//            .requestEmail()
//            .build()
//
//        gsc = GoogleSignIn.getClient(this, gso)
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                .setServerClientId(getString(R.string.clientId))
//                .setFilterByAuthorizedAccounts(true)
//                .build())
//            .build()
//
//
//        binding.bttnGoogleSignin.setOnClickListener {
//            val intent = gsc.signInIntent
//            startActivityForResult(intent, GOOGLE_REQUEST_CODE)
//
//
//        }
















        //Email password signin
//        binding.registerBttn.setOnClickListener{
//
//            val email = binding.emailET.text.toString();
//            val pass = binding.passowrdET.text.toString();
//
//            app.emailPassword.registerUserAsync(email,pass){
//                binding.resultTf.text = "user registered"
//            }
//
//        }
//
//        binding.loginBttn.setOnClickListener{
//
//            val email = binding.emailET.text.toString();
//            val pass = binding.passowrdET.text.toString();
//
//            val credentials = Credentials.emailPassword(email,pass)
//            app.loginAsync(credentials){result ->
//
//
//                if(result.error == null)  binding.resultTf.text = "logged in as ${result.get().id}"
//                else { binding.resultTf.text = "some error occured ${result.error.localizedMessage}"   }
//
//            }
//        }


//        Anonymous login
        app.loginAsync(Credentials.anonymous()){result->

            if(result.error == null){
                Log.i("TAG", "onCreate:  login successful ${result.get().id}"  )
            }
            else{
                Log.i("TAG", "onCreate:  login successful ${result.get().id}"  )
            }

        }

//        login code
//        if(app!!.currentUser()?.id == null) {
//            app!!.loginAsync(Credentials.anonymous()) { result ->
//                if (result.error == null)
//                    Log.i("TAG", "onCreate: successfully logged in ... ${result.get().id}")
//            }
//        }
//
//        val myDoc = Document("name" , "futureTech").append("video" , "youtube").append("startedDate" , 2023)
//
//        Log.i("TAG", "onCreate:... $myDoc")
//        findViewById<Button>(R.id.getBttn).setOnClickListener{
//            app?.currentUser()?.functions?.callFunctionAsync("insertFunction",
//                listOf(myDoc),Document::class.java){result ->
//                if(result.error == null){
//                    findViewById<TextView>(R.id.textView).text =  "insert successful"
//                    Log.i("TAG", "onCreate:   insert successful")
//                }
//                else{
//                    Log.i("TAG", "onCreate: error ${result.error.errorMessage}")
//                }
//
//            }
//        }
//        Get Code
//
//        var oldDocument = Document()
//
//        binding!!.getBttn.setOnClickListener{
//
//            app.currentUser()?.functions?.callFunctionAsync("getData", listOf(null),Document::class.java){result ->
//
//                if(result.error == null){
//
//                    oldDocument = result.get()
//                    binding!!.name.text = oldDocument.getString("name");
//                    binding!!.video.text = oldDocument.getString("video");
//                    binding!!.year.text = "${oldDocument.getInteger("startedDate")}";
//
//                }
//
//            }
//        }
//        binding!!.button2.setOnClickListener{
//
//            val sendData = Document("_id" , oldDocument.getObjectId("_id")).append("video",binding!!.editTextTextPersonName.text.toString())
//            app.currentUser()?.functions?.callFunctionAsync("updateFunction", listOf(sendData),Document::class.java){result->
//
//                if(result.error == null){
//                    binding!!.textView.text = "update successful"
//                }
//                else{
//                    Log.i("TAG", "onCreate: ... ${result.error.errorMessage}")
//                }
//            }
//
//
//        }


      //--------------Uploading Media using Realm----------------------//
        //setps:
        /*
        1. Create an AWS S3 I AM role and get credentials
        2. Add aws sdk dependency to mongodb app services
        3. Add your I AM credentials in the realm app
        4. select photo with your app and convert it into base64encoded string
        5. send the encoded string as a function parameter
        6. convert the string back into the 
         */

    binding.pickBttn.setOnClickListener{

        openGallery()
    }
    binding.uploadBttn.setOnClickListener {

        app.currentUser()?.functions?.callFunctionAsync("saveImage", listOf(imageData), Document::class.java){ result->

            binding.resultTf.text = result.get().getString("url")
        }


    }





    }


    //Image Upload start
    val PICK_IMAGE_REQUEST = 10023
    var imageData : String? =""
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            imageData = getImageData(bitmap)
            binding.resultTf.text = "image selected"
        }
    }


    fun getImageData(bitmap:Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val imageBitmap = baos.toByteArray()
        val imageData = Base64.encodeToString(imageBitmap, Base64.DEFAULT)

        return imageData
    }

    //image upload end





    private val imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                val imageData = getImageData(bitmap)
                // Use the imageData as needed
            }
        }


    //google signin
    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            if (completedTask.isSuccessful) {
                val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
                val token: String = account?.idToken!!
                Log.i("TAG", "handleSignInResult: token... $token")
                 // Replace this with your App ID

                   app.loginAsync(Credentials.jwt(token)){result->
                        if(result.isSuccess){
                        Snackbar.make(binding.root,"Login Success!! ${result.get().id}",Snackbar.LENGTH_SHORT).show()
                            }
                       else{
                            Log.i("TAG", "handleSignInResult: error... ${result.error.errorMessage}" )
                        }
                }
            } else {
                Log.e("AUTH", "Google Auth failed: ${completedTask.exception}")
            }
        } catch (e: ApiException) {
            Log.e("AUTH", "Failed to authenticate using Google OAuth: " + e.message);
        }
    }








}
