<template>
    <v-container>
        <v-card tile outlined>
            <v-data-table
                :items="table.items"
                :headers="table.headers"
                :search="table.search"
                show-expand
                :loading="table.loading"
            >
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
                    </v-toolbar>

                    <v-divider/>
                </template>

                <template v-slot:expanded-item="{ headers, item }">
                    <td
                        :colspan="headers.length"
                        class="py-4"
                        v-if="test"
                    >
                        <v-card outlined tile>
                            <v-card-title>
                                {{ item.user }}
                            </v-card-title>
                        </v-card>

                        <div
                            class="pt-4"
                            v-for="(answer, index) in item.answers"
                        >
                            <v-card outlined tile>
                                <v-card-text class="text-h6">
                                    Вопрос {{ index + 1 }}: {{ test.questions[index].value }}
                                </v-card-text>

                                <v-divider/>

                                <div v-if="test.questions[index].type === 'Текстовый вопрос'">
                                    <v-card-title>
                                        Ответ:
                                    </v-card-title>

                                    <v-divider/>

                                    <v-card-text>
                                        {{ answer.text }}
                                    </v-card-text>
                                </div>

                                <div
                                    v-if="test.questions[index].type === 'Одиночный выбор'"
                                >
                                    <v-radio-group
                                        :value="test.questions[index].options.filter(option => option.valid).map(option => option.id)[0]"
                                        class="ma-0 pa-4"
                                        single-line dense hide-details
                                        readonly
                                        outlined
                                    >
                                        <v-radio
                                            v-for="(option, index) in test.questions[index].options"
                                            :key="option.id"
                                            :value="index"
                                            :label="option.value"
                                            readonly
                                            single-line dense hide-details
                                            outlined
                                        />
                                    </v-radio-group>

                                    <v-divider/>

                                    <v-card-title>
                                        Ответ:
                                    </v-card-title>

                                    <v-divider/>

                                    <v-radio-group
                                        value="1"
                                        class="ma-0 pa-4"
                                        single-line dense hide-details
                                        outlined
                                    >
                                        <v-radio
                                            :label="answer.option"
                                            value="1"
                                            readonly
                                            single-line dense hide-details
                                            outlined
                                        />
                                    </v-radio-group>
                                </div>

                                <div v-if="test.questions[index].type === 'Множественный выбор'">
                                    <div class="pa-4">
                                        <v-checkbox
                                            v-for="option in test.questions[index].options"
                                            v-model="option.valid"
                                            :key="option.id"
                                            readonly
                                            :label="option.value"
                                            single-line dense hide-details
                                            outlined
                                        />
                                    </div>

                                    <v-divider/>

                                    <v-card-title>
                                        Ответ:
                                    </v-card-title>

                                    <v-divider/>

                                    <div class="pa-4">
                                        <v-checkbox
                                            v-for="option in answer.options"
                                            :key="option"
                                            :label="option"
                                            :value="option.selected"
                                            v-model="option.selected"
                                            readonly
                                            single-line dense hide-details
                                            outlined
                                        />
                                    </div>
                                </div>
                            </v-card>
                        </div>
                    </td>
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

class Test {
    id
    name

    /**
     * @type {Array<Question>}
     */
    questions
}

class Question {
    id

    value
    type

    /**
     * @type {Array<Option>}
     */
    options
}

class Option {
    id
    value
    valid
}

class Result {
    id
    user
    total
    createdAt
    createdAtFormat

    /**
     * @type {Array<Answer>}
     */
    answers
}

class Answer {
    id
    total
    text
    option
    options
}

export default {
    components: {},
    data() {
        return {
            table: {
                /**
                 * @type {Array<Result>}
                 */
                items: [],
                headers: [
                    {text: 'Имя пользователя', value: 'user'},
                    {text: 'Создан', value: 'createdAtFormat'},
                    {text: 'Количество баллов', value: 'total'}
                ],
                search: '',
                loading: false
            },
            /**
             * @type {Test}
             */
            test: undefined,
            results: undefined,
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
        loadItems() {
            this.table.loading = true

            api.get(`${endpoints.result}/${this.$route.params.id}`)
                .then(resp => {
                    this.table.items = resp.data

                    let formatter = new Intl.DateTimeFormat('ru-RU', {
                        timeZone: 'Europe/Moscow',
                        hour12: false,
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit',
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                    });

                    for (let item of this.table.items) {
                        item.createdAtFormat = formatter.format(new Date(item.createdAt)) + ' (МСК)'
                    }

                    this.table.loading = false
                })
            api.get(`${endpoints.test}/${this.$route.params.id}`)
                .then(resp => this.test = resp.data)
        },
    }
}
</script>