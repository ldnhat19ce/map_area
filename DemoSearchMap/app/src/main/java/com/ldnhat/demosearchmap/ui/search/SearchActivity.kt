package com.ldnhat.demosearchmap.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.databinding.ActivitySearchBinding
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), UpdateCountry {

//    private val viewModel by lazy{
//        ViewModelProvider(this).get(SearchViewModel::class.java)
//    }
    private val viewModel: SearchViewModel by viewModel()

    private lateinit var binding:ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        getDataFromHome()
        handleStateTextInput()

        viewModel.backClick.observe(this, {
            if (it){
                onBackPressed()
                viewModel.onBackCliked()
            }
        })

        showCountryDetail()


        viewModel.stateButton.observe(this, {
            if (it){
                binding.btnSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
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
            }else{
                binding.btnSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.dimgray))
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
            println("state district: "+it)
            if (it) {
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
            println("state subdistrict: " + it)
            if (it) {
                binding.textSubDistrict.isEnabled = true
                binding.textSubDistrict.setOnClickListener {
                    val fragmentCountry = FragmentCountry()

                    val bundle = Bundle()
                    bundle.putParcelable("TYPE", Type.SUBDISTRICT)
                    bundle.putString("CODE", viewModel.districtCode.value)
                    fragmentCountry.arguments = bundle
                    fragmentCountry.show(supportFragmentManager, null)

                }
            } else {
                binding.textSubDistrict.isEnabled = false
                println("state disble")
            }

        })
    }

    private fun handleStateTextInput(){

        viewModel.provinceCode.observe(this, {
            if (it.isNullOrEmpty()){
                viewModel.onStateTextInputProvinceVisible()
                viewModel.onStateTextInputDistrictDisable()
                viewModel.onStateTextInputSubDistrictDisable()
            }else{
                viewModel.onStateTextInputProvinceVisible()
                viewModel.onStateTextInputDistrictVisible()
                viewModel.onStateTextInputSubDistrictDisable()
            }
        })

        viewModel.districtCode.observe(this, {
            if (it.isNullOrEmpty()) {
                viewModel.onStateTextInputSubDistrictDisable()
            } else {
                viewModel.onStateTextInputSubDistrictVisible()
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

    private fun getDataFromHome(){

        val intent = intent

        val bundle = intent?.getBundleExtra("BUNDLE")
        val province: CountryDetail? = bundle?.getParcelable("PROVINCE")

        var district = CountryDetail()
        if (bundle?.getParcelable<CountryDetail>("DISTRICT") != null){
            district = bundle.getParcelable("DISTRICT")!!
        }

        var subDistrict = CountryDetail()
        if (bundle?.getParcelable<CountryDetail>("SUBDISTRICT") != null){
            subDistrict = bundle.getParcelable("SUBDISTRICT")!!
        }
        if (province != null) {
            viewModel.getCountryDetail(province, district, subDistrict)
        }
    }
}