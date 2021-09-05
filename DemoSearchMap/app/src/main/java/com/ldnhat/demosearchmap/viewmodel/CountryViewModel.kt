package com.ldnhat.demosearchmap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.repository.CountryRepository
import com.ldnhat.demosearchmap.repository.CountryRepositoryImpl
import com.ldnhat.demosearchmap.repository.Resource
import com.ldnhat.demosearchmap.ui.search.Type
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception
import kotlin.jvm.internal.Intrinsics

class CountryViewModel : ViewModel() {

    private val closeButtonClick = PublishSubject.create<Boolean>()

    private val _closeButtonClick = MutableLiveData<Boolean>()

    val closeButtonState:LiveData<Boolean>
    get() = _closeButtonClick

    private val compositeDisposable = CompositeDisposable()

    private val repository: CountryRepository = CountryRepositoryImpl()

    private var typeOfCountry = PublishSubject.create<Type>()

    private val _countryDetail = MutableLiveData<MutableList<CountryDetail>>()

    val countryDetail:LiveData<MutableList<CountryDetail>>
        get() = _countryDetail

    private val _type = MutableLiveData<Type>()

    val type:LiveData<Type>
    get() = _type

    private val _provinceCode = MutableLiveData<String>()
    private val _districtCode = MutableLiveData<String>()



    init {
        handleTypeOfCountry()
        onCloseButtonClick()

    }



    private fun onCloseButtonClick(){
        closeButtonClick.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{
                _closeButtonClick.value = it
            }.addTo(compositeDisposable)
    }

    fun onCloseButtonClicked(){
        closeButtonClick.onNext(true)
    }

    fun onCloseButtonClickSuccess(){
        closeButtonClick.onNext(false)

    }

    fun setType(type: Type, code : String){
        if (type == Type.PROVINCE){

        }else if (type == Type.DISTRICT){
            _provinceCode.value = code
        }else if (type == Type.SUBDISTRICT){
            _districtCode.value = code
        }
        typeOfCountry.onNext(type)
    }

    private fun getAllProvince(){
        repository.findAllProvince(compositeDisposable).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleProvince(it)
            }.addTo(compositeDisposable)
    }

    private fun handleProvince(resource : Resource<ApiResponse<MutableList<CountryDetail>>>){
        val countryDetail: ApiResponse<MutableList<CountryDetail>>? = resource.data
        println("status: "+resource.status)
        if (countryDetail?.result != null){
            _countryDetail.value = countryDetail.result
        }else{
            _countryDetail.value = null
        }
    }

    fun getAllDistrict(provinceCode : String){
        repository.findAllDistrict(compositeDisposable, provinceCode).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleDistrict(it)
            }.addTo(compositeDisposable)
    }

    private fun handleDistrict(resource: Resource<ApiResponse<MutableList<CountryDetail>>>){
        val districts:ApiResponse<MutableList<CountryDetail>>? = resource.data
        if (districts?.result != null){
            _countryDetail.value = districts.result
        }else{
            _countryDetail.value = null
        }
    }

    fun getAllSubDistrict(districtCode : String){
        repository.findAllSubDistrict(compositeDisposable, districtCode).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleSubDistrict(it)
            }.addTo(compositeDisposable)
    }

    private fun handleSubDistrict(resource: Resource<ApiResponse<MutableList<CountryDetail>>>){
        val subDistricts:ApiResponse<MutableList<CountryDetail>>? = resource.data
        if (subDistricts?.result != null){
            _countryDetail.value = subDistricts.result
        }else{
            _countryDetail.value = null
        }
    }

    private fun handleTypeOfCountry(){
        try {
            typeOfCountry.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    //println("type: $it")
                    _type.value = it
                    handleTypeOfCountry(it.type)
                }.addTo(compositeDisposable)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun handleTypeOfCountry(type: String){
        if (type == Type.PROVINCE.type){
            this.getAllProvince()
        }else if (type == Type.DISTRICT.type){
            _provinceCode.value?.let { getAllDistrict(it) }
        }else if (type == Type.SUBDISTRICT.type){
            _districtCode.value?.let {
                getAllSubDistrict(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}