export default {
    namespaced: true,
    state: {
        profile: user,
        snackbarMessage: '',
        snackbarTime: 0,
    },
    mutations: {
        CHANGE_SNACKBAR_MESSAGE(state, value) {
            state.snackbarMessage = value;
            state.snackbarTime++
        }
    },
    actions: {
        showMessage({commit}, value) {
            commit('CHANGE_SNACKBAR_MESSAGE', value)
        }
    }
};