package vnjp.monstarlaplifetime.apartmentssearch.screen.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_login.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl

class LoginActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        userRepository = UserRepositoryImpl(databaseReference, firebaseAuth)
    }

    fun eventOnClickHandler(view: View) {
        when (view) {
            buttonLogin -> login()
            textForgotPassword -> forgotPassword()
            buttonInitRegister -> initRegister()
        }
    }

    private fun initRegister() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun forgotPassword() {
        Toast.makeText(this, "This function is not implement", Toast.LENGTH_LONG).show()
    }

    private fun login() {
        val email: String = editTextEmail.text.toString()
        val pass: String = editTextPass.text.toString()
        if (email.isEmpty() || pass.isEmpty()) return
        userRepository.login(email, pass,
            onResponseLogin = { isComplete, message ->
                Log.d("firebase", message)
//                if (isComplete) firebaseAuth.signOut()
            })


    }
}
