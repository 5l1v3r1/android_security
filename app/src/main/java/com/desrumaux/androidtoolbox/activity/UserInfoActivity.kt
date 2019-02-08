package com.desrumaux.androidtoolbox.activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.model.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UserInfoActivity : AppCompatActivity() {

    companion object {
        const val FILE = "user.json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                birthDateValue.text = formatter.format(cal.time)
            }

        birthDateValue.setOnClickListener {
            showDatePicker(dateSetListener, cal)
        }

        saveBtn.setOnClickListener {
            saveInfoToJSON(
                firstNameField.text.toString(),
                lastNameField.text.toString(),
                birthDateValue.text.toString()
            )
        }

        readBtn.setOnClickListener {
            val msg = readInfoFromJSON()
            showPopupMessage(msg)
        }
    }

    private fun showPopupMessage(msg: String) {
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(msg).setTitle("Informations utilisateur")

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveInfoToJSON(firstName: String, lastName: String, date: String) {
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && date != getString(R.string.userinfo_date_value)) {
            try {
                val fileOutput = openFileOutput(FILE, Context.MODE_PRIVATE)
                fileOutput.write(getJSONData(firstName, lastName, date).toByteArray())
                fileOutput.close()
            } catch (e: FileNotFoundException) {
                Log.e("ErrorStorage", e.message)
            }
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getJSONData(firstName: String, lastName: String, date: String): String {
        val gson = Gson()
        val user = User(firstName, lastName, date)
        return gson.toJson(user)
    }

    private fun readFromFile(): String {
        val fileInput = openFileInput(FILE)
        val bufferedReader = fileInput.bufferedReader()
        // Read the text from buffferReader and store in String variable
        return bufferedReader.use { it.readText() }
    }

    private fun readInfoFromJSON(): String {
        val gson = Gson()
        val user = gson.fromJson(readFromFile(), User::class.java)
        //Initialize the String Builder
        val stringBuilder = StringBuilder("Utilisateur\n---------------------")
        stringBuilder.append("\nPrénom : " + user.firstName)
        stringBuilder.append("\nNom : " + user.lastName)
        stringBuilder.append("\nDate de naissance : " + user.birthDate)
        stringBuilder.append("\nAge : " + computeAge(user.birthDate.toString()))
        return stringBuilder.toString()
    }

    private fun computeAge(date: String): Int {
        var age: Int
        val birth = Calendar.getInstance()
        val current = Calendar.getInstance()

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        birth.time = sdf.parse(date)

        age = current.get(Calendar.YEAR) - birth.get(Calendar.YEAR)

        if (current.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener, cal: Calendar) {
        // Get Current Date
        val currentYear = cal.get(Calendar.YEAR)
        val currentMonth = cal.get(Calendar.MONTH)
        val currentDay = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }
}
