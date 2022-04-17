package pers.vaccae.memorymonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pers.vaccae.memorymonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            ByteTest()
        } catch (e: Throwable) {
            Log.i("except", e.message.toString());
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = "Hello World"
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}