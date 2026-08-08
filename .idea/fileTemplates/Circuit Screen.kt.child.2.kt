package ${PACKAGE_NAME}

import androidx.compose.runtime.Composable
import com.sebastianvm.contacts.features.base.Presenter
import com.sebastianvm.contacts.features.base.ScreenState
import com.sebastianvm.contacts.features.base.withEventHandler
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@CircuitInject(${NAME}Screen::class, AppScope::class)
@Inject
internal class ${NAME}Presenter : Presenter<${NAME}State, ${NAME}UiEvent> {
    @Composable
    override fun present(): ScreenState<${NAME}State, ${NAME}UiEvent> {
        return ${NAME}State.Loading withEventHandler {}
    }
}
