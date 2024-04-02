sealed class ButtonState {
    data object Initial : ButtonState()

    data object Clicked : ButtonState()
    data object Loading : ButtonState()
    data object Completed : ButtonState()
}