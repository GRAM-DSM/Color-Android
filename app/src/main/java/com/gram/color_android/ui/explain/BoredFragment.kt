package com.gram.color_android.ui.explain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gram.color_android.R
import kotlinx.android.synthetic.main.fragment_bored_explain.*

class BoredFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bored_explain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bored_prev_ib.setOnClickListener{
            (activity as ExplainActivity).replaceFragment(SadFragment())
        }
        bored_next_ib.setOnClickListener{
            (activity as ExplainActivity).replaceFragment(LoveFragment())
        }

        bored_close_ib.setOnClickListener{
            (activity as ExplainActivity).finish()
        }
    }
}