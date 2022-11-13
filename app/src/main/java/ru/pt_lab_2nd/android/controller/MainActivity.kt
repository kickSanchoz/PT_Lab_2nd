package ru.pt_lab_2nd.android.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.repository.ProductRepository
import ru.pt_lab_2nd.android.utils.Resource
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
        binding.rvProducts.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.rvProducts.adapter = productAdapter
    }

    private fun observeView() {
        binding.btnDbToDefault.setOnClickListener {
            productsInsertStatus.value = Resource.loading()

            CoroutineScope(job).launch(IO) {
                val productList = mutableListOf(
                    Product(
                        1,
                        "Стул",
                        100,
                        10,
                    ),
                    Product(
                        2,
                        "Стол",
                        150,
                        3,
                    ),
                    Product(
                        3,
                        "Диван",
                        200,
                        1,
                    ),
                )
                val res = productRepository.insertAllProducts(productList)
                productsInsertStatus.postValue(res)
            }

        }
    }

    private fun observeData() {
        productRepository.getAllProducts().observe(this) {
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
                    tvName.text = product.name
                    tvPrice.text = getString(R.string.Price_string, product.price.toString())
                    tvCount.text = product.count.toString()

                    root.setOnClickListener {
                        tvCount.text = "ы"
                    }
                }
            }
        }
    }
}