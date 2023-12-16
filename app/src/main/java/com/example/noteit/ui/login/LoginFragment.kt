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
import com.example.noteit.databinding.FragmentLoginBinding
import com.example.noteit.models.UserRequest
import com.example.noteit.utils.Helper
import com.example.noteit.utils.NetworkResult
import com.example.noteit.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding?=null
    val binding get() = _binding!!
    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
   lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater,container,false)

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
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            val user = getUserRequest()
            val validateResponses = validateResponses()
             if (validateResponses.first){
                 authViewModel.loginUser(user)
             }else{
                showValidationError(validateResponses.second)
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
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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

    private fun getUserRequest(): UserRequest {
      return binding.run {
          UserRequest(
              "",
              txtEmail.text.toString(),
              txtPassword.text.toString()
          )
      }
    }

    fun showValidationError(error:String){
         binding.txtError.text = error
    }


    private fun validateResponses(): Pair<Boolean,String> {
     val email:String = binding.txtEmail.text.toString()
     val password:String = binding.txtPassword.text.toString()
      return authViewModel.validateResponse("",email,password,true)
    }

}