package com.mobdao.adoptapet.screens.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.screens.onboarding.OnboardingViewModel.NavAction.Completed
import com.mobdao.adoptapet.screens.onboarding.OnboardingViewModel.UiState
import com.mobdao.domain.models.Address

@Composable
fun OnboardingScreen(
    onCompleted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    when (navActionEvent?.getContentIfNotHandled()) {
        Completed -> onCompleted()
        null -> {}
    }

    UiContent(
        uiState = uiState,
        onFailedToGetAddress = viewModel::onFailedToGetAddress,
        onAddressSelected = viewModel::onAddressSelected,
        onNextClicked = viewModel::onNextClicked,
        onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog,
    )
}

@Composable
private fun UiContent(
    uiState: UiState,
    onAddressSelected: (Address) -> Unit,
    onFailedToGetAddress: (Throwable?) -> Unit,
    onNextClicked: () -> Unit,
    onDismissGenericErrorDialog: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (welcomeTextRef, searchBarRef, nextButtonRef) = createRefs()

        Text(
            text = "Welcome to Adopt a Pet.\nPlease enter a location so we can improve our search for pets.",
            modifier = Modifier.constrainAs(welcomeTextRef) {
                centerHorizontallyTo(parent)
                linkTo(top = parent.top, bottom = parent.bottom, bias = 0.25f)
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        LocationSearchBar(
            modifier = Modifier.constrainAs(searchBarRef) {
                centerHorizontallyTo(parent)
                linkTo(top = parent.top, bottom = parent.bottom, bias = 0.5f)
            },
            selectedAddress = uiState.selectedAddress,
            paddingHorizontal = 16.dp,
            onAddressSelected = onAddressSelected,
            onError = onFailedToGetAddress,
        )
        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .constrainAs(nextButtonRef) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                },
        ) {
            Text(text = stringResource(R.string.next))
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}