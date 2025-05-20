package com.example.fourlink

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

class SurrenderDialogFragment : DialogFragment() {

    private var currentPlayer: String? = null
    private var onSurrenderConfirmed: ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPlayer = arguments?.getString(ARG_CURRENT_PLAYER)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        val inflater = LayoutInflater.from(activity)
        val view: View = inflater.inflate(R.layout.dialog_surrender, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val surrenderMessage = view.findViewById<TextView>(R.id.tvSurrenderMessage)
        val yesButton = view.findViewById<Button>(R.id.btnYes)
        val noButton = view.findViewById<Button>(R.id.btnNo)

        surrenderMessage.text = "SURRENDER?"

        yesButton.setOnClickListener {
            val opponent = if (currentPlayer == "YELLOW") "RED" else "YELLOW"
            onSurrenderConfirmed?.invoke(opponent)
            dismiss()
        }

        noButton.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (300 * resources.displayMetrics.density).toInt(),
            (160 * resources.displayMetrics.density).toInt()
        )
    }

    fun setOnSurrenderConfirmed(callback: (String) -> Unit) {
        onSurrenderConfirmed = callback
    }

    companion object {
        private const val ARG_CURRENT_PLAYER = "arg_current_player"

        fun newInstance(currentPlayer: String): SurrenderDialogFragment {
            val fragment = SurrenderDialogFragment()
            val args = Bundle()
            args.putString(ARG_CURRENT_PLAYER, currentPlayer)
            fragment.arguments = args
            return fragment
        }
    }
}
