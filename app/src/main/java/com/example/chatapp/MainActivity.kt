package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerviewUser: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var myAuth: FirebaseAuth
    private lateinit var myDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myAuth = FirebaseAuth.getInstance()
        myDatabaseRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        recyclerviewUser = findViewById(R.id.recyclerview_user)
        recyclerviewUser.layoutManager = LinearLayoutManager(this)
        recyclerviewUser.adapter = adapter

        myDatabaseRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //called everytime when loaded, so we need to clear the previous results
                userList.clear()

                for (postSnapshot in snapshot.children) {
                    var currentUser = postSnapshot.getValue(User::class.java)

                    if (myAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {  }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            myAuth.signOut()
            val intent = Intent(this@MainActivity, LogIn::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}