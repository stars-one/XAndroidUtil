package site.starsone.xandroidutil.util

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.roundToInt

object NetworkUtil {
    private val regex = Regex("""time=([\d.]+)""")

    /**
     * 域名ping
     * @param host ip地址或域名
     * @return Pair<Boolean, Int> 结果示例: 成功: Pair(true,200) 失败: Pair(false,-1)
     */
    fun ping(host: String): Pair<Boolean, Int> {
        val command = "ping -c 1 -i 0.2 -W 2 $host"
        var networkTimeResult = -1
        try {
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line = reader.readLine()
            while (line != null) {
                if (line.contains("time")) {
                    val matchResult = regex.find(line)
                    val networkTime = matchResult?.groupValues?.getOrNull(1)
                    if (networkTime != null) {
                        //四舍五入
                        networkTimeResult = networkTime.toDouble().roundToInt()
                        return Pair(networkTimeResult != -1, networkTimeResult)
                    }
                }
                line = reader.readLine()
            }
            return Pair(false, -1)
        } catch (e: Exception) {
            e.printStackTrace()
            return Pair(false, -1)
        }

    }
}