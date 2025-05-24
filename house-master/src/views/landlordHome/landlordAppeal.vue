<template>
  <div class="landlord-appeal">
    <el-table :data="reportList" style="width: 100%;" border v-loading="loading">
      <el-table-column prop="reportid" label="举报ID" width="100"></el-table-column>
      <el-table-column prop="houseid" label="房屋ID" width="100"></el-table-column>
      <el-table-column prop="reportee" label="被举报人"></el-table-column>
      <el-table-column prop="reporttitle" label="举报标题"></el-table-column>
      <el-table-column prop="reportcontent" label="举报内容"></el-table-column>
      <el-table-column prop="reportdate" label="举报日期" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.reportdate) }} <!-- 格式化日期 -->
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template slot-scope="scope">
          {{ translateStatus(scope.row.status) }} <!-- 状态转换为中文 -->
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="primary" @click="openAppealDialog(scope.row)">提交申诉</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5,10, 20, 30]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 申诉对话框 -->
    <el-dialog title="提交申诉" :visible.sync="appealDialogVisible" width="30%">
      <el-form :model="appealForm" label-width="80px">
        <el-form-item label="申诉类型">
          <el-radio-group v-model="appealForm.appealtype">
            <el-radio label="举报不实">举报不实</el-radio>
            <el-radio label="已整改">已整改</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="申诉内容">
          <el-input type="textarea" v-model="appealForm.appealContent"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="appealDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAppeal">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      reportList: [], // 表格数据
      loading: false, // 加载状态
      currentPage: 1, // 当前页码
      pageSize: 5, // 每页显示的条数
      total: 0, // 总记录数
      appealDialogVisible: false, // 申诉对话框是否可见
      appealForm: {
        appealtype: "举报不实", // 默认申诉类型
        appealContent: "", // 申诉内容
        reportid: null, // 当前举报ID
        houseid: null, // 房屋ID
      },
    };
  },
  created() {
    this.fetchReports(); // 初始化时获取数据
  },
  methods: {
    // 获取举报记录
    async fetchReports() {
  this.loading = true;
  try {
    const token = JSON.parse(localStorage.getItem("token"));
    const reportee = token.username;
    const response = await request({
      method: "get",
      url: "/report/getReportsByReporteePaged",
      params: {
        reportee: reportee,
        page: this.currentPage,
        pageSize: this.pageSize,
      },
    });
    console.log("后端返回的响应数据：", response.data); // 打印响应数据
    if (response.data && response.data.code === 1) {
      this.reportList = response.data.data.rows; // 将 list 替换为 rows
      this.total = response.data.data.total;
    } else {
      this.$message.error("获取数据失败：" + (response.data ? response.data.msg : "未知错误"));
    }
  } catch (error) {
    console.error("请求失败：", error);
    this.$message.error("网络异常，请稍后重试");
  } finally {
    this.loading = false;
  }
},


    // 处理每页显示条数变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1; // 每页条数变化时，重置页码为第一页
      this.fetchReports();
    },

    // 处理当前页码变化
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchReports();
    },

    // 打开申诉对话框
    openAppealDialog(row) {
      this.appealForm.reportid = row.reportid; // 设置当前举报ID
      this.appealForm.houseid = row.houseid; // 设置房屋ID
      this.appealDialogVisible = true;
    },

    // 提交申诉
    async submitAppeal() {
      try {
        const token = JSON.parse(localStorage.getItem("token"));
        const appealer = token.username; // 获取申诉人
        const { appealtype, appealContent, reportid, houseid } = this.appealForm;

        // 参数校验
        if (!reportid || !houseid || !appealtype || !appealContent) {
          this.$message.error("请填写完整的申诉信息");
          return;
        }

        // 提交申诉
        const response = await request({
          method: "post",
          url: "/appeal/submitAppeal",
          data: {
            reportid: reportid,
            houseid: houseid, // 传入 houseid
            appealer: appealer,
            appealtype: appealtype,
            appealcontent: appealContent, // 确保字段名与后端一致
          },
        });

        // 处理返回结果
        if (response.data && response.data.code === 1) {
          this.$message.success("申诉提交成功");
          this.appealDialogVisible = false; // 关闭对话框
          this.fetchReports(); // 刷新数据
        } else {
          this.$message.error("申诉提交失败：" + (response.data ? response.data.msg : "未知错误"));
        }
      } catch (error) {
        console.error("请求失败：", error);
        this.$message.error("网络异常，请稍后重试");
      }
    },

    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return ""; // 如果日期为空，返回空字符串
      const date = new Date(dateString);
      // 转换为北京时间（UTC+8）
      const beijingTime = new Date(date.getTime() + 8 * 60 * 60 * 1000);
      // 格式化为 YYYY-MM-DD HH:mm:ss
      return beijingTime.toISOString().replace("T", " ").substring(0, 19);
    },

    // 将状态转换为中文
    translateStatus(status) {
      switch (status) {
        case "pending":
          return "待处理";
        case "approved":
          return "已通过";
        case "rejected":
          return "未通过";
        default:
          return status;
      }
    },
  },
};
</script>

<style scoped>
.landlord-appeal {
  padding: 20px;
  text-align: center;
}
</style>
