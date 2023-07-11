package srmvec.cse.exam

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId


class LoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    companion object {
        private const val RC_SIGN_IN = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hide top
        //hide top
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_post_list)
        setContentView(R.layout.activity_login)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        database = firebaseDatabase.reference
        createSignInIntent()

    }




    private fun createSignInIntent() {
        val providers = arrayListOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.loginimg)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            var response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK){
                FirebaseAuth.getInstance().currentUser

                FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnSuccessListener(this) { instanceIdResult ->
                        val token: String = instanceIdResult.getToken()
                        database.child("admin-token").child(token).child("nofi").setValue("done")
                    }
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)

            }

            else{

                if(response == null){
                    finish()
                }
                if (response?.getError()?.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //Show No Internet Notification
                    return
                }

                if(response?.getError()?.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, response.error?.errorCode.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d("ERRORCODE", response.error?.errorCode.toString())
                    return
                }
            }
        }
    }




}
