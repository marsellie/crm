import Vue from "vue"
import VueRouter from "vue-router"
import Main from "../pages/Tests.vue";
import Login from "../pages/Login.vue";
import Test from "../pages/Test.vue";
import Results from "../pages/Results.vue";
import Users from "../pages/Users.vue";

Vue.use(VueRouter);

const routes = [
    {path: '/', component: Main},
    {path: '/login', component: Login},
    {path: '/test/:id', component: Test},
    {path: '/result/:id', component: Results},
    {path: '/user', component: Users},
    {path: '*', redirect: '/'}
];

export default new VueRouter({
    mode: 'history',
    routes
})