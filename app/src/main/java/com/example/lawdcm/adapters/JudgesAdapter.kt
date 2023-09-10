package com.example.lawdcm.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lawdcm.databinding.ItemJudgeDetailBinding
import com.example.lawdcm.models.judgeDetails
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class JudgesAdapter(val cntxt : Context) : RecyclerView.Adapter<JudgesAdapter.ViewHolder>() {

    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var binding : ItemJudgeDetailBinding
    var judgesList = listOf<judgeDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemJudgeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return judgesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val judgeDetailInHand = judgesList[position]
        holder.tvJudgeName.text = judgeDetailInHand.judgeName
        holder.tvJudgeId.text = judgeDetailInHand.judgeId

        storageRef.child("judgeProfile").child("${judgeDetailInHand.judgeId!!}.jpg")
            .downloadUrl.addOnSuccessListener {
                Toast.makeText(cntxt, "$it", Toast.LENGTH_SHORT).show()
                Glide.with(cntxt).load(it).into(holder.imgJudge)
        }
        .addOnFailureListener { Toast.makeText(cntxt, "${it.message}", Toast.LENGTH_SHORT).show() }
        .addOnCanceledListener { Toast.makeText(cntxt, "gadbad2", Toast.LENGTH_SHORT).show() }
    }

    fun setjudgeList(list: List<judgeDetails>) {
        judgesList = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemJudgeDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgJudge = binding.imgJudge
        val tvJudgeName = binding.tvJudgeName
        val tvJudgeId = binding.tvJudgeId
    }
}