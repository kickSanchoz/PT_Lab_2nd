package ru.pt_lab_2nd.android.base

import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.model.DialogWindowSize

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment() {
    private var _binding: VB? = null
    val binding
        get() = _binding!!

    val configurator = Configurator()

    override fun getTheme(): Int = configurator.theme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkConfiguratorSettings()

        parseArguments()
    }

    private fun checkConfiguratorSettings() {
        if (configurator.layout == null) {
            throw IllegalStateException("Configurator: layout value is required")
        }
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

    open fun setupView() {}

    open fun observeView() {}

    open fun observeData() {}

    override fun onStart() {
        super.onStart()
        configureDialogFragment() //Настройка диалога должна происходить в onStart, иначе параметры лейаута не применяются
    }

    private fun configureDialogFragment() {
        binding.lifecycleOwner = viewLifecycleOwner

        dialog?.apply {
            isCancelable = configurator.isCancelable
            setCanceledOnTouchOutside(configurator.isCloseableOnTouchOutside)

            window?.apply {
                /*
                * Флаг позволяет всегда отрисовывать фон статус бара и затемняет его, если
                * windowIsFloating == true.
                * Например, при высоте окна MATCH_PARENT статус бар становится полностью чёрным, а с
                * данным флагом затемняется и остаётся полупрозрачным
                * */
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                //Делает статус бар прозрачным и он становится colorPrimary цвета приложения
                statusBarColor = ResourcesCompat.getColor(resources, R.color.transparent, null)

                setWindowAnimations(configurator.animationStyle)
                setLayout(
                    configurator.width.value,
                    configurator.height.value
                )
                setGravity(configurator.gravity)
            } ?: run {
                throw IllegalStateException("Activity window is not visual")
            }
        } ?: run {
            throw IllegalStateException("Dialog is not found")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        class Configurator {

            /** Лейаут для отображения
             *
             * По умолчанию: null
             * */
            var layout: Int? = null

            /** Гравити окна
             *
             * По умолчанию: Gravity.BOTTOM
             * */
            var gravity: Int = Gravity.BOTTOM

            /** Тема диалог фрагмента
             *
             * По умолчанию: R.style.DialogFragmentTheme
             * */
            @StyleRes
            var theme: Int = R.style.DialogFragmentTheme

            /** Ширина окна
             *
             * По умолчанию: DialogWindowSize.MatchParent
             * */
            var width: DialogWindowSize = DialogWindowSize.MatchParent

            /** Высота окна
             *
             * По умолчанию: DialogWindowSize.WrapContent
             * */
            var height: DialogWindowSize = DialogWindowSize.WrapContent

            /** Возможно ли закрыть окно
             *
             * По умолчанию: true
             * */
            var isCancelable: Boolean = true

            /** Возможно ли закрыть касанием вне окна
             *
             * По умолчанию: true
             * */
            var isCloseableOnTouchOutside: Boolean = true

            /** Стиль анимации
             *
             * По умолчанию: R.style.DialogAnimation
             * */
            @StyleRes
            var animationStyle: Int = R.style.DialogAnimation
        }
    }
}