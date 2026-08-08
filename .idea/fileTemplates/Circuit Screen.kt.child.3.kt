package ${PACKAGE_NAME}

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@CircuitInject(${NAME}Screen::class, AppScope::class)
@Inject
internal class ${NAME}Ui : Ui<${NAME}State, ${NAME}UiEvent>() {
    @Composable
    override fun Content(
        state: ${NAME}State,
        handleEvent: EventHandler<${NAME}UiEvent>,
        modifier: Modifier,
    ) {
        Text("Hello, world!")
    }
}
