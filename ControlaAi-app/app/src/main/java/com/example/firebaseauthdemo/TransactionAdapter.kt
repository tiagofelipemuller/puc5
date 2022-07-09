package com.example.firebaseauthdemo

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TransactionAdapter(private val transactionList : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var transactionArrayList : ArrayList<TransactionId> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction,
        parent, false)
        // Get the id of all the transactions
        getTransactionIdData()
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = transactionList[position]

        holder.transactionOperation.text = if (currentItem.operation == "Depositar") "Depósito" else "Gasto"
        holder.transactionAmount.text = "R$${currentItem.amount}"
        holder.transactionCategory.text = currentItem.category
        holder.transactionDate.text = currentItem.date
        holder.deleteBtn.setOnClickListener {


            val current = transactionArrayList[position].id
            //Apaga o elemento do banco
            FirebaseFirestore.getInstance().collection("transactions").document(current)
                .delete()
            // Atualiza a lista para mostrar as mudanças
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionOperation : TextView = itemView.findViewById(R.id.transactionOperations)
        val transactionAmount : TextView = itemView.findViewById(R.id.transactionAmount)
        val transactionCategory : TextView = itemView.findViewById(R.id.transactionCategory)
        val transactionDate : TextView = itemView.findViewById(R.id.transactionDate)
        val deleteBtn : ImageButton = itemView.findViewById(R.id.delete_transaction)
    }

    private fun getTransactionIdData() {

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseFirestore.getInstance().collection("transactions")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "encontrado ${document.id} => ${document.data["date"]}")
                    if (document.data["uid"].toString() == userUid) {
                        val newTransaction =
                            TransactionId(
                                document.id,
                            )
                        transactionArrayList.add(newTransaction)
                    }
                }

            }
    }

}