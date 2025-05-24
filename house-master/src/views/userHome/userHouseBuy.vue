<template>
  <div>
    
    <!-- 使用 Element UI 的表格组件 -->
    <el-table :data="buyRequests" style="width: 100%;" stripe border v-loading="loading">
      <el-table-column prop="houseid" label="房屋ID" width="120"></el-table-column>
      <el-table-column prop="buyContent" label="申请内容"></el-table-column>
      <el-table-column prop="requestDate" label="申请时间" width="180">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.requestDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="管理员审核状态" width="180">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.status)">
            {{ translateStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

dayjs.extend(utc);
dayjs.extend(timezone);

export default {
  data() {
    return {
      buyRequests: [], // 购买申请列表
      loading: false, // 加载状态
      total: 0, // 总记录数
      pageSize: 5, // 每页显示条数
      currentPage: 1, // 当前页码
    };
  },
  created() {
    this.fetchBuyRequests();
  },
  methods: {
    async fetchBuyRequests() {
      this.loading = true;
      try {
        const user = JSON.parse(localStorage.getItem("token"));
        if (!user || !user.username) {
          console.error("User information is missing or invalid");
          return;
        }
        const response = await request({
          method: "get",
          url: "/buy/requests/paged",
          params: {
            buyer: user.username,
            page: this.currentPage,
            pageSize: this.pageSize,
          },
        });
        this.buyRequests = response.data.data.rows;
        this.total = response.data.data.total;
      } catch (error) {
        console.error("Failed to fetch buy requests:", error);
        this.$message.error("获取购买申请列表失败，请稍后重试");
      } finally {
        this.loading = false;
      }
    },
    // 分页切换事件
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchBuyRequests();
    },
    // 分页大小变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1; // 重置为第一页
      this.fetchBuyRequests();
    },
    // 根据状态返回标签类型
    statusTagType(status) {
      switch (status) {
        case "pending":
          return "warning";
        case "approved":
          return "success";
        case "rejected":
          return "danger";
        default:
          return "info";
      }
    },
    // 翻译状态
    translateStatus(status) {
      switch (status) {
        case "pending":
          return "审核中";
        case "approved":
          return "已通过";
        case "rejected":
          return "已拒绝";
        default:
          return "未知状态";
      }
    },
    // 格式化时间
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      return dayjs.utc(dateTime).tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss');
    },
  },
};
</script>

<style lang="less" scoped>
.container {
  padding: 20px;
}
.block {
  margin-top: 20px;
  text-align: center;
}
</style>