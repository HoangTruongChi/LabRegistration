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

                  <p class="h4 mb-4">Thông tin các phòng</p>
                  
                  <div class="row">
                      <div class="col-md-4">
                        <div class="form-group">
                          <label>Ngày sử dụng:</label>
                          <input v-model="date" class="form-control" type="date" @change="getAll()">
                        </div>
                      </div>

                      <div class="col-md-4">
                        <div class="form-group">
                          <label>Thời gian bắt đầu:</label>
                          <input @change="getAll()" v-model="startTime" type="text" class="form-control" placeholder="Start">
                        </div>
                      </div>


                      <div class="col-md-4">
                        <div class="form-group">
                          <label>Thời gian kết thúc:</label>
                          <input @change="getAll()" v-model="endTime" type="text" class="form-control" placeholder="End">
                        </div>
                      </div>
                  </div>

                  <div class="row"> </div>

                  <form class="text-center border border-light p-5">
                    <div class="row">
                      <div class="col-3" v-for="(room, idx) in rooms" :key="idx">
                        <div class="form-group">
                          <button v-if="room.maxPeople > 5" type="button" class="btn btn-success btn-block">{{room.name}}: {{room.maxPeople}} slot</button>
                          <button v-if="room.maxPeople <= 5" type="button" class="btn btn-warning btn-block">{{room.name}}: {{room.maxPeople}} slot</button>
                        </div>
                      </div>
                    </div>
                  </form>
                </form>
              </card>
          </div>
      </section>
  </div>
</template>

<script>

import {getAvailableInRoom} from "../api/room";

export default {
  name: "RoomInfo",
  data(){
    return {
      rooms: undefined,
      date: new Date(),
      startTime: 6,
      endTime: 18
    }
  },
  created(){
    this.getAll()
  },
  methods: {
    getAll(){
      if(this.date === undefined || this.date === null){
        this.date = new Date()
      }

      if(this.startTime === undefined || this.startTime === null){
        this.startTime = 6
      }

      if(this.endTime === undefined || this.endTime === null){
        this.endTime = 18
      }

      getAvailableInRoom(this.date, this.startTime, this.endTime).then(resp => {
        this.rooms = resp.data.rooms;
        console.log(this.rooms);
      })
    }
  }
}
</script>

<style >
</style>
