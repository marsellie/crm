<template>
  <v-container>
    <v-dialog width="450" v-if="editDepartmentForm.state" v-model="editDepartmentForm.state">
      <v-card>
        <v-card-title>
          Редактирование кафедры
        </v-card-title>
        <v-divider/>
        <div class="pa-4">
          <v-text-field
              v-model="editDepartmentForm.name"
              placeholder="Название кафедры"
              single-line dense hide-details
              outlined
          />
        </div>
        <v-divider/>
        <v-card-actions>
          <v-btn
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

    <v-dialog width="450" v-if="deleteDepartment.state" v-model="deleteDepartment.state">
      <v-card>
        <v-card-title>
          Удалить кафедру?
          <v-spacer/>
          <v-btn
              @click="deleteDepartment.state = false"
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
              :href="'/department/' + item.id"
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
          <v-toolbar flat>
            <v-col cols="2">
              <v-file-input
                  v-model="imported"
                  :color="colors.button"
                  placeholder="импорт в бд"
                  hide-details single-line dense
                  outlined
              >
                импорт в бд
              </v-file-input>
            </v-col>
            <v-btn
                :color="colors.button"
                @click="exportFromDb"
                class="ml-4"
                outlined
            >
              экспорт из бд
            </v-btn>
          </v-toolbar>
          <v-expansion-panels flat tile focusable>
            <v-expansion-panel>
              <v-expansion-panel-header>
                <b>
                  Форма добавления кафедры
                </b>
              </v-expansion-panel-header>
              <v-expansion-panel-content>
                <v-col :cols="$vuetify.breakpoint.smAndUp ? 5 : 12" class="pa-0">
                  <v-text-field
                      v-model="newDepartmentForm.name"
                      class="pt-4"
                      placeholder="Название кафедры"
                      single-line dense hide-details
                      outlined
                  />
                </v-col>
                <v-btn
                    @click="create"
                    :disabled="newDepartmentForm.name.length === 0"
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
  watch: {
    imported(newVal) {
      if (!newVal)
        return

      let reader = new FileReader()
      reader.readAsText(newVal, "UTF-8")
      reader.onloadend = (evt) => {
        JSON.parse(reader.result).forEach(
            department => api.post(endpoints.department, department)
                .then(resp => {
                  if (resp.errorMessage)
                    this.showMessage(resp.errorMessage)
                  else {
                    this.showMessage("Импорт успешно завершен")
                    this.loadItems()
                  }
                })
        )
        this.imported = undefined
      }
    }
  },
  data() {
    return {
      imported: undefined,
      table: {
        items: [],
        headers: [
          {text: 'Название кафедры', value: 'name'},
          {text: 'Действия', value: 'actions', align: 'right'}
        ],
        search: '',
      },
      deleteDepartment: {
        id: undefined,
        state: false
      },
      editDepartmentForm: {
        state: false,
        id: '',
        name: ''
      },
      newDepartmentForm: {
        name: ''
      },
      colors: colors
    }
  },
  created() {
    this.loadItems()
  },
  methods: {
    ...mapActions("app", ["showMessage"]),
    exportFromDb() {
      let txtContent = JSON.stringify(this.table.items)
      let link = window.document.createElement("a");
      link.setAttribute("href", "data:text/plain;charset=utf-8,%EF%BB%BF" + encodeURI(txtContent));
      link.setAttribute("download", "data.txt");
      link.click();
    },
    setDeleteItem(item) {
      this.deleteDepartment.id = item.id
      this.deleteDepartment.state = true
    },
    deleteItem() {
      api.delete(endpoints.department + "/" + this.deleteDepartment.id)
          .then(resp => {
            if (resp.errorMessage)
              this.showMessage(resp.errorMessage)
            else {
              this.showMessage("Кафедра успешно удалена")
              this.loadItems()
              this.deleteDepartment.state = false
            }
          })
    },
    setEditForm(item) {
      this.editDepartmentForm.id = item.id
      this.editDepartmentForm.name = item.name
      this.editDepartmentForm.state = true
    },
    saveEdit() {
      api.put(endpoints.department,
          {
            id: this.editDepartmentForm.id,
            name: this.editDepartmentForm.name
          })
          .then(resp => {
            if (resp.errorMessage)
              this.showMessage(resp.errorMessage)
            else {
              this.showMessage("Кафедра успешно изменена")
              this.loadItems()
              this.editDepartmentForm.state = false
            }
          })
    },
    loadItems() {
      api.get(endpoints.department)
          .then(resp => {
            this.table.items = resp.data
          })
    },
    create() {
      api.post(endpoints.department, this.newDepartmentForm)
          .then(resp => {
            if (resp.errorMessage)
              this.showMessage(resp.errorMessage)
            else {
              this.showMessage("Кафедра добавлена")
              this.loadItems()
              this.newDepartmentForm = {name: ''}
            }
          })
    }
  }
}
</script>