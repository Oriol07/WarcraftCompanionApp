package com.oriolcomas.warcraft.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.Callback
import com.oriolcomas.warcraft.network.FirestoreService
import com.oriolcomas.warcraft.view.adapter.ProfileAdapter
import com.oriolcomas.warcraft.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_search_profile_dialog.*
import kotlinx.android.synthetic.main.fragment_username_dialog.*

class usernameDialog : DialogFragment() {

    private lateinit var btnAddAvatar: Button
    private lateinit var etAvatarLink: EditText
    private lateinit var pbAddAvatar: ProgressBar

    val firestoreService = FirestoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_username_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarAvatar.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_close)

        toolbarAvatar.setTitleTextColor(Color.WHITE)
        toolbarAvatar.setNavigationOnClickListener {
            dismiss()
        }

        initViews(view);

        initListeners(view)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun initViews(itemView: View)
    {

        btnAddAvatar = itemView.findViewById<Button>(R.id.btnAddAvatar)
        etAvatarLink = itemView.findViewById<EditText>(R.id.etAvatarLink)
        pbAddAvatar = itemView.findViewById(R.id.pbAddAvatar)

    }

    private fun initListeners(itemView: View) {

        btnAddAvatar.setOnClickListener {
            pbAddAvatar.visibility = View.VISIBLE
            val image = etAvatarLink.text.toString()
            //TODO: check if image is valid
            if (!isImageValid(image))
            {
                Log.i("AddNewAvatar", "Image is not valid")
                itemView.findViewById<EditText>(R.id.etAvatarLink).error = getString (R.string.invalid_image)
                pbAddAvatar.visibility = View.GONE
                return@setOnClickListener
            }

            //New Avatar :)
            firestoreService.UpdateAvatar(image, object: Callback<String> {
                override fun onSuccess(result: String?) {
                    pbAddAvatar.visibility = View.GONE
                    btnAddAvatar.isEnabled = true
                    showMessage(getString(R.string.add_avatar))
                    etAvatarLink.getText().clear()
                    dismiss()

                }

                override fun onFailed(exception: Exception) {
                    pbAddAvatar.visibility = View.GONE
                    btnAddAvatar.isEnabled = true
                    showMessage(getString(R.string.add_post_error))

                }
            })
        }
    }

    private fun isImageValid(image: String) : Boolean {
        val imageRegex = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)"


        return image.isNotBlank()
                && image.contains(Regex(imageRegex))
    }

    private fun showMessage(text: String)
    {

        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show()
    }

}