package vnjp.monstarlaplifetime.apartmentssearch.screen.authentication


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.layout_register.*
import vnjp.monstarlaplifetime.apartmentssearch.R


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnRegister: Button
    private lateinit var buttonInitLogin: Button
    private lateinit var authenticationViewModel: AuthenticationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)
        initView()
        initEvent()
    }

    private fun initView() {
        authenticationViewModel =
            ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        btnRegister = findViewById(R.id.buttonRegister)
        buttonInitLogin = findViewById(R.id.buttonInitLogin)


    }

    private fun initEvent() {
        btnRegister.setOnClickListener(this)
        buttonInitLogin.setOnClickListener(this)
    }

    @SuppressLint("ShowToast")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonRegister -> {
                val email = editTextEmail.text.toString()
                val pass = editTextPass.text.toString()
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please input your email and password!!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    register(email, pass)
                }
            }
            R.id.buttonInitLogin ->
                startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register(email: String, password: String) {
        authenticationViewModel.register(email, password)
        authenticationViewModel.isSuccess.observe(this, Observer {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
        authenticationViewModel.exception.observe(this, Observer {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_LONG
            ).show()
        })
    }

}
