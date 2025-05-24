<template>
  <div>
    <el-container>
      <!-- 左侧导航栏 -->
      <el-aside width="200px" style="background-color: #545c64;">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-vertical"
          mode="vertical"
          @select="handleSelect"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          :router="true"
        >
          <el-menu-item index="/adminHome/firstpage">
            <i class="el-icon-document"></i>
            <span>主页</span>
          </el-menu-item>
          <el-menu-item index="/adminHome/adminMessageBoard">
            <i class="el-icon-setting"></i>
            <span>留言管理</span>
          </el-menu-item>
          <el-menu-item index="/adminHome/userAdmin">
            <i class="el-icon-setting"></i>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/adminHome/reportManagement">
            <i class="el-icon-warning"></i>
            <span>举报管理</span>
          </el-menu-item>
          <el-menu-item index="/adminHome/appealManagement">
            <i class="el-icon-warning"></i>
            <span>申诉管理</span>
          </el-menu-item>
          <el-menu-item index="/adminHome/BuyManagement">
            <i class="el-icon-document"></i>
            <span>购买申请管理</span>
          </el-menu-item>
          <el-menu-item index="/adminHome/contractManagement">
            <i class="el-icon-document"></i>
            <span>合同管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 右侧主要内容区域 -->
      <el-container>
        <el-header style="background-color: #009688; height: 56.22px;">
          <div style="position: relative">
            <span style="font-weight: bold; font-size: larger; color: white">房屋租赁管理系统</span>
            <el-button
              type="danger"
              style="float: right; margin-top: 10px"
              @click="returnLogin"
            >
              退出
            </el-button>
          </div>
        </el-header>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: "/adminHome/firstpage", // 默认激活项
    };
  },
  methods: {
    returnLogin() {
      localStorage.removeItem("token");
      window.location.href = "/";
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style lang="less" scoped>
.el-container {
  display: flex;
  min-height: 100vh;
}

.el-aside {
  background-color: #545c64;
}

.el-header {
  background-color: #009688;
  color: white;
  text-align: center;
  line-height: 56.22px;
  padding: 0;
  height: 56.22px;
}

.el-main {
  background-color: #fff;
  color: #333;
  padding: 20px;
  flex: 1;
}

.el-footer {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
}
</style>
