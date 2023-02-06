package com.steccos.mynoteapp.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.steccos.mynoteapp.R
import com.steccos.mynoteapp.data.models.Priority
import com.steccos.mynoteapp.data.models.ToDoData
import com.steccos.mynoteapp.data.viewModel.ToDoViewModel
import com.steccos.mynoteapp.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        //set menu
        setHasOptionsMenu(true)

        view.current_title_et.setText(args.currentitem.title)
        view.current_description_et.setText(args.currentitem.description)
        view.current_priorities_spinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentitem.priority))
        view.current_priorities_spinner.onItemSelectedListener = mSharedViewModel.listener

        return view

    }

    //onCreateOptionsMenu is deprecated
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            updateItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val getPriority = current_priorities_spinner.selectedItem

        val validation = mSharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            //update current item
            val updatedItem = ToDoData(
                args.currentitem.id,
                title,
                mSharedViewModel.parsePriority(getPriority.toString()),
                description
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()
            //navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please, fill out all fields", Toast.LENGTH_SHORT).show()
        }

    }
}