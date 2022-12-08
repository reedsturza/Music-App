package edu.moravian.csci215.AndroidMusicPlayer

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.moravian.csci215.AndroidMusicPlayer.databinding.FragmentPlaylistBinding
import edu.moravian.csci215.AndroidMusicPlayer.databinding.PlaylistItemBinding
import kotlinx.coroutines.launch
import java.util.*

/**
 * A fragment that displays the list of playlists
 */
class PlaylistFragment : Fragment() {
    /** Binding for the views of the fragment (nullable version) */
    private var _binding: FragmentPlaylistBinding? = null
    /** Binding for the views of the fragment (non-nullable accessor) */
    private val binding: FragmentPlaylistBinding
        get() = checkNotNull(_binding) { "Binding is currently null! Oh-uh!" }

    /** The view model containing the playlists we are listing */
    private val playlistViewModel: PlaylistViewModel by viewModels()

    /** The list of playlists we are showing */
    private var playlists: List<Playlist> = emptyList()

    /** Create the binding view for this layout. */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        // linear layout manager
        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    /**
     * Once the view is created we can:
     *    - set up the recycler view
     *    - add the menu provider to the host activity
     *    - use a coroutine to collect the songs from the database (and notify
     *      the adapter that things have changed)
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup the recycler view
        binding.playlistRecyclerView.adapter = PlaylistAdapter()

        // menu provider to the host activity
        requireActivity().addMenuProvider(
            PlaylistMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        // Uses a coroutine to collect the events from the database (and notify the adapter that things have changed)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistViewModel.playlists.collect {
                    playlists = it
                    (binding.playlistRecyclerView.adapter as PlaylistFragment.PlaylistAdapter).notifyDataSetChanged()
                }
            }
        }
    }

    /** On destroying the view, clean up the binding. */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Navigate to the song player fragment showing the specified event
     * @param song the song to show the details for
     */
//    fun showPlaylist(playlist: Playlist) {
//        findNavController().navigate(
//            MusicListFragmentDirections.showClickedEvent(song.id)
//        )
//    }

    /**
     * Add the event to the playlist database and then show that playlist (as if
     * it had been clicked).
     * @param playlist the playlist to add to the database
     */
    private fun insertPlaylist(playlist: Playlist) {
        lifecycleScope.launch {
            playlistViewModel.insertPlaylist(playlist)
            // showPlaylist(playlist)
        }
    }

    /**
     * Delete the playlist from the playlist database
     * @param playlist the playlist being removed from the database
     */
    private fun removePlaylist(playlist: Playlist) {
        lifecycleScope.launch {
            playlistViewModel.removePlaylist(playlist)
        }
    }

    /**
     * Create and add a new playlist to the playlist database
     */
    private fun createAndAddPlaylist() {
        lifecycleScope.launch {
            val newPlaylist = Playlist(
                playlistId = UUID.randomUUID(),
                playlistName = ""
            )
            insertPlaylist(newPlaylist)
        }
    }

    /**
     *
     */
    private fun goToCreatePlaylist() {
        findNavController().navigate(
            PlaylistFragmentDirections
        )
    }

    /**
     * function to go back to the musicListFragment when the back menu item is clicked
     */
    private fun goBack() {
        findNavController().navigate(
            PlaylistFragmentDirections.backToMusicListFromPlaylists()
        )
    }

    /**
     * inner class for the PlaylistMenu and its functions
     */
    private inner class PlaylistMenu: MenuProvider {

        /**
         * onCreate inflate the menus defined in fragment_playlist_menu
         * @param menu
         * @param menuInflater
         */
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_playlist_menu, menu)
        }

        /**
         * onMenuItemSelected handles when a new playlist is selected
         * @param menuItem
         */
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.new_playlist) {
                goToCreatePlaylist()
                return true
            }
            else if (menuItem.itemId == R.id.back_to_music_list_from_playlists) {
                goBack()
                return true
            }
            return false
        }
    }

    /**
     * The ViewHolder for the items in the recycler view. This uses the layout
     * given in playlist_item.xml
     */
    private inner class PlaylistHolder(val binding: PlaylistItemBinding): RecyclerView.ViewHolder(binding.root) {
        /**
         * Update this view holder to display the given item.
         * @param playlist the playlist whose data we should use
         */
        fun bind(playlist: Playlist) {
            binding.apply {
                playlistName.text = playlist.playlistName
            }

            binding.root.setOnClickListener {
                // showPlaylist(playlist)
            }
        }
    }

    /**
     * The adapter for the playlist recycler view. This takes a list of
     * playlists as the backing data and produces playlistHolders for
     * the ViewHolders.
     */
    private inner class PlaylistAdapter() : RecyclerView.Adapter<PlaylistHolder>() {
        /**
         * To create the view holder we inflate the layout we want to use for
         * each item and then return an ItemViewHolder holding the inflated view
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PlaylistHolder(PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        /**
         * Binds an existing ViewHolder to the item at a given position in the data
         */
        override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
            holder.bind(playlists[position])
        }

        /**
         * The number of items in the list
         */
        override fun getItemCount(): Int {
            return playlists.size
        }
    }
}