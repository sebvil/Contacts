#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}
#end

import androidx.compose.runtime.Composable
import com.sebastianvm.watcher.mvvm.ScreenState
import com.sebastianvm.watcher.mvvm.WatcherPresenter
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(screen = ${NAME}Screen::class, scope = AppScope::class)
class ${NAME}Presenter : WatcherPresenter<${NAME}State, ${NAME}UserAction> {
    @Composable
    override fun present(): ScreenState<${NAME}State, ${NAME}UserAction> {
        return ScreenState(${NAME}State() { action ->
            when (action) {
            }
        }
    }
}
