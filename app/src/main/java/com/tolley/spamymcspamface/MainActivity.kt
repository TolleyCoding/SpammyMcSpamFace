package com.tolley.spamymcspamface

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var spamState = false
        var msgAmount = 0;
        val smsManager = SmsManager.getDefault() as SmsManager
        val button: Button = findViewById(R.id.controlButton)
        val numberField: EditText = findViewById(R.id.numberField)
        val msgField: EditText = findViewById(R.id.msgField)
        val timeField: EditText = findViewById(R.id.timeField)
        val msgCounter: TextView = findViewById(R.id.msgCounter)

        var MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
        val mainSpamHandler = Handler(Looper.getMainLooper())
        val sendMsg = object : Runnable {
            override fun run() {
                Toast.makeText(this@MainActivity, "Message Sent", Toast.LENGTH_SHORT).show()
                smsManager.sendTextMessage(numberField.text.toString(), null, msgField.text.toString(), null,  null)
                mainSpamHandler.postDelayed(this, timeField.text.toString().toLong())
                msgAmount++
                msgCounter.setText("Messages Sent: " + msgAmount)
            }
        }

        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_SEND_SMS)

                // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        button.setOnClickListener {

            if (!spamState) {
                button.setText("Stop")
                button.setBackgroundColor(Color.parseColor("#F44336"))
                spamState = true
                Toast.makeText(this@MainActivity, "Spam Started", Toast.LENGTH_SHORT).show()
                mainSpamHandler.post(sendMsg)
            } else {
                button.setText("Start")
                button.setBackgroundColor(Color.parseColor("#4CAF50"))
                spamState = false
                Toast.makeText(this@MainActivity, "Spam Stopped", Toast.LENGTH_SHORT).show()
                mainSpamHandler.removeCallbacks(sendMsg)
            }

        }
    }
}
