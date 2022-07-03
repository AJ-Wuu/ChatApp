package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerviewChat: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var myDatabaseRef: DatabaseReference

    //create a pair of unique rooms only for the current chat
    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        myDatabaseRef = FirebaseDatabase.getInstance().getReference()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderRoom + receiverRoom

        supportActionBar?.title = name

        recyclerviewChat = findViewById(R.id.recyclerview_chat)
        messageBox = findViewById(R.id.edit_messagebox)
        sendButton = findViewById(R.id.image_send_button)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        recyclerviewChat.layoutManager = LinearLayoutManager(this)
        recyclerviewChat.adapter = messageAdapter

        myDatabaseRef.child("chat").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {  }
            })

        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            myDatabaseRef.child("chat").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    myDatabaseRef.child("chat").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            messageBox.setText("")

        }
    }
}