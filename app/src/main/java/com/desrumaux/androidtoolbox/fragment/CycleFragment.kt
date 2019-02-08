package com.desrumaux.androidtoolbox.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.desrumaux.androidtoolbox.R
import kotlinx.android.synthetic.main.fragment_cycle.*

/**
 * A simple [Fragment] subclass.
 *
 */
class CycleFragment : Fragment() {

    var isFragmentRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showInfo(" On CreateView ")
        isFragmentRunning = true
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cycle, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        showInfo(" On Attach ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showInfo(" On Create ")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showInfo(" On Activity Created ")
    }

    override fun onStart() {
        super.onStart()
        showInfo(" On Start ")
    }

    override fun onResume() {
        super.onResume()
        showInfo(" On Resume ")
    }

    override fun onPause() {
        super.onPause()
        showInfo(" On Pause ")
    }

    override fun onStop() {
        super.onStop()
        showInfo(" On Stop ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentRunning = false
        showInfo(" On Destroy View ")
    }

    override fun onDestroy() {
        super.onDestroy()
        showInfo(" On Destroy ")
    }

    override fun onDetach() {
        super.onDetach()
        showInfo(" On Detach ")
    }

    private fun showInfo(msg: String)
    {
        if (isFragmentRunning)
        {
            fragment_text.text = fragment_text.text.toString().plus(msg + "\n")
        }
        else
        {
            Log.d("FRAGMENT", msg)
        }
    }
}
