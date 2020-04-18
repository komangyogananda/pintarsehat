package com.kulguy.pintarsehat.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.kulguy.pintarsehat.R

class WarningDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.dialog_title)
        val image = view.findViewById<ImageView>(R.id.dialog_image)
        val message = view.findViewById<TextView>(R.id.dialog_message)
        val button = view.findViewById<MaterialButton>(R.id.dialog_action_button)

        if (arguments != null){
            title.text = arguments?.getString("title")
            arguments?.getInt("image_source")?.let { image.setImageResource(it) }
            message.text = arguments?.getString("message")
        }

        button.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_warning, container, false);
    }
}