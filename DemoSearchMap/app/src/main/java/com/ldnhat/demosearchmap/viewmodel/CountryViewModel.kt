package com.ldnhat.demosearchmap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.repository.CountryRepository
import com.ldnhat.demosearchmap.repository.Resource
import com.ldnhat.demosearchmap.repository.Status
import com.ldnhat.demosearchmap.ui.search.Type
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.apache.commons.lang3.StringUtils

class CountryViewModel(private val repository : CountryRepository) : ViewModel() {

    private val closeButtonClick = PublishSubject.create<Boolean>()

    private val _closeButtonClick = MutableLiveData<Boolean>()

    val closeButtonState:LiveData<Boolean>
    get() = _closeButtonClick

    private val compositeDisposable = CompositeDisposable()

    private var typeOfCountry = PublishSubject.create<Type>()

    private val _countryDetail = MutableLiveData<MutableList<CountryDetail>>()

    val countryDetail:LiveData<MutableList<CountryDetail>>
        get() = _countryDetail

    private val _countryDetailSearch = MutableLiveData<MutableList<CountryDetail>>()

    val countryDetailSearch:LiveData<MutableList<CountryDetail>>
    get() = _countryDetailSearch

    private val _type = MutableLiveData<Type>()

    val type:LiveData<Type>
    get() = _type

    private val _provinceCode = MutableLiveData<String>()
    private val _districtCode = MutableLiveData<String>()
    val rxSearchCountryDetail = BehaviorSubject.create<CharSequence>()

    private val rxLoadingState = PublishSubject.create<Boolean>()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState:LiveData<Boolean>
    get() = _loadingState

    private val rxSuccessState = PublishSubject.create<Boolean>()

    private val _successState = MutableLiveData<Boolean>()
    val successState:LiveData<Boolean>
        get() = _successState

    private val rxErrorState = PublishSubject.create<String>()

    private val _errorState = MutableLiveData<String>()
    val errorState:LiveData<String>
        get() = _errorState

    init {
        handleTypeOfCountry()
        onCloseButtonClick()
        addSearchCountryDetail()
        addLoadingState()
        addErrorState()
        addSuccessState()
        _loadingState.value = false
    }

    private fun addLoadingState(){
        rxLoadingState.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _loadingState.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addErrorState(){
        rxErrorState.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _errorState.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addSuccessState(){
        rxSuccessState.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _successState.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addSearchCountryDetail(){
        rxSearchCountryDetail.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                println(it)
                filterArea(it)
            }.addTo(compositeDisposable)
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


    private fun filterArea(keyword : CharSequence){
        val areaFilter:MutableList<CountryDetail> = ArrayList()

        if (keyword.isNotBlank()){
            countryDetail.value?.let {
                for (countryDetail : CountryDetail in it){
                    val stringAccent:String = StringUtils.stripAccents(countryDetail.name)
                    if (StringUtils.contains(StringUtils.toRootLowerCase(stringAccent), StringUtils.toRootLowerCase(keyword.toString()))
                        || StringUtils.contains(StringUtils.toRootLowerCase(countryDetail.name), StringUtils.toRootLowerCase(keyword.toString()))
                    ){
                        areaFilter.add(countryDetail)
                    }
                }
            }
        }else{
            countryDetail.value?.let { areaFilter.addAll(it) }
        }
        _countryDetailSearch.postValue(areaFilter)

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
        handleState(resource)
        val countryDetail: ApiResponse<MutableList<CountryDetail>>? = resource.data
        println("status: "+resource.status)

        if (countryDetail?.result != null){
            _countryDetail.value = countryDetail.result
            _countryDetailSearch.value = countryDetail.result
        }else{
            _countryDetail.value = null
        }
    }

    private fun getAllDistrict(provinceCode : String){
        repository.findAllDistrict(compositeDisposable, provinceCode).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleDistrict(it)
            }.addTo(compositeDisposable)
    }

    private fun handleDistrict(resource: Resource<ApiResponse<MutableList<CountryDetail>>>){
        handleState(resource)
        val districts:ApiResponse<MutableList<CountryDetail>>? = resource.data
        if (districts?.result != null){
            _countryDetail.value = districts.result
            _countryDetailSearch.value = districts.result
        }else{
            _countryDetail.value = null
        }
    }

    private fun getAllSubDistrict(districtCode : String){
        repository.findAllSubDistrict(compositeDisposable, districtCode).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleSubDistrict(it)
            }.addTo(compositeDisposable)
    }

    private fun handleSubDistrict(resource: Resource<ApiResponse<MutableList<CountryDetail>>>){
        handleState(resource)
        val subDistricts:ApiResponse<MutableList<CountryDetail>>? = resource.data
        if (subDistricts?.result != null){
            _countryDetail.value = subDistricts.result
            _countryDetailSearch.value = subDistricts.result
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
        when (type) {
            Type.PROVINCE.type -> {
                this.getAllProvince()
            }
            Type.DISTRICT.type -> {
                _provinceCode.value?.let { getAllDistrict(it) }
            }
            Type.SUBDISTRICT.type -> {
                _districtCode.value?.let {
                    getAllSubDistrict(it)
                }
            }
        }
    }

    private fun handleState(resource: Resource<ApiResponse<MutableList<CountryDetail>>>){
        when (resource.status) {
            Status.LOADING -> {
                rxLoadingState.onNext(true)
                rxErrorState.onNext("")
                rxSuccessState.onNext(false)
            }
            Status.SUCCESS -> {
                rxLoadingState.onNext(false)
                rxErrorState.onNext("")
                rxSuccessState.onNext(true)
            }
            Status.ERROR -> {
                rxLoadingState.onNext(false)
                rxErrorState.onNext(resource.message)
                rxSuccessState.onNext(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}