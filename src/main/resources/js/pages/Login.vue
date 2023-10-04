<template>
    <v-container class="fill-height">
        <v-card
            style="margin: auto; border: 1px solid"
            :color="colors.focus"
            tile flat
            width="450"
        >
            <v-card-title>
                <span
                    class="text-center"
                    style="width: 100%"
                >
                    Вход
                </span>
            </v-card-title>

            <v-divider style="background-color: black"/>

            <div class="py-4 px-6">
                <v-text-field
                    v-model="username"
                    class="pb-4"
                    placeholder="Логин"
                    prepend-inner-icon="person"
                    clearable
                    dense hide-details
                    solo
                />
                <v-text-field
                    v-model="password"
                    :type="show ? 'text' : 'password'"
                    prepend-inner-icon="lock"
                    :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                    @click:append="show = !show"
                    placeholder="Пароль"
                    clearable
                    dense hide-details
                    solo
                />
            </div>

            <v-divider style="background-color: black"/>

            <div class="py-4 px-6">
                <v-btn
                    @click="login"
                    style="width: 100%"
                    :disabled="password.length===0 || username.length === 0"
                    :ripple="false"
                    outlined
                    :color="colors.button"
                >
                    Войти
                </v-btn>
            </div>
        </v-card>
    </v-container>
</template>

<script>
import {mapActions, mapState} from 'vuex'
import Snackbar from "../components/util/Snackbar.vue";
import api from "../use/api";
import colors from "../use/colors";
import endpoints from "../use/endpoints";

export default {
    created() {
        if (this.profile) {
            this.$router.push("/")
        }
    },
    data() {
        return {
            colors: colors,
            username: '',
            password: '',
            show: false,
        }
    },
    components: {Snackbar},
    computed: {
        ...mapState('app', ["profile"])
    },
    methods: {
        ...mapActions('app', ["showMessage"]),
        login() {
            let formData = new FormData();
            formData.append("username", this.username)
            formData.append("password", this.password)

            api.post(endpoints.login, formData)
                .then(data => {
                    if (data.errorMessage)
                        this.showMessage(data.errorMessage)
                    else {
                        this.$router.push("/")
                        this.$router.go()
                    }
                })
        }
    },
}
</script>

<style scoped>

</style>