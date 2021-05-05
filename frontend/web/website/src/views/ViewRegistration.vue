<template>
  <div class="profile-page">
      <section class="section-profile-cover section-shaped my-0">
          <div class="shape shape-style-1 shape-primary shape-skew alpha-4">
              <span></span>
              <span></span>
              <span></span>
              <span></span>
              <span></span>
              <span></span>
              <span></span>
          </div>
      </section>
      <section class="section section-skew">
          <div class="container">
              <card shadow class="card-profile mt--300" no-body>
                <form role="form" class="text-center border border-light p-5" >

                  <p class="h4 mb-4">Phòng Lab đã đăng ký</p>
                
                  <table class="table table-bordered">
                    <thead>
                      <tr>
                        <th scope="col">STT</th>
                        <th scope="col">Ngày đăng ký</th>
                        <th scope="col">Trạng thái đăng ký</th>
                        <th scope="col"></th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(reg, idx) in registrations" :key="idx">
                        <th scope="row">{{idx+1}}</th>
                        <td>{{ formatDate(reg.createdAt) }}</td>

                        <td v-if="reg.status==1">Thành công</td>
                        <td v-if="reg.status==0">Thất bại</td>
                        <td v-if="reg.status==2">Đang xử lý</td>
                        <td v-if="reg.status==-1">Đã hủy</td>
                        <td>
                          <template v-if="reg.status == 1">
                            <button class="btn btn-success btn-sm" type="submit"><router-link :to="{ name: 'regisInfo', params: { id: reg.id }}" style="color:black">Chi tiết</router-link></button>
                          </template>
                          <template v-if="reg.status == 0">
                            <button disabled class="btn btn-success btn-sm" type="submit">Xem kết quả</button>
                          </template>
                          <template v-if="reg.status == 2">
                            <button class="btn btn-primary btn-sm" type="submit"><router-link :to="{ name: 'change', params: { registration: reg }}" style="color:black">Chỉnh sửa</router-link></button>
                            <button class="btn btn-danger btn-sm" v-on:click="cancelRegister(reg.id)">Hủy bỏ</button>
                          </template>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                
                    
                </form>
                <!-- Default form contact -->

              </card>
          </div>
      </section>
  </div>
</template>
<script>
import {getAll} from '../api/registration'
import {cancel} from '../api/registration'

export default {
  name: "ViewRegistration",
  data(){
    return {
      registrations: undefined
    }
  },
  created(){
    this.getList();
  },
  methods:{
    getList(){
      getAll().then(resp => {
        this.registrations = resp.data.registrations;
      })
    },
    cancelRegister(id){
      if(confirm("Bạn có chắc chắn muốn hủy đăng kí?")){
        cancel(id).then(resp => {
          alert(resp.msg)
          this.getList();
        })
      }
    },
    formatDate(d){
      d = new Date(d)
      const ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(d)
      const mo = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(d)
      const da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(d)
      return ye+'-'+mo+'-'+da
    }
  }
};
</script>
<style>
</style>

