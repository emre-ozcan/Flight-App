package com.emreozcan.flightapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.data.RetrofitObject
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.models.Report
import com.emreozcan.flightapp.models.User
import com.emreozcan.flightapp.models.notification.PushNotification
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION_REPORT
import com.emreozcan.flightapp.util.Constants.Companion.FIREBASE_COLLECTION_USER
import com.emreozcan.flightapp.util.Constants.Companion.STORAGE_REPORT_REFERENCE
import com.emreozcan.flightapp.util.DataResult
import com.emreozcan.flightapp.util.DataStoreRepository
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val dataStore = DataStoreRepository(application.applicationContext)
    private val storage = FirebaseStorage.getInstance()

    val airportsList: MutableLiveData<List<Airports>> = MutableLiveData()

    val currentUser = auth.currentUser
    val userLogin: MutableLiveData<User> = MutableLiveData()

    val readOnboarding = dataStore.readOnboarding.asLiveData()
    val readLanguageCode = dataStore.readLanguage.asLiveData()


    val dataResult: MutableLiveData<DataResult> = MutableLiveData()

    /**DataStore*/
    fun saveOnboarding() {
        viewModelScope.launch {
            dataStore.onBoardingShowed()
        }
    }

    fun saveLanguageCode(languageCode: String){
        viewModelScope.launch {
            viewModelScope.launch {
                dataStore.setLanguage(languageCode)
            }
        }
    }

    /**Get Data From Firebase*/
    fun getData() {

        dataResult.value = DataResult.Loading()

        var tempList = arrayListOf<Airports>()
        database.collection(FIREBASE_COLLECTION).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.d("data", exception.localizedMessage!!)
                dataResult.value = DataResult.Error(exception.localizedMessage)
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
                                    it.get("companyName"),
                                    it.get("flightStartAndFinishTime"),
                                    it.get("capacity"),
                                    it.get("hour"),
                                    it.get("flightCode"),
                                    it.get("startAndTargetCode")
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
                if (tempList.isNotEmpty()) {
                    airportsList.value = tempList
                    dataResult.value = DataResult.Success()
                }
            }
        }
    }

    fun sendReport(selectedImage: Uri?,user: User,complaint: String,fragment: Fragment){
        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"
        var imageUrl = ""

        val imageReference = storage.reference.child(STORAGE_REPORT_REFERENCE).child(imageName)
        if (selectedImage!= null){
            imageReference.putFile(selectedImage).addOnSuccessListener { _ ->
                storage.reference.child(STORAGE_REPORT_REFERENCE).child(imageName)
                    .downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                        database.collection(FIREBASE_COLLECTION_REPORT).add(Report(user.userName,user.userSurname
                            ,user.userEmail,imageUrl,complaint)).addOnSuccessListener {

                            Toast.makeText(fragment.context,"Succesfully Sent",Toast.LENGTH_LONG).show()
                            fragment.findNavController().navigate(R.id.action_reportFragment_to_action_profile)

                        }.addOnFailureListener { exception ->
                            Toast.makeText(fragment.context,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                        }

                    }
            }
        }else{
            database.collection(FIREBASE_COLLECTION_REPORT).add(Report(user.userName,user.userSurname
                ,user.userEmail,imageUrl,complaint)).addOnSuccessListener {

                Toast.makeText(fragment.context,"Succesfully Sent",Toast.LENGTH_LONG).show()
                fragment.findNavController().navigate(R.id.action_reportFragment_to_action_profile)

            }.addOnFailureListener { exception ->
                Toast.makeText(fragment.context,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
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
                val f1 =
                    Flights("Turkish Airlines", "09:45;11:45", "20", "Hour", "TK1919", "SAW,SZF")
                val f2 = Flights("Pegasus", "08:50;12:45", "10", "Hour", "TK9876", "ABC,DEF")
                val flightList = listOf(f1, f2)

                database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                    .set(User(name, surname, email, password, flightList))
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

            Toast.makeText(fragment.context, fragment.getString(R.string.succesful_login), Toast.LENGTH_LONG).show()
            fragment.findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            fragment.activity?.finish()

        }.addOnFailureListener { exception ->
            Toast.makeText(
                fragment.context,
                exception.localizedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getUser(uid: String) {
        database.collection(FIREBASE_COLLECTION_USER).document(uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.d("data", exception.localizedMessage!!)
                } else {
                    if (snapshot != null) {
                        val document = snapshot.data
                        document?.let {
                            val name = it["userName"] as String
                            val surname = it["userSurname"] as String
                            val password = it["userPassword"] as String
                            val email = it["userEmail"] as String
                            val flightHashList =
                                it["flightHistoryList"] as List<HashMap<String, String>>

                            val flightList = arrayListOf<Flights>()

                            flightHashList.forEach {
                                val flight =
                                    Flights(
                                        it["companyName"],
                                        it["flightStartAndFinishTime"],
                                        it["capacity"],
                                        it["hour"],
                                        it["flightCode"],
                                        it["startAndTargetCode"]
                                    )
                                flightList.add(flight)
                            }

                            val user = User(name, surname, email, password, flightList)
                            userLogin.value = user
                        }

                    }
                }
            }
    }

    fun logout() {
        database.clearPersistence()
        auth.signOut()
    }

    fun changeUserInformations(
        name: String,
        surname: String,
        email: String,
        password: String,
        context: Context,
    ) {
        var isChanged = false

        val credential = EmailAuthProvider.getCredential(
            userLogin.value!!.userEmail!!,
            userLogin.value!!.userPassword!!
        )

        currentUser!!.reauthenticate(credential).addOnFailureListener { exception ->
            Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT)
                .show()
        }

        if (userLogin.value?.userEmail != email) {
            isChanged = true
            currentUser.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                        .update("userEmail", email)

                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (userLogin.value?.userPassword != password) {
            isChanged = true
            currentUser.updatePassword(password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                        .update("userPassword", password)

                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (userLogin.value?.userName != name) {
            isChanged = true
            database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                .update("userName", name)

        }

        if (userLogin.value?.userSurname != surname) {
            isChanged = true
            database.collection(FIREBASE_COLLECTION_USER).document(auth.currentUser!!.uid)
                .update("userSurname", surname)
        }
        if (isChanged) {
            Toast.makeText(context, context.getString(R.string.changes_applied), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.any_change), Toast.LENGTH_SHORT).show()
        }

    }


     fun sendNotification(notification: PushNotification,context: Context) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitObject.api.postNotification(notification)
            if (response.isSuccessful){
                Toast.makeText(context,response.message().toString(),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,response.errorBody().toString(),Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            e.printStackTrace()
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
    /**Push Data to Firebase*/
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



}