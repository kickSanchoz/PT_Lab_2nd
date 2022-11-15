package ru.pt_lab_2nd.android.controller

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.lifecycle.MutableLiveData
import coil.load
import coil.size.Scale
import kotlinx.coroutines.SupervisorJob
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.base.BaseDialogFragment
import ru.pt_lab_2nd.android.model.DialogWindowSize
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.Purchase
import ru.pt_lab_2nd.android.model.repository.PurchaseRepository
import ru.pt_lab_2nd.android.utils.Resource
import ru.pt_lab_2nd.android.utils.getPlaceholder
import ru.pt_lab_2nd.databinding.DialogPurchaseBinding
import javax.inject.Inject

class PurchaseDialogFragment2 : BaseDialogFragment<DialogPurchaseBinding>() {

    init {
        configurator.apply {
            layout = R.layout.dialog_purchase
            gravity = Gravity.BOTTOM
            width = DialogWindowSize.MatchParent
            height = DialogWindowSize.Specified(0.75f)
        }
    }

    @Inject
    lateinit var purchaseRepository: PurchaseRepository

    private val job = SupervisorJob()

    private var product: Product? = null

    private lateinit var purchase: MutableLiveData<Purchase>

    private val purchaseStatus = MutableLiveData<Resource<Unit>>()

    override fun parseArguments() {
        arguments?.let {
            product = it.getParcelable(KEY_PRODUCT)
            product?.let { p ->
                purchase = MutableLiveData(
                    Purchase(
                        product = p,
                    )
                )
            }

        }
    }

    override fun observeData() {
        binding.apply {
            product?.let { p ->
                ivPicture.load(p.url) {
                    crossfade(true)
                    placeholder(getPlaceholder(root.context))
                    scale(Scale.FIT)
                    error(R.color.grey)
                }
                tvName.text = p.name
                tvPrice.text = getString(R.string.Price_string, p.price.toString())
            }
        }

        purchase.observe(viewLifecycleOwner) {
            binding.btnBuy.isEnabled = it.isValid()
        }

        purchaseStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.e("purchaseStatus", "Всё ок")
                    dismiss()
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    it.errorBody?.message?.let { err ->
                        Log.e("Error", err)
                    } ?: run {
                        throw IllegalStateException("Ошибка добавления в БД")
                    }
                }
                Resource.Status.NETWORK_ERROR -> {

                }
            }
        }
    }

    companion object {
        private const val TAG = "PurchaseDialogFragment"
        const val RESULT_REQUEST_KEY = "request.purchase"

        /**
         * Получить bundle, необходимый для открытия диалоговой модалки
         * @param product приобретаемый товар
         * */
        fun getBundle(product: Product): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PRODUCT, product)
            }
        }

        private const val KEY_PRODUCT = "FnsDifferentData"
    }
}