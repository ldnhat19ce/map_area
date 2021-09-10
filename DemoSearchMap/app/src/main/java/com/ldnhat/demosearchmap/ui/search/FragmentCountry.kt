package com.ldnhat.demosearchmap.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.adapter.CountryAdapter
import com.ldnhat.demosearchmap.adapter.CountryListener
import com.ldnhat.demosearchmap.databinding.FragmentSearchBinding
import com.ldnhat.demosearchmap.viewmodel.CountryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCountry : DialogFragment() {

    private val viewModel : CountryViewModel by viewModel()

    private lateinit var binding:FragmentSearchBinding
    private lateinit var type : Type
    private lateinit var updateCountry:UpdateCountry
    private var code: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments

        updateCountry = activity as UpdateCountry

        bundle?.let {
            type = it.getParcelable("TYPE")!!
            code = it.getString("CODE").toString()
            println("code: "+code)
            //viewModel.setType(type)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setType(type, code)

        handleAdapter()
        handleTitle()

        viewModel.closeButtonState.observe(viewLifecycleOwner, {
            if (it){
                dismiss()
                viewModel.onCloseButtonClickSuccess()
            }
        })

    }

    private fun handleAdapter(){
        val adapter = CountryAdapter(CountryListener {
            when(it.type){
                Type.PROVINCE.type -> {
                    updateCountry.dataListener(Type.PROVINCE, it)
                    dismiss()
                }
                Type.DISTRICT.type -> {
                    updateCountry.dataListener(Type.DISTRICT, it)
                    dismiss()
                }
                Type.SUBDISTRICT.type -> {
                    updateCountry.dataListener(Type.SUBDISTRICT, it)
                    dismiss()
                }
            }
        })

        binding.rvSearch.adapter = adapter

        viewModel.countryDetailSearch.observe(viewLifecycleOwner, {
            if (it != null){
                adapter.submitList(it)
            }
        })
    }



    private fun handleTitle(){
        viewModel.type.observe(viewLifecycleOwner, {

            val title: String = when (it.type) {
                Type.PROVINCE.type -> {
                    getString(R.string.choose_province)
                }
                Type.DISTRICT.type -> {
                    getString(R.string.choose_district)
                }
                Type.SUBDISTRICT.type -> {
                    getString(R.string.choose_subdistrict)
                }
                else -> getString(R.string.choose_province)
            }
            // println("title: $title")
            binding.chooseAreaTitle.text = title
        })
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}