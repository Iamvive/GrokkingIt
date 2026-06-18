package com.iamvive.grokkingit.effect

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.iamvive.grokkingit.domain.model.StepResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffectPlayer @Inject constructor(
    private val soundManager: SoundManager
) {
    fun playEffect(result: StepResult, hapticFeedback: HapticFeedback) {
        when (result) {
            StepResult.INITIAL -> {
                // Subtle or no feedback for initial step
            }
            StepResult.FOUND -> {
                soundManager.playSuccess()
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            StepResult.TOO_LOW, StepResult.TOO_HIGH -> {
                soundManager.playTick()
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
            StepResult.NOT_FOUND -> {
                soundManager.playError()
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
}
