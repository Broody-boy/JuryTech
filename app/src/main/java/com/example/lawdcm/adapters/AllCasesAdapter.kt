package com.example.lawdcm.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lawdcm.databinding.ItemCasesBinding
import com.example.lawdcm.models.CaseDetails
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AllCasesAdapter(val cntxt : Context) : RecyclerView.Adapter<AllCasesAdapter.ViewHolder>() {

    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var binding : ItemCasesBinding
    var casesList = listOf<CaseDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCasesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return casesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val caseDetailInHand = casesList[position]
        holder.tvJudgeName.text = caseDetailInHand.judge!!.judgeName
        holder.tvCaseId.text = caseDetailInHand.caseId
        holder.tvCaseName.text = caseDetailInHand.caseName
        holder.tvCaseActSection.text = "Act " + caseDetailInHand.caseAct + " Section " + caseDetailInHand.caseActSection
        holder.tvCaseId.text = caseDetailInHand.caseId
        holder.tvCaseCategory.text = caseDetailInHand.caseCategory
        //holder.tvJudgeId.text = judgeDetailInHand.judgeId

        storageRef.child("judgeProfile").child("${caseDetailInHand.judge!!.judgeId!!}.jpg")
            .downloadUrl.addOnSuccessListener {
                Toast.makeText(cntxt, "$it", Toast.LENGTH_SHORT).show()
                Glide.with(cntxt).load(it).into(holder.imgJudge)
            }
            .addOnFailureListener { Toast.makeText(cntxt, "${it.message}", Toast.LENGTH_SHORT).show() }
            .addOnCanceledListener { Toast.makeText(cntxt, "gadbad2", Toast.LENGTH_SHORT).show() }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setcaseList(list: List<CaseDetails>) {
        casesList = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemCasesBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgJudge = binding.imgImageofJudgeAssigned
        val tvJudgeName = binding.tvJudgeName
        val tvCaseName = binding.tvCaseName
        val tvCaseId = binding.tvCaseId
        val tvCaseCategory = binding.tvCaseCategory
        val tvCaseActSection = binding.tvCaseActSection
    }
}