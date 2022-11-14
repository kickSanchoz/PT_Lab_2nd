package ru.pt_lab_2nd.android.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.repository.ProductRepository
import ru.pt_lab_2nd.android.utils.ItemOffsetDecoration
import ru.pt_lab_2nd.android.utils.Resource
import ru.pt_lab_2nd.android.utils.getPlaceholder
import ru.pt_lab_2nd.databinding.ActivityMainBinding
import ru.pt_lab_2nd.databinding.ItemProductBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var productRepository: ProductRepository

    private var mViewBinding: ActivityMainBinding? = null
    val binding get() = mViewBinding!!

    private val job = SupervisorJob()

    private var purchaseDialogFragment: PurchaseDialogFragment? = null

    private val productAdapter = ProductAdapter()

    private val productsInsertStatus = MutableLiveData<Resource<Unit>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setupView()
        observeView()
        observeData()
        setContentView(binding.root)
    }

    private fun setupView() {
        binding.rvProducts.addItemDecoration(
            ItemOffsetDecoration(
                binding.root.context,
                R.dimen.item_offset
            )
        )
        binding.rvProducts.adapter = productAdapter
    }

    private fun observeView() {
        binding.btnDbToDefault.setOnClickListener {
            productsInsertStatus.value = Resource.loading()

            CoroutineScope(job).launch(IO) {
                val productList = mutableListOf(
                    Product(
                        id = 1,
                        url = "https://www.pngrepo.com/png/21590/180/chair.png",
                        name = "Стул",
                        price = 100,
                        count = 10,
                    ),
                    Product(
                        id = 2,
                        url = "https://www.pngrepo.com/png/164217/180/table.png",
                        name = "Стол",
                        price = 150,
                        count = 3,
                    ),
                    Product(
                        id = 3,
                        url = "https://www.pngrepo.com/png/8313/180/couch.png",
                        name = "Диван",
                        price = 200,
                        count = 1,
                    ),
                )
                val res = productRepository.insertAllProducts(productList)
                productsInsertStatus.postValue(res)
            }

        }
    }

    private fun observeData() {
        productRepository.getAllProducts().observe(this) {
            Log.e("products", "$it")
            if (!it.isNullOrEmpty()) {
                productAdapter.clear()
                productAdapter.addAll(it)
            }
        }
    }

    inner class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
        private var productList = mutableListOf<Product>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ProductViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bind(productList[position])
        }

        override fun getItemCount(): Int = productList.size

        fun clear() {
            productList.clear()
            notifyDataSetChanged()
        }

        fun addAll(products: List<Product>) {
            productList.addAll(products)
            notifyDataSetChanged()
        }

        inner class ProductViewHolder(private val viewBinding: ItemProductBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {
            fun bind(product: Product) {
                viewBinding.apply {
                    ivPicture.load(product.url) {
                        crossfade(true)
                        placeholder(getPlaceholder(root.context))
                        scale(Scale.FIT)
                        error(R.color.grey)
                    }
                    tvName.text = product.name
                    tvPrice.text = getString(R.string.Price_string, product.price.toString())
                    tvCount.text =
                        getString(R.string.Remaining_with_colon_string, product.count.toString())

                    root.setOnClickListener {
                        when {
                            purchaseDialogFragment?.isVisible != true && product.count > 0 -> {
                                purchaseDialogFragment = PurchaseDialogFragment().apply {
                                    arguments = PurchaseDialogFragment.getBundle(product)
                                }
                                purchaseDialogFragment?.show(supportFragmentManager, null)
                            }
                            product.count <= 0 -> {
                                Snackbar
                                    .make(root, "Товар закончился", Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}