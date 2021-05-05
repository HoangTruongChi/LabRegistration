<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.key" placeholder="Name" style="width: 500px;padding-right:20px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="fas fa-search" @click="handleFilter">
        Search
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="fas fa-edit" @click="handleCreate">
        Add
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="fas fa-download" @click="handleDownload">
        Export
      </el-button>
      <el-checkbox v-model="showDelete" class="filter-item" style="margin-left:15px;" @change="viewDelete()">
        View deleted
      </el-checkbox>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="ID" prop="id"  align="center" width="50" >
        <template slot-scope="{row}">
          <span>{{ row.countryId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name"  align="center" width="110" >
        <template slot-scope="{row}">
          <span>{{ row.countryName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Code"  align="center" width="80" >
        <template slot-scope="{row}">
          <span>{{ row.countryCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Prefix Number"  align="center" width="130" >
        <template slot-scope="{row}">
          <span>{{ row.countryPrefixNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Add Prefix"  align="center" width="130" >
        <template slot-scope="{row}">
          <span>{{ row.addPrefix }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Description"  align="center" width="120" >
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Delete"  align="center" width="80" >
        <template slot-scope="{row}">
          <span v-if="row.delete">True</span>
          <span v-if="!row.delete">False</span>
        </template>
      </el-table-column>

      <el-table-column label="Actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="!row.delete" type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <el-popconfirm
            v-if="!row.delete"
            confirmButtonText='OK'
            cancelButtonText='No, Thanks'
            icon="fas fa-info-circle"
            iconColor="red"
            title="Are you sure to delete this?"
            @onConfirm="handleDelete(row,$index,1)"
          >
            <el-button slot="reference"  size="mini" type="danger">
              Delete
            </el-button>
          </el-popconfirm>

          <el-popconfirm
            v-if="row.delete"
            confirmButtonText='OK'
            cancelButtonText='No, Thanks'
            icon="fas fa-info-circle"
            iconColor="red"
            title="Are you sure to restore this?"
            @onConfirm="handleDelete(row,$index,0)"
          >
            <el-button slot="reference"  size="mini" type="danger">
              Restore
            </el-button>
          </el-popconfirm>
          
          
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" center>
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="130px" style="width: 400px; margin-left:50px;">
        <el-form-item v-show="dialogStatus === 'update'" label="Id" prop="countryId">
          <el-input v-model="temp.countryId"  :disabled="true"/>
        </el-form-item>
        <el-form-item label="Name" prop="countryName">
          <el-input v-model="temp.countryName" />
        </el-form-item>
        <el-form-item label="Code" prop="countryCode">
          <el-input v-model="temp.countryCode" />
        </el-form-item>
        <el-form-item label="Prefix Number" prop="countryPrefixNumber">
          <el-input v-model="temp.countryPrefixNumber" />
        </el-form-item>
        <el-form-item label="Add Prefix" prop="addPrefix">
          <el-input v-model="temp.addPrefix" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="temp.description" />
        </el-form-item>
        <el-form-item v-show="dialogStatus === 'update'" label="Delete" prop="delete">
          <el-select v-model="temp.delete" class="filter-item" placeholder="Please select">
            <el-option v-for="item in deleteOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchCountries, createCountry, updateCountry, deleteCountry } from '@/api/country'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination


const deleteOptions = [
  {key: 1, display_name: 'True'},
  {key: 0, display_name: 'False'}
]


export default {
  name: 'Room',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      dialogConfirmDeleteVisible: false,
      dialogConfirmRestoreVisible: false,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        deleted: 0,
        page: 1,
        limit: 10,
        key: null
      },
      deleteOptions,
      showDelete: false,
      temp: {
        countryId: undefined,
        countryName: "",
        countryCode: "",
        countryPrefixNumber: "",
        addPrefix: "",
        description: "",
        delete: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      downloadLoading: false,
      rules: {
          countryName: [
            { required: true, message: 'countryName is required', trigger: 'blur' }
          ],
          countryCode: [
            { required: true, message: 'countryCode is required', trigger: 'blur' }
          ],
          countryPrefixNumber: [
            { required: true, message: 'countryPrefixNumber is required', trigger: 'blur' }
          ],
          // addPrefix: [
          //   { required: true, message: 'addPrefix is required', trigger: 'blur' }
          // ],
          description: [
            { required: true, message: 'description is required', trigger: 'blur' }
          ],
          delete: [
            { required: true, message: 'description is required', trigger: 'change' }
          ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchCountries(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0 * 1000)
      })
    },
    resetTemp() {
      this.temp = {
        countryId: undefined,
        countryName: "",
        countryCode: "",
        countryPrefixNumber: "",
        addPrefix: "",
        description: "",
        delete: 0
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.delete = 0
          this.temp.countryId = 0
          createCountry(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }else{
          console.log("Not valid")
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCountry(tempData).then(() => {
            const index = this.list.findIndex(v => v.countryId === this.temp.countryId)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }else{
          console.log("Not valid")
        }
      })
    },
    handleDelete(row, index, deleted) {
      // let msg = "Are you sure to restore it?"
      // if(deleted){
      //   msg = "Are you sure to delete it?"
      // }
      // if(confirm(msg)){
        deleteCountry(row.countryId, deleted).then(() => {
          if(deleted){
            this.$notify({
              title: 'Success',
              message: 'Delete Successfully',
              type: 'success',
              duration: 2000
            })
          }else{
            this.$notify({
              title: 'Success',
              message: 'Restore Successfully',
              type: 'success',
              duration: 2000
            })
          }
          
          this.list.splice(index, 1)
        })
      //}
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Country Id', 'Country Name', 'Country Code', 'Country Prefix Number', 'Add Prefix', 'Description', 'Delete']
        const filterVal = ['countryId', 'countryName', 'countryCode', 'countryPrefixNumber', 'addPrefix', 'description', 'delete']
        const data = this.formatJson(filterVal)
        console.log(data)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'countries_list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    viewDelete: function(){
      this.listQuery.deleted = this.showDelete?1:0
      this.getList()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
  }
}
</script>
