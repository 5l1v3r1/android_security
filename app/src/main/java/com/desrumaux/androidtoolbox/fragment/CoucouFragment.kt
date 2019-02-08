package com.desrumaux.androidtoolbox.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.desrumaux.androidtoolbox.R


/**
 * A simple [Fragment] subclass.
 *
 */
class CoucouFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coucou, container, false)
    }


}
