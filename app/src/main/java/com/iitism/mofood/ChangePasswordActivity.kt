package com.iitism.mofood

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var edtOtp:EditText
    private lateinit var edtPasssword:EditText
    private lateinit var edtconfPassword:EditText
    private lateinit var btnSubmit:Button
    var mobile=""
    var otp=""
    var password=""
    var confpassword=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        mobile=intent.getStringExtra("mobile") ?:""
        edtOtp=findViewById(R.id.edt_txt_otp)
        edtPasssword=findViewById(R.id.edt_txt_new_password)
        edtconfPassword=findViewById(R.id.edt_txt_conf_new_password)
        btnSubmit=findViewById(R.id.btn_submit)
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when(p0!!.id)
        {
            R.id.btn_submit ->{
                otp=edtOtp.text.toString()
                password=edtPasssword.text.toString()
                confpassword=edtconfPassword.text.toString()
                check()
            }
        }

    }
    private fun check()
    {
        if(password == confpassword)
        {
            process()
        }
        else
        {
            Toast.makeText(this@ChangePasswordActivity,"Password do not match",Toast.LENGTH_SHORT).show()
        }
    }

    private fun process()
    {
        val queue= Volley.newRequestQueue(this@ChangePasswordActivity)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
        val jsonParam= JSONObject()
        jsonParam.put("mobile_number",mobile)
        jsonParam.put("password",password)
        jsonParam.put("otp",otp)


        val jsonRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParam, Response.Listener {
            println("response $it")
            val re=it.getJSONObject("data")
            val success = re.getBoolean("success")

            try {
                // progressBar.visibility=View.GONE
                if (success) {
                    val x=re.getString("successMessage")
                    Toast.makeText(this@ChangePasswordActivity, x, Toast.LENGTH_SHORT)
                        .show()
                    val pref=getSharedPreferences("user",Context.MODE_PRIVATE)
                    pref.edit().clear().apply()
                        val intent= Intent(this@ChangePasswordActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()



                } else {
                    //if(activity!=null)
                    Toast.makeText(this@ChangePasswordActivity, "error occurred", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            catch (e: JSONException)
            {
                //if(activity!=null)
                Toast.makeText(this@ChangePasswordActivity, "Unexpected Error Occurred", Toast.LENGTH_SHORT)
                    .show()
            }

        }, Response.ErrorListener {

            Toast.makeText(this@ChangePasswordActivity, "Server Error Occurred", Toast.LENGTH_SHORT)
                .show()
        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "7ff8a62bff3cd6"
                return headers
            }
        }
        queue.add(jsonRequest)
    }

}
