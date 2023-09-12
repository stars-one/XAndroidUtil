package com.hjq.logcat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import java.io.File
import java.io.IOException
import java.util.*

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/Logcat
 * time   : 2020/01/24
 * desc   : Logcat 显示窗口
 */
class LogcatActivity : AppCompatActivity(), TextWatcher,
    View.OnLongClickListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, LogcatManager.Callback,
    LogcatAdapter.OnItemLongClickListener, LogcatAdapter.OnItemClickListener {
    private lateinit var mRootView: View
    private lateinit var mBarView: View
    private lateinit var mCheckBox: CheckBox
    private lateinit var mSaveView: View
    private lateinit var mLevelLayout: ViewGroup
    private lateinit var mLevelView: TextView
    private lateinit var mInputView: EditText
    private lateinit var mIconView: ImageView
    private lateinit var mClearView: View
    private lateinit var mHideView: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mDownView: View
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: LogcatAdapter
    private var mLogLevel = LogLevel.VERBOSE

    /** 暂停输出日志标记  */
    private var mPauseLogFlag = false

    /** Tag 过滤规则  */
    private val mTagFilter: MutableList<String> =
        ArrayList()

    /** 搜索关键字  */
    private val mSearchKeyword = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logcat_activity_logcat)
        mRootView = findViewById(R.id.ll_log_root)
        mBarView = findViewById(R.id.ll_log_bar)
        mCheckBox = findViewById(R.id.cb_log_switch)
        mSaveView = findViewById(R.id.iv_log_save)
        mLevelLayout = findViewById(R.id.ll_log_level)
        mLevelView = findViewById(R.id.tv_log_level)
        mInputView = findViewById(R.id.et_log_search_input)
        mIconView = findViewById(R.id.iv_log_search_icon)
        mClearView = findViewById(R.id.iv_log_logcat_clear)
        mHideView = findViewById(R.id.iv_log_logcat_hide)
        mRecyclerView =
            findViewById(R.id.lv_log_logcat_list)
        mDownView = findViewById(R.id.ib_log_logcat_down)
        mAdapter = LogcatAdapter(this)
        mAdapter.setOnItemClickListener(this)
        mAdapter.setOnItemLongClickListener(this)
        mRecyclerView.setAnimation(null)
        mRecyclerView.setAdapter(mAdapter)
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.setLayoutManager(mLinearLayoutManager)
        mCheckBox.setOnCheckedChangeListener(this)
        mInputView.addTextChangedListener(this)
        mInputView.setText(LogcatConfig.getLogcatText())
        setLogLevel(LogcatConfig.getLogcatLevel())
        mSaveView.setOnClickListener(this)
        mLevelLayout.setOnClickListener(this)
        mIconView.setOnClickListener(this)
        mClearView.setOnClickListener(this)
        mHideView.setOnClickListener(this)
        mDownView.setOnClickListener(this)
        mSaveView.setOnLongClickListener(this)
        mCheckBox.setOnLongClickListener(this)
        mLevelLayout.setOnLongClickListener(this)
        mClearView.setOnLongClickListener(this)
        mHideView.setOnLongClickListener(this)

        // 开始捕获
        LogcatManager.start(this)
        mRecyclerView.postDelayed(
            Runnable { mLinearLayoutManager.scrollToPosition(mAdapter.itemCount - 1) },
            500
        )
        initFilter()
        refreshLayout()
        
        val flag = SPUtils.getInstance().getBoolean("locat_has_tip",false)
        if (!flag) {
            AlertDialog.Builder(this).setTitle("使用说明").setMessage("本页面功能主要是为了方便开发者排查错误整的。\n 1.先点击上方的倒数第二个按钮(扫把)，先当前所有日志清空; \n 2.点击最右边的按钮,将当前页面最小化; 3.回到页面,重复你遇到问题的操作; \n 4.再次进入本页面,点击第二个按钮,将日志保存并分享给开发者来进行问题排查!")
                .setPositiveButton("确定"){dilog,witch ->
                    dilog.dismiss()
                }
                .setPositiveButton("不再提示"){dilog,witch ->
                    SPUtils.getInstance().put("locat_has_tip",true)
                    dilog.dismiss()
                }
                .create().show()
        }

    }

    override fun onReceiveLog(info: LogcatInfo) {
        // 这个日志是当前进程打印的
        // if (Integer.parseInt(info.getPid()) != android.os.Process.myPid()) {
        //    return;
        // }
        // 这个 Tag 必须不在过滤列表中
        if (mTagFilter.contains(info.tag)) {
            return
        }
        mRecyclerView.post(LogRunnable(info))
    }

    override fun onLongClick(v: View): Boolean {
        if (v === mCheckBox) {
            LogcatUtils.toast(this, R.string.logcat_capture)
        } else if (v === mSaveView) {
            LogcatUtils.toast(this, R.string.logcat_save)
        } else if (v === mLevelView) {
            LogcatUtils.toast(this, R.string.logcat_level)
        } else if (v === mClearView) {
            LogcatUtils.toast(this, R.string.logcat_empty)
        } else if (v === mHideView) {
            LogcatUtils.toast(this, R.string.logcat_close)
        }
        return true
    }

    override fun onClick(v: View) {
        if (v === mSaveView) {
            try {
                val file = LogcatUtils.saveLogToFile(this, mAdapter.data)
                shareLogFile(file)
                LogcatUtils.toast(
                    this,
                    resources.getString(R.string.logcat_save_succeed) + file.path
                )
            } catch (e: IOException) {
                e.printStackTrace()
                LogcatUtils.toast(
                    this,
                    resources.getString(R.string.logcat_save_fail)
                )
            }
        } else if (v === mLevelLayout) {
            ChooseWindow(this)
                .setList(*ARRAY_LOG_LEVEL)
                .setListener { position ->
                    when (position) {
                        0 -> setLogLevel(LogLevel.VERBOSE)
                        1 -> setLogLevel(LogLevel.DEBUG)
                        2 -> setLogLevel(LogLevel.INFO)
                        3 -> setLogLevel(LogLevel.WARN)
                        4 -> setLogLevel(LogLevel.ERROR)
                        else -> {
                        }
                    }
                }
                .show()
        } else if (v === mIconView) {
            val keyword = mInputView.text.toString()
            if ("" == keyword) {
                showSearchKeyword()
            } else {
                mInputView.setText("")
            }
        } else if (v === mClearView) {
            LogcatManager.clear()
            mAdapter.clearData()
        } else if (v === mHideView) {
            onBackPressed()
        } else if (v === mDownView) {
            // 滚动到列表最底部
            mLinearLayoutManager.scrollToPosition(mAdapter.itemCount - 1)
        }
    }

    override fun onCheckedChanged(
        buttonView: CompoundButton,
        isChecked: Boolean
    ) {
        mPauseLogFlag = if (isChecked) {
            LogcatUtils.toast(this, R.string.logcat_capture_pause)
            LogcatManager.pause()
            true
        } else {
            LogcatManager.resume()
            false
        }
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {
        mInputView.removeCallbacks(mSearchRunnable)
        mInputView.postDelayed(mSearchRunnable, 500)
        mInputView.removeCallbacks(mSearchKeywordRunnable)
        mInputView.postDelayed(mSearchKeywordRunnable, 3000)
    }

    override fun onItemClick(info: LogcatInfo, position: Int) {
        mAdapter.onItemClick(position)
    }

    override fun onItemLongClick(info: LogcatInfo, position: Int) {
        ChooseWindow(this)
            .setList(
                R.string.logcat_options_copy,
                R.string.logcat_options_share,
                R.string.logcat_options_delete,
                R.string.logcat_options_shield
            )
            .setListener { location ->
                when (location) {
                    0 -> copyLog(position)
                    1 -> shareLog(position)
                    2 -> mAdapter.removeItem(position)
                    3 -> addFilter(mAdapter.getItem(position).tag)
                    else -> {
                    }
                }
            }
            .show()
    }

    private fun copyLog(position: Int) {
        val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(
            ClipData.newPlainText("log", mAdapter.getItem(position).content)
        )
        LogcatUtils.toast(this@LogcatActivity, R.string.logcat_copy_succeed)
    }

    private fun shareLog(position: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mAdapter.getItem(position).content)
        startActivity(
            Intent.createChooser(
                intent,
                resources.getString(R.string.logcat_options_share)
            )
        )
    }

    /**
     * 分享日志文件
     * @param file 日志文件
     */
    private fun shareLogFile(file: File) {
        openShare(file,"分享日志文件")
    }

    /**
     * 打开系统分享弹窗 (Open the system sharing popup)
     */
    fun openShare(file: File, title: String = "分享文件") {
        val uri = FileProvider.getUriForFile(this,
            AppUtils.getAppPackageName() + ".provider",
            file
        )
//        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // Put the Uri and MIME type in the result Intent
        intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension))

        //https://stackoverflow.com/questions/3918517/calling-startactivity-from-outside-of-an-activity-context
        val chooserIntent: Intent = Intent.createChooser(intent, title)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(chooserIntent)
    }

    private fun setLogLevel(level: String) {
        if (level == mLogLevel) {
            refreshLogLevelLayout()
            return
        }
        mLogLevel = level
        mAdapter.setLogLevel(level)
        LogcatConfig.setLogcatLevel(level)
        afterTextChanged(mInputView.text)
        refreshLogLevelLayout()
    }

    private fun refreshLogLevelLayout() {
        val arrayLogLevel: Array<String>
        arrayLogLevel = if (LogcatUtils.isPortrait(this)) {
            ARRAY_LOG_LEVEL_PORTRAIT
        } else {
            ARRAY_LOG_LEVEL
        }
        when (mLogLevel) {
            LogLevel.VERBOSE -> mLevelView.text = arrayLogLevel[0]
            LogLevel.DEBUG -> mLevelView.text = arrayLogLevel[1]
            LogLevel.INFO -> mLevelView.text = arrayLogLevel[2]
            LogLevel.WARN -> mLevelView.text = arrayLogLevel[3]
            LogLevel.ERROR -> mLevelView.text = arrayLogLevel[4]
            else -> {
            }
        }
    }

    private inner class LogRunnable(private val info: LogcatInfo) : Runnable {
        override fun run() {
            mAdapter.addItem(info)
        }

    }

    /**
     * 初始化 Tag 过滤器
     */
    private fun initFilter() {
        try {
            mTagFilter.addAll(LogcatUtils.readTagFilter(this))
        } catch (e: IOException) {
            e.printStackTrace()
            LogcatUtils.toast(this, R.string.logcat_read_config_fail)
        }
        val list =
            resources.getStringArray(R.array.logcat_filter_list)
        for (tag in list) {
            if (tag == null || "" == tag) {
                continue
            }
            if (mTagFilter.contains(tag)) {
                continue
            }
            mTagFilter.add(tag)
        }
    }

    /**
     * 添加过滤的 TAG
     */
    private fun addFilter(tag: String) {
        if ("" == tag) {
            return
        }
        if (mTagFilter.contains(tag)) {
            return
        }
        mTagFilter.add(tag)
        try {
            val newTagFilter: MutableList<String> =
                ArrayList(mTagFilter)
            newTagFilter.removeAll(
                Arrays.asList(
                    *resources.getStringArray(
                        R.array.logcat_filter_list
                    )
                )
            )
            val file = LogcatUtils.writeTagFilter(this, newTagFilter)
            LogcatUtils.toast(
                this,
                resources.getString(R.string.logcat_shield_succeed) + file.path
            )

            // 从列表中删除关于这个 Tag 的日志
            val data = mAdapter.data
            for (i in data.indices) {
                val info = data[i]
                if (info.tag == tag) {
                    mAdapter.removeItem(i)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            LogcatUtils.toast(this, R.string.logcat_shield_fail)
        }
    }

    override fun onBackPressed() {
        // 清除输入焦点
        mInputView.clearFocus()
        // 移动到上一个任务栈
        moveTaskToBack(false)
    }

    override fun onResume() {
        super.onResume()
        if (mPauseLogFlag) {
            return
        }
        LogcatManager.resume()
    }

    override fun onPause() {
        super.onPause()
        if (mPauseLogFlag) {
            return
        }
        LogcatManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogcatManager.destroy()
        mInputView.removeCallbacks(mSearchRunnable)
        mInputView.removeCallbacks(mSearchKeywordRunnable)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refreshLogLevelLayout()
        if (mAdapter == null) {
            return
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun showSearchKeyword() {
        if (mSearchKeyword.isEmpty()) {
            return
        }
        ChooseWindow(this)
            .setList(mSearchKeyword)
            .setListener { position ->
                mInputView.setText(mSearchKeyword[position])
                mInputView.setSelection(mInputView.text.toString().length)
            }
            .show()
    }

    private fun refreshLayout() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // 沉浸式状态栏只有 Android 4.4 才有的
            return
        }
        val window = window
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        mBarView.setPadding(0, 0, 0, 0)
        mRootView.setPadding(0, 0, 0, 0)
        if (LogcatUtils.isPortrait(this)) {
            if (window != null) {
                // 在竖屏的状态下显示状态栏和导航栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 实现状态栏图标和文字颜色为亮色
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (window != null) {
                    val params = window.attributes
                    // 会让屏幕到延伸刘海区域中
                    params.layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    window.attributes = params
                }
            }
            mBarView.setPadding(0, LogcatUtils.getStatusBarHeight(this), 0, 0)
        } else {
            window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            mBarView.setPadding(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    resources.displayMetrics
                ).toInt(), 0,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    resources.displayMetrics
                ).toInt(), 0
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (window != null) {
                    val params = window.attributes
                    // 不会让屏幕到延伸刘海区域中，会留出一片黑色区域
                    params.layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
                    window.attributes = params
                }
            } else {
                if (LogcatUtils.isActivityReverse(this)) {
                    mRootView.setPadding(0, 0, LogcatUtils.getStatusBarHeight(this), 0)
                } else {
                    mRootView.setPadding(LogcatUtils.getStatusBarHeight(this), 0, 0, 0)
                }
            }
        }
    }

    /**
     * 搜索关键字任务
     */
    private val mSearchRunnable = Runnable {
        val keyword = mInputView.text.toString()
        LogcatConfig.setLogcatText(keyword)
        mAdapter.setKeyword(keyword)
        mLinearLayoutManager.scrollToPosition(mAdapter.itemCount - 1)
        if ("" != keyword) {
            mIconView.visibility = View.VISIBLE
            mIconView.setImageResource(R.drawable.logcat_ic_empty)
        } else {
            if (!mSearchKeyword.isEmpty()) {
                mIconView.visibility = View.VISIBLE
                mIconView.setImageResource(R.drawable.logcat_ic_history)
            } else {
                mIconView.visibility = View.GONE
            }
        }
    }

    /**
     * 搜索关键字记录任务
     */
    private val mSearchKeywordRunnable = Runnable {
        val keyword = mInputView.text.toString()
        if ("" == keyword) {
            return@Runnable
        }
        if (mSearchKeyword.contains(keyword)) {
            return@Runnable
        }
        mSearchKeyword.add(0, keyword)
    }

    companion object {
        private val ARRAY_LOG_LEVEL = arrayOf("Verbose", "Debug", "Info", "Warn", "Error")
        private val ARRAY_LOG_LEVEL_PORTRAIT = arrayOf("V", "D", "I", "W", "E")
    }
}