package ru.pt_lab_2nd.android.controller

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import coil.load
import coil.size.Scale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.Purchase
import ru.pt_lab_2nd.android.model.repository.PurchaseRepository
import ru.pt_lab_2nd.android.utils.Resource
import ru.pt_lab_2nd.android.utils.getPlaceholder
import ru.pt_lab_2nd.databinding.DialogPurchaseBinding
import javax.inject.Inject

//@AndroidEntryPoint
class PurchaseDialogFragment : DialogFragment() {

    @Inject
    lateinit var purchaseRepository: PurchaseRepository

    private var _binding: DialogPurchaseBinding? = null
    private val binding
        get() = _binding!!

    private val job = SupervisorJob()

    private var product: Product? = null

    private lateinit var purchase: MutableLiveData<Purchase>

    private val purchaseStatus = MutableLiveData<Resource<Unit>>()

    override fun getTheme(): Int = R.style.DialogFragmentTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    private fun parseArguments() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_purchase, container, false)
        setupView()
        observeView()
        observeData()
        return binding.root
    }

    private fun setupView() {

    }

    private fun observeView() {
        binding.apply {
            etEnterName.addTextChangedListener {
                purchase.value?.let { pur ->
                    purchase.value = pur.copy(
                        customer = pur.customer.copy(
                            name = it.toString()
                        )
                    )
                }
            }

            etDeliver.addTextChangedListener {
                purchase.value?.let { pur ->
                    purchase.value = pur.copy(
                        customer = pur.customer.copy(
                            address = it.toString()
                        )
                    )
                }
            }

            btnBuy.setOnClickListener {
                purchaseStatus.value = Resource.loading()

                CoroutineScope(job).launch {
                    purchase.value?.let {
                        val res = purchaseRepository.insertPurchase(it)
                        purchaseStatus.postValue(res)
                    }

                }
            }
        }
    }

    private fun observeData() {
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
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setGravity(Gravity.BOTTOM)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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