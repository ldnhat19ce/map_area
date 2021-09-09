package com.ldnhat.demosearchmap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.model.CountryDetail
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function3
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
        zip()
        onButtonSearchClick()
        _textArea.value = app.getString(R.string.search_title)

    }
    
    private fun zip(){
        Observable.zip(rxProvince, rxDistrict, rxSubDistrict) {
            zProvince, zDistrict, zSubDistrict ->
            if (zProvince.id != null && zDistrict.id == null && zSubDistrict.id == null){
                "${zProvince.name}"
            }else if (zProvince.id != null && zDistrict.id != null && zSubDistrict.id == null){
                "${zDistrict.name}, ${zProvince.name}"
            }else{
                "${zSubDistrict.name}, ${zDistrict.name}, ${zProvince.name}"
            }
        }.subscribe ({
            println("test: $it")
            _textArea.value = it
        }, {
            it.printStackTrace()
        }).addTo(compositeDisposable)
    }


    fun handleArea(province : CountryDetail, district : CountryDetail, subDistrict : CountryDetail){
        //println("province: "+province.name+" district: "+district.name+" subdistrict: "+subDistrict.name)

        if (province.id != null && district.id == null && subDistrict.id == null){
            rxProvince.onNext(province)
            rxDistrict.onNext(CountryDetail())
            rxSubDistrict.onNext(CountryDetail())
        }else if (province.id != null && district.id != null && subDistrict.id == null){
            rxProvince.onNext(province)
            rxDistrict.onNext(district)
            rxSubDistrict.onNext(CountryDetail())
        }else if (province.id != null && district.id != null && subDistrict.id != null){
            rxProvince.onNext(province)
            rxDistrict.onNext(district)
            rxSubDistrict.onNext(subDistrict)

        }
        _province.value = province
        _district.value = district
        _subDistrict.value = subDistrict

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