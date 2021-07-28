package com.gram.color_android.ui.explain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gram.color_android.R
import kotlinx.android.synthetic.main.fragment_angry_explain.*

class AngryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_angry_explain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        angry_next_ib.setOnClickListener{
            (activity as ExplainActivity).replaceFragment(HappyFragment())
        }
        angry_close_ib.setOnClickListener{
            (activity as ExplainActivity).finish()
        }
    }
}