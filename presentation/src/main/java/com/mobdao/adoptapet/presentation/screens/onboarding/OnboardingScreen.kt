package com.mobdao.adoptapet.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.AddressSelected
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.NextClicked

@Composable
fun OnboardingScreen(
    onNavAction: (OnboardingNavAction) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

    UiContent(
        uiState = uiState,
        onUiAction = viewModel::onUiAction,
    )
}

@Composable
private fun UiContent(
    uiState: OnboardingUiState,
    onUiAction: (OnboardingUiAction) -> Unit = {},
) {
    ConstraintLayout(
        modifier =
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
    ) {
        val (welcomeTextRef, searchBarRef, imageRef, nextButtonRef) = createRefs()

        Text(
            text = "Welcome to Adopt a Pet.\nPlease enter a location so we can improve our search for pets.",
            modifier =
                Modifier.constrainAs(welcomeTextRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top, margin = 54.dp)
                },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        LocationSearchBar(
            modifier =
                Modifier.constrainAs(searchBarRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(welcomeTextRef.bottom, margin = 54.dp)
                },
            initialAddress = uiState.selectedAddress,
            onAddressSelected = { onUiAction(AddressSelected(it)) },
            onError = { throwable -> onUiAction(FailedToGetAddress(throwable)) },
        )
        Image(
            painter = painterResource(R.drawable.onboarding_image),
            contentDescription = "",
            modifier =
                Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
                    .constrainAs(imageRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(searchBarRef.top, margin = 100.dp)
                    },
            contentScale = ContentScale.FillWidth,
        )
        Button(
            onClick = { onUiAction(NextClicked) },
            modifier =
                Modifier
                    .padding(bottom = 16.dp)
                    .constrainAs(nextButtonRef) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom)
                    },
            enabled = uiState.nextButtonIsEnabled,
        ) {
            Text(text = stringResource(R.string.next))
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = { onUiAction(DismissGenericErrorDialog) })
    }
}

@Preview(showBackground = true)
@Composable
private fun UiContentPreview() {
    AdoptAPetTheme {
        UiContent(
            uiState = OnboardingUiState(),
        )
    }
}
