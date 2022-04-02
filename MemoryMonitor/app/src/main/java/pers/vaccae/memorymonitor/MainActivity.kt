package pers.vaccae.memorymonitor

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pers.vaccae.memorymonitor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ByteTest()
        //getMaxMemoryInfo()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = "Hello World"
    }

    override fun onDestroy() {
        super.onDestroy()
        Monitor.release()
    }

    private fun getMaxMemoryInfo() {
        val rt = Runtime.getRuntime()
        val maxMemory = rt.maxMemory()
        Log.e("MaxMemory:", java.lang.Long.toString(maxMemory / (1024 * 1024)))
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        Log.e("MemoryClass:", activityManager.memoryClass.toString())
        Log.e(
            "LargeMemoryClass:",
            activityManager.largeMemoryClass.toString()
        )
    }

}