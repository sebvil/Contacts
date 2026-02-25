#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}
#end

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.watcher.mvvm.Ui
import com.sebastianvm.watcher.mvvm.util.PreviewScreen
import com.sebastianvm.watcher.mvvm.util.ScreenPreviews
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(${NAME}Screen::class, AppScope::class)
class ${NAME}Ui : Ui<${NAME}State, ${NAME}UserAction>() {

    @Composable
    override fun Content(
        state: ${NAME}State,
        handle: (${NAME}UserAction) -> Unit,
        modifier: Modifier,
    ) {
        TODO("Add your UI here")
    }
}



internal class ${NAME}Previews : ScreenPreviews<${NAME}State>() {
    override val ui: Ui<${NAME}State, *>
        get() = ${NAME}Ui()

    @PreviewScreen
    @Composable
    internal fun DefaultState() {
        Preview(state = ${NAME}State())
    }
}
