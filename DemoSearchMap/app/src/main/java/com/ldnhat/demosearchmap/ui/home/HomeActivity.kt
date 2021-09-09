package com.ldnhat.demosearchmap.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.databinding.ActivityHomeBinding
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.ui.search.SearchActivity
import com.ldnhat.demosearchmap.viewmodel.HomeViewModel
import com.ldnhat.demosearchmap.viewmodel.HomeViewModelFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {
//
//    private val viewModel by lazy {
//        ViewModelProvider(this, HomeViewModelFactory(this.application)).get(HomeViewModel::class.java)
//    }

    private val viewModel:HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.viewModel = viewModel

        binding.mapView.getMapAsync(this)

        viewModel.buttonClick.observe(this, {
            if (it){
                val intent = Intent(this, SearchActivity::class.java)

                val bundle = Bundle()
                bundle.putParcelable("PROVINCE", viewModel.province.value)
                bundle.putParcelable("DISTRICT", viewModel.district.value)
                bundle.putParcelable("SUBDISTRICT", viewModel.subDistrict.value)
                intent.putExtra("BUNDLE", bundle)
                startActivityForResult(intent, 33)
                viewModel.onClickToSearchSuccess()
            }
        })

        binding.lifecycleOwner = this

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){

            33 -> {
                val bundle = data?.getBundleExtra("BUNDLE")
                val province: CountryDetail? = bundle?.getParcelable("PROVINCE_VALUE")

                var district = CountryDetail()
                if (bundle?.getParcelable<CountryDetail>("DISTRICT_VALUE") != null){
                    district = bundle.getParcelable("DISTRICT_VALUE")!!
                }

                var subDistrict = CountryDetail()
                if (bundle?.getParcelable<CountryDetail>("SUBDISTRICT_VALUE") != null){
                    subDistrict = bundle.getParcelable("SUBDISTRICT_VALUE")!!
                }
                if (province != null) {
                    viewModel.handleArea(province, district, subDistrict)
                }
            }


        }
    }

    override fun onMapReady(map4D: Map4D) {
        map4D.enable3DMode(true)
    }
}