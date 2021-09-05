package com.ldnhat.demosearchmap.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.databinding.ActivitySearchBinding
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity(), UpdateCountry {

    private val viewModel by lazy{
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private lateinit var binding:ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.backClick.observe(this, {
            if (it){
                onBackPressed()
                viewModel.onBackCliked()
            }
        })


        showCountryDetail()
        handleStateTextInput()

        viewModel.stateButton.observe(this, {
            if (it){
                binding.btnSearch.setOnClickListener {
                    val intent = Intent()
                    val bundle = Bundle()
                    bundle.putParcelable("PROVINCE_VALUE", viewModel.provinceCountryDetail.value)
                    bundle.putParcelable("DISTRICT_VALUE", viewModel.districtCountryDetail.value)
                    bundle.putParcelable("SUBDISTRICT_VALUE", viewModel.subDistrictCountryDetail.value)
                    intent.putExtra("BUNDLE", bundle)
                    setResult(33, intent)
                    finish()
                }
            }
            viewModel.handleStateButton()
        })
    }

    private fun showCountryDetail(){
        binding.textProvince.setOnClickListener {
            val fragmentCountry = FragmentCountry()

            val bundle = Bundle()
            bundle.putParcelable("TYPE", Type.PROVINCE)
            fragmentCountry.arguments = bundle
            fragmentCountry.show(supportFragmentManager, null)
        }

        viewModel.stateTextInputDistrict.observe(this, {
            if (it){
                binding.textDistrict.setOnClickListener {
                    val fragmentCountry = FragmentCountry()

                    val bundle = Bundle()
                    bundle.putParcelable("TYPE", Type.DISTRICT)
                    bundle.putString("CODE", viewModel.provinceCode.value)
                    fragmentCountry.arguments = bundle
                    fragmentCountry.show(supportFragmentManager, null)
                }
            }
        })

        viewModel.stateTextInputSubDistrict.observe(this, {
            if (it){
                binding.textSubDistrict.setOnClickListener {
                    val fragmentCountry = FragmentCountry()

                    val bundle = Bundle()
                    bundle.putParcelable("TYPE", Type.SUBDISTRICT)
                    bundle.putString("CODE", viewModel.districtCode.value)
                    fragmentCountry.arguments = bundle
                    fragmentCountry.show(supportFragmentManager, null)
                }
            }

        })
    }

    private fun handleStateTextInput(){
        viewModel.provinceCountryDetail.observe(this, {
            if (it == null){
                viewModel.onStateTextInputProvinceVisible()
                viewModel.onStateTextInputDistrictDisable()
                viewModel.onStateTextInputSubDistrictDisable()
            }else{
                viewModel.onStateTextInputProvinceVisible()
                viewModel.onStateTextInputDistrictVisible()
                viewModel.onStateTextInputSubDistrictDisable()
            }
        })

        viewModel.districtCountryDetail.observe(this, {
            if (it != null) {
                viewModel.onStateTextInputSubDistrictVisible()
            } else {
                viewModel.onStateTextInputSubDistrictDisable()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        shutdown(0, Intent())
    }

    private fun shutdown(i : Int, intent : Intent){
        setResult(i, intent)
        finish()
    }

    override fun dataListener(type: Type, countryDetail: CountryDetail) {
        viewModel.handleCountryDetail(type, countryDetail)
    }
}