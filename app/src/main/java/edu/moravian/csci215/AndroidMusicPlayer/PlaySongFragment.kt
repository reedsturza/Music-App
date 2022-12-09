package edu.moravian.csci215.AndroidMusicPlayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.moravian.csci215.AndroidMusicPlayer.databinding.FragmentPlaySongBinding
import kotlinx.coroutines.Runnable
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
     *  -previousFragment: the previous fragment that a song was click
     */
    private val args: PlaySongFragmentArgs by navArgs()

    /** when the back button on the menu is clicked it takes the user to the previous fragment */
    private var previousFragment: String = ""

    /** The view model containing th event we are showing/editing */
    private val playSongViewModel: PlaySongViewModel by viewModels{
        PlaySongViewModelFactory(args.songId)
    }

    /** for when the seekbar is changed while the song is playing */
    lateinit var runnable: Runnable
    lateinit var handler: Handler

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
        Log.i("M:", playSongViewModel.song.value?.songId.toString())
        super.onViewCreated(view, savedInstanceState)

        //sets the previousFragment so when the back button on the menu is clicked it takes the user back
        previousFragment = args.previousFragment

        // changes whether the song is liked or not in the database
        if (playSongViewModel.song.value?.liked == false) {
            playSongViewModel.updateSong { oldSong -> oldSong.copy(liked = true) }
            // if the song isn't liked, when the liked is clicked it changes to ic_not_like
        } else if (playSongViewModel.song.value?.liked == true) {
            playSongViewModel.updateSong { oldSong -> oldSong.copy(liked = false) }
        }

        val mediaPlayer: MediaPlayer = MediaPlayer.create(activity, playSongViewModel.song.value!!.rawPath)
        // set the seek bar to 0
        binding.seekBar.progress = 0
        //set the max value that the seek bar can reach
        binding.seekBar.max = mediaPlayer.duration

        binding.playButton.setOnClickListener {
            // check if the song is being played
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                // change the play button to a pause button
                binding.playButton.setImageResource(R.drawable.ic_pause_button)
            } else {
                // if the music is playing and the user presses pause changes the pause button back to play
                mediaPlayer.pause()
                binding.playButton.setImageResource(R.drawable.ic_play_button)
            }
        }

        // when we change our seek bar progress the song will change position
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        // for when the user moves the seek bar while the song is playing
        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }

        // when the music is done the play button and seekbar reset
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.ic_play_button)
            binding.seekBar.progress = 0
        }

        // the menu provider to the host activity
        requireActivity().addMenuProvider(
            PlaySongMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        // use coroutine to collect the song form the database
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playSongViewModel.song.collect { song ->
                    song?.let { updateUI(it) }
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
    private fun goBack(previousFragment: String) {
        findNavController().navigate(
            if (previousFragment == "LikedMusic") {
                PlaySongFragmentDirections.backToLikedMusicList()
            } else {
                PlaySongFragmentDirections.backToShowSongs()
            }
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
            if (menuItem.itemId == R.id.back_to_previous_fragment) {
                goBack(previousFragment)
                return true
            }
            return false
        }
    }

    /**
     * Updates the UI to match the song. This also sets click listeners
     * to the like feature
     * @param song the song that is being played
     */
    private fun updateUI(song: Song) {
        binding.apply {
            /** sets the song name and artist to the correct values */
            Log.i("M:", song.toString())
            Log.i("M:", song.songName)
            songName.text = song.songName
            artist.text = song.artist

            /** preset the liked to ic_like or ic_not_like */
            if (song.liked) {
                liked.setImageResource(R.drawable.ic_liked)
            } else {
                liked.setImageResource(R.drawable.ic_not_liked)
            }

            /** change the like or not like when clicked */
            liked.setOnClickListener {
                // if the song is like, when the liked is clicked it changes to ic_like
                if (playSongViewModel.song.value?.liked == false) {
                    liked.setImageResource(R.drawable.ic_liked)
                // if the song isn't liked, when the liked is clicked it changes to ic_not_like
                } else if (playSongViewModel.song.value?.liked == true) {
                    liked.setImageResource(R.drawable.ic_not_liked)
                }
            }
        }
    }
}