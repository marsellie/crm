<template>
  <v-app>
    <v-app-bar v-if="profile" :color="colors.main" flat app>
      <v-spacer/>
      <span v-if="profile">
                <b>Администратор</b>
            </span>
      <v-btn
          v-if="profile"
          @click="logout"
          :color="colors.button"
          class="ml-4 white--text"
          rounded
      >
        Выход
      </v-btn>
    </v-app-bar>

    <snackbar/>

    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import {mapState} from "vuex";
import Snackbar from "./components/util/Snackbar.vue";
import colors from "./use/colors";
import api from "./use/api";
import endpoints from "./use/endpoints";

export default {
  components: {Snackbar},
  data() {
    return {
      colors: colors
    }
  },
  computed: {
    ...mapState('app', ["profile"])
  },
  methods: {
    logout() {
      api.post(endpoints.logout)
          .then(resp => this.$router.go())
    },
  },
  created() {
    if (!this.profile && this.$route.fullPath !== "/login") {
      this.$router.push("/login")
    }
  }
}
</script>