package com.iitism.mofood

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var txtname:EditText
    lateinit var txtemail:EditText
    lateinit var txtmobile:EditText
    private lateinit var txtaddress:EditText
    lateinit var txtpassword:EditText
    private lateinit var txtconfpassword:EditText
    lateinit var btnsignup:Button
    lateinit var progressBar: ProgressBar

    var name=""
    var email= ""
    var mobile=""
    var address=""
    private var password=""
    private var confpassword=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txtname=findViewById(R.id.sign_edt_txt_name)
        txtemail=findViewById(R.id.sign_edt_txt_email)
        txtaddress=findViewById(R.id.sign_edt_txt_del_address)
        txtmobile=findViewById(R.id.sign_edt_txt_mobile)
        txtpassword=findViewById(R.id.sign_edt_txt_password)
        txtconfpassword=findViewById(R.id.sign_edt_txt_conf_password)
        btnsignup=findViewById(R.id.btn_sign_up)
        progressBar=findViewById(R.id.sign_up_progress_bar)

        btnsignup.setOnClickListener()
        {
            name=txtname.text.toString()
            email=txtemail.text.toString()
            mobile=txtmobile.text.toString()
            address=txtaddress.text.toString()
            password=txtpassword.text.toString()
            confpassword=txtconfpassword.text.toString()

            if(mobile.length<10)
            {
                Toast.makeText(this@SignUpActivity,"Invalid mobile number",Toast.LENGTH_SHORT).show()
            }
            else
            {
                when {
                    password!=confpassword ->{
                        Toast.makeText(
                            this@SignUpActivity,
                            "passwords do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    password.length<4 -> {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Password should be greater than 4 letters",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                   else ->{
                        register()
                    }
                }

            }

        }

    }
    private fun register()
    {
        progressBar.visibility=View.VISIBLE

        val queue=Volley.newRequestQueue(this@SignUpActivity)
        val url = "http://13.235.250.119/v2/register/fetch_result/"
        val jsonParam=JSONObject()
        jsonParam.put("name",name)
        jsonParam.put("mobile_number",mobile)
        jsonParam.put("password",password)
        jsonParam.put("address",address)
        jsonParam.put("email",email)

        val jsonRequest=object :JsonObjectRequest(Request.Method.POST,url,jsonParam,Response.Listener {

            val re=it.getJSONObject("data")
            val success = re.getBoolean("success")
            println("Response $it")
            println("$success")
            try {
                progressBar.visibility=View.GONE
                if (success) {
                    var userid=""

                    val data = re.getJSONObject("data")
                        userid=data.getString("user_id")


                    val sharedPreferences=getSharedPreferences("user",Context.MODE_PRIVATE)
                    var editor=sharedPreferences.edit()
                    editor.putString("name",name)
                    editor.putString("email",email)
                    editor.putString("mobile",mobile)
                    editor.putString("address",address)
                    editor.putString("user_id",userid)
                    editor.apply()
                    editor.commit()

                    val intent= Intent(this@SignUpActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()


                } else {
                    //if(activity!=null)
                        Toast.makeText(this@SignUpActivity, "Error Occurred", Toast.LENGTH_SHORT)
                            .show()
                }
            }
            catch (e: JSONException)
            {
                //if(activity!=null)
                    Toast.makeText(this@SignUpActivity, "Unexpected Error Occurred", Toast.LENGTH_SHORT)
                        .show()
            }

        },Response.ErrorListener {

            Toast.makeText(this@SignUpActivity, "Server Error Occurred", Toast.LENGTH_SHORT)
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
