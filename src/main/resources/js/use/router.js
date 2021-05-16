import Vue from "vue"
import VueRouter from "vue-router"
import Main from "../pages/Main.vue";
import Login from "../pages/Login.vue";
import Faculty from "../pages/Department.vue";
import Teacher from "../pages/Teacher.vue";

Vue.use(VueRouter);

const routes = [
    {path: '/', component: Main},
    {path: '/login', component: Login},
    {path: '/department/:id', component: Faculty},
    {path: '/teacher/:id', component: Teacher},
    {path: '*', redirect: '/'}
];

export default new VueRouter({
    mode: 'history',
    routes
})