package com.example.madpractical7_20012021014

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.example.madpractical7_20012021014.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var mili:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.textview42.format12Hour = "hh:mm:ss a"

        binding.cardview2.visibility= View.GONE

        binding.btnCreateAlarm.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            var min = cal.get(Calendar.MINUTE)
            val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = {
                    view, h, m ->
                mili=getMillis(h,m)
                setAlarm(getMillis(h,m),"Start")
                binding.cardview2.visibility=View.VISIBLE
                binding.textview4.text=h.toString()+":"+m.toString()
            }),hour,min,false)
            tpd.show()
        }

        binding.btnCancelAlarm.setOnClickListener{
            setAlarm(mili,"Stop")
            binding.cardview2.visibility=View.GONE
        }
    }
    fun setAlarm(millisTime: Long, str: String)
    {
        val intent = Intent(this, AlarmBroadCastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 234324243, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(str == "Start") {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
        }else if(str == "Stop")
        {
            alarmManager.cancel(pendingIntent)
            sendBroadcast(intent)
        }
    }

    fun getMillis(hour:Int,min:Int):Long
    {
        val setcalendar = Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0
        return setcalendar.timeInMillis
    }


}