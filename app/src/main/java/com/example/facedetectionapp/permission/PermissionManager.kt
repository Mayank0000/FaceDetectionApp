package com.example.facedetectionapp.permission

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.facedetectionapp.R
import java.lang.ref.WeakReference


class PermissionManager private constructor(
    private val lifecycleOwner: WeakReference<LifecycleOwner>,
    private val permission: Permission
) : DefaultLifecycleObserver {

    private lateinit var permissionCheck: ActivityResultLauncher<String>
    private var onPermissionResult: IPermissionResult? = null
    private var activity: ComponentActivity? = null
    private var permissionDeniedMessage: String? = null
    private var permissionPermanentlyDeniedMessage: String? = null

    init {
        lifecycleOwner.get()?.lifecycle?.addObserver(this)
    }

    companion object {
        fun from(lifecycleOwner: LifecycleOwner, permission: Permission) =
            PermissionManager(WeakReference(lifecycleOwner), permission)
    }

    override fun onCreate(owner: LifecycleOwner) {
        permissionCheck = if (owner is ComponentActivity) {
            owner.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                checkPermissionAndCallBack(it)
            }
        } else {
            (owner as Fragment).registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                checkPermissionAndCallBack(it)
            }
        }
        activity = lifecycleOwner.get() as? ComponentActivity
        super.onCreate(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        activity = null
        onPermissionResult = null
    }

    fun permissionDeniedMessage(
        permissionDeniedMessage: String
    ): PermissionManager {
        this.permissionDeniedMessage = permissionDeniedMessage
        return this
    }

    fun permissionPermanentlyDeniedMessage(
        permissionDeniedMessage: String
    ): PermissionManager {
        this.permissionPermanentlyDeniedMessage = permissionDeniedMessage
        return this
    }

    fun observe(onPermissionResult: IPermissionResult): PermissionManager {
        this.onPermissionResult = onPermissionResult
        return this
    }

    fun request() {
        requestPermission()
    }

    private fun showAlertDialog(activity: ComponentActivity, permanentlyDenied: Boolean) {
        val posButton = if (permanentlyDenied) {
            activity.getString(R.string.go_to_settings)
        } else {
            activity.getString(R.string.ask_again)
        }
        val message = if (permanentlyDenied) {
            permissionPermanentlyDeniedMessage
        } else {
            permissionDeniedMessage
        } ?: activity.getString(
            R.string.permission_description_permanently
        )
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.permission_title))
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(activity.getString(R.string.no_thanks)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(posButton) { _, _ ->
                if (permanentlyDenied) {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + activity.packageName)
                    )
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                } else {
                    requestPermission()
                }
            }.show()
    }

    private fun requestPermission() {
        permissionCheck.launch(
            permission.permission
        )
    }

    private fun checkPermissionAndCallBack(hadPermission: Boolean) {
        if (hadPermission) {
            onPermissionResult?.permissionGranted(permission = permission)
        } else {
            activity?.let { act ->
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        act,
                        permission.permission
                    )
                ) {
                    showAlertDialog(act, false)
                } else {
                    showAlertDialog(act, true)
                }
            }
        }
    }

    interface IPermissionResult {
        fun permissionGranted(permission: Permission)
    }
}
