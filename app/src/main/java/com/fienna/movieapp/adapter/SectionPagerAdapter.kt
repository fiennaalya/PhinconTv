package com.fienna.movieapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fienna.movieapp.view.onboarding.ItemOnboardingFragment

class SectionsPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = ItemOnboardingFragment()
        fragment.arguments = Bundle().apply {
            putInt(ItemOnboardingFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }

}