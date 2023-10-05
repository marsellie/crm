<template>
    <div>
        <v-card outlined tile>
            <v-card-title>
                {{ result.user }}
            </v-card-title>

            <div v-if="test.scored">
                <v-divider/>

                <v-card-subtitle>
                    Общее количество баллов: {{ result.total }} / {{ test.total }}
                </v-card-subtitle>
            </div>
        </v-card>

        <div
            class="pt-4"
            v-for="(answer, index) in result.answers"
        >
            <v-card outlined tile>
                <v-card-text class="text-h6">
                    Вопрос {{ index + 1 }}: {{ test.questions[index].value }}
                </v-card-text>


                <div class="pa-4" v-if="test.scored">
                    <v-divider/>

                    <v-row align="center" class="ma-0 pa-0 pt-4">
                        <v-col class="ma-0 pa-0" cols="2">
                            Максимальное количество баллов:
                        </v-col>

                        <v-col class="ma-0 pa-0" cols="1">
                            {{ test.questions[index].score }}
                        </v-col>
                    </v-row>

                    <v-row align="center" class="ma-0 pa-0 pt-4">
                        <v-col class="ma-0 pa-0" cols="2">
                            Выставленное количество баллов:
                        </v-col>

                        <v-col
                            v-if="editable"
                            class="ma-0 pa-0"
                            cols="1"
                        >
                            <v-text-field
                                v-model="resultForm.answers[index].score"
                                type="number"
                                clearable
                                single-line dense hide-details
                                outlined
                            />
                        </v-col>

                        <v-col
                            v-if="!editable"
                            class="ma-0 pa-0"
                            cols="2"
                        >
                            {{ resultForm.answers[index].score }}
                        </v-col>
                    </v-row>
                </div>

                <v-divider/>

                <div v-if="test.questions[index].type === 'Текстовый вопрос'">
                    <v-card-text>
                        {{ answer.text }}
                    </v-card-text>
                </div>

                <div v-if="test.questions[index].type === 'Одиночный выбор'">
                    <v-radio-group
                        v-model="answer.optionId"
                        class="ma-0 pa-4"
                        readonly
                        single-line dense hide-details
                        outlined
                    >
                        <v-radio
                            v-for="option in test.questions[index].options"
                            :key="option.id"
                            :value="option.id"
                            :label="option.value"
                            :color="option.valid ? 'green' : 'red'"
                            readonly
                            single-line dense hide-details
                            outlined
                        >
                            <span
                                slot="label"
                                :class="option.id === answer.optionId || option.valid ? (option.valid ? 'green--text' : 'red--text') : 'theme--light'"
                            >
                                {{ option.value }}
                            </span>
                        </v-radio>
                    </v-radio-group>
                </div>

                <div v-if="test.questions[index].type === 'Множественный выбор'">
                    <div class="pa-4">
                        <v-checkbox
                            v-for="option in test.questions[index].options"
                            :input-value="answer.optionIds.includes(option.id) ? '1' : undefined"
                            :color="option.valid ? 'green' : 'red'"
                            readonly
                            single-line dense hide-details
                            outlined
                        >
                            <span
                                slot="label"
                                :class="answer.optionIds.includes(option.id) || option.valid ? (option.valid ? 'green--text' : 'red--text') : 'theme--light'"
                            >
                                {{ option.value }}
                            </span>
                        </v-checkbox>
                    </div>
                </div>
            </v-card>
        </div>

        <div class="pt-4" v-if="editable">
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
    </div>
</template>

<script>
import colors from "../use/colors"
import api from "../use/api";
import endpoints from "../use/endpoints";
import {mapActions} from "vuex";

class ResultForm {

    /**
     * @type {Array<AnswerForm>}
     */
    answers = []
}

class AnswerForm {
    id
    score
}

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
    props: {
        /**
         * @type {Result}
         */
        result: {},
        /**
         * @type {Test}
         */
        test: {},
        editable: {},
        callback: {}
    },
    data() {
        return {
            resultForm: new ResultForm(),
            colors: colors
        }
    },
    created() {
        this.resultForm.answers = this.result.answers.map(a => {
            let answer = new AnswerForm();
            answer.id = a.id
            answer.score = a.total
            return answer
        })
    },
    methods: {
        ...mapActions("app", ["showMessage"]),
        save() {
            api.put(`${endpoints.result}/${this.result.id}`, this.resultForm)
                .then(resp => {
                    if (resp.errorMessage)
                        this.showMessage(resp.errorMessage)
                    else {
                        this.showMessage("Результат сохранен")
                        this.$emit('reload-results')
                    }
                })
        }
    }
}
</script>