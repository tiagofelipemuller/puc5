package com.example.firebaseauthdemo.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.firebaseauthdemo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class OperationsFragment : Fragment() {
    private var rootView: View? = null
    override fun onResume() {
        super.onResume()
        val operations = resources.getStringArray(R.array.operations)
        val categories = resources.getStringArray(R.array.categories)

        val arrayAdapterOperations = ArrayAdapter(requireContext(), R.layout.dropdownitemlayout, operations)
        val arrayAdapterCategories = ArrayAdapter(requireContext(), R.layout.dropdownitemlayout, categories)

        val operationsView: AutoCompleteTextView = rootView!!.findViewById(R.id.opType)
        val categoriesView: AutoCompleteTextView = rootView!!.findViewById(R.id.categoryType)

        operationsView.setAdapter(arrayAdapterOperations)
        categoriesView.setAdapter(arrayAdapterCategories)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragmentoperationslayout, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var date = ""

        val userUid = auth.currentUser!!.uid
        val today = Calendar.getInstance()
        val opField: AutoCompleteTextView = requireView().findViewById(R.id.opType)
        val btnAdd: Button = requireView().findViewById(R.id.btnAdd)

        val amount: TextInputEditText = requireView().findViewById(R.id.amount_input)

        val catField: AutoCompleteTextView = requireView().findViewById(R.id.categoryType)
        val picker: DatePicker = requireView().findViewById(R.id.picker)
        picker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) {
                view, year, month, day ->
            val month = month + 1
            date = "$month/$day/$year"
        }
        val dashboardFragment = DashboardFragment()

        btnAdd.setOnClickListener {
            when {
                TextUtils.isEmpty(date) -> {
                    Toast.makeText(
                        context,
                        "Por favor, escolha uma data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val transactionInfo: MutableMap<String, Any>  = HashMap()
                    transactionInfo["uid"] = userUid
                    transactionInfo["operations"] = opField.text.toString()
                    transactionInfo["category"] = catField.text.toString()
                    transactionInfo["amount"] = amount.text.toString().toDouble()
                    transactionInfo["date"] = date

                    //salva a transação no banco
                    db.collection("transactions").document()
                        .set(transactionInfo)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Transação foi salva no banco de dados.",
                                Toast.LENGTH_SHORT
                            ).show()
                            replaceFragment(dashboardFragment)
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Transação não foi salva no banco de dados.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    //Pega o saldo

                    val docRef = db.collection("users").document(userUid)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d(ContentValues.TAG, "existFrg: ${document.get("balance")}")
                                var finalBalance = document.getDouble("balance")
                                if (finalBalance != null) {
                                    //Caso a opção selecionada seja depositar, o saldo será somado, senão, descontado.
                                    if(opField.text.toString() == "Depositar") {
                                        finalBalance += amount.text.toString().toDouble()
                                    } else {
                                        finalBalance -= amount.text.toString().toDouble()
                                    }
                                    //Atualiza o saldo do usuário
                                    db.collection("users").document(userUid)
                                        .update("balance", finalBalance)
                                        .addOnSuccessListener { Log.d(TAG, "Saldo atualizado com sucesso!") }
                                        .addOnFailureListener { e -> Log.w(TAG, "Erro ao atualizar o saldo", e) }
                                }
                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }
                    }
                }
            }
        }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}

