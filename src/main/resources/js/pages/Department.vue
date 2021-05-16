<template>
  <v-container v-if="department">
    <v-btn
        :color="colors.button"
        :ripple="false"
        x-large plain
        href="/"
    >
      <v-icon>
        chevron_left
      </v-icon>
      Назад
    </v-btn>
    <v-dialog width="450" v-if="editTeacherForm.state" v-model="editTeacherForm.state">
      <v-card>
        <v-card-title>
          Редактирование преподавателя
        </v-card-title>
        <v-divider/>
        <div class="pa-4">
          <v-text-field
              v-model="editTeacherForm.name"
              placeholder="ФИО преподавателя"
              class="pb-4"
              single-line dense hide-details
              outlined
          />
          <v-text-field
              v-model="editTeacherForm.position"
              placeholder="Должность"
              class="pb-4"
              single-line dense hide-details
              outlined
          />
          <v-text-field
              v-model="editTeacherForm.workingRate"
              placeholder="Ставка"
              single-line dense hide-details
              outlined
          />
        </div>
        <v-divider/>
        <v-card-actions>
          <v-btn
              :color="colors.button"
              :disabled="!isEditValid"
              style="width: 100%;"
              @click="saveEdit"
              outlined
          >
            сохранить
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog width="450" v-if="deleteTeacher.state" v-model="deleteTeacher.state">
      <v-card>
        <v-card-title>
          Удалить преподавателя?
          <v-spacer/>
          <v-btn
              @click="deleteTeacher.state = false"
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
          :search="table.search"
      >
        <template v-slot:item.actions="{ item }">
          <v-btn
              :color="colors.button"
              :href="'/teacher/' + item.id"
              outlined
          >
            перейти
          </v-btn>
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
            Кафедра {{ department.name }}
          </v-card-title>
          <v-expansion-panels flat tile focusable>
            <v-expansion-panel>
              <v-expansion-panel-header>
                <b>
                  Форма добавления преподавателя
                </b>
              </v-expansion-panel-header>
              <v-expansion-panel-content>
                <v-col :cols="$vuetify.breakpoint.smAndUp ? 5 : 12" class="pa-0">
                  <v-text-field
                      v-model="newTeacherForm.name"
                      class="pt-4"
                      placeholder="ФИО преподавателя"
                      single-line dense hide-details
                      outlined
                  />
                  <v-text-field
                      v-model="newTeacherForm.position"
                      class="pt-4"
                      placeholder="Должность"
                      single-line dense hide-details
                      outlined
                  />
                  <v-text-field
                      v-model="newTeacherForm.workingRate"
                      class="pt-4"
                      placeholder="Ставка"
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
      department: undefined,
      table: {
        search: '',
        items: [],
        headers: [
          {text: 'ФИО', value: 'name'},
          {text: 'Должность', value: 'position'},
          {text: 'Ставка', value: 'workingRate'},
          {text: 'Действия', value: 'actions', align: 'right'},
        ],
      },
      deleteTeacher: {
        id: undefined,
        state: false
      },
      editTeacherForm: {
        state: false,
        id: '',
        name: '',
        position: '',
        workingRate: ''
      },
      newTeacherForm: {
        name: '',
        position: '',
        workingRate: 0.5
      },
      colors: colors
    }
  },
  computed: {
    isNewValid() {
      return this.newTeacherForm.name.length !== 0
          && this.newTeacherForm.position.length !== 0
          && this.newTeacherForm.workingRate.length !== 0
          && parseFloat(this.newTeacherForm.workingRate) > 0
          && parseFloat(this.newTeacherForm.workingRate) <= 1.5
    },
    isEditValid() {
      return this.editTeacherForm.name.length !== 0
          && this.editTeacherForm.position.length !== 0
          && this.editTeacherForm.workingRate.length !== 0
          && parseFloat(this.editTeacherForm.workingRate) > 0
          && parseFloat(this.editTeacherForm.workingRate) <= 1.5
    }
  },
  created() {
    this.loadItems()
  },
  methods: {
    ...mapActions("app", ["showMessage"]),
    setDeleteItem(item) {
      this.deleteTeacher.id = item.id
      this.deleteTeacher.state = true
    },
    deleteItem() {
      api.delete(endpoints.teacher +"/" + this.department.id + "/" + this.deleteTeacher.id)
          .then(resp => {
            if (resp.errorMessage)
              this.showMessage(resp.errorMessage)
            else {
              this.showMessage("Преподаватель успешно удален")
              this.loadItems()
              this.deleteTeacher.state = false
            }
          })
    },
    setEditForm(item) {
      this.editTeacherForm.id = item.id
      this.editTeacherForm.name = item.name
      this.editTeacherForm.workingRate = item.workingRate
      this.editTeacherForm.position = item.position
      this.editTeacherForm.state = true
    },
    saveEdit() {
      api.put(endpoints.teacher,
          {
            id: this.editTeacherForm.id,
            name: this.editTeacherForm.name,
            position: this.editTeacherForm.position,
            workingRate: this.editTeacherForm.workingRate
          })
          .then(resp => {
            if (resp.errorMessage)
              this.showMessage(resp.errorMessage)
            else {
              this.showMessage("Преподаватель успешно изменён")
              this.loadItems()
              this.editTeacherForm.state = false
            }
          })
    },
    loadItems() {
      api.get(endpoints.department + "/" + this.$route.params.id)
          .then(resp => {
            this.department = resp.data
            this.table.items = resp.data.teachers
          })
    },
    create() {
      api.post(endpoints.teacher + "/" + this.department.id, this.newTeacherForm)
          .then(resp => {
            if (resp.errorMessage)
              this.showMessage(resp.errorMessage)
            else {
              this.showMessage("Преподаватель добавлен")
              this.loadItems()
              this.newTeacherForm = {
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