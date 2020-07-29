package com.iitism.mofood.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.iitism.mofood.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private lateinit var txtname:TextView
    private lateinit var txtmobile:TextView
    private lateinit var txtaddress:TextView
    private lateinit var txtemail:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        txtname =view.findViewById(R.id.profile_name)
        txtaddress=view.findViewById(R.id.profile_address)
        txtemail=view.findViewById(R.id.profile_email)
        txtmobile=view.findViewById(R.id.profile_mobile)

        val pref= activity?.getSharedPreferences("user",Context.MODE_PRIVATE)
        if (pref != null) {
            txtname.text=pref.getString("name","")
            txtmobile.text=pref.getString("mobile","")
            txtaddress.text=pref.getString("address","")
            txtemail.text=pref.getString("email","")
        }

        return view
    }

}
