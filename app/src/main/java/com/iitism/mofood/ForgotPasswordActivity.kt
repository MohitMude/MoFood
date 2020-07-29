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

class ForgotPasswordActivity : AppCompatActivity() ,View.OnClickListener{
    private lateinit var edtemai:EditText
    private lateinit var edtmobile:EditText
    private lateinit var btnSend:Button
    private var mobile:String =""
    private var email:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtemai=findViewById(R.id.edt_txt_forgot_email)
        edtmobile=findViewById(R.id.edt_txt_forgot_mobile)
        btnSend=findViewById(R.id.btn_send_otp)
        btnSend.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when(p0!!.id)
        {
            R.id.btn_send_otp ->{
                mobile=edtmobile.text.toString()
                email=edtemai.text.toString()
                send()
            }
        }
    }
    private fun send()
    {
        val queue= Volley.newRequestQueue(this@ForgotPasswordActivity)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
        val jsonParam= JSONObject()
        jsonParam.put("mobile_number",mobile)
        jsonParam.put("email",email)


        val jsonRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParam, Response.Listener {

            val re=it.getJSONObject("data")
            val success = re.getBoolean("success")
            println("response $it")
            try {
                // progressBar.visibility=View.GONE
                if (success) {
                      val x=re.getBoolean("first_try")
                    if(x)
                    {
                        val intent= Intent(this@ForgotPasswordActivity,ChangePasswordActivity::class.java)
                        intent.putExtra("mobile",mobile)
                        startActivity(intent)
                    }
                    else
                    {
                        val intent= Intent(this@ForgotPasswordActivity,ChangePasswordActivity::class.java)
                        intent.putExtra("mobile",mobile)
                        startActivity(intent)
                    }


                    finish()


                } else {
                    //if(activity!=null)
                    Toast.makeText(this@ForgotPasswordActivity, "error occurred", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            catch (e: JSONException)
            {
                //if(activity!=null)
                Toast.makeText(this@ForgotPasswordActivity, "Unexpected Error Occurred", Toast.LENGTH_SHORT)
                    .show()
            }

        }, Response.ErrorListener {

            Toast.makeText(this@ForgotPasswordActivity, "Server Error Occurred", Toast.LENGTH_SHORT)
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
