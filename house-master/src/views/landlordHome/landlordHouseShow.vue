<template>
  <div>
    <div class="filter-bar">
      <input v-model="filters.community" type="text" placeholder="输入小区名" />
      <select v-model="filters.statue">
        <option value="">全部</option>
        <option value="0">待租</option>
        <option value="1">已出租</option>
        <option value="2">需整改</option>
      </select>
      <input v-model.number="filters.minPrice" type="number" placeholder="最低价格" class="small-input" />
      <input v-model.number="filters.maxPrice" type="number" placeholder="最高价格" class="small-input" />
      <input v-model.number="filters.minFloor" type="number" placeholder="最低楼层" class="small-input" />
      <input v-model.number="filters.maxFloor" type="number" placeholder="最高楼层" class="small-input" />
      <input v-model.number="filters.minArea" type="number" placeholder="最小面积" class="small-input" />
      <input v-model.number="filters.maxArea" type="number" placeholder="最大面积" class="small-input" />
      <button @click="applyFilters">
        <i class="el-icon-search"></i>
        查询
      </button>
    </div>

    <div class="content">
      <template v-if="filteredHouses.length">
        <div class="card" v-for="house in filteredHouses" :key="house.houseid" @click="goToDetail(house.houseid)">
          <img :src="'/images/' + house.image" alt="房屋图片" />
          <div class="card-body">
            <span class="status" :class="statusClass(house)">{{ statusText(house) }}</span>
            <p v-if="house.isMine" class="mine-label">我的房源</p>
            <p>{{ house.community }} <span>￥{{ house.price }} / 月</span></p>
            <p>面积：{{ house.area }} m² | 楼层：{{ house.floor }}</p>
            <p>房型：{{ house.type }}</p>
            <p>地址：{{ house.address }}</p>
          </div>
        </div>
      </template>
      <p v-else class="no-results">未找到符合条件的房源</p>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      user: {},
      houses: [],
      filters: {
        community: "",
        statue: "",
        minPrice: null,
        maxPrice: null,
        minFloor: null,
        maxFloor: null,
        minArea: null,
        maxArea: null,
      },
    };
  },
  computed: {
    filteredHouses() {
      return this.houses.filter((house) => {
        return Object.entries(this.filters).every(([key, value]) => {
          if (!value) return true;
          if (key === "statue") return house.statue === Number(value);
          if (key.includes("min")) return house[key.replace("min", "").toLowerCase()] >= value;
          if (key.includes("max")) return house[key.replace("max", "").toLowerCase()] <= value;
          return house[key].includes(value);
        });
      });
    },
    statusClass() {
      return (house) => {
        if (house.statue === 2) return "needsFix";
        return house.statue === 0 ? "notRented" : house.statue === 1 && this.user.username === house.tenantname ? "myHouse" : "rented";
      };
    },
    statusText() {
      return (house) => {
        if (house.statue === 2) return "需整改";
        return house.statue === 0 ? "待租" : house.statue === 1 && this.user.username === house.tenantname ? "本人居住" : "已出租";
      };
    },
  },
  created() {
    this.getToken();
    this.getHouses();
  },
  methods: {
    getToken() {
      const token = localStorage.getItem("token");
      this.user = JSON.parse(token);
      console.log("当前用户名:", this.user.username); // 打印调试信息
    },
    getHouses() {
      request({
        method: "get",
        url: "/landlord/houseShow",
      })
        .then((resp) => {
          const allHouses = resp.data.data || [];
          // 标记是否为自己的房源
          this.houses = allHouses.map((house) => {
            house.isMine = house.housename === this.user.username; // 假设 housename 是房东的名字
            return house;
          });
        })
        .catch((error) => {
          console.error("获取房源失败:", error);
        });
    },
    goToDetail(houseid) {
      this.$router.push(`/landlordHome/userhouse/detail/${houseid}`);
    },
    applyFilters() {
      this.$forceUpdate();
    },
  },
};
</script>

<style scoped>
.filter-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.filter-bar input,
.filter-bar select {
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}
.filter-bar .small-input {
  width: 100px;
}
.filter-bar button {
  padding: 5px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.filter-bar button:hover {
  background-color: #0056b3;
}
.content {
  padding: 20px;
}
.card {
  position: relative;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 20px;
}
.card img {
  width: 100%;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
}
.card-body {
  padding: 10px;
}
.no-results {
  text-align: center;
  color: gray;
}
.status {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: green;
  color: white;
  padding: 3px 6px;
  border-radius: 5px;
  font-size: 13px;
}
.status.rented {
  background-color: red;
}
.status.myHouse {
  background-color: orange;
}
.mine-label {
  color: #67c23a;
  font-weight: bold;
}
</style>
