package com.example.noteit.ui.login

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.noteit.R
import com.example.noteit.databinding.FragmentRegisterBinding
import com.example.noteit.models.UserRequest
import com.example.noteit.utils.Helper
import com.example.noteit.utils.NetworkResult
import com.example.noteit.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding:FragmentRegisterBinding?= null
    private val binding get()=_binding!!
    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        if (tokenManager.getTokens()!=null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textLogin = binding.tvLogin
        textLogin.setOnClickListener {
           it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val user = getUserRequest()
            val validateResponse =validateResponses()
            if (validateResponse.first){
                authViewModel.registerUser(user)
            }else{
                showValidationError(validateResponse.second)
            }
        }
        bindObserver()
    }

    private fun bindObserver() {
        authViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Helper.dismissProgressBar()
                      tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    Helper.dismissProgressBar()
                    showValidationError(it.message.toString())
                }

                is NetworkResult.Loading -> {
                   Helper.showCustomProgressBar(requireActivity())
                 }

            }
        }
    }

    private fun showValidationError(error:String) {
        binding.txtError.text = error
    }

    private fun validateResponses():Pair<Boolean,String> {
        val username:String = binding.txtUsername.text.toString()
        val email:String = binding.txtEmail.text.toString()
        val password:String = binding.txtPassword.text.toString()
       return authViewModel.validateResponse(username,email,password,false)
    }
    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(
                txtUsername.text.toString(),
                txtEmail.text.toString(),
                txtPassword.text.toString()
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}