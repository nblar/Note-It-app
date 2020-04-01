package com.example.android.app_project

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addnotes_.*
import java.lang.Exception

class Addnotes_Activity : AppCompatActivity() {

     val dbTable="Notes"
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnotes_)

        try {
            val bundle:Bundle=intent.extras
            id=bundle.getInt("ID",0)
            if(id!=0){
                titleedit.setText(bundle.getString("name"))
                descEt.setText(bundle.getString("des"))
            }
        } catch (ex:Exception){}
    }
    fun addFunc(view: View) {
        var dbManager=DbManager(this)
        var values=ContentValues()
        values.put("Title",titleedit.text.toString());
        values.put("Description",descEt.text.toString())

        if(id==0){
            val ID=dbManager.insert(values)
            if(ID>0)
            {
                Toast.makeText(this,"Note is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(this,"Note is not added, try again", Toast.LENGTH_SHORT).show()

            }
        }
        else {
            var selectionArgs= arrayOf(id.toString())
            val ID= dbManager.update(values,"ID=?",selectionArgs)
            if (ID>0){
                Toast.makeText(this,"Note is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(this,"ERROR Adding note...", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
