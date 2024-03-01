package com.fienna.movieapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.fienna.movieapp.R
import com.fienna.movieapp.databinding.CustomToastBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

object CustomSnackbar {
    @SuppressLint("RestrictedApi")
    fun showSnackBar(context: Context, root: View, text: String){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CustomToastBinding.inflate(inflater)
        val snackBar = Snackbar.make(root, "", Snackbar.LENGTH_INDEFINITE)

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        binding.toastDescription.text = text

        snackbarLayout.apply {
            val lp = layoutParams as FrameLayout.LayoutParams
            lp.gravity = Gravity.TOP
            layoutParams = lp
        }

        snackbarLayout.addView(binding.root,0)

        snackBar.apply {
            view.setBackgroundColor(Color.TRANSPARENT)
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

            val slideDownAnim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            val slideUpAnim = AnimationUtils.loadAnimation(context, R.anim.slide_up)

            view.startAnimation(slideDownAnim)

            slideDownAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view.startAnimation(slideUpAnim)
                }

                override fun onAnimationRepeat(p0: Animation?) {}
            })

            slideUpAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view.isVisible = false
                    dismiss()
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
            show()
        }
    }

    @SuppressLint("RestrictedApi")
    fun showSnackBarWithAction(context: Context, root: View, text: String, action : () -> Unit){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CustomToastBinding.inflate(inflater)
        val snackBar = Snackbar.make(root, "", Snackbar.LENGTH_INDEFINITE)

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        binding.toastDescription.text = text

        snackbarLayout.apply {
            val lp = layoutParams as FrameLayout.LayoutParams
            lp.gravity = Gravity.TOP
            layoutParams = lp
        }

        snackbarLayout.addView(binding.root,0)

        snackBar.apply {
            view.setBackgroundColor(Color.TRANSPARENT)
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

            val slideDownAnim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            val slideUpAnim = AnimationUtils.loadAnimation(context, R.anim.slide_up)

            view.startAnimation(slideDownAnim)

            slideDownAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view.startAnimation(slideUpAnim)
                }

                override fun onAnimationRepeat(p0: Animation?) {}
            })

            slideUpAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view.isVisible = false
                    dismiss()
                    action.invoke()
                }
                override fun onAnimationRepeat(p0: Animation?) {}

            })
            show()
        }
    }

}