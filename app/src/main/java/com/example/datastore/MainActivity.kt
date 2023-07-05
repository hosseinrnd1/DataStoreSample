package com.example.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
        val key=binding.keyTIE.text.toString()
        val value=binding.valueTIE.text.toString()

            lifecycleScope.launch {
                save(key,value)
            }

        }


        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value=read(binding.inputKeyTIE.text.toString())
                binding.showValue.text=value ?: "No Value Found"
            }

        }

    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)

        dataStore.edit { setting ->
            setting[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String):String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null;
    }
}