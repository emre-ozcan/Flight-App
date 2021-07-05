package com.emreozcan.flightapp.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.models.User
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION_USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val airportsList: MutableLiveData<List<Airports>> = MutableLiveData()
    val currentUser = auth.currentUser

    val userLogin: MutableLiveData<User> = MutableLiveData()



    fun pushData() {
        val f1 = Flights("Turkish Airlines", "09:45;11:45", "20", "Hour", "TK1919", "SAW,SZF")
        val f2 = Flights("Pegasus", "08:50;12:45", "20", "Hour", "TK1919", "ABC,DEF")
        val f3 = Flights("Anadolu Jet", "09:45;11:45", "20", "Hour", "TK1919", "DEF,ABC")
        val f4 = Flights("Onur air", "09:45;11:45", "20", "Hour", "TK1919", "SZF,ABC")
        val f5 = Flights("Atlas Global", "09:45;11:45", "20", "Hour", "TK1919", "XYZ,YZX")
        val f6 = Flights("Sunexpress", "09:45;11:45", "20", "Hour", "TK1919", "ABC,XYZ")
        val f7 = Flights("Corendon Airlines", "09:45;11:45", "20", "Hour", "TK1919", "DEF,ZYX")
        val f8 = Flights("Lufthanse", "09:45;11:45", "20", "Hour", "TK1919", "MAC,WIN")

        val flightList = listOf(f1, f2, f3, f4, f5, f6, f7, f8)


        val a1 = Airports("SAW", "Sabiha Gökçen", "Turkey", "123456", "123.132,456.789", flightList)
        val a2 = Airports("MCA", "Tokyo Haneda", "JAPAN", "654321", "123.132,456.789", flightList)
        val a3 = Airports("WIN", "Doha Hamad", "QATAR", "789456", "123.132,456.789", flightList)
        val a4 =
            Airports("TAL", "Seoul Incheon", "SOUTH KOREA", "123798", "123.132,456.789", flightList)
        val a5 = Airports("ABC", "Munich", "GERMANY", "753951", "123.132,456.789", flightList)
        val a6 = Airports("CBA", "Hong Kong", "CHINA", "159753", "123.132,456.789", flightList)
        val a7 = Airports("DEF", "Tokyo Narita", "JAPAN", "785632", "123.132,456.789", flightList)
        val a8 = Airports(
            "FED",
            "Amsterdam Schiphol",
            "NETHERLANDS",
            "1238569",
            "123.132,456.789",
            flightList
        )



        database.collection(FIREBASE_COLLECTION).add(a1)
        database.collection(FIREBASE_COLLECTION).add(a2)
        database.collection(FIREBASE_COLLECTION).add(a3)
        database.collection(FIREBASE_COLLECTION).add(a4)
        database.collection(FIREBASE_COLLECTION).add(a5)
        database.collection(FIREBASE_COLLECTION).add(a6)
        database.collection(FIREBASE_COLLECTION).add(a7)
        database.collection(FIREBASE_COLLECTION).add(a8)

    }

    fun getData() {
        var tempList = arrayListOf<Airports>()
        database.collection(FIREBASE_COLLECTION).addSnapshotListener { snapshot, exception ->
            if (exception != null) {

            } else {
                if (snapshot != null && !snapshot.isEmpty) {
                    val documents = snapshot.documents
                    tempList.clear()
                    for (doc in documents) {
                        val shortName = doc.get("airportCode") as String
                        val airportName = doc.get("airportName") as String
                        val country = doc.get("country") as String
                        val phoneNumber = doc.get("phoneNumber") as String
                        val latitudeAndLongitude = doc.get("latitudeLongitude") as String
                        val flightList = doc.get("flightList") as List<Flights>

                        val tempAirport = Airports(
                            shortName,
                            airportName,
                            country,
                            phoneNumber,
                            latitudeAndLongitude,
                            flightList
                        )
                        println(tempAirport)
                        tempList.add(tempAirport)
                    }
                }
                if (!tempList.isNullOrEmpty()) {
                    airportsList.value = tempList
                }
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        name: String,
        surname: String,
        fragment: Fragment
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(fragment.context, "Succesfully Signup !", Toast.LENGTH_LONG).show()

                database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                    .set(User(name, surname, email, password))
                fragment.findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                fragment.context,
                "Something went wrong: ${exception.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun login(email: String, password: String,fragment: Fragment) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(fragment.context, "Succesfully Login !", Toast.LENGTH_LONG).show()
            fragment.findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }.addOnFailureListener {  exception ->
            Toast.makeText(
                fragment.context,
                "Error ! : ${exception.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getUser(uid:String){

        database.collection(FIREBASE_COLLECTION_USER).document(uid).get().addOnSuccessListener { document->
            if (document != null){
                val doc = document.data!!
                val name = doc.get("userName") as String
                val surname = doc.get("userSurname") as String
                val password = doc.get("userPassword") as String
                val email = doc.get("userEmail") as String

                val user = User(name,surname,email,password)
                userLogin.value = user

            }
        }.addOnFailureListener {

        }
    }

    fun logout(){
        auth.signOut()
    }


}