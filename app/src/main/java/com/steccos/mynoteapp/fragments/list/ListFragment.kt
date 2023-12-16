package com.steccos.mynoteapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.steccos.mynoteapp.R
import com.steccos.mynoteapp.data.models.ToDoData
import com.steccos.mynoteapp.data.viewModel.ToDoViewModel
import com.steccos.mynoteapp.databinding.FragmentListBinding
import com.steccos.mynoteapp.fragments.SharedViewModel
import com.steccos.mynoteapp.fragments.list.adapter.ListAdapter
import com.steccos.mynoteapp.utils.hideKeyboard
import com.steccos.mynoteapp.utils.observeOnce
import jp.wasabeef.recyclerview.animators.LandingAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

     private val mToDoViewModel : ToDoViewModel by viewModels ()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

     private val adapter : ListAdapter by lazy { ListAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //Setup recyclerView
        setupRecyclerview()
        //observe livedata
        mToDoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            mSharedViewModel.checkIfDatabaseIsEmpty(data)
            adapter.setData(data)
            binding.recyclerViewListView.scheduleLayoutAnimation()
        }

        //Hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_all -> confirmRemoval()
                    R.id.menu_priority_high ->
                        mToDoViewModel.sortByHighPriority.observe(viewLifecycleOwner) {
                            adapter.setData(it)
                        }
                    R.id.menu_priority_low ->
                        mToDoViewModel.sortByLowPriority.observe(viewLifecycleOwner) {
                            adapter.setData(it)
                        }

                    android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
    private fun setupRecyclerview() {
        val recyclerView = binding.recyclerViewListView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        //set the item Animator
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 100
        }

        //swipe to delete
        swipeToDelete(recyclerView)
    }
    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                //delete item
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                //restore deletedData fun
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
            //next line is required in the animation library but crashes the app when I press 'undo'
            //adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner) { list ->
            list.let {
                Log.d("ListFragment", "searchThroughDatabase")
                adapter.setData(it)
            }
        }
    }


    //show alert dialog to confirm removal of all items
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully removed: everything!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}

