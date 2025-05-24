<template>
  <div>
    <!-- 购买申请列表 -->
    <el-table stripe :data="buyRequests" style="width: 100%">
      
      <el-table-column prop="requestid" label="申请ID" width="100"></el-table-column>
      <el-table-column prop="houseid" label="房屋ID" width="120"></el-table-column>
      <el-table-column prop="buyer" label="购买者" width="120"></el-table-column>
      <el-table-column prop="landlordname" label="房东" width="120"></el-table-column>
      <el-table-column prop="buyContent" label="购买者申请内容"></el-table-column>
      <el-table-column prop="requestDate" label="购买者申请时间" width="180">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.requestDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.status)">
            {{ translateStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="handleApprove(scope.row.requestid)"
            :disabled="scope.row.status !== 'pending'"
          >同意</el-button>
          <el-button
            type="danger"
            size="mini"
            @click="handleReject(scope.row.requestid)"
            :disabled="scope.row.status !== 'pending'"
          >拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页栏 -->
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
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
      buyRequests: [], // 购买申请列表数据
      currentPage: 1, // 当前页码
      pageSize: 5, // 每页显示条数
      total: 0, // 总记录数
    };
  },
  created() {
    this.fetchBuyRequests(); // 初始化时加载数据
  },
  methods: {
    // 获取购买申请列表数据
    async fetchBuyRequests() {
      try {
        const response = await request({
          method: "get",
          url: "/buy/requests/admin/paged",
          params: {
            page: this.currentPage,
            pageSize: this.pageSize,
          },
        });
        this.buyRequests = response.data.data.rows; // 更新购买申请列表
        this.total = response.data.data.total;
      } catch (error) {
        console.error("加载购买申请列表失败: ", error);
      }
    },

    // 处理分页大小变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1; // 重置为第一页
      this.fetchBuyRequests(); // 重新加载数据
    },

    // 处理当前页码变化
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchBuyRequests(); // 重新加载数据
    },

    // 处理同意申请
    async handleApprove(requestid) {
      try {
        await this.updateBuyRequestStatus(requestid, "approved");
        this.$message.success("申请已同意");
        this.fetchBuyRequests(); // 重新加载数据
      } catch (error) {
        console.error("同意申请失败: ", error);
        this.$message.error("同意申请失败");
      }
    },

    // 处理拒绝申请
    async handleReject(requestid) {
      try {
        await this.updateBuyRequestStatus(requestid, "rejected");
        this.$message.success("申请已拒绝");
        this.fetchBuyRequests(); // 重新加载数据
      } catch (error) {
        console.error("拒绝申请失败: ", error);
        this.$message.error("拒绝申请失败");
      }
    },

    // 更新申请状态
    async updateBuyRequestStatus(requestid, status) {
      try {
        await request({
          method: "put",
          url: `/buy/request/${requestid}/status`,
          params: {
            status: status,
          },
        });
      } catch (error) {
        console.error("更新申请状态失败: ", error);
        throw error; // 抛出错误，以便上层方法捕获
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
.block {
  margin-top: 20px;
  text-align: center;
}
</style>
