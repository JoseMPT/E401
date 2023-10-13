package utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class AlertMessages (private val context: Context){

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showAlert(title: String, message: String){
        val alert = AlertDialog.Builder(context)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Ok"){ _, _ ->}
        alert.show()
    }
}