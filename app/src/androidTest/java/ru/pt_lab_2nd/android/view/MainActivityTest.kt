package ru.pt_lab_2nd.android.view

import android.content.Context
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle.State
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.pt_lab_2nd.R
import ru.pt_lab_2nd.android.controller.MainActivity
import ru.pt_lab_2nd.android.controller.PurchaseDialogFragment
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.dao.ProductDao
import ru.pt_lab_2nd.android.model.database.AppDatabase
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.model.repository.ProductRepository

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var productDaoDataSource: ProductDaoDataSource
    private lateinit var productRepository: ProductRepository

    private lateinit var productList: List<Product>

    @get: Rule
    public var mActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()
        productDao = db.productDao()
        productDaoDataSource = ProductDaoDataSource(productDao)
        productRepository = ProductRepository(productDaoDataSource)

        productList = listOf(
            Product(
                id = 1,
                name = "Шкаф",
                price = 250,
                count = 32,
            ),
            Product(
                id = 2,
                name = "Тумбочка",
                price = 160,
                count = 634,
            ),
        )
    }

//    @Test
//    fun clickForAddData() = runBlocking {
//        onView(withId(R.id.btn_dbToDefault)).perform(click())
//    }

    @Test
    fun buyButtonDisabled() {
        with(
            launchFragment<PurchaseDialogFragment>(
                initialState = State.STARTED,
                fragmentArgs = PurchaseDialogFragment.getBundle(
                    Product(
                        id = 2,
                        name = "Тумбочка",
                        price = 160,
                        count = 634,
                    )
                )
            )
        ) {
            onFragment {
                moveToState(State.RESUMED)
            }
            onView(withId(R.id.btn_buy)).inRoot(isDialog()).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun buyButtonEnabled() {
        with(
            launchFragment<PurchaseDialogFragment>(
                initialState = State.STARTED,
                fragmentArgs = PurchaseDialogFragment.getBundle(
                    Product(
                        id = 2,
                        name = "Тумбочка",
                        price = 160,
                        count = 634,
                    )
                )
            )
        ) {
            onFragment {
                moveToState(State.RESUMED)
                it.binding.etEnterName.setText("Name")
                it.binding.etDeliver.setText("Address")

            }
            onView(withId(R.id.btn_buy)).inRoot(isDialog()).check(matches(isEnabled()))
        }
    }
}