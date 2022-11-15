package ru.pt_lab_2nd.android.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.model.DialogWindowSize

open class BaseDialogFragment<VB: ViewDataBinding> : DialogFragment() {
    private var _binding: VB? = null
    val binding
        get() = _binding!!

    val configurator = Configurator()

    override fun getTheme(): Int = configurator.style

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    open fun parseArguments() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, configurator.layout!!, container, false)
        setupView()
        observeView()
        observeData()
        return binding.root
    }

    open fun setupView() {

    }

    open fun observeView() {
    }

    open fun observeData() {

    }

    override fun onStart() {
        super.onStart()
        configureDialogFragment() //Настройка диалога должна происходить в onStart, иначе параметры лейаута не применяются
    }

    private fun configureDialogFragment() {
        binding.lifecycleOwner = viewLifecycleOwner

        dialog?.apply {

            window?.setWindowAnimations(R.style.DialogAnimation)
            //TODO если высота лейаута WRAP_CONTENT, при 0 статус бар становится черным и не работает, при любой другой все отрабатывет
            window?.attributes?.y = 1

            window?.setLayout(
                configurator.width.let { w ->
                    when(w) {
                        is DialogWindowSize.MatchParent -> {
                            (w.value * resources.displayMetrics.widthPixels).toInt()
                        }
                        is DialogWindowSize.WrapContent -> {
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        }
                        is DialogWindowSize.Specified -> {
                            (w.value * resources.displayMetrics.widthPixels).toInt()
                        }
                    }
                },
                configurator.height.let { h ->
                    when(h) {
                        is DialogWindowSize.MatchParent -> {
                            (h.value * resources.displayMetrics.heightPixels).toInt()
                        }
                        is DialogWindowSize.WrapContent -> {
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        }
                        is DialogWindowSize.Specified -> {
                            (h.value * resources.displayMetrics.heightPixels).toInt()
                        }
                    }
                }
            )
            window?.setGravity(configurator.gravity)
        }
    }

    //TODO перенести в базовый диалог фрагмент
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        class Configurator {
            var layout: Int? = null

            var gravity: Int = Gravity.BOTTOM

            var style: Int = R.style.DialogFragmentTheme

            var width: DialogWindowSize = DialogWindowSize.MatchParent

            var height: DialogWindowSize = DialogWindowSize.WrapContent
        }
    }
}