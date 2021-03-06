package com.vald3nir.my_nanny.presentation.register

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.vald3nir.my_nanny.R
import com.vald3nir.my_nanny.common.core.BaseActivity
import com.vald3nir.my_nanny.common.extensions.afterTextChanged
import com.vald3nir.my_nanny.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity() {

    private val viewModel: RegisterViewModel by viewModel()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.view = this
        setupObservers()
    }

    private fun setupObservers() {

        binding.apply {

            toolbar.apply {
                title.text = getString(R.string.register)
                btnBack.setOnClickListener { onBackPressed() }
            }

            btnRegister.setOnClickListener { registerNewUser() }

            edtEmail.afterTextChanged { registerDataChanged() }

            edtPassword.apply { afterTextChanged { registerDataChanged() } }

            edtConfirmPassword.apply {
                afterTextChanged { registerDataChanged() }
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            registerNewUser()
                    }
                    false
                }
            }
        }

        viewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val loginState = it ?: return@Observer
            binding.btnRegister.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.edtEmail.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                binding.edtPassword.error = getString(loginState.passwordError)
            }
            if (loginState.passwordNotEquals != null) {
                binding.edtConfirmPassword.error = getString(loginState.passwordNotEquals)
            }
        })
    }

    private fun registerDataChanged() {
        binding.apply {
            viewModel.registerDataChanged(
                edtEmail.text.toString(),
                edtPassword.text.toString(),
                edtConfirmPassword.text.toString()
            )
        }
    }

    private fun registerNewUser() {
        binding.apply {
            viewModel.registerNewUser(
                email = edtEmail.text.toString(),
                password = edtPassword.text.toString()
            )
        }
    }
}
