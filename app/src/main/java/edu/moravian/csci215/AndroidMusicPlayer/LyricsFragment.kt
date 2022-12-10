package edu.moravian.csci215.AndroidMusicPlayer

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.moravian.csci215.AndroidMusicPlayer.databinding.FragmentLyricsBinding
import kotlinx.coroutines.launch

class LyricsFragment : Fragment() {
    /** Binding for the views of the fragment (nullable version) */
    private var _binding: FragmentLyricsBinding? = null
    /** Binding for the views of the fragment (non-nullable accessor) */
    private val binding: FragmentLyricsBinding
        get() = checkNotNull(_binding) { "Binding is currently null! Oh-uh!" }

    /**
     * the arguments passed to this fragment:
     *  - songId: the song id to use grab the song to show
     */
    private val args: LyricsFragmentArgs by navArgs()

    /** The view model containing th event we are showing/editing */
    private val lyricsViewModel: LyricsViewModel by viewModels{
        LyricsViewModelFactory(args.songId)
    }

    /** Create the binding view for this layout. */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLyricsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * Collect the song from the database,
     * set up the on click listeners for the music to play and pause
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // the menu provider to the host activity
        requireActivity().addMenuProvider(
            LyricsMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        // use coroutine to collect the song form the database
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                lyricsViewModel.lyrics.collect { lyrics ->
                    lyrics?.let { updateUI(it) }
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
     * function to go back to the musicListFragment when the back menu item is clicked
     */
    private fun goBack() {
        findNavController().navigate(
            if (args.previousFragment == "MusicList") {
                LyricsFragmentDirections.backToMusicListFromLyrics()
            } else {
                LyricsFragmentDirections.backToLikedMusicFromLyrics()
            }
        )
    }

    /**
     * inner class for the Calendar menu and its functions
     */
    private inner class LyricsMenu: MenuProvider {

        /**
         * onCreateMenu inflate the menus defined in fragment_event_menu
         * @param menu
         * @param menuInflater
         */
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_lyrics_menu, menu)
        }

        /**
         * onMenuItemSelected handles going back to musicLists and saving the song as liked or not
         * @param menuItem
         */
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.back_to_previous_fragment) {
                goBack()
                return true
            }
            return false
        }
    }

    /**
     * Updates the UI to match the lyrics.
     * @param lyrics the lyrics being shown
     */
    private fun updateUI(lyrics: Lyrics) {
        binding.apply {
            songName.text = args.songName
            artist.text = args.songArtist
            songLyrics.text = lyrics.lyrics
        }
    }
}