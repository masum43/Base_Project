package com.mispran.outlet_order.common.extensions

import android.app.Activity
import android.content.*
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.*


// Any
inline fun runWithoutException(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        Timber.e("Inside runWithoutException")
        Timber.e(e)
    }
}

/*
* [Activity] Extension Function
*/

fun Activity.requireActivity() = this

fun AppCompatActivity.toast(text: String?) {
    text?.let { Toast.makeText(this,text, Toast.LENGTH_SHORT).show() }
}

fun AppCompatActivity.setActionBarTitle(title: String) {
    this.supportActionBar?.title = title
}

fun AppCompatActivity.hideKeyboard() {
    val view: View? = this.currentFocus
    view?.let {
        val imm: InputMethodManager? = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
/*
* [Context] Extension Function
*/
inline fun <reified T> Context.launchActivity(bundle: Bundle? = null) {
    startActivity(Intent(this,T::class.java).apply { bundle?.let { putExtras(it) } })
}

inline fun <reified T> Context.clearStackAndStartActivity() {
    startActivity(Intent(this,T::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    })
}

fun Context.sendEmail(emails: Array<String>, subject: String?, body: String?) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, emails)
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, subject) }
        body?.let { putExtra(Intent.EXTRA_TEXT, body) }
    }
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    }
}

fun Context.copyToClipboard(label: String = "", text: String) {
    val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}
/*
* [Coroutines] Extension Function
*/

suspend fun <T,R> T.IO(block: suspend T.() -> R) = withContext(Dispatchers.IO) {
    block()
}

suspend fun <T,R> T.default(block: suspend T.() -> R) = withContext(Dispatchers.Default) {
    block()
}
/*
* [EditText] Extenstions
*/

val EditText.string: String
    get() = this.text.toString()

val EditText.trimmedString: String
    get() = this.text.toString().trim()




/*
* Exception
* */
val Exception.errorMessage: String
    get() {
        return when(this) {
            is ConnectException -> "Unable to connect to the server. Please check your connection"
            is SocketTimeoutException -> "Connection timeout please try again"
            is IOException -> this.message ?: "Server error please try again"
            else -> "Error occurred please try again"
        }
    }

/*
* [Fragment] Extension Function
*/
fun Fragment.toast(text: String?) {
    this.context?.let { context ->
        text?.let { Toast.makeText(context,text,Toast.LENGTH_SHORT).show() }
    }
}

val Fragment.viewLifecycleScope
    get() = viewLifecycleOwner.lifecycleScope

val Fragment.viewLifecycle
    get() = viewLifecycleOwner.lifecycle

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Fragment.navigate(@IdRes id: Int, extras: Bundle? = null) {
    findNavController().navigate(id, extras)
}

fun Fragment.updateStatusbarColor(color: Int) {
    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    requireActivity().window.statusBarColor = color
}

fun Fragment.getColor(@ColorRes color: Int) = ContextCompat.getColor(requireContext(), color)

val Fragment.contentResolver
    get() = requireContext().contentResolver

fun Fragment.requireApplication() = requireContext().applicationContext!!

inline fun <reified T> Fragment.launchActivity(bundle: Bundle? = null) {
    startActivity(Intent(requireContext(), T::class.java).apply { bundle?.let { putExtras(it) } })
}
/*
* [Flow] Extension Function
*/

val <T> Flow<T>.lastValue: T?
    get() = (this.asLiveData().value)

// Moshi
inline fun <reified T> List<T>.fromListToJSON(): String {
    val type = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter: JsonAdapter<List<T>> = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build().adapter(type)
    return adapter.toJson(this)
}

inline fun <reified T> String.fromJSONToList(): List<T> {
    val type = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter: JsonAdapter<List<T>> = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build().adapter(type)
    return adapter.fromJson(this) ?: listOf()
}

inline fun <reified T> String.fromJSONToPOJO(): T {
    val adapter = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build().adapter(T::class.java)
    return adapter.fromJson(this)!!
}

inline fun <reified T> T.fromPOJOToJSON(): String {
    val adapter = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build().adapter(T::class.java)
    return adapter.toJson(this)
}
/*
* OkHttp
* */
suspend fun Call.executeSuspended(): Response = withContext(Dispatchers.IO) { execute() }

/*
* View
* */

fun View.visible() { this.visibility = View.VISIBLE }

fun View.invisible() { this.visibility = View.INVISIBLE }

fun View.gone() { this.visibility = View.GONE }

fun View.enable() { this.isEnabled = true }

fun View.disable() { this.isEnabled = false }

fun View.disable(lifecycleScope: LifecycleCoroutineScope, timeInMillis: Long) {
    lifecycleScope.launch {
        runWithoutException {
            this@disable.isEnabled = false
            delay(timeInMillis)
            this@disable.isEnabled = true
        }
    }
}


/**
 * Converts DP into pixel
 */
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts pixel into dp
 */
val Int.px: Float
    get() = (this / Resources.getSystem().displayMetrics.density)


fun View.setMargins(l: Int, t: Int, r: Int, b: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        this.requestLayout()
    }
}

fun Context.changeActivity(target: Class<*>, bundle: Bundle) =
    startActivity(Intent(this, target).putExtras(bundle))

fun Context.changeActivity(target: Class<*>) =
    startActivity(Intent(this, target))

fun Fragment.changeActivity(target: Class<*>) {
    startActivity(Intent(context, target))
}

fun Fragment.changeActivity(target: Class<*>, bundle: Bundle) =
    startActivity(Intent(context, target).putExtras(bundle))

fun AppCompatActivity.showErrorLog(message: String) {
    Timber.e(message)
}

fun Fragment.showErrorLog(message: String) {
    Timber.e(message)
}

