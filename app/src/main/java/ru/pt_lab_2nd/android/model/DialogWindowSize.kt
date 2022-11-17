package ru.pt_lab_2nd.android.model

import android.view.ViewGroup
import androidx.annotation.FloatRange
import ru.pt_lab_2nd.android.MainApplication

sealed class DialogWindowSize {
    abstract val value: Int

    /**
     * @param scale- значение от 0f до 1f, указывает размер в процентном соотношении от размера экрана по указанной оси
     * */
    class SpecifiedWidth(
        @FloatRange(from = 0.0, to = 1.0)
        private val scale: Double
    ) : DialogWindowSize() {
        override val value = (MainApplication.res.displayMetrics.widthPixels * scale).toInt()
    }

    /**
     * @param scale- значение от 0f до 1f, указывает размер в процентном соотношении от размера экрана по указанной оси
     * */
    class SpecifiedHeight(
        @FloatRange(from = 0.0, to = 1.0)
        private val scale: Double
    ) : DialogWindowSize() {
        override val value = (MainApplication.res.displayMetrics.heightPixels * scale).toInt()
    }

    object WrapContent : DialogWindowSize() {
        override val value = ViewGroup.LayoutParams.WRAP_CONTENT
    }
    object MatchParent : DialogWindowSize() {
        override val value = ViewGroup.LayoutParams.MATCH_PARENT
    }
}
