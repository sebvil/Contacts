package ${PACKAGE_NAME}

import com.sebastianvm.contacts.features.base.UiState

internal sealed interface ${NAME}State : UiState {
    data object Loading : ${NAME}State

    data object Error : ${NAME}State
}
