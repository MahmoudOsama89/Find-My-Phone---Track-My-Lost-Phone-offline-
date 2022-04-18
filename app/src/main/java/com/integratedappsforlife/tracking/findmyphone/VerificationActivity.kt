package com.integratedappsforlife.tracking.findmyphone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.firebase.BuildConfig
import com.integratedappsforlife.tracking.findmyphone.util.Authentication
import com.integratedappsforlife.tracking.findmyphone.util.AuthModelView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
//import com.mytracker.familylocator.familytracker.MyNavigationTutorial
//import com.mytracker.gpstracker.familytracker.BuildConfig
//import com.mytracker.familylocator.familytracker.RegisterNameActivity
import com.integratedappsforlife.tracking.findmyphone.util.CountryData
import timber.log.Timber
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import android.telephony.TelephonyManager
import android.util.Log
import java.util.*


class VerificationActivity : AppCompatActivity() {

    private val VERIFICATION_TIMEOUT: Long = 60 * 1000
    private val PREF_VERIFICATION: String = "pref_verification"
    private val VERIFICATION_CODE: String = "ver_code"
    lateinit var phoneEt : EditText
    lateinit var verifyBtn : Button
    lateinit var checkBtn: Button
    lateinit var countDownTimer: TextView
    lateinit var code: String
    lateinit var id: String
    lateinit var countryCode: String

    lateinit var auth: FirebaseAuth
    lateinit var textForNote: TextView
    lateinit var spinner: Spinner
    lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var verifyCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var authInterface: AuthModelView.AuthInterface
    private val authentication = Authentication(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_verification)
        val settingSaved = SettingSaved(this)
        val u = FirebaseAuth.getInstance().currentUser
        if( u != null && u.phoneNumber != null){
                if(settingSaved.loadPhoneNumber() == null){
                    settingSaved.savePhoneNumber(u.phoneNumber)
                }
                val intent = Intent(this, MainActivity::class.java)
                //intent.putExtra("SHOW_WELCOME", true)
                startActivity(intent)
                finish()

        }
        spinner = findViewById(R.id.country_code)
        phoneEt = findViewById(R.id.phone_et)
        verifyBtn = findViewById(R.id.verify_btn)
        countDownTimer = findViewById(R.id.count_down)
        checkBtn = findViewById(R.id.code_btn)
        textForNote = findViewById(R.id.noteAbove)
//        auth = FirebaseAuth.getInstance()

        spinnerAdapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                CountryData.countryNames)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                countryCode = CountryData.countryAreaCodes[p2]
            }
        }
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = tm.simCountryIso
        val countryCodeValue = tm.networkCountryIso
        val loc = Locale("", countryCode)
        val fullname = loc.getDisplayCountry()
        val spinnerPosition = spinnerAdapter.getPosition(fullname);

        spinner.setSelection(spinnerPosition)


        Log.d("Hey256",countryCode+"   "+countryCodeValue + fullname)

        code = ""
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            phoneEt.setText("650-555-3434")
            code = "123456"
        } else {
            code = ""
        }
        id = ""

        countDownTimer.visibility = View.GONE

        authentication.init()

        updateUI(false)
        verifyBtn.setOnClickListener {
            if (checkInput()) {
                spinner.visibility = View.GONE
                verifyPhone()
                holdVerification()
            } else {
                Toast.makeText(this, "Please enter your phone", Toast.LENGTH_SHORT).show()
            }
        }
        checkBtn.setOnClickListener {
            code = phoneEt.text.toString()
            try {
                val credential = PhoneAuthProvider.getCredential(id, code)
                authentication.signIn(credential)
                textForNote.visibility = View.GONE
            } catch (e: Exception) {
                val toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }

        }

        authInterface = object: AuthModelView.AuthInterface {
            override fun onCompleted(p0: PhoneAuthCredential?) {
                Timber.d("onCompleted: ")
                holdVerification()
                updateUI(true)
            }

            override fun onFailed(p0: FirebaseException?) {
                Timber.e(p0)
                updateUI(false)
                Toast.makeText(
                        this@VerificationActivity,
                        p0.let { it?.localizedMessage },
                        Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCodeSent(p0: String?) {
                Timber.d("onCodeSent: ")
                checkBtn.visibility = View.VISIBLE
                id = p0!!
            }

            override fun onException(p0: FirebaseAuthInvalidCredentialsException) {
                Timber.e(p0)
                updateUI(false)
            }
        }
        authentication.addListener(authInterface)
    }


    private fun updateUI(isChecking: Boolean) {
        if (isChecking) {
            phoneEt.hint = getString(R.string.ver_code)
            phoneEt.inputType = InputType.TYPE_CLASS_NUMBER
//            checkBtn.visibility = View.VISIBLE
            verifyBtn.visibility = View.GONE
            phoneEt.setText("")
            if(BuildConfig.DEBUG){
                phoneEt.setText("123456")
//                checkBtn.visibility = View.VISIBLE
            }
        } else {
            verifyBtn.isEnabled = true
//            if(BuildConfig.DEBUG) phoneEt.setText("+1 650-555-3434")
            phoneEt.hint = getString(R.string.enter_your_phone)
            phoneEt.setText("")
            phoneEt.inputType = InputType.TYPE_CLASS_PHONE
            checkBtn.visibility = View.GONE
            verifyBtn.visibility = View.VISIBLE
            countDownTimer.visibility = View.GONE
        }
    }
    private fun verifyPhone() {
        var phone = "+" + countryCode + phoneEt.text.toString()
        if(BuildConfig.DEBUG) phone = "+1650-555-3434"
        Timber.d("phoone: %s", phone)
        authentication.verify(phone)
        updateUI(true)
    }

    private fun holdVerification() {
        verifyBtn.isEnabled = false
        countDownTimer.visibility = View.VISIBLE
        startTimer()
    }

    private fun startTimer() {
        object :CountDownTimer(VERIFICATION_TIMEOUT, 1000){
            override fun onFinish() {
                verifyBtn.isEnabled = true
                countDownTimer.visibility = View.GONE
            }

            override fun onTick(p0: Long) {
                countDownTimer.text = ((p0/1000).toString())
            }
        }.start()
    }

    private fun checkInput(): Boolean {
        return !phoneEt.text.equals("")
    }
}
