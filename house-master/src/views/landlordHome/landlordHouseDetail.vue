<template>
  <div class="house-details">
    <el-dialog title="修改信息" :visible.sync="updatedialogVisible" width="40%" :before-close="handleClose">
      <el-form ref="form" :model="house" label-width="80px">
        <el-form-item label="小区">
          <el-input v-model="house.community" placeholder="请输入小区" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="房屋面积">
          <el-input v-model="house.area" placeholder="请输入房屋面积" show-word-limit />
        </el-form-item>
        <el-form-item label="楼层">
          <el-input v-model="house.floor" placeholder="请输入楼层" show-word-limit />
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="house.price" placeholder="请输入价格" show-word-limit />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="house.address" placeholder="请输入地址" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="户型">
          <el-input v-model="house.type" placeholder="请输入户型" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="朝向">
          <el-input v-model="house.toward" placeholder="请输入朝向" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="周边">
          <el-input v-model="house.surrounding" placeholder="请输入周边" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="装修">
          <el-input v-model="house.decoration" placeholder="请输入装修" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="房东电话">
          <el-input v-model="house.housephone" placeholder="请输入房东电话" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="图片">
          <input type="file" @change="onFileChange" required />
          <img v-if="imagePreview" :src="imagePreview" alt="预览" width="150" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updatedialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateHouse">确定</el-button>
      </span>
    </el-dialog>

    <div class="house-header">
      <img :src="'/images/' + house.image" alt="房屋图片" class="house-image" />
    </div>
    <div class="house-info">
      <h2>房屋详情</h2>
      <ul>
        <li><strong>面积：</strong>{{ house.area }} 平方米</li>
        <li><strong>楼层：</strong>{{ house.floor }} 层</li>
        <li><strong>租金：</strong>{{ house.price }} 元/月</li>
        <li><strong>小区：</strong>{{ house.community }}</li>
        <li><strong>地址：</strong>{{ house.address }}</li>
        <li><strong>户型：</strong>{{ house.type }}</li>
        <li><strong>朝向：</strong>{{ house.toward }}</li>
        <li><strong>周边：</strong>{{ house.surrounding }}</li>
        <li><strong>装修：</strong>{{ house.decoration }}</li>
      </ul>
    </div>

    <div class="contact-info">
      <h2>联系方式</h2>
      <p><strong>房东姓名：</strong>{{ house.housename }}</p>
      <p><strong>联系电话：</strong>{{ house.housephone }}</p>
      <p v-if="house.tenantname !== ''"><strong>租客姓名：</strong>{{ house.tenantname }}</p>
    </div>

    <div class="action-buttons">
      <button v-if="this.user.username == house.housename" @click="editHouseButton" class="edit-button">
        <i class="el-icon-edit"></i>
        修改信息
      </button>
      <button v-if="this.user.username == house.housename" @click="deleteHouse" class="delete-button">
        <i class="el-icon-delete"></i>
        删除房源
      </button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "LandlordHouseDetail",
  data() {
    return {
      updatedialogVisible: false,
      imagePreview: null,
      house: {},
      user: {},
    };
  },
  created() {
    this.getToken();
    this.house.houseid = this.$route.params.houseid;
    this.getHouseDetail();
  },
  methods: {
    getToken() {
      const token = localStorage.getItem("token");
      this.user = JSON.parse(token);
      if (!this.user) {
        this.$router.push("/login");
      }
    },
    getHouseDetail() {
      request({
        method: "get",
        url: "/landlord/userhouse/detail",
        params: { houseid: this.house.houseid },
      }).then((resp) => {
        this.house = resp.data.data;
      });
    },
    deleteHouse() {
      this.$confirm("此操作将永久删除该房源, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return request({
            method: "delete",
            url: "/landlord/userhouse/delete",
            params: { houseid: this.house.houseid },
          });
        })
        .then(() => {
          this.$message.success("删除成功");
          this.$router.push({ path: "/landlordHome/landlordHouse" });
        })
        .catch((err) => {
          if (err !== "cancel") {
            console.error("删除失败:", err);
            this.$message.error("删除失败");
          }
        });
    },
    editHouseButton() {
      this.updatedialogVisible = true;
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(() => {
          done();
        })
        .catch(() => {});
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const formData = new FormData();
        formData.append("image", file);
        request({
          method: "post",
          url: "/upload",
          data: formData,
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }).then((resp) => {
          this.house.image = resp.data.data;
          this.imagePreview = URL.createObjectURL(file);
          this.$message.success("图片上传成功");
        });
      }
    },
    updateHouse() {
      request({
        method: "put",
        url: "/landlord/userhouse/update",
        data: this.house,
      }).then(() => {
        this.$message.success("修改成功");
        this.updatedialogVisible = false;
        this.getHouseDetail();
      });
    },
  },
};
</script>

<style scoped>
.house-details {
  font-family: Arial, sans-serif;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background: #f9f9f9;
}
.house-header {
  text-align: center;
  margin-bottom: 20px;
}
.house-image {
  max-width: 100%;
  height: auto;
  border-radius: 10px;
}
.house-info ul {
  list-style: none;
  padding: 0;
}
.house-info li {
  margin: 10px 0;
  line-height: 1.6;
}
.contact-info {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ddd;
}
.action-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: space-around;
}
.edit-button {
  padding: 10px 20px;
  background-color: #f3a406;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.delete-button {
  padding: 10px 20px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
