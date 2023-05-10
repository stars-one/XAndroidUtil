package site.starsone.xandroidutil

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.logcat.LogcatActivity
import site.starsone.xandroidutil.util.GlobalDataConfig
import site.starsone.xandroidutil.view.FloatingActionBtnMenu

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

        btnGotoLog.setOnClickListener {
            ActivityUtils.startActivity(LogcatActivity::class.java)

        }

        btnGoNext.setOnClickListener {
//            MyGlobalData.config.setValue("87878")
            ActivityUtils.startActivity(SecondActivity::class.java)

        }

        val fabMenu = findViewById<FloatingActionBtnMenu>(R.id.fabMenu)

        //使用menu构造子项列表
        /*fabMenu.buildItemsByMenuData(R.menu.mymenu_jj) {
            when (it) {
                R.id.menuFirst -> {
                    ToastUtils.showShort("点击了first")
                }
                R.id.menuSec -> {
                    ToastUtils.showShort("点击了sec")
                }
            }
        }*/
        //使用list数据构造子项列表
        val list = listOf(
            FloatingActionBtnMenu.MenuItemData("", R.drawable.ic_baseline_adb_24),
            FloatingActionBtnMenu.MenuItemData(
                "sec", R.drawable.ic_baseline_adb_24,
                FloatingActionBtnMenu.MenuItemStyle()
            )
        )
        fabMenu.buildItemsByListData(list) {
            when (it) {
                0 -> ToastUtils.showShort("点击了first")
                1 -> ToastUtils.showShort("点击了sec")
            }
        }

//        repeat(2) {
//            val fab = FloatingActionButton(this)
//            fab.setImageResource(R.drawable.ic_baseline_adb_24)
//            fab.size = FloatingActionButton.SIZE_NORMAL
//            fab.setOnClickListener {
//                ToastUtils.showShort("点击了子按钮")
//            }
//            fabMenu.addFloatingActionButton(fab)
//        }


//        val tvHome = findViewById<RemixIconTextView>(R.id.tvHome)
//        tvHome.iconName = "home-5-fill"
    }
}

object MyGlobalData {
    val config = GlobalDataConfig("name", "")
}