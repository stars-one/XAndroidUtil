package site.starsone.xandroidutil.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import site.starsone.xandroidutil.R

class FloatingActionMenu @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private var mExpanded = false
    lateinit var mAddButton: FloatingActionButton
    private val mButtons = arrayListOf<View>()

    private val llFabMenuItems = LinearLayout(getContext())

    // 添加悬浮按钮到容器内
    fun addFloatingActionButton(button: View) {
        mButtons.add(button)
        button.visibility = View.GONE
        llFabMenuItems.addView(button)
    }

    // 删除悬浮按钮从容器内
    fun removeFloatingActionButton(button: View?) {
        mButtons.remove(button)
        llFabMenuItems.removeView(button)
    }

    val collapseAnim by lazy {
        val animator = ObjectAnimator.ofFloat(mAddButton, "rotation", 45f, 0f)
        animator.interpolator = AnticipateOvershootInterpolator()
        animator.duration = 200 // 动画时长（单位：毫秒）

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                collapse()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator
    }

    val expandedAnim by lazy {
        val animator = ObjectAnimator.ofFloat(mAddButton, "rotation", 0f, 45f)
        animator.interpolator = AnticipateOvershootInterpolator()
        animator.duration = 200 // 动画时长（单位：毫秒）

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                expand()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator
    }

    // 切换展开/关闭状态
    fun toggle() {
        if (mExpanded) {
            collapseAnim.start()
        } else {
            expandedAnim.start()
        }
    }

    // 展开悬浮菜单
    fun expand() {
        mExpanded = true

        //显示所有子按钮
        mButtons.forEach {
            it.visibility = View.VISIBLE
        }

    }

    // 关闭悬浮菜单
    fun collapse() {
        mExpanded = false
        mButtons.forEach {
            it.visibility = View.GONE
        }

    }

    init {
        this.orientation = VERTICAL
        this.gravity = Gravity.END or Gravity.BOTTOM
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        //设置放置按钮组的布局
        llFabMenuItems.orientation = VERTICAL
        llFabMenuItems.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        llFabMenuItems.gravity = Gravity.END

        // 设置添加按钮的属性
        mAddButton = FloatingActionButton(context!!)
        mAddButton.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.x_fab_add
            )
        )
        addView(mAddButton)

        // 监听添加按钮的点击事件
        mAddButton.setOnClickListener { toggle() }

        //放在前面
        addView(llFabMenuItems, 0)

        //测试
        val view = LayoutInflater.from(getContext())
            .inflate(R.layout.fab_menu_item, null)
        addFloatingActionButton(view)
    }
}