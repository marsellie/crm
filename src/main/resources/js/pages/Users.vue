<template>
    <v-container>
        <v-dialog width="800" v-if="userForm.state" v-model="userForm.state">
            <v-card outlined>
                <v-card-title>
                    Форма {{ userForm.id ? 'редактирования' : 'добавления' }} пользователя
                </v-card-title>

                <v-divider style="border-color: black"/>

                <div class="pa-4">
                    <v-text-field
                        v-model="userForm.name"
                        placeholder="ФИО пользователя"
                        clearable
                        single-line dense hide-details
                        outlined
                    />

                    <v-text-field
                        class="pt-4"
                        v-model="userForm.username"
                        placeholder="Логин пользователя"
                        clearable
                        single-line dense hide-details
                        outlined
                    />

                    <v-text-field
                        class="pt-4"
                        v-model="userForm.password"
                        placeholder="Пароль пользователя"
                        clearable
                        single-line dense hide-details
                        outlined
                    />
                </div>

                <v-divider style="border-color: black"/>

                <v-card-title>
                    Роли пользователя
                </v-card-title>

                <v-divider style="border-color: black"/>

                <div class="pa-4">
                    <v-checkbox
                        v-model="userForm.roles"
                        v-for="role in Object.entries(rolesMap).map(([enumValue, name]) => ({ enum: enumValue, name }))"
                        :key="role.enum"
                        :value="role.enum"
                        :label="role.name"
                        single-line dense hide-details
                        outlined
                    />
                </div>

                <v-divider style="border-color: black"/>

                <v-card-actions>
                    <v-btn
                        :color="colors.button"
                        style="width: 100%;"
                        @click="save"
                        outlined
                    >
                        сохранить
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog width="450" v-if="deleteUser.state" v-model="deleteUser.state">
            <v-card>
                <v-card-title>
                    Удалить пользователя?
                    <v-spacer/>
                    <v-btn
                        @click="deleteUser.state = false"
                        icon
                    >
                        <v-icon>
                            clear
                        </v-icon>
                    </v-btn>
                </v-card-title>
                <v-divider style="border-color: black"/>
                <v-card-actions>
                    <v-btn
                        :color="colors.button"
                        style="width: 100%;"
                        @click="deleteItem"
                        outlined
                    >
                        да
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-card tile outlined>
            <v-data-table
                :items="table.items"
                :headers="table.headers"
                :search="table.search"
                :loading="table.loading"
            >
                <template v-slot:item.actions="{ item }">
                    <v-btn
                        :color="colors.button"
                        @click="setEditForm(item)"
                        outlined
                    >
                        редактирование
                    </v-btn>
                    <v-btn
                        :color="colors.button"
                        @click="setDeleteItem(item)"
                        outlined
                    >
                        удалить
                    </v-btn>
                </template>

                <template v-slot:top>
                    <v-toolbar flat tile>
                        <v-col cols="5">
                            <v-text-field
                                v-model="table.search"
                                placeholder="Поиск в таблице"
                                append-icon="search"
                                outlined
                                hide-details dense
                                single-line
                            />
                        </v-col>
                        <v-btn
                            @click="loadItems"
                            :color="colors.button"
                            outlined
                        >
                            обновить
                        </v-btn>

                        <v-spacer/>

                        <v-btn
                            @click="setCreateItem"
                            :color="colors.button"
                            outlined
                        >
                            добавить
                        </v-btn>

                    </v-toolbar>

                    <v-divider/>
                </template>
            </v-data-table>
        </v-card>
    </v-container>
</template>

<script>
import colors from '../use/colors';
import api from "../use/api";
import endpoints from "../use/endpoints";
import {mapActions, mapState} from "vuex";

class UserForm {
    state = false

    id
    name
    username
    password

    roles = []
}

export default {
    components: {},
    data() {
        return {
            rolesMap: {
                ADMIN: "Администратор",
                USER: "Пользователь",
            },
            table: {
                loading: false,
                items: [],
                headers: [
                    {text: 'Идентификатор пользователя', value: 'id'},
                    {text: 'ФИО', value: 'name'},
                    {text: 'Логин', value: 'username'},
                    {text: 'Роли', value: 'rolesJoined'},
                    {text: 'Действия', value: 'actions'},
                ],
                search: '',
            },
            deleteUser: {
                state: false,
                id: undefined
            },
            userForm: new UserForm(),
            colors: colors
        }
    },
    created() {
        this.loadItems()
    },
    computed: {
        ...mapState('app', ["profile"]),
    },
    methods: {
        ...mapActions("app", ["showMessage"]),
        setDeleteItem(item) {
            this.deleteUser.id = item.id
            this.deleteUser.state = true
        },
        deleteItem() {
            api.delete(`${endpoints.user}/${this.deleteUser.id}`)
                .then(resp => {
                    if (resp.errorMessage) {
                        this.showMessage(resp.errorMessage)
                    } else {
                        this.showMessage("Пользователь успешно удален")
                        this.loadItems()
                        this.deleteUser.state = false
                    }
                })
        },
        setEditForm(item) {
            Object.assign(this.userForm, item)

            this.userForm.state = true
        },
        save() {
            if (this.userForm.id) {
                api.put(`${endpoints.user}/${this.userForm.id}`, this.userForm)
                    .then(resp => {
                        if (resp.errorMessage) {
                            this.showMessage(resp.errorMessage)
                        } else {
                            this.showMessage("Пользователь успешно обновлен")
                            this.loadItems()
                            this.userForm.state = false
                        }
                    })
            } else {
                api.post(endpoints.user, this.userForm)
                    .then(resp => {
                        if (resp.errorMessage) {
                            this.showMessage(resp.errorMessage)
                        } else {
                            this.showMessage("Пользователь успешно добавлен")
                            this.loadItems()
                            this.userForm.state = false
                        }
                    })
            }
        },
        loadItems() {
            this.table.loading = true
            api.get(endpoints.user)
                .then(resp => {
                    this.table.items = resp.data
                    for (let item of this.table.items) {
                        item.rolesJoined = item.roles
                            .map(item => this.rolesMap[item])
                            .join(', ')
                    }

                    this.table.loading = false
                })
        },
        setCreateItem() {
            this.userForm = new UserForm()
            this.userForm.state = true
        },
    }
}
</script>