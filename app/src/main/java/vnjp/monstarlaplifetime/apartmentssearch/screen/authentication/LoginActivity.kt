package vnjp.monstarlaplifetime.apartmentssearch.screen.authentication


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.layout_login.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttonLogin: Button
    private lateinit var textForgotPassword: TextView
    private lateinit var buttonInitRegister: Button
    private lateinit var authenticationViewModel: AuthenticationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        initView()
    }

    private fun initView() {
        buttonLogin = findViewById(R.id.buttonLogin)
        textForgotPassword = findViewById(R.id.textForgotPassword)
        buttonInitRegister = findViewById(R.id.buttonInitRegister)
        authenticationViewModel =
            ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
    }

    @SuppressLint("ShowToast")
    private fun login() {
        val email: String = editTextEmail.text.toString()
        val pass: String = editTextPass.text.toString()
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu !", Toast.LENGTH_LONG).show()
        } else {
            authenticationViewModel.login(email, pass)
            authenticationViewModel.isSuccess.observe(this, Observer {
                if (it) {
                    startActivity(Intent(this, ItemsListActivity::class.java))
                    finish()
                }
            })
            authenticationViewModel.exception.observe(this, Observer {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogin -> login()

            R.id.textForgotPassword -> Toast.makeText(
                this,
                "This function is not implement",
                Toast.LENGTH_LONG
            ).show()

            R.id.buttonInitRegister -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
