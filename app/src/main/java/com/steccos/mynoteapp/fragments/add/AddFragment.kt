package com.steccos.mynoteapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.steccos.mynoteapp.R
import com.steccos.mynoteapp.data.models.ToDoData
import com.steccos.mynoteapp.data.viewModel.ToDoViewModel
import com.steccos.mynoteapp.databinding.FragmentAddBinding
import com.steccos.mynoteapp.fragments.SharedViewModel

class AddFragment : Fragment() {

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_add) {
                    insertDataToDb()
                } else if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()


        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if(validation){
            //insert data to database
            val newData = ToDoData(
                0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription
            )
            //next line causes the add note 'clicker' to crash
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_SHORT).show()
            //navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else{
            Toast.makeText(requireContext(), "Please, fill all fields", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


