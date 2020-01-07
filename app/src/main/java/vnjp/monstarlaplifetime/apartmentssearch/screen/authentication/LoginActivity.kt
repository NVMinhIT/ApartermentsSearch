package vnjp.monstarlaplifetime.apartmentssearch.screen.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_login.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var userRepository: UserRepository
    private lateinit var buttonLogin: Button
    private lateinit var textForgotPassword: Button
    private lateinit var buttonInitRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        initView()
        initEvent()
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        userRepository = UserRepositoryImpl(databaseReference, firebaseAuth)
    }

    private fun initEvent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initView() {
        buttonLogin = findViewById(R.id.buttonLogin)
        textForgotPassword = findViewById(R.id.textForgotPassword)
        buttonInitRegister = findViewById(R.id.buttonInitRegister)

    }

    @SuppressLint("ShowToast")
    private fun login() {
        val email: String = editTextEmail.text.toString()
        val pass: String = editTextPass.text.toString()
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu !", Toast.LENGTH_LONG).show()
        } else {
            userRepository.login(email, pass,
                onResponseLogin = { isComplete, message ->
                    Log.d("firebase", message)
                    if (isComplete) {
                        startActivity(Intent(this, ItemsListActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show()
                    }

                })

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogin -> {
                login()
            }
            R.id.textForgotPassword -> {
                Toast.makeText(this, "This function is not implement", Toast.LENGTH_LONG).show()
            }
            R.id.buttonInitRegister -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
