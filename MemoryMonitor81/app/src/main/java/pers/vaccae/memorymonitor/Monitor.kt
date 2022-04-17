package pers.vaccae.memorymonitor

import android.content.Context
import android.os.Build
import android.os.Debug
import android.util.Log
import java.io.DataOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间： 15:13
 * 功能模块说明：
 */

object Monitor {

    private const val LIB_NAME = "libmemorymonitor.so"


    fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //查找SO的路径
            val libDir: File = File(context.filesDir, "lib")
            if (!libDir.exists()) {
                libDir.mkdirs()
            }
            //判断So库是否存在，不存在复制过来
            val libSo: File = File(libDir, LIB_NAME)
            if (libSo.exists()) libSo.delete()

//            val fileno = File(context.packageManager.getApplicationInfo("pers.vaccae.memorymonitor", 0).sourceDir)
//            Log.i("jvmti", "fileno Path:$fileno")

            val findLibrary =
                ClassLoader::class.java.getDeclaredMethod("findLibrary", String::class.java)
            val libFilePath = findLibrary.invoke(context.classLoader, "memorymonitor") as String
            Log.i("jvmti", "so Path:$libFilePath")

            Files.copy(
                Paths.get(File(libFilePath).absolutePath), Paths.get(
                    libSo.absolutePath
                )
            )


            //加载SO库
            val agentPath = libSo.absolutePath
            System.load(agentPath)

            //开启JVMTI事件监听
            val logDir = File(context.filesDir, "log")
            if (!logDir.exists()) logDir.mkdir()

            //agent连接到JVMTI
            attachAgent(agentPath, context.classLoader);
        } else {
            Log.i("jvmti", "系统版本无法全用JVMTI")
        }
    }

    //agent连接到JVMTI
    private fun attachAgent(agentPath: String, classLoader: ClassLoader) {
        //Android 9.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Debug.attachJvmtiAgent(agentPath, null, classLoader)
        } else {
            //android 9.0以下版本使用反射方式加载
            val vmDebugClazz = Class.forName("dalvik.system.VMDebug")
            val attachAgentMethod = vmDebugClazz.getMethod("attachAgent", String::class.java)
            attachAgentMethod.isAccessible = true
            attachAgentMethod.invoke(null, agentPath)
        }

    }

}