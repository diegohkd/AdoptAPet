package com.mobdao.adoptapet.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.mobdao.adoptapet.screens.onboarding.OnboardingViewModel.NavAction
import com.mobdao.adoptapet.screens.onboarding.OnboardingViewModel.UiState
import com.mobdao.domain.models.Address

@Composable
fun OnboardingScreen(
    onNavAction: (NavAction) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

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
    onAddressSelected: (Address?) -> Unit,
    onFailedToGetAddress: (Throwable?) -> Unit,
    onNextClicked: () -> Unit,
    onDismissGenericErrorDialog: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
    ) {
        val (welcomeTextRef, searchBarRef, imageRef, nextButtonRef) = createRefs()

        Text(
            text = "Welcome to Adopt a Pet.\nPlease enter a location so we can improve our search for pets.",
            modifier = Modifier.constrainAs(welcomeTextRef) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, margin = 54.dp)
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        LocationSearchBar(
            modifier = Modifier.constrainAs(searchBarRef) {
                centerHorizontallyTo(parent)
                top.linkTo(welcomeTextRef.bottom, margin = 54.dp)
            },
            initialAddress = uiState.selectedAddress,
            onAddressSelected = onAddressSelected,
            onError = onFailedToGetAddress,
        )
        Image(
            painter = painterResource(R.drawable.onboarding_image),
            contentDescription = "",
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .constrainAs(imageRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(searchBarRef.top, margin = 100.dp)
                },
            contentScale = ContentScale.FillWidth
        )
        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .constrainAs(nextButtonRef) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                },
            enabled = uiState.nextButtonIsEnabled
        ) {
            Text(text = stringResource(R.string.next))
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}
