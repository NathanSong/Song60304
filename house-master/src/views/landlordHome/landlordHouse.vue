<template>
  <div>
    <!-- 添加房源对话框 -->
    <el-dialog
      title="添加房源"
      :visible.sync="AdddialogVisible"
      width="40%"
      @close="resetForm"
    >
      <el-form :model="form" label-width="100px">
        <!-- 图片上传 -->
        <el-form-item label="房屋图片">
          <input type="file" @change="onFileChange" ref="fileInput" />
          <img v-if="imagePreview" :src="imagePreview" class="preview-image" />
        </el-form-item>

        <!-- 必填信息 -->
        <el-form-item label="小区名称" required>
          <el-input v-model="form.community"></el-input>
        </el-form-item>

        <el-form-item label="详细地址" required>
          <el-input v-model="form.address"></el-input>
        </el-form-item>

        <el-form-item label="房间面积" required>
          <el-input-number
            v-model="form.area"
            :min="10"
            :step="1"
            controls-position="right"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="楼层" required>
          <el-input-number
            v-model="form.floor"
            :min="1"
            controls-position="right"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="月租金" required>
          <el-input-number
            v-model="form.price"
            :min="0"
            :controls="false"
            prefix="￥"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="房型" required>
          <el-select v-model="form.type">
            <el-option label="一室一厅" value="一室一厅"></el-option>
            <el-option label="两室一厅" value="两室一厅"></el-option>
            <el-option label="三室一厅" value="三室一厅"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="朝向">
          <el-input v-model="form.toward"></el-input>
        </el-form-item>

        <el-form-item label="装修">
          <el-input v-model="form.decoration" placeholder="请输入装修信息"></el-input>
        </el-form-item>

        <el-form-item label="周边信息">
          <el-input type="textarea" v-model="form.surrounding"></el-input>
        </el-form-item>

        <el-form-item label="房东电话">
          <el-input v-model="form.housephone" disabled></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button @click="AdddialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="addSubmit"
          :disabled="!formValid"
        >提交</el-button>
      </span>
    </el-dialog>

    <!-- 房源列表 -->
    <div style="text-align: right; margin-right: 20%;">
      <button @click="addHouseButton" class="addHouse" center>
        <i class="el-icon-circle-plus">添加房源</i>
      </button>
    </div>
    <div class="content">
      <div
        class="card"
        v-for="house in houses"
        :key="house.houseid"
        @click="goToDetail(house.houseid)"
      >
        <img :src="'/images/' + house.image" alt="房屋图片" />
        <div class="card-body">
          
<span
  class="status"
  :class="{
    notRented: house.statue == 0,
    rented: house.statue == 1,
    needsFix: house.statue == 2,
    sold: house.statue == 3,
    purchased: house.statue == 4
  }"
>
  <span v-if="house.statue == 0">未出租</span>
  <span v-else-if="house.statue == 1">已出租</span>
  <span v-else-if="house.statue == 2">需整改</span>
  <span v-else-if="house.statue == 3">已出售</span>
  <span v-else>已购买</span>
</span>


          <p>
            {{ house.community }} <span></span> ￥{{ house.price }} / 月
          </p>
          <p>面积：{{ house.area }}/m²</p>
          <p>房型：{{ house.type }}</p>
          <p>地址：{{ house.address }}</p>
          <p>装修：{{ house.decoration }}</p>
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
      AdddialogVisible: false,
      user: {
        username: "",
        userphone: ""
      },
      houses: [],
      form: {
        statue: 0,
        community: "",
        area: null,
        floor: null,
        price: null,
        address: "",
        type: "",
        toward: "",
        surrounding: "",
        decoration: "普通装修", // 默认装修信息
        housephone: "",
        image: ""
      },
      imagePreview: null
    };
  },
  computed: {
    formValid() {
      return [
        this.form.community,
        this.form.address,
        this.form.area > 0,
        this.form.price > 0,
        this.form.type
      ].every(Boolean);
    }
  },
  created() {
    this.getToken();
    this.getUserHouse();
  },
  methods: {
    addHouseButton() {
      this.AdddialogVisible = true;
      this.form.housephone = this.user.userphone; // 自动填充房东电话
    },
    resetForm() {
      this.imagePreview = null;
      this.form = {
        ...this.$options.data().form,
        housephone: this.user.userphone // 重置表单时保持房东电话
      };
      // 重置文件输入框的值
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = "";
      }
    },
    onFileChange(e) {
      const file = e.target.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append("image", file);

      request({
        method: "post",
        url: "/upload",
        data: formData
      })
        .then((resp) => {
          this.form.image = resp.data.data;
          this.imagePreview = URL.createObjectURL(file);
          this.$message.success("图片上传成功");
        })
        .catch(() => {
          this.$message.error("图片上传失败");
        });
    },
    addSubmit() {
      const payload = {
        ...this.form,
        housename: this.user.username,
        housephone: this.user.userphone // 确保房东电话正确
      };

      request({
        method: "post",
        url: "/landlord/userhouse/add",
        data: payload
      })
        .then(() => {
          this.$message.success("房源添加成功");
          this.AdddialogVisible = false;
          this.getUserHouse();
        })
        .catch(() => {
          this.$message.error("新增失败");
        });
    },
    getToken() {
      const token = localStorage.getItem("token");
      this.user = JSON.parse(token);
      if (!this.user) {
        this.$router.push("/login");
      }
    },
    getUserHouse() {
      request({
        method: "get",
        url: "/landlord/userHouse",
        params: {
          username: this.user.username
        }
      }).then((response) => {
        this.houses = response.data.data;
      });
    },
    goToDetail(houseid) {
      this.$router.push({ path: `userhouse/detail/${houseid}` });
    }
  }
};
</script>

<style>
.preview-image {
  width: 200px;
  height: 150px;
  object-fit: cover;
  margin-top: 10px;
  border-radius: 4px;
}

.addHouse {
  margin-bottom: 10px;
  padding: 8px 10px;
  background-color: #67c23a;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.addHouse:hover {
  background-color: #0056b3;
}

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
  padding: 3px 3px;
  border-radius: 5px;
  font-size: 13px;
  position: absolute;
  line-height: 20px;
  top: 10px;
  right: 10px;
  z-index: 1;
}

.status.rented {
  background-color: red;
}

.status.needsFix {
  background-color: #666; /* 需整改 */
}

p {
  font-size: 16px;
  margin: 5px 0;
  line-height: 20px;
  text-align: left;
}
</style>
