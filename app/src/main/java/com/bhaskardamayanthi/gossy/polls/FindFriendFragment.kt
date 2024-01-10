package com.bhaskardamayanthi.gossy.polls

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.ChooseCollageAdapter
import com.bhaskardamayanthi.gossy.adapter.FindFriendAdapter
import com.bhaskardamayanthi.gossy.databinding.FragmentFindFriendBinding
import com.bhaskardamayanthi.gossy.managers.GETfromFirebaseManager
import com.bhaskardamayanthi.gossy.model.CollageModel
import com.bhaskardamayanthi.gossy.model.Contact
import com.bhaskardamayanthi.gossy.model.UserModel
import com.bhaskardamayanthi.gossy.viewModel.FindFragmentViewModel
import com.bhaskardamayanthi.gossy.viewModel.FriendRecommendationViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FindFriendFragment : Fragment() {
    private lateinit var binding: FragmentFindFriendBinding
    private lateinit var viewModel: FindFragmentViewModel
    private lateinit var friendsViewModel: FriendRecommendationViewModel
    private lateinit var adapter:FindFriendAdapter
    private val REQUEST_CONTACTS_PERMISSION = 123
    private lateinit var  list:List<UserModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        friendsViewModel = ViewModelProvider(requireActivity())[FriendRecommendationViewModel::class.java]
        binding = FragmentFindFriendBinding.inflate(layoutInflater, container, false)
        binding.shimmer.startShimmer()
        viewModel = ViewModelProvider(requireActivity())[FindFragmentViewModel::class.java]
       adapter = FindFriendAdapter(requireContext())
        binding.findFriendRv.layoutManager = LinearLayoutManager(requireContext())
        binding.findFriendRv.addItemDecoration(
            DividerItemDecoration(
                binding.findFriendRv.context,
                DividerItemDecoration.VERTICAL
            )
        )

        friendsViewModel.friendRecommendations.observe(viewLifecycleOwner){data->
            adapter.getFriends(data)

        }

        viewModel.dataList.observe(requireActivity()) { data ->

            list = data
            adapter.updateData(data)


        }
        viewModel.isLoding.observe(requireActivity()){isLoading->
            if (!isLoading){
                binding.shimmer.hideShimmer()
                binding.findFriendRv.visibility = View.VISIBLE
                binding.shimmer.visibility= View.GONE
            }

        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS_PERMISSION
            )
        } else {
            fetchContacts(requireContext()){contacts ->
                adapter.updateContacts(contacts)
                val contactsWithoutDuplicates = contacts.toSet().toList()
                friendsViewModel.fetch(contactsWithoutDuplicates)


       }

        }

        binding.findFriendRv.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchUsersByName(newText)
                }else{
                    friendsViewModel.friendRecommendations.observe(viewLifecycleOwner){data->
                        adapter.getFriends(data)

                    }
                }
                return true
            }
        })



        return binding.root
    }


    private fun fetchContacts(context: Context, contactsCallback: (List<Contact>) -> Unit) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val contacts = getContacts(context)
            contactsCallback(contacts)
        }
    }
    fun searchUsersByName(name: String) {

        val query = Firebase.database("https://gossy-fbbcf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").orderByChild("name").startAt(name).endAt(name + "\uf8ff").limitToLast(10)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val filteredList = ArrayList<UserModel>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    user?.let { filteredList.add(it) }
                }

                if (filteredList.isNotEmpty()) {
                    adapter.getFriends(filteredList)
                    binding.findFriendRv.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }

    private suspend fun getContacts(context: Context): List<Contact> {
        return withContext(Dispatchers.IO) {
            val contactList = mutableListOf<Contact>()
            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )

            cursor?.use {
                val nameIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumberIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                    val phoneNumber =
                        if (phoneNumberIndex != -1) it.getString(phoneNumberIndex) else ""

                    contactList.add(Contact(name, phoneNumber))
                }
            }

            cursor?.close()

            return@withContext contactList
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

}



