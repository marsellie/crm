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
                <template v-slot:item.finishedCheckbox="{ item }">
                    {{item.result.finished ? 'Нет' : 'Да'}}
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
                    </v-toolbar>

                    <v-divider/>
                </template>

                <template v-slot:expanded-item="{ headers, item }">
                    <td
                        :colspan="headers.length"
                        class="py-4"
                        v-if="item.test"
                    >
                        <test-result
                            @reload-results="loadItems"
                            :test="item.test"
                            :result="item.result"
                        />
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
import TestResult from "./TestResult.vue";

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
    optionId
    optionIds
}

export default {
    components: {TestResult},
    data() {
        return {
            table: {
                /**
                 * @type {Array<Result>}
                 */
                items: [],
                headers: [
                    {text: 'Создан', value: 'createdAtFormat'},
                    {text: 'Количество баллов', value: 'result.total'},
                    {text: 'Необходима проверка', value: 'finishedCheckbox'}
                ],
                search: '',
                loading: false
            },
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

            api.get(`${endpoints.result}`)
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
                        item.createdAtFormat = formatter.format(new Date(item.result.createdAt)) + ' (МСК)'
                    }

                    this.table.loading = false
                })
        },
    }
}
</script>