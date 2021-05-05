<template>
    <div class="profile-page">
        <section class="section-profile-cover section-shaped my-0">
            <div class="shape shape-style-1 shape-primary shape-skew alpha-4">
                <span></span>
                <span></span>
                <span></span>
            </div>
        </section>
        <div class="container"> 
            <card shadow class="card-profile mt--300" no-body>
                <form role="form" class="text-center border border-light p-5" >

                    <p class="h1 mb-3">Thông tin thiết bị</p>

                    <p class="text-left h6 mb-3">Mã phòng: {{room.code}}</p> 
                    <p class="text-left h6 mb-3">Tên phòng: {{room.name}}</p>  
           
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th scope="column" class="text-center">STT</th>
                                <th scope="column" class="text-center">Tên thiết bị</th>
                                <th scope="column" class="text-center">Số lượng</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(equip, idx) in room.equipmentOfRooms" :key="idx">
                                <th scope="row" class="text-center">{{idx + 1}}</th>
                                <td>{{equip.equipment.name}}</td>
                                <td>{{equip.amount}}</td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </card>
        </div>
    </div>
</template>

<script>
export default {
    name: "Model",
    data(){
        return {
            room: undefined
        }
    },
    created(){
        this.room = this.$route.params.roomInfo;
    }
}
</script>

<style>

</style>