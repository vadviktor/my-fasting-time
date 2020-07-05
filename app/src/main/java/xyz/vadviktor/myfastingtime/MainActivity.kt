package xyz.vadviktor.myfastingtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private const val DEBUG_TAG_GESTURES = "Gestures"

class MainActivity : AppCompatActivity() {
    private lateinit var currentTime: Calendar
    private lateinit var mDetector: GestureDetectorCompat
    private val screenScrollRanges = ArrayList<ClosedFloatingPointRange<Double>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupScreenScrollRanges()
        mDetector = GestureDetectorCompat(this, MyGestureListener())

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

        setupTime()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent): Boolean {
            Log.d(DEBUG_TAG_GESTURES, "onDown: $event")
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.d(DEBUG_TAG_GESTURES, "current X: ${e2.x}")
            val i = screenScrollRanges.indexOfFirst { e2.x in it }
            if (i != -1){
                updateHours(i)
                sb_hours.progress = i
            }
            return true
        }
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

    private fun setupScreenScrollRanges() {
        val screenWidth = this.resources.displayMetrics.widthPixels
        val padding = screenWidth * 0.1
        val chunk = (screenWidth - 2 * padding) / 24

        for (i in 0..23) {
            val from = padding + i * chunk
            val to = padding + ((i + 1) * chunk) - 1
            screenScrollRanges.add(from..to)
        }
    }

    override fun onResume() {
        setupTime()

        super.onResume()
    }
}

