package xyz.vadviktor.myfastingtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var currentTime: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTime()

        sb_hours.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateHours(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("Seekbar", "start tracking")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("Seekbar", "stop tracking")
            }
        })
    }

    private fun setupTime() {
        currentTime = Calendar.getInstance()
        tv_next_hour.text = currentTime.get(Calendar.HOUR_OF_DAY).toString()

        currentTime.add(Calendar.HOUR_OF_DAY, -16)
        tv_previous_hour.text = currentTime.get(Calendar.HOUR_OF_DAY).toString()
        sb_hours.progress = currentTime.get(Calendar.HOUR_OF_DAY)
    }

    private fun updateHours(previousHour: Int) {
        tv_previous_hour.text = previousHour.toString()

        val previousTime = Calendar.getInstance()
        previousTime.set(Calendar.HOUR_OF_DAY, previousHour)
        previousTime.add(Calendar.HOUR_OF_DAY, 16)
        tv_next_hour.text = previousTime.get(Calendar.HOUR_OF_DAY).toString()
    }

    override fun onResume() {
        setupTime()

        super.onResume()
    }
}

