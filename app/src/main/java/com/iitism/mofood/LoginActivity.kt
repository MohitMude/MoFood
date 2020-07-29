package com.iitism.mofood

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var txtmobile:EditText
    private lateinit var txtpassword:EditText
    private lateinit var textsignup:TextView
    private lateinit var txtforgot:TextView
    private lateinit var btnLogin:Button
    var mobile:String=""
    var password:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtmobile=findViewById(R.id.edt_txt_mobile)
        txtpassword=findViewById(R.id.edt_txt_password)
        txtforgot=findViewById(R.id.txt_forgot_pass)
        textsignup=findViewById(R.id.txt_sign_up)
        btnLogin=findViewById(R.id.btn_login)

        btnLogin.setOnClickListener(this)
        textsignup.setOnClickListener(this@LoginActivity)
        txtforgot.setOnClickListener(this@LoginActivity)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btn_login -> {
                mobile=txtmobile.text.toString()
                password=txtpassword.text.toString()

                if(mobile.length < 10)
                {
                    Toast.makeText(this@LoginActivity,"Invalid mobile number",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    login()
                }
            }
            R.id.txt_sign_up ->{
                val intent= Intent(this@LoginActivity,SignUpActivity::class.java)
                startActivity(intent)

            }
            R.id.txt_forgot_pass ->{
                val intent=Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
                startActivity(intent)

            }


        }

    }

    private fun login()
    {
       // progressBar.visibility=View.VISIBLE

        val queue= Volley.newRequestQueue(this@LoginActivity)
        val url = "http://13.235.250.119/v2/login/fetch_result/"
        val jsonParam= JSONObject()
        jsonParam.put("mobile_number",mobile)
        jsonParam.put("password",password)


        val jsonRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParam, Response.Listener {

            val re=it.getJSONObject("data")
            val success = re.getBoolean("success")
            println("response $it")
            println("response $re")
            println("response $success")
            try {
               // progressBar.visibility=View.GONE
                if (success) {
                    println("response received")
                    var userid=""
                    var name=""
                    var address=""
                    var email=""
                    val data = re.getJSONObject("data")

                        userid=data.getString("user_id")
                        name=data.getString("name")
                        email=data.getString("email")
                        address=data.getString("address")


                    println("response received")
                    val sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE)
                    var editor=sharedPreferences.edit()
                    editor.putString("name",name)
                    editor.putString("email",email)
                    editor.putString("mobile",mobile)
                    editor.putString("address",address)
                    editor.putString("user_id",userid)
                    editor.apply()
                    editor.commit()
                    println("response received")
                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()


                }
                else {
                    //if(activity!=null)
                    Toast.makeText(this@LoginActivity, "Wrong credentials", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            catch (e: JSONException)
            {
                //if(activity!=null)
                Toast.makeText(this@LoginActivity, "Unexpected Error Occurred "+e.message, Toast.LENGTH_SHORT)
                    .show()
            }

        }, Response.ErrorListener {

            Toast.makeText(this@LoginActivity, "Server Error Occurred", Toast.LENGTH_SHORT)
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
