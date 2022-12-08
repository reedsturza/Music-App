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
import edu.moravian.csci215.AndroidMusicPlayer.databinding.FragmentPlaySongBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * The fragment for a single song. It allows for playing and pausing of the music.
 * This is the only fragment that can play music
 */
class PlaySongFragment : Fragment() {
    /** Binding for the views of the fragment (nullable version) */
    private var _binding: FragmentPlaySongBinding? = null
    /** Binding for the views of the fragment (non-nullable accessor) */
    private val binding: FragmentPlaySongBinding
        get() = checkNotNull(_binding) { "Binding is currently null! Oh-uh!" }

    /**
     * the arguments passed to this fragment:
     *  - songId: the song id to use grab the song to show
     */
    private val args: PlaySongFragmentArgs by navArgs()

    /** The view model containing th event we are showing/editing */
    private val playSongViewModel: PlaySongViewModel by viewModels{
        PlaySongViewModelFactory(args.songId)
    }

    /** Create the binding view for this layout. */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaySongBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * Collect the song from the database,
     * set up the on click listeners for the music to play and pause
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            playSongName.text = playSongViewModel.song.value?.songName
            playArtist.text = playSongViewModel.song.value?.artist
        }

        binding.playButton.setOnClickListener {
           (requireActivity() as MainActivity).musicPlayer.play()
        }

        binding.pauseButton.setOnClickListener {
           // (requireActivity() as MainActivity).musicPlayer.pause()
        }

        // the menu provider to the host activity
        requireActivity().addMenuProvider(
            PlaySongMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
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
            PlaySongFragmentDirections.backToShowSongs()
        )
    }

    /**
     * inner class for the Calendar menu and its functions
     */
    private inner class PlaySongMenu: MenuProvider {

        /**
         * onCreateMenu inflate the menus defined in fragment_event_menu
         * @param menu
         * @param menuInflater
         */
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_play_song, menu)
        }

        /**
         * onMenuItemSelected handles when an event is either saved or deleted
         * @param menuItem
         */
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.back_to_music_list_from_play_song) {
                goBack()
                return true
            }
            return false
        }
    }
}