package com.example.lawdcm.bottomnav

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.lawdcm.adapters.JudgesAdapter
import com.example.lawdcm.databinding.AddNewJudgeDialogBinding
import com.example.lawdcm.databinding.FragmentJudgeBinding
import com.example.lawdcm.models.JudgeDetails
import com.example.lawdcm.singleton.ActiveJudges
import com.example.lawdcm.singleton.registrarLoggedIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

class JudgeFragment : Fragment() {

    private lateinit var binding : FragmentJudgeBinding
    private lateinit var dialogBinding : AddNewJudgeDialogBinding
    private var judgeImageUri : Uri? = null
    private lateinit var storageRef: StorageReference
    private lateinit var judgesAdapter : JudgesAdapter

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        judgeImageUri = uri

        if(uri != null){
            Glide.with(requireActivity()).load(uri).into(dialogBinding.imgJudge)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJudgeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isAdded)
            return

        storageRef = FirebaseStorage.getInstance().reference
        judgesAdapter = JudgesAdapter(requireActivity())

        binding.btnAddJudge.setOnClickListener {
            showAddJudgeDialog()
        }

        binding.rvActiveJudges.adapter = judgesAdapter

        ActiveJudges.activeJudgesList.observe(viewLifecycleOwner){
            judgesAdapter.setjudgeList(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showAddJudgeDialog() {
        val dialog = Dialog(requireActivity())

        dialogBinding = AddNewJudgeDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        //dialog.findViewById<Button>(R.id.btnReview).setOnClickListener {
        //    dialog.dismiss()
        //}


        dialogBinding.imgJudge.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        dialogBinding.btnAdd.setOnClickListener {

            if(dialogBinding.etJudgeId.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Enter judge id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(dialogBinding.etJudgeName.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Enter judge Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(judgeImageUri == null){
                Toast.makeText(requireActivity(), "Select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val snack = Snackbar.make(binding.root,"Uploading judge details...", Snackbar.LENGTH_LONG)
            snack.show()

            runBlocking {
                dialog.setCancelable(false)
                requireActivity().onBackPressedDispatcher.addCallback (requireActivity() , object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // This is done to block the back button so that the user can't exit the page until the uploading task is done
                    }
                })
                uploadPicToFirebase(judgeImageUri!!, snack, dialog)
            }

            //vm.updateListingDetailsIntoFirebase(currObj.id)
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun uploadPicToFirebase(uri: Uri, snack : Snackbar, dialog: Dialog) {

        val judgeId = dialogBinding.etJudgeId.text.toString()
        val judgeName = dialogBinding.etJudgeName.text.toString()

        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
        val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val storagePath = storageRef.child("judgeProfile/$judgeId.jpg")
        val uploadTask = storagePath.putBytes(data)

        uploadTask.addOnSuccessListener {
            val task = it.metadata?.reference?.downloadUrl
            task?.addOnSuccessListener {

                val dbReference : DatabaseReference = FirebaseDatabase.getInstance().getReference().child("judges").child(judgeId)
                dbReference.setValue(JudgeDetails(judgeId, judgeName, registrarLoggedIn.courtType, registrarLoggedIn.courtId))

                snack.dismiss()
                dialog.dismiss()
            }
        }
    }
}