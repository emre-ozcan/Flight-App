package com.emreozcan.flightapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.models.User
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION_USER
import com.emreozcan.flightapp.util.DataStoreRepository
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val airportsList: MutableLiveData<List<Airports>> = MutableLiveData()
    val currentUser = auth.currentUser

    val userLogin: MutableLiveData<User> = MutableLiveData()

    val dataStore = DataStoreRepository(application.applicationContext)

    val readOnboarding = dataStore.readOnboarding.asLiveData()

    fun saveOnboarding() {
        viewModelScope.launch {
            dataStore.onBoardingShowed()
        }
    }


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
                        val flightHashList = doc.get("flightList") as List<HashMap<String, String>>

                        val flightList = arrayListOf<Flights>()

                        flightHashList.forEach {
                            val flight =
                                Flights(
                                    it.get("companyName")!!,
                                    it.get("flightStartAndFinishTime")!!,
                                    it.get("capacity")!!,
                                    it.get("hour")!!,
                                    it.get("flightCode")!!,
                                    it.get("startAndTargetCode")!!
                                )
                            flightList.add(flight)
                        }

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
                fragment.activity?.finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                fragment.context,
                "Something went wrong: ${exception.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun login(email: String, password: String, fragment: Fragment) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(fragment.context, "Succesfully Login !", Toast.LENGTH_LONG).show()
            fragment.findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            fragment.requireActivity().finish()
        }.addOnFailureListener { exception ->
            Toast.makeText(
                fragment.context,
                "Error ! : ${exception.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getUser(uid: String) {

        database.collection(FIREBASE_COLLECTION_USER).document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val doc = document.data!!
                    val name = doc.get("userName") as String
                    val surname = doc.get("userSurname") as String
                    val password = doc.get("userPassword") as String
                    val email = doc.get("userEmail") as String

                    val user = User(name, surname, email, password)
                    userLogin.value = user

                }
            }.addOnFailureListener {

            }
    }

    fun logout() {
        auth.signOut()
    }

    fun changeUserInformations(
        name: String,
        surname: String,
        email: String,
        password: String,
        context: Context
    ) {
        val credential = EmailAuthProvider.getCredential(
            userLogin.value!!.userEmail,
            userLogin.value!!.userPassword
        )
        currentUser!!.reauthenticate(credential).addOnFailureListener { exception ->
            Toast.makeText(context, "Error ! ${exception.localizedMessage}", Toast.LENGTH_SHORT)
                .show()

        }

        if (userLogin.value?.userEmail != email) {
            currentUser!!.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                        .update("userEmail", email)
                    Toast.makeText(context, "Email Updated", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, "Error ! ${exception.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (userLogin.value?.userPassword != password) {
            currentUser!!.updatePassword(password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                        .update("userPassword", password)

                    Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, "Error ! ${exception.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (userLogin.value?.userName != name) {
            database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                .update("userName", name)
        }

        if (userLogin.value?.userSurname != surname) {
            database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                .update("userSurname", surname)
        }


    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}