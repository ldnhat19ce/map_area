package com.ldnhat.demosearchmap.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.repository.CountryRepository
import com.ldnhat.demosearchmap.repository.CountryRepositoryImpl
import com.ldnhat.demosearchmap.repository.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class HomeViewModel(app: Application) : AndroidViewModel(app){

    private val buttonClicks = PublishSubject.create<Boolean>()

    private val _buttonClick = MutableLiveData<Boolean>()

    val buttonClick:LiveData<Boolean>
    get() = _buttonClick

    private val rxProvince = PublishSubject.create<CountryDetail>()

    private val _province = MutableLiveData<CountryDetail>()

    val province:LiveData<CountryDetail>
    get() = _province

    private val rxDistrict = PublishSubject.create<CountryDetail>()

    private val _district = MutableLiveData<CountryDetail>()

    val district:LiveData<CountryDetail>
        get() = _district

    private val rxSubDistrict = PublishSubject.create<CountryDetail>()

    private val _subDistrict = MutableLiveData<CountryDetail>()

    val subDistrict:LiveData<CountryDetail>
        get() = _subDistrict

    private val _textArea = MutableLiveData<String>()

    val textArea:LiveData<String>
    get() = _textArea

    private val compositeDisposable = CompositeDisposable()


    init {
        onButtonSearchClick()
        addProvinceState()
        addDistrictState()
        addSubDistrictState()
        _textArea.value = app.getString(R.string.search_title)
    }

    private fun addProvinceState(){
        rxProvince.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                println(it.name)
                _province.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addDistrictState(){
        rxDistrict.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _district.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addSubDistrictState(){
        rxSubDistrict.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _subDistrict.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun handleArea(province : CountryDetail, district : CountryDetail, subDistrict : CountryDetail){
        println("province: "+province.name+" district: "+district.name+" subdistrict: "+subDistrict.name)

        if (province.id != null && district.id == null && subDistrict.id == null){
            rxProvince.onNext(province)
            _textArea.value = province.name
        }else if (province.id != null && district.id != null && subDistrict.id == null){
            rxProvince.onNext(province)
            rxDistrict.onNext(district)
            _textArea.value = province.name+", "+district.name
        }else if (province.id != null && district.id != null && subDistrict.id != null){
            rxProvince.onNext(province)
            rxDistrict.onNext(district)
            rxSubDistrict.onNext(subDistrict)
            _textArea.value = province.name+", "+district.name+", "+subDistrict.name
        }
    }

    private fun onButtonSearchClick(){
        buttonClicks.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                onButtonSearchClicked(it)
            }.addTo(compositeDisposable)
    }

    private fun onButtonSearchClicked(boolean: Boolean){
        _buttonClick.value = boolean
    }

    fun onClickToSearch(){
        buttonClicks.onNext(true)
    }

    fun onClickToSearchSuccess(){
        buttonClicks.onNext(false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    enum class Empty{
        VOID
    }
}