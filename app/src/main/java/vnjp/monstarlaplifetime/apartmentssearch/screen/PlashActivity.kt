package vnjp.monstarlaplifetime.apartmentssearch.screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.victor.loading.rotate.RotateLoading
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.screen.authentication.LoginActivity
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListActivity

class PlashActivity : AppCompatActivity() {
    private lateinit var rotateLoading: RotateLoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plash)
        rotateLoading = findViewById(R.id.rotateloading)
        rotateLoading.start()
        Handler().postDelayed({
            var intent: Intent? = null
            val user = FirebaseAuth.getInstance().currentUser
            intent = if (user != null) {
                Intent(this, ItemsListActivity::class.java)
//                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)
        rotateLoading.stop()
    }
}
