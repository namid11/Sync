package com.example.sync.Manager

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.*


class IpPortManager(val context: Context) {
    val sharedPref = context.getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
    private var ip: String = sharedPref.getString("ip", "192.168.100.2") ?: "192.168.100.2"
    private var port: Int = sharedPref.getInt("port", 8080)

    fun getIP(): String = ip

    fun getPort(): Int = port

    fun reloadAddressInfo() {
        this.ip = sharedPref.getString("ip", "192.168.100.2") ?: "192.168.100.2"
        this.port = sharedPref.getInt("port", 8080)
    }
}


// PC操作命令の送信ソケット
var operationSocket: DatagramSocket? = null

fun runSocket(ip: String, port: Int, jsonObj: JSONObject, forceExecute: Boolean = false) {
    // ソケットが接続中でも、強制実行する場合、100msだけ待つ
    val waitClosedSocketTime = System.currentTimeMillis()
    var diffWaitTime = System.currentTimeMillis() - waitClosedSocketTime
    while (operationSocket != null && diffWaitTime < 100) {
        if (forceExecute  && !operationSocket!!.isClosed) {
            diffWaitTime = System.currentTimeMillis() - waitClosedSocketTime
            Log.d("[runSocket]", "waiting until closed socket. wait time:%d".format(diffWaitTime))
            continue
        } else {
            break
        }
    }

    // send operated information on socket
    try {
        operationSocket = DatagramSocket(port)
        val address = InetAddress.getByName(ip)

        Thread {
            try {
                val packet = DatagramPacket(jsonObj.toString().toByteArray(), jsonObj.toString().toByteArray().size, address, 8080)
                operationSocket?.send(packet)
                operationSocket?.close()
            } catch (e: Exception) {
                operationSocket?.close()
                e.printStackTrace()
                Log.d("[WARNING]", e.message)
            }
        }.start()
    } catch (e: Exception) {
        Log.e("on Socket", e.message + "\n" + e.localizedMessage)
    }


}