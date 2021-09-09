package com.ldnhat.demosearchmap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.ui.search.Type
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class SearchViewModel : ViewModel() {

    private val rxBackClick = PublishSubject.create<Boolean>()

    private val _backClick = MutableLiveData<Boolean>()

    val backClick: LiveData<Boolean>
        get() = _backClick

    private val rxProvinceCountryDetail = PublishSubject.create<CountryDetail>()

    private val _provinceCountryDetail = MutableLiveData<CountryDetail>()

    val provinceCountryDetail:LiveData<CountryDetail>
    get() = _provinceCountryDetail

    private val rxDistrictCountryDetail = PublishSubject.create<CountryDetail>()

    private val _districtCountryDetail = MutableLiveData<CountryDetail>()

    val districtCountryDetail:LiveData<CountryDetail>
        get() = _districtCountryDetail

    private val rxSubDistrictCountryDetail = PublishSubject.create<CountryDetail>()

    private val _subDistrictCountryDetail = MutableLiveData<CountryDetail>()

    val subDistrictCountryDetail:LiveData<CountryDetail>
        get() = _subDistrictCountryDetail

    private val compositeDisposable = CompositeDisposable()

    private val rxStateTextInputProvince = PublishSubject.create<Boolean>()

    private val _stateTextInputProvince = MutableLiveData<Boolean>()

    val stateTextInputProvince:LiveData<Boolean>
    get() = _stateTextInputProvince

    private val rxStateTextInputDistrict = PublishSubject.create<Boolean>()

    private val _stateTextInputDistrict = MutableLiveData<Boolean>()

    val stateTextInputDistrict:LiveData<Boolean>
        get() = _stateTextInputDistrict

    private val rxStateTextInputSubDistrict = PublishSubject.create<Boolean>()

    private val _stateTextInputSubDistrict = MutableLiveData<Boolean>()

    val stateTextInputSubDistrict:LiveData<Boolean>
        get() = _stateTextInputSubDistrict

    private val _provinceCode = MutableLiveData<String>()

    val provinceCode:LiveData<String>
    get() = _provinceCode

    private val _districtCode = MutableLiveData<String>()

    val districtCode:LiveData<String>
    get() = _districtCode

    private val rxStateButton = PublishSubject.create<Boolean>()

    private val _stateButton = MutableLiveData<Boolean>()

    val stateButton:LiveData<Boolean>
    get() = _stateButton



    init {
        backClick()
        addProvinceCountryDetail()
        addDistrictCountryDetail()
        addSubDistrictCountryDetail()
        addStateTextInputProvince()
        addStateTextInputDistrict()
        addStateTextInputSubDistrict()
        _stateButton.value = false
        addStateButton()
    }

    private fun addStateButton(){
        rxStateButton.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateButton.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun handleStateButton(){
        if (provinceCountryDetail.value != null){
            rxStateButton.onNext(true)
        }else{
            rxStateButton.onNext(false)
        }
    }

    private fun backClick(){
        rxBackClick.observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe {
                _backClick.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addProvinceCountryDetail(){
        rxProvinceCountryDetail.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _provinceCountryDetail.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addDistrictCountryDetail(){
        rxDistrictCountryDetail.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _districtCountryDetail.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addSubDistrictCountryDetail(){
        rxSubDistrictCountryDetail.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
            _subDistrictCountryDetail.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addStateTextInputProvince(){
        rxStateTextInputProvince.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateTextInputProvince.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun onStateTextInputProvinceVisible(){
        rxStateTextInputProvince.onNext(true)
    }

    fun onStateTextInputProvinceDisable(){
        rxStateTextInputProvince.onNext(false)
    }

    private fun addStateTextInputDistrict(){
        rxStateTextInputDistrict.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateTextInputDistrict.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun onStateTextInputDistrictVisible(){
        rxStateTextInputDistrict.onNext(true)
    }

    fun onStateTextInputDistrictDisable(){
        rxStateTextInputDistrict.onNext(false)
    }

    private fun addStateTextInputSubDistrict(){
        rxStateTextInputSubDistrict.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateTextInputSubDistrict.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun onStateTextInputSubDistrictVisible(){
        rxStateTextInputSubDistrict.onNext(true)
    }

    fun onStateTextInputSubDistrictDisable(){
        rxStateTextInputSubDistrict.onNext(false)
    }

    fun onBackClick(){
        rxBackClick.onNext(true)
    }

    fun onBackCliked(){
        rxBackClick.onNext(false)
    }

    fun handleCountryDetail(type : Type, countryDetail: CountryDetail){
        if (type == Type.PROVINCE){
            rxProvinceCountryDetail.onNext(countryDetail)
            _districtCountryDetail.postValue(null)
            _subDistrictCountryDetail.postValue(null)
            _provinceCode.postValue(countryDetail.code)
            _districtCode.postValue(null)
        }else if (type == Type.DISTRICT){
            rxDistrictCountryDetail.onNext(countryDetail)
            _subDistrictCountryDetail.postValue(null)
            _districtCode.postValue(countryDetail.code)
        }else if (type == Type.SUBDISTRICT){
            rxSubDistrictCountryDetail.onNext(countryDetail)
        }
    }

    fun getCountryDetail(province : CountryDetail, district : CountryDetail, subDistrict : CountryDetail){
        _provinceCountryDetail.postValue(province)
        _districtCountryDetail.postValue(district)
        _provinceCode.postValue(province.code)
        _subDistrictCountryDetail.postValue(subDistrict)
        _districtCode.postValue(district.code)
    }

    fun clearProvinceCode(){
        _provinceCode.postValue(null)
    }

    fun clearDistrictCode(){
        _districtCode.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}