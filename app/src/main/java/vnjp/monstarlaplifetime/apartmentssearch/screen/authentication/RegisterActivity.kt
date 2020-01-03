package vnjp.monstarlaplifetime.apartmentssearch.screen.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_register.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl

class RegisterActivity : AppCompatActivity() {

    lateinit var databaseReference: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()
        userRepository = UserRepositoryImpl(databaseReference, firebaseAuth)

    }

    fun handleEventOnClick(view: View) {
        when (view) {
            buttonRegister -> register()
            buttonInitLogin -> startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {
        val email = editTextEmail.text.toString()
        val pass = editTextPass.text.toString()
        if (email.isEmpty() || pass.isEmpty()) return

        //need mvvm pattern
        userRepository.register(email, pass, onResponseRegister = { isComplete, message ->

            Log.d("firebase", message)
            if (isComplete) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })
    }

}
