package com.moment.studywithme.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moment.studywithme.R
import com.moment.studywithme.Screen

@Composable
fun SplashScreen(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_loading_hand))
    val logoAnimationState =
        animateLottieCompositionAsState(composition = composition)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth(),
            composition = composition,
            progress = { logoAnimationState.progress }
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Carma",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "An app designed to cultivate discipline and foster a focused zone",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
        navController.navigate(Screen.Home.route) {
            popUpTo(0)
        }

    }
}