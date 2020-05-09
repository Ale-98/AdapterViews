package it.uninsubria.pdm.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Inizializzo autenticatore e assegno listener al bottone per fare signup
        auth = Firebase.auth
        registerButton.setOnClickListener({
            onSignUpClick()
        })
    }

    // Controllo che siano stati riempiti tutti i campi e chiamo createUser(..)
    private fun onSignUpClick() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val userName = nameEditText.text.toString().trim()
        if (userName.isEmpty()) {
            nameEditText.error = "Enter userName"
            return
        }
        if (email.isEmpty()) {
            emailEditText.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            passwordEditText.error = "Enter password"
            return
        }
        createUser(userName, email, password)
    }

    // Crea utente e definisce un listener per cogliere la chiamata di ritorno dal server per conoscere l'esito della registrazione
    private fun createUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Se l'operazione è andata a buon fine...
                if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val currenyUser = auth.currentUser
                    val uid = currenyUser!!.uid
                    val userMap = HashMap<String, String>()
                    // Uso hashMap per creare coppia chiave valore(JSON) per memorizzazione informazioni utente in firebase
                    userMap["name"] = userName
                    val database = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                    database.setValue(userMap).addOnCompleteListener { task ->
                        // Se l'operazione va a buon fine viene avviata la main activity e terminata l'activity corrente
                        if (task.isSuccessful) {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
// If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
