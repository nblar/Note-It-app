package com.example.android.app_project

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.ClipboardManager
import android.util.EventLogTags
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.view.*
import java.util.*
import javax.security.auth.DestroyFailedException
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var listNotes  = ArrayList<note> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadQuery("dsv")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }
    private fun LoadQuery(title: String) {
        var dbManager = DbManager(this)
        var projections = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title);
        var cursor= dbManager.Query(projections, "Title like ?", selectionArgs,"Title")
        listNotes.clear();
        if(cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"));
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(note(ID,Title,Description))


            }while (cursor.moveToNext())
        }
        var myNotesAdapter= MyNotesAdaper(this,listNotes)
        notes.adapter= myNotesAdapter
        //////geting total number of tasks //////
        val total=notes.count
        //actionbar//
        val mactionbar=supportActionBar
        if(mactionbar!=null){
            // setting action bar as subtitle of actionbar
            mactionbar.subtitle="You have $total note(s) in the list.."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        ///////search view 59 min///////
        return super.onCreateOptionsMenu(menu);
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item !=null){
            when(item.itemId){
                R.id.butadd ->{ //might cause error
                    startActivity(Intent(this, Addnotes_Activity::class.java)) // starting a new activity whenever a button is pressed
                }
                R.id.action_set->{
                    Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class MyNotesAdaper : BaseAdapter {
        var listNotesAdapter= ArrayList<note>(); //43 21 sec
        var context:Context?=null

        constructor(context: Context,listNotesArray: ArrayList<note>) : super() {
            this.listNotesAdapter=listNotesAdapter
            this.context= context;
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.row,null)
            val myNote= listNotesAdapter[position]
            myView.txtTv.text=myNote.nodeName;
            myView.txtTv2.text=myNote.nodeDes;

            myView.deletebtn.setOnClickListener{ ////1 04 26
                var dbManager= DbManager(this.context!!)
              val seletionArgs= arrayOf(myNote.nodeId.toString())
                dbManager.delete("ID=?",seletionArgs)
                LoadQuery("%")
            }
            //fwq
            myView.editebtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            myView.cpybtn.setOnClickListener {
                val title=myView.txtTv.text.toString();
                val desc=myView.txtTv2.text.toString();
                val s=title+"\n"+desc;
                val cb=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text=s;
                Toast.makeText(this@MainActivity,"Copied....", Toast.LENGTH_SHORT).show();

            }
            return myView;
        }

        override fun getItem(position: Int): Any {

            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        override fun getCount(): Int {
           return listNotesAdapter.size
        }

    }

    private fun GoToUpdateFun(myNote: note) {
        var inent=Intent(this,Addnotes_Activity::class.java)
        intent.putExtra("ID", myNote.nodeId)
        intent.putExtra("name",myNote.nodeName)
        //51 min
        inent.putExtra("des", myNote.nodeDes)
        startActivity(intent)
    }
}
/////////////share activity is on 56:21////////////


