<template>
  <div>
    <!-- 申诉列表 -->
    <el-table stripe :data="appealList" style="width: 100%">
      
      <el-table-column prop="appealid" label="申诉ID" width="100"></el-table-column>
      <el-table-column prop="houseid" label="房屋ID" width="120"></el-table-column>
      <el-table-column prop="appealer" label="申诉人" width="120"></el-table-column>
      <el-table-column prop="appealtype" label="申诉类型" width="150">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.appealtype === '举报不实'" type="warning">举报不实</el-tag>
          <el-tag v-else-if="scope.row.appealtype === '已整改'" type="success">已整改</el-tag>
          <el-tag v-else type="info">{{ scope.row.appealtype }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="appealcontent" label="申诉内容"></el-table-column>
      <el-table-column prop="appealdate" label="申诉日期" width="150">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.appealdate) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120">
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
            @click="handleAppeal(scope.row, 'approved')"
            :disabled="scope.row.status !== 'pending'"
          >通过</el-button>
          <el-button
            type="danger"
            size="mini"
            @click="handleAppeal(scope.row, 'rejected')"
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
      appealList: [], // 申诉列表数据
      currentPage: 1, // 当前页码
      pageSize: 5, // 每页显示条数
      total: 0, // 总记录数
    };
  },
  created() {
    this.fetchAppealList(); // 初始化时加载数据
  },
  methods: {
    // 获取申诉列表数据
    async fetchAppealList() {
      try {
        const response = await request({
          method: "get",
          url: "/appeal/getAppealList",
          params: {
            page: this.currentPage,
            pageSize: this.pageSize,
          },
        });
        this.appealList = response.data.data.rows; // 更新申诉列表
        this.total = response.data.data.total;
      } catch (error) {
        console.error("加载申诉列表失败: ", error);
        this.$message.error("加载申诉列表失败，请稍后重试");
      }
    },

    // 处理分页大小变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1; // 重置为第一页
      this.fetchAppealList(); // 重新加载数据
    },

    // 处理当前页码变化
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchAppealList(); // 重新加载数据
    },

    // 处理申诉
    async handleAppeal(row, status) {
      try {
        const response = await request({
          method: "post",
          url: "/appeal/handleAppeal",
          data: { appealid: row.appealid, status },
        });
        if (response.data.code === 1) {
          this.$message.success(response.data.msg || "操作成功");
          this.fetchAppealList(); // 刷新列表
        } else {
          this.$message.error(response.data.msg || "操作失败");
        }
      } catch (error) {
        console.error("处理申诉失败: ", error);
        this.$message.error("处理申诉失败，请稍后重试");
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
