package com.example.diabetter.view.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diabetter.R
import com.example.diabetter.databinding.FragmentHomeBinding
import com.example.diabetter.databinding.TodayMenuBinding
import com.example.diabetter.utils.StatusBar
import com.example.diabetter.view.detail_menu.DetailMenuActivity
import com.example.diabetter.adapter.RecommendationMenuAdapter
import com.example.diabetter.adapter.setupRecyclerView
import com.example.diabetter.data.MenuData
import com.example.diabetter.databinding.OtherFoodBinding
import com.example.diabetter.view.detail_food.DetailFoodActivity
import com.example.diabetter.view.main.RefreshFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var menuTodayMenuBinding: TodayMenuBinding
    private lateinit var otherFoodBinding : OtherFoodBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = binding

        menuTodayMenuBinding = binding.menuToday
        otherFoodBinding = binding.otherFood

        StatusBar.addStatusBarMargin(requireActivity(), binding.tvHello, 32)
        StatusBar.addStatusBarMargin(requireActivity(), binding.ivSave, 32)

        binding.rvRecommendationMenu.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendationMenu.adapter = RecommendationMenuAdapter(5)

        menuTodayMenuBinding.apply {
            listOf(tvSeeDetail, tvDetailMenuToday).forEach { tv ->
                tv.setOnClickListener {
                    val intent = Intent(requireContext(), DetailMenuActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        val menuDataList = listOf(
            MenuData(10001.0),
            MenuData(10002.0),
            MenuData(10003.0),
            MenuData(10004.0),
            MenuData(10005.0),
            MenuData(10006.0),
        )

        val menuViews = listOf(
            otherFoodBinding.menu1,
            otherFoodBinding.menu2,
            otherFoodBinding.menu3,
            otherFoodBinding.menu4,
            otherFoodBinding.menu5,
            otherFoodBinding.menu6
        )

        menuViews.forEachIndexed { index, menuView ->
            menuView.setOnClickListener {
                val intent = Intent(requireContext(), DetailFoodActivity::class.java).apply {
                    putExtra("menu_data", menuDataList[index])
                }
                startActivity(intent)
            }
        }

        binding.tvRefresh2.setOnClickListener {
            val showPopUpRefresh = RefreshFragment()
            showPopUpRefresh.show((activity as AppCompatActivity).supportFragmentManager, "RefreshFragment")
        }

        setupRecyclerView(binding.rvRecommendationMenu, MENU_RECOMMENDATION_COUNT) { mostVisibleItemPosition ->
            binding.apply {
                circle1.setImageResource(
                    if (mostVisibleItemPosition == 0) R.drawable.active_circle
                    else R.drawable.inactive_circle
                )
                circle2.setImageResource(
                    if (mostVisibleItemPosition == 1) R.drawable.active_circle
                    else R.drawable.inactive_circle
                )
                circle3.setImageResource(
                    if (mostVisibleItemPosition == 2) R.drawable.active_circle
                    else R.drawable.inactive_circle
                )
                circle4.setImageResource(
                    if (mostVisibleItemPosition == 3) R.drawable.active_circle
                    else R.drawable.inactive_circle
                )
                circle5.setImageResource(
                    if (mostVisibleItemPosition == 4) R.drawable.active_circle
                    else R.drawable.inactive_circle
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("Testt", "Destroy")
    }

    companion object {
        private const val MENU_RECOMMENDATION_COUNT = 5
    }
}