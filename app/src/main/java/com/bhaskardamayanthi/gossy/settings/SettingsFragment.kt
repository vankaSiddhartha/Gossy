package com.bhaskardamayanthi.gossy.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bhaskardamayanthi.gossy.R
import com.bhaskardamayanthi.gossy.adapter.SettingsAdapters
import com.bhaskardamayanthi.gossy.databinding.FragmentSettingsBinding
import com.bhaskardamayanthi.gossy.localStore.StoreManager

class SettingsFragment : Fragment() {

private lateinit var binding:FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        val storeManager = StoreManager(requireContext())
        val  name = storeManager.getString("name","")
        val fakeName = storeManager.getString("fakeName","")
        val fakeProfile = storeManager.getString("fakeImg","")
        val profile = storeManager.getString("profile","")

        val settingsList: List<Pair<String, String>> = listOf(
            "Premium Mode" to "",
            "Fire Mode🔥" to "FireMode",
            "My Account" to "",
            "Name" to name,
            "Gossy Name" to fakeName,
            "Profile" to profile,
            "Gossy Profile" to fakeProfile,
            "Support" to "",
            "Help Center" to "https://gossy.vercel.app/help-center",
            "Terms of Service" to "https://gossy.vercel.app/terms-of-service",
            "Privacy Policy" to "https://gossy.vercel.app/privacy-policy",
            "Company" to "",
            "About" to "https://gossy.vercel.app/about",
            "Company Blogs" to "https://gossy.vercel.app/blogs",
            "Careers" to "https://gossy.vercel.app/careers",
            "Feedback" to "https://gossy.vercel.app/contact",
            "Follow Us" to "",
            "Twitter" to "",
            "Instagram" to "",
            "LinkedIn" to "",
            "Website" to "",
            "Gossy website" to "https://gossy.vercel.app/"



        )
        binding.settingsRv.addItemDecoration(DividerItemDecoration(binding.settingsRv.context, DividerItemDecoration.VERTICAL))
        binding.settingsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.settingsRv.adapter = SettingsAdapters(settingsList,requireContext())
        return binding.root
    }


}