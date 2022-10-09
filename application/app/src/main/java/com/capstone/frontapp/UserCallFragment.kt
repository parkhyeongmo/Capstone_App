package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class UserCallFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_call, container, false)

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