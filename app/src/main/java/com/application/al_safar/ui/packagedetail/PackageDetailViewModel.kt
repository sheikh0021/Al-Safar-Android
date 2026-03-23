package com.application.al_safar.ui.packagedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.Package
import com.application.al_safar.domain.usecase.booking.GetPackageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackageDetailViewModel @Inject constructor(
    private val getPackageUseCase: GetPackageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PackageDetailUiState>(PackageDetailUiState.Loading)
    val uiState: StateFlow<PackageDetailUiState> = _uiState

    fun loadPackage(packageId: String) {
        viewModelScope.launch {
            _uiState.value = PackageDetailUiState.Loading
            getPackageUseCase(packageId.toIntOrNull() ?: 0)
                .onSuccess { pkg -> _uiState.value = PackageDetailUiState.Success(pkg) }
                .onFailure { error ->
                    _uiState.value =
                        PackageDetailUiState.Error(error.message ?: "Failed to load package")
                }
        }
    }
}

sealed class PackageDetailUiState {
    object Loading : PackageDetailUiState()
    data class Success(val umrahPackage: Package) : PackageDetailUiState()
    data class Error(val message: String) : PackageDetailUiState()
}
