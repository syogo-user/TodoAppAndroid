package com.example.todoappandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_TASK = "com.example.todotask.TASK"
const val EXTRA_TASK_ID = "com.example.todotask.TASKID"

class MainActivity : AppCompatActivity() {
    private lateinit var mTaskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_TASK_ID, mTaskAdapter.getMaxId())
            startActivity(intent)
        }

        // ListViewの設定
        mTaskAdapter = TaskAdapter( this@MainActivity)
        listView1.setOnItemClickListener { parent, view, position, id ->
            // listViewをタップ時
            val task = parent.adapter.getItem(position) as Task
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_TASK, task)
            startActivity(intent)
        }

        listView1.setOnItemLongClickListener { parent, view, postion, id ->
            // listViewを長押し
            // タスク削除
            val task = parent.adapter.getItem(postion) as Task
            val alert = AlertDialog.Builder(this@MainActivity)
            alert.setTitle("削除")
            alert.setMessage(task.title + "を削除しますか？")
            alert.setPositiveButton("OK") { _, _ ->
                deleteTask(mTaskAdapter.taskList[postion].id)
                reloadListView()
            }
            alert.setNegativeButton("CANCEL", null)
            val dialog = alert.create()
            dialog.show()
            true
        }


    }

    override fun onStart() {
        super.onStart()
        // ログイン済みか確認
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        } else {
            reloadListView()
        }
    }

    private fun reloadListView() {
        // データを取得し、日付順にソート
        val db = FirebaseFirestore.getInstance()
        val tasks = db.collection("tasks")
        tasks.get().addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful()) {
                val taskList = it.result.toObjects(Task::class.java)
                mTaskAdapter.taskList = taskList
                // ListViewのアダプターに設定する
                listView1.adapter = mTaskAdapter
                // アダプターにデータの変更を通知する
                mTaskAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun deleteTask(id: Int) {
        // タスクの削除
        val db = FirebaseFirestore.getInstance()
        // TODO
        val ref = db.collection("tasks").document("uid" + id.toString())
        ref.delete()
            .addOnSuccessListener {
                // TODO
            }
            .addOnFailureListener{
                // TODO
            }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
