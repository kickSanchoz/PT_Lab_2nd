package ru.pt_lab_2nd.android.model

import androidx.annotation.FloatRange

sealed class DialogWindowSize {
    /**
     * @param value- значение от 0f до 1f, указывает размер в процентном соотношении от размера экрана по указанной оси
     * */
    open class Specified(
        @FloatRange(from = 0.0, to = 1.0)
        val value: Float
    ) : DialogWindowSize()

    object WrapContent : DialogWindowSize()
    object MatchParent : Specified(1f)
}
