package realapps.live.gundasupermarket.stockRequestModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import realapps.live.gundasupermarket.databinding.ActivityStockRequestsBinding
import realapps.live.gundasupermarket.homeModule.ProductsAdapter
import realapps.live.gundasupermarket.homeModule.modalClass.Product
import realapps.live.gundasupermarket.stockRequestModule.apiFunctions.StockRequestViewModel
import realapps.live.gundasupermarket.stockRequestModule.modalClass.StockRequestData
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions

@AndroidEntryPoint
class StockRequestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockRequestsBinding

    private lateinit var stockRequestViewModel: StockRequestViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockRequestsBinding.inflate(layoutInflater)
        stockRequestViewModel = ViewModelProvider(this)[StockRequestViewModel::class.java]
        setContentView(binding.root)

//        initTabLayout()

        initTabs()
        onCLickListeners()

        getStockRequests()
        observers()
    }


    private fun initTabs() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pending"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Accepted"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Rejected"))

//        binding.tabLayout.setSelectedTabIndicator(R.drawable.tab_bg_normal_blue)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = it.position
                    filterStockRequestsList(position)
//                    UtilFunctions.showToast(applicationContext, "Tab selected: $position")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })
    }


//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun initTabLayout() {
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pending"))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Accepted"))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Rejected"))
//        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL;
//
//        //Set selected tab style
//        setTabBG(R.drawable.tab_bg_normal_blue, R.drawable.tab_bg_normal, R.drawable.tab_bg_normal)
//
//        binding.tabLayout.setSelectedTabIndicator(R.drawable.tab_bg_normal_blue)
////        binding.tabLayout.setSelectedTabIndicatorColor(getColor(R.color.tab_underline_color))
//
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                // Tab selected, perform actions here
//                val tabText = tab?.text
//
//                if (tabText == "Pending") {
//                    setTabBG(
//                        R.drawable.tab_bg_normal_blue,
//                        R.drawable.tab_bg_normal,
//                        R.drawable.tab_bg_normal
//                    )
//
//                    UtilFunctions.showToast(applicationContext,"P")
//                }
//
//                if (tabText == "Accepted") {
//                    setTabBG(
//                        R.drawable.tab_bg_normal,
//                        R.drawable.tab_bg_normal_blue,
//                        R.drawable.tab_bg_normal
//                    )
//
//                    UtilFunctions.showToast(applicationContext,"A")
//
//                }
//
//                if (tabText == "Rejected") {
////                    gotoPending()
//                    setTabBG(
//                        R.drawable.tab_bg_normal,
//                        R.drawable.tab_bg_normal,
//                        R.drawable.tab_bg_normal_blue
//                    )
//
//                    UtilFunctions.showToast(applicationContext,"R")
//
//
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                // Tab unselected, perform actions here
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                // Tab reselected, perform actions here
//            }
//        })
//
//    }
//
//    private fun setTabBG(tab1: Int, tab2: Int, tab3: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            val tabStrip = binding.tabLayout.getChildAt(0) as? ViewGroup
//            val tabView1 = tabStrip?.getChildAt(0)
//            val tabView2 = tabStrip?.getChildAt(1)
//            val tabView3 = tabStrip?.getChildAt(2)
//
//            tabView1?.let {
//                val paddingStart = it.paddingStart
//                val paddingTop = it.paddingTop
//                val paddingEnd = it.paddingEnd
//                val paddingBottom = it.paddingBottom
//                ViewCompat.setBackground(it, AppCompatResources.getDrawable(it.context, tab1))
////                ViewCompat.setPaddingRelative(it, paddingStart, paddingTop, paddingEnd, paddingBottom)
//            }
//
//            tabView2?.let {
//                val paddingStart = it.paddingStart
//                val paddingTop = it.paddingTop
//                val paddingEnd = it.paddingEnd
//                val paddingBottom = it.paddingBottom
//                ViewCompat.setBackground(it, AppCompatResources.getDrawable(it.context, tab2))
////                ViewCompat.setPaddingRelative(it, paddingStart, paddingTop, paddingEnd, paddingBottom)
//            }
//
//            tabView3?.let {
//                val paddingStart = it.paddingStart
//                val paddingTop = it.paddingTop
//                val paddingEnd = it.paddingEnd
//                val paddingBottom = it.paddingBottom
//                ViewCompat.setBackground(it, AppCompatResources.getDrawable(it.context, tab3))
////                ViewCompat.setPaddingRelative(it, paddingStart, paddingTop, paddingEnd, paddingBottom)
//            }
//        }
//    }

    fun onCLickListeners()
    {
        binding.btBack.setOnClickListener {
            finish()
        }
    }

    fun getStockRequests() {
        stockRequestViewModel.getStockRequests()
    }

    fun observers() {
        stockRequestViewModel.getStockRequestsResponse().observe(this) {
            if(it.code!=195)
            {
                if(it.code==200)
                {
                    stockList=it.stockRequestList
                    filterStockRequestsList(0)
//                    initStockRequestsRv(it.stockRequestList)
                }
            }
        }
    }

    private fun initStockRequestsRv(stockRequestList: ArrayList<StockRequestData>) {
        val stockAdapter = StockAdapter(stockRequestList, this, ::onRequestClicked)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = stockAdapter
        }

    }

    fun filterStockRequestsList(requestsStatus: Int)
    {
         val filteredStockList : ArrayList<StockRequestData> = ArrayList()

        for (localItems in stockList) {
            if (localItems.RequestStatus==requestsStatus) {
                filteredStockList.add(localItems)
            }
        }

        initStockRequestsRv(filteredStockList)
    }


    private var stockList : ArrayList<StockRequestData> = ArrayList()


    private fun filteredStockRequestsRv(stockRequestList: ArrayList<StockRequestData>) {
        val stockAdapter = StockAdapter(stockRequestList, this, ::onRequestClicked)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = stockAdapter
        }

    }

    fun onRequestClicked(stockRequestData: StockRequestData)
    {

    }

}