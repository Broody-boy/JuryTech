package com.example.lawdcm.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lawdcm.databinding.ItemCaseBinding
import com.example.lawdcm.databinding.ItemJudgeDetailBinding
import com.example.lawdcm.models.CaseDetails
import com.example.lawdcm.models.JudgeDetails
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CasesAdapter(val cntxt : Context) : RecyclerView.Adapter<CasesAdapter.ViewHolder>() {

    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var binding : ItemCaseBinding
    var casesList = listOf<CaseDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return casesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val caseDetailInHand = casesList[position]
//        holder.tvJudgeName.text = caseDetailInHand.judgeName
//        holder.tvJudgeId.text = judgeDetailInHand.judgeId

        storageRef.child("judgeProfile").child("${caseDetailInHand.judge!!.judgeId!!}.jpg")
            .downloadUrl.addOnSuccessListener {
                Toast.makeText(cntxt, "$it", Toast.LENGTH_SHORT).show()
//                Glide.with(cntxt).load(it).into(holder.imgJudge)
            }
            .addOnFailureListener { Toast.makeText(cntxt, "${it.message}", Toast.LENGTH_SHORT).show() }
            .addOnCanceledListener { Toast.makeText(cntxt, "gadbad2", Toast.LENGTH_SHORT).show() }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setcaseList(list: List<CaseDetails>) {
        casesList = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemCaseBinding) : RecyclerView.ViewHolder(binding.root) {


//        val imgJudge = binding.imgJudge
//        val tvJudgeName = binding.tvJudgeName
//        val tvJudgeId = binding.tvJudgeId
    }
}