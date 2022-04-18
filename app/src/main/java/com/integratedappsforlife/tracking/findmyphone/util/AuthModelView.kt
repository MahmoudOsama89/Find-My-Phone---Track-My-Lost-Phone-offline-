package com.integratedappsforlife.tracking.findmyphone.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
//import com.mytracker.familylocator.familytracker.MyNavigationTutorial
//import com.mytracker.familylocator.familytracker.RegisterNameActivity
import timber.log.Timber
import com.integratedappsforlife.tracking.findmyphone.MainActivity
import com.integratedappsforlife.tracking.findmyphone.SettingSaved
import com.integratedappsforlife.tracking.findmyphone.SettingSaved.MyPREFERENCES

class AuthModelView constructor(private val context: Context, private val auth: FirebaseAuth) {
    val sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
    private lateinit var authInterface: AuthInterface

    fun addListener(authInterface: AuthInterface) {
        this.authInterface = authInterface
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCredential:success")
//                        val uri = Uri.parse("android.resource://com.mytracker.gpstracker.familytracker/drawable/defaultprofile")
                        val editor = sharedpreferences.edit()
                        val user: FirebaseUser = task.result!!.user

                        val PhoneN = user.phoneNumber
                        Log.d("NewTRY",PhoneN)
                        val settingSaved = SettingSaved(context)
                        settingSaved.savePhoneNumber(PhoneN)
                        val checkOne = settingSaved.loadPhoneNumber()
                        Log.d("NewTRY","checkOne :"+checkOne)

                        context.startActivity(Intent(context, MainActivity::class.java))
                        context.finish()
                        // ...

                    } else {
                        // Sign in failed, display a message and update the UI
                        Timber.d(task.exception, "signInWithCredential:failure")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            authInterface.onException(task.exception as FirebaseAuthInvalidCredentialsException)
                        }
                    }
                }
    }

    interface AuthInterface {
        fun onCompleted(p0: PhoneAuthCredential?)
        fun onFailed(p0: FirebaseException?)
        fun onCodeSent(p0: String?)
        fun onException(p0: FirebaseAuthInvalidCredentialsException)
    }
}