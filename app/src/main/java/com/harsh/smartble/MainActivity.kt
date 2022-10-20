package com.harsh.smartble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.harsh.smartble.databinding.ActivityMainBinding
import com.psp.bluetoothlibrary.Bluetooth
import com.psp.bluetoothlibrary.BluetoothListener
import com.psp.bluetoothlibrary.Connection
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bluetooth = Bluetooth(this)
    private var connection = Connection(this)
    private lateinit var connectionListener: BluetoothListener.onConnectionListener
    private lateinit var receiverListener: BluetoothListener.onReceiveListener
    private lateinit var device: BluetoothDevice
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.send.setOnClickListener {
            connection.send(binding.Message.text.toString())
        }
        binding.reset.setOnClickListener {
            connection.disconnect()
            isConnceted()
        }
        binding.serverStart.setOnClickListener {
            initServer()
            initListeners()
        }
    }

    private fun initServer() {
        try {
            connection.accept(true, connectionListener, receiverListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initListeners() {
        bluetooth.turnOnWithoutPermission()
        connection.setUUID(UUID.fromString("2464d2d0-4f9f-11ed-bdc3-0242ac120002"))
        connectionListener = object : BluetoothListener.onConnectionListener {
            override fun onConnectionStateChanged(socket: BluetoothSocket?, state: Int) {
                Toast.makeText(this@MainActivity, state.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onConnectionFailed(errorCode: Int) {
                Toast.makeText(this@MainActivity, errorCode.toString(), Toast.LENGTH_LONG).show()
                connection.disconnect()
                initServer()
            }
        }
        receiverListener = BluetoothListener.onReceiveListener {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun isConnceted() {
        if (connection.isConnected) {
            Log.e("Connected", "True")
        } else {
            initServer()
            initListeners()
        }
    }


}
