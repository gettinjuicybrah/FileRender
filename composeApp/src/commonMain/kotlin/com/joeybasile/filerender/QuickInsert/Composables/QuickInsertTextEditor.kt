package com.joeybasile.filerender.QuickInsert.Composables

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import com.joeybasile.filerender.QuickInsert.State.QuickInsertTextEditorState
import kotlinx.coroutines.delay


private const val CURSOR_BLINK_SPEED_MS = 500L

@Composable
fun QuickInsertTextEditor(
    state: QuickInsertTextEditorState,
    modifier: Modifier = Modifier,
    isFocusable: Boolean = true
){

    /*

    **** BASICS *****

    - To use FocusRequester, it needs to be instantiated using remember { FocusRequester() } to preserve its state across recompositions.
     Then it is attached to a composable using the focusRequester modifier.
     To request focus, the requestFocus() method is called on the FocusRequester instance, typically within a LaunchedEffect or in response to an event.

    ***** *****


    ***** remember REASONING *****
    remember is a composable function in Kotlin used within Jetpack Compose to preserve state across recompositions.
    Recomposition occurs when the UI is redrawn to reflect changes in data or state.
    Without remember, values would be reset on each recomposition.
    remember ensures that a value is only calculated once during the initial composition and then stored for subsequent recompositions.
    *****
    -----
    ***** FocusRequester REASONING *****
    serves as a handle to request focus on a specific composable element.
     It allows for programmatic control over which UI element has focus.enabling actions such as:
        Setting initial focus when a screen loads.
        Shifting focus in response to user interactions or events.


    ***** *****
     */
    val focusRequester = remember { FocusRequester() }

    /*
    -----------
    BASICS:
    -----------
    It runs when first composed (initial composition).
It restarts whenever any key (parameter) in the key list changes.
It does not restart just because the parent recomposes (unless a key changes).


    ****** LaunchedEffect REASONING *****
     is a composable function that launches a coroutine to execute a suspend function within the
           lifecycle of a composable, ensuring lifecycle-aware execution and proper cancellation when the composable is removed.

     will recompose every time its parent recomposes because the key provided to LaunchedEffect is Unit, which is a constant.
     * This means that LaunchedEffect will restart on every recomposition of its parent.
    ***** *****

     */
    LaunchedEffect(Unit) {
        if (isFocusable) focusRequester.requestFocus()
    }
    /*
    Will execute any time any of the parameters change, AND upon initial composition.
     */
    LaunchedEffect(
        state.isFocused,
        //state.cursorPos,
        isFocusable
    ){
        /*
        PURPOSE:
        to immediately exit the effect if the editor is disabled, ensuring that:
        1.) If enabled is false, the text editor should not be interactive.
            The cursor should not be visible or blinking.
            Calling state.updateFocus(false) ensures the editor loses focus when disabled.
        2.) Stops the Infinite Loop Early
            The effect contains a while (state.isFocused) loop for cursor blinking.
            If the editor is disabled, we don't want to enter this loop.
            The return@LaunchedEffect prevents unnecessary execution.
        3.) Ensures Consistency in State
            state.updateFocus(false) ensures the internal state reflects the disabled status.
            Prevents the cursor from lingering if the editor was focused before being disabled.
         */
        if (!isFocusable) {
            state.updateFocus(false)
            return@LaunchedEffect
        }
        state.cursorState.setVisibleTrue()
        while (state.isFocused) {
            delay(CURSOR_BLINK_SPEED_MS)
            state.cursorState.toggleVisibility()
        }
    }

    Box(
        modifier = modifier
            /*
            Chains additional modifications to an existing modifier
             */
            .then(
                if (isFocusable) {
                    /*
                    Allows the Box composable to request focus.
                     */
                    Modifier.focusRequester(focusRequester)
                } else {
                    /*
                    A no-op - does nothing.
                     */
                    Modifier
                }
            )
            /*
            The .onSizeChanged { size -> ... } modifier is a standard Compose modifier that detects when the size of the composable changes.
            In this case, when the size of the Box changes, it triggers state.onViewportSizeChange(size.toSize()).

            The viewport size of the text editor would be updated in various scenarios, primarily because the amount of screen space available to the editor changes.
             */
            .onSizeChanged {
                size ->
                state.onViewportSizeChange(
                    size.toSize()
                )
            }

    ) {

    }

}