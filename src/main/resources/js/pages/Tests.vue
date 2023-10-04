<template>
    <v-container>
        <v-dialog width="800" v-if="testForm.state" v-model="testForm.state">
            <v-card outlined>
                <v-card-title>
                    Форма {{ testForm.id ? 'редактирования' : 'добавления' }} теста
                </v-card-title>

                <v-divider style="border-color: black"/>

                <div class="pa-4">

                    <v-text-field
                        v-model="testForm.name"
                        placeholder="Название теста"
                        clearable
                        single-line dense hide-details
                        outlined
                    />

                    <v-checkbox
                        class="pt-4"
                        v-model="testForm.scored"
                        label="С оценкой"
                        clearable
                        single-line dense hide-details
                        outlined
                    />
                </div>

                <v-divider style="border-color: black"/>

                <v-card-title>
                    <div style="float: left">Вопросы теста</div>

                    <v-spacer/>

                    <div style="float: right">
                        <v-btn
                            :color="colors.button"
                            style="width: 100%;"
                            @click="addQuestion"
                            outlined
                        >
                            добавить
                        </v-btn>
                    </div>
                </v-card-title>

                <v-divider style="border-color: black"/>

                <div class="px-4 pb-4">
                    <div
                        v-for="(question, index) in testForm.questions"
                        class="pt-4"
                    >
                        <v-card outlined style="border-radius: 0; border-color: black">
                            <v-card-title :style="`background-color:${colors.main}`">
                                <div style="float: left">Вопрос #{{ index + 1 }}</div>

                                <v-spacer/>

                                <div style="float: right">
                                    <v-btn
                                        :color="colors.button"
                                        style="width: 100%;"
                                        @click="removeItem(testForm.questions, question)"
                                        outlined
                                    >
                                        удалить вопрос
                                    </v-btn>
                                </div>
                            </v-card-title>

                            <v-divider style="border-color: black"/>

                            <div class="pa-4">
                                <v-text-field
                                    v-model="question.value"
                                    placeholder="Формулировка вопроса"
                                    clearable
                                    single-line dense hide-details
                                    outlined
                                />

                                <v-text-field
                                    v-if="testForm.scored"
                                    v-model="question.score"
                                    placeholder="Количество баллов за вопрос"
                                    type="number"
                                    class="pt-4"
                                    clearable
                                    single-line dense hide-details
                                    outlined
                                />

                                <v-select
                                    v-model="question.type"
                                    :items="types"
                                    label="Тип вопроса"
                                    class="pt-4"
                                    single-line dense hide-details
                                    outlined
                                />
                            </div>


                            <div v-if="question.type && question.type !== 'Текстовый вопрос'">
                                <v-divider style="border-color: black"/>

                                <v-card-title>
                                    <div style="float: left">Варианты ответа</div>

                                    <v-spacer/>

                                    <div style="float: right">
                                        <v-btn
                                            :color="colors.button"
                                            style="width: 100%;"
                                            @click="addOption(question)"
                                            outlined
                                        >
                                            добавить
                                        </v-btn>
                                    </div>
                                </v-card-title>

                                <v-divider style="border-color: black"/>

                                <v-row
                                    v-if="testForm.scored"
                                    class="ma-0 px-4"
                                >
                                    <v-spacer/>

                                    <v-col class="pa-0 pt-1 text-center" cols="2" >
                                        Правильный ответ?
                                    </v-col>
                                </v-row>

                                <div class="pb-4">
                                    <div
                                        v-for="(option, optionIndex) in question.options"
                                        class="px-4"
                                    >
                                        <v-row justify="center" class="ma-0 pt-4">
                                            <v-col class="pa-0">
                                                <v-text-field
                                                    v-model="option.value"
                                                    placeholder="Пример ответа"
                                                    clearable
                                                    single-line dense hide-details
                                                    outlined
                                                    append-outer-icon="delete"
                                                    @click:append-outer="removeItem(question.options, option)"
                                                />
                                            </v-col>
                                            <v-col
                                                class="pa-0 d-flex justify-center"
                                                cols="2"
                                                v-if="testForm.scored"
                                            >
                                                <v-checkbox
                                                    v-model="option.valid"
                                                    single-line dense hide-details
                                                    outlined
                                                />
                                            </v-col>
                                        </v-row>
                                    </div>
                                </div>
                            </div>
                        </v-card>
                    </div>
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

        <v-dialog width="450" v-if="deleteTest.state" v-model="deleteTest.state">
            <v-card>
                <v-card-title>
                    Удалить тест?
                    <v-spacer/>
                    <v-btn
                        @click="deleteTest.state = false"
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
                <template v-slot:item.scoredCheckbox="{ item }">
                    <v-checkbox
                        v-model="item.scored"
                        readonly
                        single-line dense hide-details
                        outlined
                    />
                </template>

                <template v-slot:item.actions="{ item }">
                    <v-btn
                        :color="colors.button"
                        :href="'/test/' + item.id"
                        outlined
                    >
                        пройти
                    </v-btn>
                    <v-btn
                        :color="colors.button"
                        v-if="item.creatorId === profile.id"
                        :href="'/result/' + item.id"
                        outlined
                    >
                        ответы
                    </v-btn>
                    <v-btn
                        :color="colors.button"
                        v-if="item.creatorId === profile.id"
                        @click="setEditForm(item)"
                        outlined
                    >
                        редактирование
                    </v-btn>
                    <v-btn
                        :color="colors.button"
                        v-if="item.creatorId === profile.id "
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

class TestForm {
    state = false

    id = null
    name = ''
    scored = false

    /**
     * @type {Array<Question>}
     */
    questions = []

    constructor() {
        this.questions = [new Question()]
    }
}

class Question {
    position = 0
    value = ''
    type = 'Одиночный выбор'

    /**
     * @type {Array<Option>}
     */
    options = []

    constructor(position) {
        if (position) {
            this.position = position
        }
        this.options = [new Option()]
    }
}

class Option {
    valid = false
    value = ''
}

export default {
    components: {},
    data() {
        return {
            table: {
                loading: false,
                items: [],
                headers: [
                    {text: 'Идентификатор теста', value: 'id'},
                    {text: 'Название теста', value: 'name'},
                    {text: 'С оценкой', value: 'scoredCheckbox'},
                    {text: 'Действия', value: 'actions', align: 'right'}
                ],
                search: '',
            },
            deleteTest: {
                state: false,
                id: undefined
            },
            testForm: new TestForm(),
            colors: colors,
            types: [
                "Одиночный выбор",
                "Множественный выбор",
                "Текстовый вопрос"
            ]
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
            this.deleteTest.id = item.id
            this.deleteTest.state = true
        },
        deleteItem() {
            api.delete(`${endpoints.test}/${this.deleteTest.id}`)
                .then(resp => {
                    if (resp.errorMessage) {
                        this.showMessage(resp.errorMessage)
                    } else {
                        this.showMessage("Тест успешно удален")
                        this.loadItems()
                        this.deleteTest.state = false
                    }
                })
        },
        setEditForm(item) {
            api.get(`${endpoints.test}/${item.id}`)
                .then(resp => {
                    if (resp.errorMessage) {
                        this.showMessage(resp.errorMessage)
                    } else {
                        Object.assign(this.testForm, resp.data)

                        this.testForm.state = true
                    }
                })
        },
        save() {
            if (this.testForm.id) {
                api.put(`${endpoints.test}/${this.testForm.id}`, this.testForm)
                    .then(resp => {
                        if (resp.errorMessage) {
                            this.showMessage(resp.errorMessage)
                        } else {
                            this.showMessage("Тест успешно обновлен")
                            this.loadItems()
                            this.testForm.state = false
                        }
                    })
            } else {
                api.post(endpoints.test, this.testForm)
                    .then(resp => {
                        if (resp.errorMessage) {
                            this.showMessage(resp.errorMessage)
                        } else {
                            this.showMessage("Тест успешно добавлен")
                            this.loadItems()
                            this.testForm.state = false
                        }
                    })
            }
        },
        loadItems() {
            this.table.loading = true
            api.get(endpoints.test)
                .then(resp => {
                    this.table.items = resp.data
                    this.table.loading = false
                })
        },
        setCreateItem() {
            this.testForm = new TestForm()
            this.testForm.state = true
        },
        addQuestion() {
            let question = new Question(this.testForm.questions.length);
            this.testForm.questions.push(question)
        },
        /**
         * @param question {Question}
         */
        addOption(question) {
            question.options.push(new Option())
        },
        /**
         * @param source {Array}
         * @param item {Object}
         */
        removeItem(source, item) {
            if (source.length === 1) {
                this.showMessage('Нельзя указать больше')
            }
            let idx = source.indexOf(item)
            source.splice(idx, 1)
        }
    }
}
</script>