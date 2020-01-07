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
import kotlinx.android.synthetic.main.layout_register.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var databaseReference: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userRepository: UserRepository
    private lateinit var btnRegister: Button
    private lateinit var buttonInitLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)
        initView()
        initEvent()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()
        userRepository = UserRepositoryImpl(databaseReference, firebaseAuth)

    }


    private fun initView() {
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
                if (email.isEmpty() || pass.isEmpty()) return
                //need mvvm pattern
                userRepository.register(email, pass, onResponseRegister = { isComplete, message ->
                    Log.d("firebase", message)
                    if (isComplete) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Đăng kí không thành công vui lòng thử lại!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
            R.id.buttonInitLogin ->
                startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}
