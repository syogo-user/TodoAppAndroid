package com.example.todoappandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_TASK = "com.example.todotask.TASK"

class MainActivity : AppCompatActivity() {
    private lateinit var mTaskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }

        // ListViewの設定
        mTaskAdapter = TaskAdapter(this@MainActivity)
        listView1.setOnItemClickListener { parent, view, position, id ->
            // listViewをタップ時
            val task = parent.adapter.getItem(position) as Task
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_TASK, task.id)
            startActivity(intent)
        }

        listView1.setOnItemLongClickListener{ parent, view, postion, id ->
            // listViewを長押し
            // タスク削除
            val task = parent.adapter.getItem(postion) as Task
            val alert = AlertDialog.Builder(this@MainActivity)
            alert.setTitle("削除")
            alert.setMessage(task.title + "を削除しますか？")
            alert.setPositiveButton("OK") { _, _ ->

                reloadListView()
            }
            alert.setNegativeButton("CANCEL", null)
            val dialog = alert.create()
            dialog.show()
            true
        }

        reloadListView()
    }

    private fun reloadListView() {
        // データを取得し、日付順にソート
//         ListViewのアダプターに設定する
        listView1.adapter = mTaskAdapter
        // アダプターにデータの変更を通知する
        mTaskAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
