package ru.pt_lab_2nd.android.model

data class Customer(
    val name: String? = null,
    val address: String? = null,
) {
    fun isValid(): Boolean {
        return !name.isNullOrEmpty()
                && !address.isNullOrEmpty()
    }
}
