package edu.moravian.csci215.AndroidMusicPlayer

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.moravian.csci215.AndroidMusicPlayer.databinding.FragmentMusicListBinding
import edu.moravian.csci215.AndroidMusicPlayer.databinding.MusicListItemBinding
import kotlinx.coroutines.launch
import java.util.*

/**
 * A fragment that displays the list of unfiltered music that isn't in a playlist
 */
class MusicListFragment : Fragment() {
    /** Binding for the views of the fragment (nullable version) */
    private var _binding: FragmentMusicListBinding? = null
    /** Binding for the views of the fragment (non-nullable accessor) */
    private val binding: FragmentMusicListBinding
        get() = checkNotNull(_binding) { "Binding is currently null! Oh-uh!" }

    /** The view model containing the songs we are listing */
    private val musicListViewModel: MusicListViewModel by viewModels()

    /** The list of songs we are showing */
    private var songs: List<Song> = emptyList()

    /** Create the binding view for this layout. */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMusicListBinding.inflate(layoutInflater, container, false)
        // linear layout manager
        binding.musicListRecyclerView.layoutManager = LinearLayoutManager(context)
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
        binding.musicListRecyclerView.adapter = MusicListAdapter()

        // the menu provider to the host activity
        requireActivity().addMenuProvider(
            MusicListMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        // Uses a coroutine to collect the events from the database (and notify the adapter that things have changed)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                musicListViewModel.songs.collect {
                    songs = it
                    (binding.musicListRecyclerView.adapter as MusicListAdapter).notifyDataSetChanged()
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
    fun showSong(song: Song) {
        findNavController().navigate(
            MusicListFragmentDirections.showClickedSong(song.songId)
        )
    }

    /**
     * Insert a song into the database when it is created
     */
    fun insertSong(songName: String, artist: String) {
        lifecycleScope.launch {
            val newSong = Song(
                songId = UUID.randomUUID(),
                songName = songName,
                artist = artist
            )
            musicListViewModel.insertSong(newSong)
        }
    }

    /**
     * function for when the menu item playlist
     * is selected to navigate to the playlists fragment.
     * Done in a function because when I tried to navigate in the
     * menu functions the app would crash
     */
    private fun goToPlaylists() {
        findNavController().navigate(
            MusicListFragmentDirections.gotToPlaylists()
        )
    }

    /**
     * inner class for the Calendar menu and its functions
     */
    private inner class MusicListMenu: MenuProvider {
        /**
         * onCreateMenu inflate the menus defined in fragment_event_menu
         * @param menu
         * @param menuInflater
         */
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_music_menu, menu)
        }

        /**
         * onMenuItemSelected handles when an event is either saved or deleted
         * @param menuItem
         */
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.go_to_playlists) {
                goToPlaylists()
                return true
            }
            return false
        }
    }

    /**
     * The ViewHolder for the items in the recycler view. This uses the layout
     * given in event_type_item.xml
     */
    private inner class MusicListHolder(val binding: MusicListItemBinding): RecyclerView.ViewHolder(binding.root) {
        /**
         * Update this view holder to display the given item.
         * @param song the song whose data we should use
         */
        fun bind(song: Song) {
            binding.apply {
                songName.text = song.songName
                artist.text = song.artist
            }

            binding.root.setOnClickListener {
                // when a song is clicked is shows the song in the PlaySongFragment
                showSong(song)
            }
        }
    }

    /**
     * The adapter for the Music list recycler view. This takes a list of
     * songs as the backing data and produces MusicListHolders for
     * the ViewHolders.
     */
    private inner class MusicListAdapter() : RecyclerView.Adapter<MusicListHolder>() {
        /**
         * To create the view holder we inflate the layout we want to use for
         * each item and then return an ItemViewHolder holding the inflated view
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MusicListHolder(MusicListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        /**
         * Binds an existing ViewHolder to the item at a given position in the data
         */
        override fun onBindViewHolder(holder: MusicListHolder, position: Int) {
            holder.bind(songs[position])
        }

        /**
         * The number of items in the list
         */
        override fun getItemCount(): Int {
            return songs.size
        }
    }
}