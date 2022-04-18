package com.integratedappsforlife.tracking.findmyphone.util

import android.app.Activity
import android.content.Context
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Authentication constructor(private val context: Context){
    lateinit var auth: FirebaseAuth

    private lateinit var verifyCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var authInterface: AuthModelView.AuthInterface
    private lateinit var authModelView: AuthModelView

    fun init(){
        auth = FirebaseAuth.getInstance()
        authModelView = AuthModelView(context, auth)
        verifyCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                Timber.d("completed")
                authInterface.onCompleted(p0)
                authModelView.signInWithPhoneAuthCredential(p0!!)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                authInterface.onFailed(p0)
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                Timber.d("onCodeSent")
                authInterface.onCodeSent(p0)
            }
        }
    }

    fun addListener(authInterface: AuthModelView.AuthInterface) {
        this.authInterface = authInterface
        authModelView.addListener(authInterface)
    }

    fun signIn(credential: PhoneAuthCredential){
        authModelView.signInWithPhoneAuthCredential(credential)
    }

    fun verify(phone: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                context as Activity, // Activity (for callback binding)
                verifyCallback) // OnVerificationStateChangedCallbacks
    }
}

