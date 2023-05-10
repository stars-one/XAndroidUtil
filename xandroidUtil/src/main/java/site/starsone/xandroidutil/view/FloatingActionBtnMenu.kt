package site.starsone.xandroidutil.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ConvertUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import site.starsone.xandroidutil.R

/**
 * 悬浮按钮组
 */
class FloatingActionBtnMenu @JvmOverloads constructor(
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
    fun removeFloatingActionButton(button: View) {
        mButtons.remove(button)
        llFabMenuItems.removeView(button)
    }

    /**
     * 主按钮的收缩动画
     */
    private val collapseAnim by lazy {
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

    /**
     * 主按钮的展开动画
     */
    private val expandedAnim by lazy {
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
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        //设置放置按钮组的布局
        llFabMenuItems.orientation = VERTICAL
        llFabMenuItems.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, ConvertUtils.dp2px(16f), ConvertUtils.dp2px(16f))
        }

        llFabMenuItems.gravity = Gravity.END

        // 设置添加按钮的属性
        mAddButton = FloatingActionButton(context!!)
        mAddButton.compatElevation = ConvertUtils.dp2px(16f).toFloat()
        mAddButton.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.x_fab_add
            )
        )
        // 创建 LayoutParams 对象
        val layoutParams =
            LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        // 设置 Margin
        layoutParams.setMargins(0, 0, ConvertUtils.dp2px(16f), ConvertUtils.dp2px(16f))
        mAddButton.layoutParams = layoutParams
        addView(mAddButton)

        // 监听添加按钮的点击事件
        mAddButton.setOnClickListener { toggle() }

        //按钮组放在前面
        addView(llFabMenuItems, 0)


        //读取xml中的属性
        val typearr = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionBtnMenu)

        val iconBgColor = typearr.getColor(
            R.styleable.FloatingActionBtnMenu_iconBgColor,
            ColorUtils.getColor(R.color.purple_700)
        )

        val iconColor = typearr.getColor(
            R.styleable.FloatingActionBtnMenu_iconColor,
            ColorUtils.getColor(R.color.white)
        )
        typearr.recycle()

        setMainBtnStyle(iconColor, iconBgColor)
    }

    /**
     * 设置主按钮的样式
     * @param iconColor 图标色
     * @param iconBgColor 图标背景色
     */
    fun setMainBtnStyle(
        @ColorInt iconColor: Int = ColorUtils.getColor(R.color.white),
        @ColorInt iconBgColor: Int = ColorUtils.getColor(R.color.purple_700)
    ) {
        mAddButton.backgroundTintList = ColorStateList.valueOf(iconBgColor)
        mAddButton.imageTintList = ColorStateList.valueOf(iconColor)
    }

    /**
     * 根据menu生成items
     */
    fun buildItemsByMenuData(
        @MenuRes menuRes: Int,
        styleList: List<MenuItemStyle>? = null,
        lambda: (Int) -> Unit
    ) {
        val popupMenu = PopupMenu(context, View(context))
        popupMenu.inflate(menuRes)
        val menu = popupMenu.menu

        if (styleList != null) {
            if (menu.size() != styleList.size) {
                throw Exception("buildItemsByMenuData方法传递的styleList长度和menu资源文件里的item数目不一致!")
            }
        }

        menu.children.forEachIndexed { index, it ->
            val iconDrawble = it.icon
            val title = it.title
            val itemId = it.itemId

            val menuItemStyle = if (styleList == null) {
                null
            } else {
                styleList[index]
            }
            createItem(title, iconDrawble, menuItemStyle) {
                lambda.invoke(itemId)
            }
        }
    }

    /**
     * 从list中获取数据
     */
    fun buildItemsByListData(listData: List<MenuItemData>, lambda: (Int) -> Unit) {
        listData.forEachIndexed { index, menuItemData ->
            val drawableId = menuItemData.drawableId
            val title = menuItemData.title
            val iconDrawable = ContextCompat.getDrawable(context, drawableId)!!
            val menuItemStyle = menuItemData.menuItemStyle

            createItem(title, iconDrawable, menuItemStyle) {
                lambda.invoke(index)
            }
        }
    }

    private fun createItem(
        title: CharSequence,
        iconDrawable: Drawable,
        menuItemStyle: MenuItemStyle?,
        onclickAction: () -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.fab_menu_item, null)
        view.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val tvDesc = view.findViewById<TextView>(R.id.tvDesc)
        val cardView = view.findViewById<CardView>(R.id.cardView)
        val fabMenuItem = view.findViewById<FloatingActionButton>(R.id.fabMenuItem)

        tvDesc.text = title
        //若不传文字,就隐藏文本
        if (title.isBlank()) {
            tvDesc.visibility = View.GONE
        } else {
            tvDesc.visibility = View.VISIBLE
        }

        fabMenuItem.setImageDrawable(iconDrawable)

        val func = {
            onclickAction.invoke()
            //同时切换状态
            toggle()
        }
        view.setOnClickListener {
            func.invoke()
        }
        fabMenuItem.setOnClickListener {
            func.invoke()
        }

        //颜色设置
        menuItemStyle?.let {
            cardView.setCardBackgroundColor(it.textBgColor)
            tvDesc.setTextColor(it.textColor)

            fabMenuItem.backgroundTintList = ColorStateList.valueOf(it.iconBgColor)
            fabMenuItem.imageTintList = ColorStateList.valueOf(it.iconColor)
        }
        addFloatingActionButton(view)
    }

    /**
     * 菜单数据
     * @param title 左边描述文本
     * @param drawableId 图标的资源id(R.drawable.xx)
     */
    data class MenuItemData(
        val title: String,
        @DrawableRes val drawableId: Int,
        val menuItemStyle: MenuItemStyle? = null
    )

    /**
     * @param textColor
     * @param textBgColor
     * @param iconColor
     * @param iconBgColor
     */
    data class MenuItemStyle(
        @ColorInt val textColor: Int = ColorUtils.getColor(R.color.black),
        @ColorInt val textBgColor: Int = ColorUtils.getColor(R.color.white),
        @ColorInt val iconColor: Int = ColorUtils.getColor(R.color.white),
        @ColorInt val iconBgColor: Int = ColorUtils.getColor(R.color.purple_700)
    )
}