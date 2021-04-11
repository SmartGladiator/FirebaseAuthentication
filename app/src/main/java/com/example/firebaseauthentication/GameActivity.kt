package com.example.firebaseauthentication

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class GameActivity : AppCompatActivity(), View.OnClickListener {
    var gridView: RecyclerView? = null
    var colorList = ArrayList<Int?>()
    var customAdapter: CustomAdapter? = null
    var btnClick = false
    private var n: CountDownTimer? = null
    var btnTimer: Button? = null
    var btnScramble: Button? = null
    private var checkMatch = 0
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var userDetails: UserDetails? = null
    var username: String? = ""
    override fun onResume() {
        super.onResume()
        btnScramble!!.isEnabled = true
        val prefs = getSharedPreferences("sharedpreference", MODE_PRIVATE)
        val restoredText = prefs.getString("name", null)
        if (restoredText != null) {
            username = prefs.getString("name", "") //"No name defined" is the default value.
        }

        if (username.equals("")) {
            username = prefs.getString("email", "") //"No name defined" is the default value.

        }

        firebaseDatabase = FirebaseDatabase.getInstance()

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase!!.getReference("userdata")
        colorList.clear()
        colorList = ArrayList()
        if (getString(R.string.app_name) == "Prod") {
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
            colorList.add(R.drawable.bg_yellow)
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
            colorList.add(R.drawable.bg_yellow)
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
            colorList.add(R.drawable.bg_yellow)
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
            colorList.add(R.drawable.bg_yellow)
        } else {
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
            colorList.add(R.drawable.bg_red)
            colorList.add(R.drawable.bg_green)
            colorList.add(R.drawable.bg_blue)
        }
        customAdapter = CustomAdapter(this, colorList, if (getString(R.string.app_name) == "Prod") 4 else 3)
        gridView!!.layoutManager = GridLayoutManager(this, if (getString(R.string.app_name) == "Prod") 4 else 3)
        gridView!!.adapter = customAdapter
        btnScramble!!.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        init()
    }

    private fun init() {
        btnTimer = findViewById(R.id.tvTimer)
        btnScramble = findViewById(R.id.btnScramble)
        gridView = findViewById<View>(R.id.gridView) as RecyclerView
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        updateSizeInfo()
    }

    private fun updateSizeInfo() {
        width = gridView!!.width
        if (customAdapter == null) {
            gridView!!.layoutManager = GridLayoutManager(this, if (getString(R.string.app_name) == "Prod") 4 else 3)
            customAdapter = CustomAdapter(this@GameActivity, colorList, if (getString(R.string.app_name) == "Prod") 4 else 3)
            gridView!!.adapter = customAdapter
        }
    }

    fun timerStart(timeLengthMilli: Long) {
        n = object : CountDownTimer(timeLengthMilli, 1000) {
            override fun onTick(milliTillFinish: Long) {
                btnTimer!!.text = (milliTillFinish / (1000 * 60)).toString() + ":" + (if (java.lang.Long.toString(milliTillFinish / 1000 - milliTillFinish / (1000 * 60) * 60).length > 1) "" else "0") + (milliTillFinish / 1000 - milliTillFinish / (1000 * 60) * 60) + " seconds more"
                checklist()
                val time = (milliTillFinish / 1000).toFloat()
                if (getString(R.string.app_name) == "Prod") {
                    if (checkMatch == 4) {
                        btnScramble!!.isEnabled = true
                        btnTimer!!.text = "Awesome"
                        checkMatch = 0
                        Collections.shuffle(colorList)
                        customAdapter!!.notifyDataSetChanged()
                        addDataFirebase(username, "100", "success", "$time sec")
                        n!!.cancel()
                    }
                } else {
                    if (checkMatch == 3) {
                        btnScramble!!.isEnabled = true
                        n!!.cancel()
                        btnTimer!!.text = "Awesome"
                        checkMatch = 0
                        Collections.shuffle(colorList)
                        customAdapter!!.notifyDataSetChanged()
                        addDataFirebase(username, "100", "success", "$time sec")
                    }
                }
            }

            override fun onFinish() {
                btnScramble!!.isEnabled = true
                val status: String
                when (checkMatch) {
                    1 -> {
                        btnTimer!!.text = "Good"
                        status = "fail"
                    }
                    2 -> {
                        status = "fail"
                        btnTimer!!.text = "Super"
                    }
                    3 -> {
                        status = "success"
                        btnTimer!!.text = "Excellent"
                    }
                    4 -> {
                        status = "success"
                        btnTimer!!.text = "Awesome"
                    }
                    else -> {
                        status = "fail"
                        btnTimer!!.text = "Better luck"
                    }
                }
                Collections.shuffle(colorList)
                customAdapter!!.notifyDataSetChanged()
                val `val` = checkMatch * 25
                addDataFirebase(username, `val`.toString(), status, if (getString(R.string.app_name) == "Prod") "60 sec" else "30 sec")
                checkMatch = 0
            }
        }
        (n as CountDownTimer).start()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnScramble -> {
                btnScramble!!.isEnabled = false
                btnClick = true
                customAdapter!!.scrumbleFn()
                customAdapter!!.notifyDataSetChanged()
                gridView!!.adapter = customAdapter
                timerStart(if (getString(R.string.app_name) == "Prod") 60000 else 30000.toLong())
            }
        }
    }

    fun checklist() {
        colorList = customAdapter!!.colorList
        val `val` = if (getString(R.string.app_name) == "Prod") 4 else 3
        when (`val`) {
            3 -> {
                checkMatch = 0
                if (colorList[3] == colorList[4] && colorList[4] == colorList[5]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[6] == colorList[7] && colorList[7] == colorList[8]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[0] == colorList[1] && colorList[2] == colorList[2]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[0] == colorList[3] && colorList[3] == colorList[6]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[2] == colorList[5] && colorList[5] == colorList[8]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[1] == colorList[4] && colorList[4] == colorList[7]) {
                    checkMatch = checkMatch + 1
                }
            }
            4 -> {
                checkMatch = 0
                if (colorList[0] == colorList[1] && colorList[2] == colorList[3]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[4] == colorList[5] && colorList[6] == colorList[7]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[8] == colorList[9] && colorList[10] == colorList[11]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[12] == colorList[13] && colorList[14] == colorList[15]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[0] == colorList[4] && colorList[8] == colorList[12]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[1] == colorList[5] && colorList[9] == colorList[3]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[2] == colorList[6] && colorList[10] == colorList[14]) {
                    checkMatch = checkMatch + 1
                }
                if (colorList[3] == colorList[7] && colorList[11] == colorList[15]) {
                    checkMatch = checkMatch + 1
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun addDataFirebase(name: String?, score: String, status: String, time: String) {
        try {
            userDetails = UserDetails(name.toString(), score, status, time)
            databaseReference!!.child(databaseReference!!.push().key!!).setValue(userDetails)
        } catch (e: Exception) {
            Log.e("GameActivity", e.message!!)
        }
    }

    companion object {
        var width = 0
    }
}