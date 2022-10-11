package com.capstone.frontapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.findNavController

class UserCallFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_call, container, false)

        view.findViewById<Button>(R.id.btn_call_req).setOnClickListener {
            val tel_num = view.findViewById<TextView>(R.id.tel_num).text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${tel_num}")
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btn_inspect).setOnClickListener {
            it.findNavController().navigate(R.id.action_userCallFragment_to_inspectFragment)
        }

        view.findViewById<Button>(R.id.btn_list).setOnClickListener {
            it.findNavController().navigate(R.id.action_userCallFragment_to_userResultListFragment)
        }

        return view
    }

}