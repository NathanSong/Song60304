<template>
  <div>
    <!-- 房源列表 -->
    <div class="content">
      <div class="card" v-for="house in houses" :key="house.houseid" @click="goToDetail(house.houseid)">
        <img :src="'/images/' + house.image" alt="房屋图片">
        <div class="card-body">
          <!-- 房屋状态 -->
          <span
            class="status"
            :class="{
              notRented: house.statue == 0,
              rented: house.statue == 1,
              needsFix: house.statue == 2,
              purchased: house.statue == 3
            }"
          >
            <span v-if="house.statue == 0">未出租</span>
            <span v-else-if="house.statue == 1">已出租</span>
            <span v-else-if="house.statue == 2">需整改</span>
            <span v-else-if="house.statue == 3">已购买</span>
          </span>

          <p>{{ house.community }} <span></span> ￥{{ house.price }} / 月</p>
          <p>面积：{{ house.area }} m²</p>
          <p>房型：{{ house.type }}</p>
          <p>地址：{{ house.address }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      user: {
        username: "",
      },
      houses: [
        {
          houseid: 1,
          statue: 0,
          area: 20,
          floor: 1,
          price: 2370,
          community: '华策中心城',
          address: '三号线 吉祥站 花园小区',
          type: '一室一厅',
          toward: '正南朝向',
          surrounding: '近地铁',
          decoration: '精装修',
          housename: '张三',
          housephone: '13812345678',
          tenantname: '李四',
          image: 'https://via.placeholder.com/300x150',
        },
      ],
    };
  },
  created() {
    this.getToken();
    this.getUserHouse();
  },
  methods: {
    getToken() {
      const token = localStorage.getItem("token");
      this.user = JSON.parse(token);
      if (this.user == null) {
        this.$router.push("/login");
      }
    },
    // 查询用户的房子-每次进入页面都会调用
    getUserHouse() {
      request({
        method: "get",
        url: "/userHome/userHouse",
        params: {
          username: this.user.username,
        }
      }).then((response) => {
        this.houses = response.data.data;
      });
    },
    goToDetail(houseid) {
      this.$router.push({ path: `userhouse/detail/${houseid}` });
    },
  },
}
</script>

<style>
.content {
  margin-left: 10px;
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
  width: 350px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.card:hover {
  transform: scale(1.02);
}

.card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  display: block;
}

.card-body {
  margin: 5px;
  padding: 15px;
  box-sizing: border-box;
}

.status {
  background-color: green;
  color: white;
  padding: 3px 6px;
  border-radius: 5px;
  font-size: 13px;
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 1;
}
.status.notRented {
  background-color: green; /* 未出租 */
}
.status.rented {
  background-color: red; /* 已出租 */
}
.status.needsFix {
  background-color: #666; /* 需整改 */
}
.status.purchased {
  background-color: #007bff; /* 已购买 */
}

p {
  font-size: 16px;
  margin: 5px 0;
  line-height: 20px;
  text-align: left;
}
</style>
