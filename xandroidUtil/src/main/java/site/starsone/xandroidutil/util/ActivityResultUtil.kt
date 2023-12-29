package site.starsone.xandroidutil.util


import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

private val nextLocalRequestCode = AtomicInteger()

private val nextKey: String
    get() = "activity_rq#${nextLocalRequestCode.getAndIncrement()}_j"

/**
 * Activity启动并回调数据(不用再写onActivityResult方法里接收数据)
 */
fun ComponentActivity.startActivityForResult(
    intent: Intent,
    result: (ActivityResult) -> Unit
) {
    var launcher by Delegates.notNull<ActivityResultLauncher<Intent>>()
    launcher = activityResultRegistry.register(
        nextKey,
        ActivityResultContracts.StartActivityForResult()
    ) {
        result.invoke(it)
        launcher.unregister()
    }
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                launcher.unregister()
                lifecycle.removeObserver(this)
            }
        }
    })
    launcher.launch(intent)
}
