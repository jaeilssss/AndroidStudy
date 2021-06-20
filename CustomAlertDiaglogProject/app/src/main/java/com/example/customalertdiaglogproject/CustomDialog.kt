package com.example.customalertdiaglogproject

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import org.w3c.dom.Text

class CustomDialog(private val context: Context) {

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context).setView(view)
    }


    private val view: View by lazy {
        View.inflate(context, R.layout.custom_alert_dialog, null)
    }

    private var dialog: AlertDialog? = null

    // 터치 리스너 구현
    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            Handler().postDelayed({
                dismiss()
            }, 5)
        }
        false
    }

    fun setTitle(@StringRes titleId: Int): CustomDialog {

        view.findViewById<Button>(R.id.titleTextView).text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence): CustomDialog {
        view.findViewById<TextView>(R.id.titleTextView).text = title
        return this
    }

    fun setMessage(@StringRes messageId: Int): CustomDialog {
        view.findViewById<TextView>(R.id.messageTextView).text = context.getText(messageId)
        return this
    }

    fun setMessage(message: CharSequence): CustomDialog {
        view.findViewById<TextView>(R.id.messageTextView).text = message
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomDialog {

        view.findViewById<Button>(R.id.positiveButton).apply {
            text = context.getText(textId)
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomDialog {
        view.findViewById<Button>(R.id.positiveButton).apply {
            this.text = text
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomDialog {
        view.findViewById<Button>(R.id.negativeButton).apply {
            text = context.getText(textId)
            this.text = text
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomDialog {
        view.findViewById<Button>(R.id.negativeButton).apply {
            this.text = text
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun create() {
        dialog = builder.create()
    }

    fun show() {
        dialog = builder.create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}