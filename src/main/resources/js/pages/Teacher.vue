<template>
    <v-container v-if="teacher">
        <v-btn
            :color="colors.button"
            :ripple="false"
            x-large plain
            @click="$router.go(-1)"
        >
            <v-icon>
                chevron_left
            </v-icon>
            Назад
        </v-btn>

        <v-dialog
            v-model="reportState"
            width="450"
        >
            <v-card>
                <v-card-title>
                    Выберите крайние даты
                </v-card-title>
                <v-divider/>
                <v-date-picker
                    v-model="reportRange"
                    full-width
                    range no-title scrollable
                    show-adjacent-months
                />
                <v-divider/>
                <v-card-actions>
                    <v-btn
                        @click="generateReport"
                        :disabled="!(reportRange && reportRange.length === 2)"
                        style="width: 100%;"
                        :color="colors.button"
                        outlined
                    >
                        сгенерировать
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-dialog width="450" v-if="editRecordForm.state" v-model="editRecordForm.state">
            <v-card>
                <v-card-title>
                    Редактирование записи
                </v-card-title>
                <v-divider/>
                <div class="pa-4">
                    <v-text-field
                        v-model="editRecordForm.name"
                        placeholder="Название мероприятия"
                        class="pb-4"
                        single-line dense hide-details
                        outlined
                    />
                    <v-text-field
                        v-model="editRecordForm.type"
                        placeholder="Вид деятельности"
                        class="pb-4"
                        single-line dense hide-details
                        outlined
                    />
                    <v-menu
                        ref="menu"
                        v-model="editRecordForm.menu"
                        :close-on-content-click="false"
                        :return-value.sync="editRecordForm.menu"
                        transition="scale-transition"
                        offset-y
                        min-width="auto"
                    >
                        <template v-slot:activator="{ on, attrs }">
                            <v-combobox
                                v-model="editRecordForm.date"
                                class="pb-4"
                                label="Дата"
                                prepend-inner-icon="mdi-calendar"
                                readonly outlined
                                hide-details
                                dense single-line
                                v-bind="attrs"
                                v-on="on"
                            />
                        </template>
                        <v-date-picker
                            v-model="editRecordForm.date"
                            no-title
                            scrollable
                        >
                            <v-spacer/>
                            <v-btn
                                text
                                color="primary"
                                @click="$refs.menu.save(editRecordForm.date)"
                            >
                                OK
                            </v-btn>
                        </v-date-picker>
                    </v-menu>
                    <v-text-field
                        v-model="editRecordForm.value"
                        placeholder="Количество баллов"
                        single-line dense hide-details
                        outlined
                    />
                </div>
                <v-divider/>
                <v-card-actions>
                    <v-btn
                        :disabled="!isEditValid"
                        :color="colors.button"
                        style="width: 100%;"
                        @click="saveEdit"
                        outlined
                    >
                        сохранить
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog width="450" v-if="deleteRecord.state" v-model="deleteRecord.state">
            <v-card>
                <v-card-title>
                    Удалить запись?
                    <v-spacer/>
                    <v-btn
                        @click="deleteRecord.state = false"
                        icon
                    >
                        <v-icon>
                            clear
                        </v-icon>
                    </v-btn>
                </v-card-title>
                <v-divider/>
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
                    <v-card-title>
                        Преподаватель {{ teacher.name }}
                        <v-spacer/>
                        <v-btn
                            @click="reportState = true"
                            :color="colors.button"
                            outlined
                        >
                            сформировать отчет
                        </v-btn>
                    </v-card-title>
                    <v-expansion-panels flat tile focusable>
                        <v-expansion-panel>
                            <v-expansion-panel-header>
                                <b>
                                    Форма добавления записи
                                </b>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-col :cols="$vuetify.breakpoint.smAndUp ? 5 : 12" class="pa-0">
                                    <v-text-field
                                        v-model="newRecordForm.name"
                                        class="pt-4"
                                        placeholder="Название мероприятия"
                                        single-line dense hide-details
                                        outlined
                                    />
                                    <v-text-field
                                        v-model="newRecordForm.type"
                                        class="pt-4"
                                        placeholder="Название деятельности"
                                        single-line dense hide-details
                                        outlined
                                    />
                                    <v-menu
                                        ref="menu"
                                        v-model="newRecordForm.menu"
                                        :close-on-content-click="false"
                                        :return-value.sync="newRecordForm.menu"
                                        transition="scale-transition"
                                        offset-y
                                        min-width="auto"
                                    >
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-combobox
                                                v-model="newRecordForm.date"
                                                class="pt-4"
                                                label="Дата"
                                                prepend-inner-icon="mdi-calendar"
                                                readonly outlined
                                                hide-details
                                                dense single-line
                                                v-bind="attrs"
                                                v-on="on"
                                            ></v-combobox>
                                        </template>
                                        <v-date-picker
                                            v-model="newRecordForm.date"
                                            no-title
                                            scrollable
                                        >
                                            <v-spacer/>
                                            <v-btn
                                                text
                                                color="primary"
                                                @click="$refs.menu.save(newRecordForm.date)"
                                            >
                                                OK
                                            </v-btn>
                                        </v-date-picker>
                                    </v-menu>
                                    <v-text-field
                                        v-model="newRecordForm.value"
                                        class="pt-4"
                                        placeholder="Количество баллов"
                                        single-line dense hide-details
                                        outlined
                                    />
                                </v-col>
                                <v-btn
                                    @click="create"
                                    :disabled="!isNewValid"
                                    class="mt-4"
                                    :color="colors.button"
                                    outlined
                                >
                                    добавить
                                </v-btn>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                    <v-divider/>
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
import {mapActions} from "vuex";

export default {
    components: {},
    data() {
        return {
            teacher: undefined,
            reportState: false,
            reportRange: undefined,
            table: {
                items: [],
                headers: [
                    {text: 'Мероприятие', value: 'name'},
                    {text: 'Деятельность', value: 'type'},
                    {text: 'Дата', value: 'date'},
                    {text: 'Баллы', value: 'value'},
                    {text: 'Действия', value: 'actions', align: 'right'},
                ],
                search: '',
            },
            deleteRecord: {
                id: undefined,
                state: false
            },
            editRecordForm: {
                state: false,
                menu: false,
                id: '',
                name: '',
                type: '',
                date: '',
                value: '',
            },
            newRecordForm: {
                menu: false,
                name: '',
                type: '',
                date: '',
                value: 0,
            },
            colors: colors
        }
    },
    computed: {
        isNewValid() {
            return this.newRecordForm.name.length !== 0
                && this.newRecordForm.date.length !== 0
                && this.newRecordForm.value.length !== 0
                && this.newRecordForm.type.length !== 0
                && parseInt(this.newRecordForm.value) > 0
        },
        isEditValid() {
            return this.editRecordForm.name.length !== 0
                && this.editRecordForm.date.length !== 0
                && this.editRecordForm.value.length !== 0
                && this.editRecordForm.type.length !== 0
                && parseInt(this.editRecordForm.value) > 0
        }
    },
    created() {
        this.loadItems()
    },
    methods: {
        ...mapActions("app", ["showMessage"]),
        generateReport() {
            this.reportRange = this.reportRange.sort()
            let filtered = this.table.items.filter((it) => it.date >= this.reportRange[0] && it.date <= this.reportRange[1])
            let dateTransformer = (date) => date.split("-").reverse().join('.')

            let csvContent = "Сумма;" + filtered.reduce((sum, current) => sum += current.value, 0)
                + "\n" + filtered.map(it => `${dateTransformer(it.date)};${it.type};${it.name};${it.value}`).join("\n")
            let link = window.document.createElement("a");
            link.setAttribute("href", "data:text/csv;charset=utf-8,%EF%BB%BF" + encodeURI(csvContent));
            link.setAttribute("download", "report.csv");
            link.click();
        },
        setDeleteItem(item) {
            this.deleteRecord.id = item.id
            this.deleteRecord.state = true
        },
        deleteItem() {
            api.delete(endpoints.activityRecord + "/" + this.teacher.id + "/" + this.deleteRecord.id)
                .then(resp => {
                    if (resp.errorMessage)
                        this.showMessage(resp.errorMessage)
                    else {
                        this.showMessage("Запись успешно удалена")
                        this.loadItems()
                        this.deleteRecord.state = false
                    }
                })
        },
        setEditForm(item) {
            this.editRecordForm.id = item.id
            this.editRecordForm.name = item.name
            this.editRecordForm.date = item.date
            this.editRecordForm.value = item.value
            this.editRecordForm.type = item.type
            this.editRecordForm.state = true
        },
        saveEdit() {
            api.put(endpoints.activityRecord,
                {
                    id: this.editRecordForm.id,
                    name: this.editRecordForm.name,
                    type: this.editRecordForm.type,
                    value: this.editRecordForm.value,
                    date: this.editRecordForm.date
                })
                .then(resp => {
                    if (resp.errorMessage)
                        this.showMessage(resp.errorMessage)
                    else {
                        this.showMessage("Запись успешно изменена")
                        this.loadItems()
                        this.editRecordForm.state = false
                    }
                })
        },
        loadItems() {
            api.get(endpoints.teacher + "/" + this.$route.params.id)
                .then(resp => {
                    this.teacher = resp.data
                    this.table.items = resp.data.activityRecords
                })
        },
        create() {
            api.post(endpoints.activityRecord + "/" + this.teacher.id, this.newRecordForm)
                .then(resp => {
                    if (resp.errorMessage)
                        this.showMessage(resp.errorMessage)
                    else {
                        this.showMessage("Запись добавлена")
                        this.loadItems()
                        this.newRecordForm = {
                            name: '',
                            position: '',
                            workingRate: 0.5
                        }
                    }
                })
        }
    }
}
</script>

<style scoped>

</style>