<template>
    <v-container v-if="test" fluid>
        <v-row justify="center">
            <v-col cols="6">
                <v-card outlined style="border-radius: 0">
                    <v-card-title>
                        {{ test.name }}
                    </v-card-title>

                    <v-card-subtitle>
                        Код теста: {{ test.id }}
                    </v-card-subtitle>
                </v-card>

                <div
                    v-if="result.finished"
                    class="pt-4"
                >
                    <v-card
                        style="border-radius: 0"
                        outlined
                    >
                        <v-card-title>
                            Ваш ответ записан
                        </v-card-title>
                    </v-card>
                </div>

                <div v-if="!result.finished">
                    <div class="pb-4">
                        <div
                            v-for="(question, index) in test.questions"
                            :key="question.id"
                            class="pt-4"
                        >
                            <v-card
                                style="border-radius: 0"
                                outlined
                            >
                                <v-card-title>
                                    Вопрос {{ index + 1 }}
                                </v-card-title>

                                <v-card-subtitle v-if="test.scored">
                                    Количество баллов: {{ question.score }}
                                </v-card-subtitle>

                                <v-divider/>

                                <v-card-text class="text-h6">
                                    {{ question.value }}
                                </v-card-text>

                                <v-divider/>

                                <v-radio-group
                                    v-if="question.type === 'Одиночный выбор'"
                                    v-model="result.answers[index].optionId"
                                    class="ma-0 pa-4"
                                    single-line dense hide-details
                                    outlined
                                >
                                    <v-radio
                                        v-for="option in question.options"
                                        :key="option.id"
                                        :value="option.id"
                                        :label="option.value"
                                        single-line dense hide-details
                                        outlined
                                    />
                                </v-radio-group>

                                <div
                                    v-if="question.type === 'Множественный выбор'"
                                    class="pa-4"
                                >
                                    <v-checkbox
                                        v-model="result.answers[index].optionIds"
                                        v-for="option in question.options"
                                        :key="option.id"
                                        :value="option.id"
                                        :label="option.value"
                                        single-line dense hide-details
                                        outlined
                                    />
                                </div>

                                <v-textarea
                                    v-if="question.type === 'Текстовый вопрос'"
                                    v-model="result.answers[index].text"
                                    class="pa-4"
                                    single-line dense hide-details
                                    outlined
                                />
                            </v-card>
                        </div>
                    </div>


                    <v-card
                        style="border-radius: 0"
                        outlined
                    >
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
                </div>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import api from "../use/api";
import endpoints from "../use/endpoints";
import {mapActions} from "vuex";
import colors from "../use/colors";

class Result {

    finished = false

    /**
     * @type {Array<Answer>}
     */
    answers
}

class Answer {
    questionId

    text
    optionId

    /**
     * @type {Array}
     */
    optionIds = []
}

class Test {
    id
    name
    scored
    /**
     * @type {Array<Question>}
     */
    questions
}

class Question {
    id

    score
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
}

export default {
    components: {},
    data() {
        return {
            /**
             * @type {Test}
             */
            test: undefined,
            /**
             * @type {Result}
             */
            result: undefined,
            colors: colors
        }
    },
    created() {
        this.loadItem()
    },
    methods: {
        ...mapActions("app", ["showMessage"]),
        loadItem() {
            api.get(`${endpoints.test}/${this.$route.params.id}`)
                .then(resp => {
                    if (resp.errorMessage)
                        this.showMessage(resp.errorMessage)
                    else {
                        this.test = new Test()

                        Object.assign(this.test, resp.data)

                        this.result = new Result()
                        this.result.answers = [...Array(this.test.questions.length)]
                            .map(_ => new Answer())

                        for (let i = 0; i < this.test.questions.length; i++) {
                            this.result.answers[i].questionId = this.test.questions[i].id
                        }
                    }
                })
        },
        save() {
            api.post(`${endpoints.result}/${this.test.id}`, this.result)
                .then(resp => {
                    if (resp.errorMessage)
                        this.showMessage(resp.errorMessage)
                    else {
                        this.result.finished = true
                    }
                })
        },
    }
}
</script>

<style scoped>

</style>