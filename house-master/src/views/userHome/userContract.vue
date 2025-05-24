<template>
  <div>
    <!-- 使用 Element UI 的表格组件 -->
    <el-table :data="contracts" style="width: 100%;" stripe border v-loading="loading">
      <el-table-column prop="contractid" label="合同ID" width="100"></el-table-column>
      <el-table-column prop="houseid" label="房屋ID" width="100"></el-table-column>
      <el-table-column prop="buyer" label="买家"></el-table-column>
      <el-table-column prop="seller" label="卖家"></el-table-column>
      <el-table-column prop="contractDate" label="合同日期" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.contractDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="order" label="合同明细"></el-table-column>
      <el-table-column prop="status" label="卖家状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.status)">
            {{ translateStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="adminstatus" label="管理员状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.adminstatus)">
            {{ translateStatus(scope.row.adminstatus) }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <div class="block">
      <el-pagination
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        :current-page="page"
        :page-sizes="[2, 5, 10, 20]"
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
      contracts: [],
      loading: false,
      page: 1, // 当前页码
      pageSize: 10, // 每页条数
      total: 0 // 总条数
    };
  },
  created() {
    this.fetchContracts();
  },
  methods: {
    async fetchContracts() {
      this.loading = true;
      try {
        const user = JSON.parse(localStorage.getItem("token"));
        if (!user || !user.username) {
          console.error("用户信息缺失或无效");
          this.$message.error("用户信息缺失，请重新登录");
          return;
        }
        const response = await request({
          method: "get",
          url: "/contract/user/" + user.username + "/paged",
          params: {
            page: this.page,
            pageSize: this.pageSize
          }
        });
        this.contracts = response.data.data.rows || [];
        this.total = response.data.data.total || 0;
      } catch (error) {
        console.error("获取合同列表失败:", error);
        this.$message.error("获取合同列表失败，请稍后重试");
        this.contracts = [];
      } finally {
        this.loading = false;
      }
    },

    formatDate(dateString) {
      if (!dateString) return '';
      return dayjs.utc(dateString).tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss');
    },

    translateStatus(status) {
      switch (status) {
        case "pending":
          return "待处理";
        case "approved":
          return "已通过";
        case "rejected":
          return "已拒绝";
        default:
          return status;
      }
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

    // 分页：页码改变
    handlePageChange(newPage) {
      this.page = newPage;
      this.fetchContracts();
    },

    // 分页：每页条数改变
    handlePageSizeChange(newPageSize) {
      this.pageSize = newPageSize;
      this.fetchContracts();
    }
  }
};
</script>

<style lang="less" scoped>
.block {
  margin-top: 20px;
  text-align: center;
}
</style>