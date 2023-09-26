package com.example.lawdcm.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lawdcm.activities.ScheduledCases
import com.example.lawdcm.databinding.ItemJudgeDetailBinding
import com.example.lawdcm.models.JudgeDetails
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class JudgesAdapter(val cntxt : Context) : RecyclerView.Adapter<JudgesAdapter.ViewHolder>() {

    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    lateinit var binding : ItemJudgeDetailBinding
    var judgesList = listOf<JudgeDetails>()

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
//                Toast.makeText(cntxt, "$it", Toast.LENGTH_SHORT).show()
                Glide.with(cntxt).load(it).into(holder.imgJudge)
        }
        .addOnFailureListener { Toast.makeText(cntxt, "${it.message}", Toast.LENGTH_SHORT).show() }
        .addOnCanceledListener { Toast.makeText(cntxt, "gadbad2", Toast.LENGTH_SHORT).show() }

        holder.imgDropDown.setOnClickListener {
            val intent = Intent(holder.itemView.context , ScheduledCases::class.java)
            intent.putExtra("judgeId" , judgeDetailInHand.judgeId)
            holder.itemView.context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setjudgeList(list: List<JudgeDetails>) {
        judgesList = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemJudgeDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgJudge = binding.imgJudge
        val tvJudgeName = binding.tvJudgeName
        val tvJudgeId = binding.tvJudgeId
        val imgDropDown = binding.expandDropDown //badme nya button lagana hai
    }
}