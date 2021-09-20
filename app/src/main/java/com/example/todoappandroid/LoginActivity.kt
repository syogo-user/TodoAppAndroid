package com.example.todoappandroid

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCreateAccountListener: OnCompleteListener<AuthResult>
    private lateinit var mLoginListener: OnCompleteListener<AuthResult>
    private lateinit var mDataBaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // アカウント作成リスナー
        mCreateAccountListener = OnCompleteListener { task ->
            if (task.isSuccessful) {
                // 成功
                val email = emailText.text.toString()
                val password = passwordText.text.toString()
                login(email, password)
            } else {
                // 失敗
                val view = findViewById<View>(android.R.id.content)
                Snackbar.make(view, "アカウント作成に失敗しました", Snackbar.LENGTH_LONG).show()
            }
            // プログレスバーを非表示
            progressBar.visibility = View.GONE
        }

        mLoginListener = OnCompleteListener { task ->
            if (task.isSuccessful) {
                // 成功
//                val user = mAuth.currentUser
//                val userRef = mDataBaseReference.child("users").child(user!!.uid)
//                if (mIsCreateAccount) {
//                    val name = nameText.text.toString()
//                    val data = HashMap<String, String>()
//                    data["name"] = name
//                    userRef.setValue(data)
//                    savaName(name)
//                }
            } else {
                // 失敗
            }
            // プログレスバー非表示
            progressBar.visibility = View.GONE
        }

        createButton.setOnClickListener{ v ->
            // キーボードを閉じる
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val name = nameText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                createAccount(email,password)
            } else {
                Snackbar.make(v, "E-mail,パスワード,名前を入力してください", Snackbar.LENGTH_LONG)
            }
        }

        loginButton.setOnClickListener{ v ->
            // キーボードを閉じる
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email,password)
            } else {
                Snackbar.make(v, "E-mailまたはパスワードを入力してください", Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        // プログレスバーを表示
        progressBar.visibility = View.VISIBLE
        // アカウント作成
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mCreateAccountListener)
    }

    private fun login(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        // ログイン
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mLoginListener)

    }

}

