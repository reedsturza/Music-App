package edu.moravian.csci215.AndroidMusicPlayer

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.DialogCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs

class SetPlaylistNameDialogFragment : DialogFragment() {

    private val args: SetPlaylistNameDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val b = AlertDialog.Builder(context)
        val dialogInflater = layoutInflater
        b.setTitle(R.string.name_playlist)
        val dialogLayout = dialogInflater.inflate(R.layout.dialog_playlist_name, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editPlaylistName)
        b.setView(dialogLayout)
        b.setPositiveButton("OK") { _, _ ->
            Toast.makeText(context, "New Playlist: " + editText.text.toString(), Toast.LENGTH_SHORT).show()
            setFragmentResult(REQUEST_KEY_PLAYLIST_NAME, bundleOf(
                BUNDLE_KEY_PLAYLIST_NAME to editText
            ))
        }
       return b.show()
    }

    companion object {
        const val REQUEST_KEY_PLAYLIST_NAME = "PLAYLIST_NAME"
        const val BUNDLE_KEY_PLAYLIST_NAME = "BUNDLE_PLAYLIST_NAME"
    }
}