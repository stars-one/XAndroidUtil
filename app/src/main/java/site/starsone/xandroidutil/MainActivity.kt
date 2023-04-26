package site.starsone.xandroidutil

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.logcat.LogcatActivity
import site.starsone.xandroidutil.util.GlobalDataConfig

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvHome = findViewById<TextView>(R.id.tvUserName)
        val btnGoNext = findViewById<Button>(R.id.btnGoNext)
        val btnGotoLog = findViewById<Button>(R.id.btnGotoLog)

        MyGlobalData.config.addCallBack {
            ToastUtils.showShort("进入回调了....")
            tvHome.text = MyGlobalData.config.currentValue
        }

        tvHome.text = MyGlobalData.config.currentValue

        btnGotoLog.setOnClickListener{
            ActivityUtils.startActivity(LogcatActivity::class.java)

        }

        btnGoNext.setOnClickListener {
//            MyGlobalData.config.setValue("87878")
            ActivityUtils.startActivity(SecondActivity::class.java)

        }

//        val tvHome = findViewById<RemixIconTextView>(R.id.tvHome)
//        tvHome.iconName = "home-5-fill"
    }
}
object MyGlobalData{
    val config = GlobalDataConfig("name","")
}