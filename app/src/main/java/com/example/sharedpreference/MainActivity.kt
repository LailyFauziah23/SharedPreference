package com.example.sharedpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var prefManager: PrefManager
    private val chanelID = "TES_NOTIF"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        with(binding) {
            btnLogin.setOnClickListener {
                val usernameUntukLogin = "admin"
                val passwordUntukLogin = "12345"
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isNotEmpty()) {
                    Toast.makeText(this@MainActivity, "Username dan password harus diisi",
                        Toast.LENGTH_SHORT).show()
                }else if (username.equals("usernameUntukLogin") &&
                    password.equals("passwordUntukLogin")){
                    prefManager.saveUsername(username)
                    checkLoginStatus()
                }else{
                    Toast.makeText(this@MainActivity, "Username atau password salah",
                        Toast.LENGTH_SHORT).show()
                }
            }
            btnLogout.setOnClickListener {
                prefManager.clearUsername()
                checkLoginStatus()
            }
            btnClear.setOnClickListener {
                prefManager.clearUsername()
                checkLoginStatus()
            }
            btnNotif.setOnClickListener {
                val builder = NotificationCompat.Builder(this@MainActivity, "my_channel")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifikasi PPPB")
                    .setContentText("Hello from the other side")
                    .setAutoCancel(true)

                val notificationManager= getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel=NotificationChannel(
                        chanelID,
                        "Notif PPPB",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notificationManager){
                        createNotificationChannel(notificationChannel)
                        notify(0, builder.build())
                    }
                } else{
                    notificationManager.notify(0, builder.build())
                }
            }
        }
    }
    fun checkLoginStatus() {
        val isLoggedIn = prefManager.getUsername()
        if (isLoggedIn.isEmpty()) {
            binding.llLogged.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        } else {
            binding.llLogged.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        }
    }
}