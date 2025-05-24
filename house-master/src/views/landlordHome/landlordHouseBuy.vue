<template>
  <div>
    <!-- 表格 -->
    <el-table :data="buyRequests" style="width: 100%;" border v-loading="loading">
      <el-table-column prop="requestid" label="申请ID" width="100"></el-table-column>
      <el-table-column prop="houseid" label="房屋ID" width="100"></el-table-column>
      <el-table-column prop="landlordname" label="卖家"></el-table-column>
      <el-table-column prop="buyer" label="买家"></el-table-column>
      <el-table-column prop="requestDate" label="申请时间" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.requestDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="管理员审核状态" width="150">
        <template slot-scope="scope">
          {{ translateStatus(scope.row.status) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            type="primary"
            @click="openAppealDialog(scope.row)"
            :disabled="scope.row.status !== 'approved' || scope.row.isSubmitted"
          >
            {{ scope.row.isSubmitted ? '已处理' : '申请处理' }}
          </el-button>
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
  
    <!-- 申请对话框 -->
    <el-dialog title="申请处理" :visible.sync="appealDialogVisible" width="30%">
      <el-form :model="appealForm" label-width="80px">
        <el-form-item label="选择">
          <el-radio-group v-model="appealForm.appealtype">
            <el-radio label="通过">通过</el-radio>
            <el-radio label="拒绝">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="合同明细">
          <el-input type="textarea" v-model="appealForm.appealContent"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="appealDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAppeal" :loading="submitting">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      buyRequests: [], // 购买申请列表
      loading: false, // 加载状态
      appealDialogVisible: false, // 申请对话框是否可见
      appealForm: {
        appealtype: "通过", // 默认申请类型
        appealContent: "", // 申请内容
        requestid: null, // 当前申请ID
      },
      page: 1, // 当前页码
      pageSize: 5, // 每页条数
      total: 0, // 总条数
      submitting: false, // 提交中状态
    };
  },
  created() {
    this.fetchBuyRequests(); // 组件加载时自动获取数据
  },
  methods: {
    // 获取购买申请列表
    async fetchBuyRequests() {
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
          url: "/buy/requests/byLandlord/paged",
          params: {
            landlordname: user.username,
            page: this.page,
            pageSize: this.pageSize,
          },
        });
        
        if (response.data && response.data.data && response.data.data.rows) {
          this.buyRequests = Array.isArray(response.data.data.rows) 
            ? response.data.data.rows.map(item => ({
                ...item,
                isSubmitted: item.isSubmitted || false
              })) 
            : [];
          this.total = response.data.data.total || 0;
        } else {
          this.buyRequests = [];
          this.total = 0;
        }
      } catch (error) {
        console.error("获取购买申请失败:", error);
        this.$message.error("获取购买申请失败，请稍后重试");
        this.buyRequests = [];
      } finally {
        this.loading = false;
      }
    },

    // 打开申请对话框
    openAppealDialog(row) {
      this.appealForm = {
        appealtype: "通过",
        appealContent: "",
        requestid: row.requestid
      };
      this.appealDialogVisible = true;
    },

    // 提交申请
    async submitAppeal() {
      this.submitting = true;
      try {
        const { appealtype, appealContent, requestid } = this.appealForm;

        if (!requestid || !appealtype || !appealContent) {
          this.$message.error("请填写完整的申请信息");
          return;
        }

        const currentRequest = this.buyRequests.find(
          (request) => request.requestid === requestid
        );
        
        if (currentRequest && currentRequest.isSubmitted) {
          this.$message.error("该申请已经提交过，不能重复提交");
          return;
        }

        const response = await request({
          method: "post",
          url: "/contract/submitAppeal",
          params: {
            requestid: requestid,
            status: appealtype === "通过" ? "approved" : "rejected",
            order: appealContent,
          },
        });

        if (response.data && response.data.code === 1) {
          this.$message.success("申请提交成功，合同已生成");
          // 更新对应请求的isSubmitted状态
          const index = this.buyRequests.findIndex(
            (request) => request.requestid === requestid
          );
          if (index !== -1) {
            this.$set(this.buyRequests[index], 'isSubmitted', true);
          }
          this.appealDialogVisible = false;
        } else {
          this.$message.error("申请提交失败：" + (response.data ? response.data.msg : "未知错误"));
        }
      } catch (error) {
        console.error("请求失败：", error);
        this.$message.error("网络异常，请稍后重试");
      } finally {
        this.submitting = false;
      }
    },

    // 格式化时间
    formatDate(dateString) {
      if (!dateString) return "";
      const date = new Date(dateString);
      const beijingTime = new Date(date.getTime() + 8 * 60 * 60 * 1000);
      return beijingTime.toISOString().replace("T", " ").substring(0, 19);
    },

    // 翻译状态
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

    // 分页：页码改变
    handlePageChange(newPage) {
      this.page = newPage;
      this.fetchBuyRequests();
    },

    // 分页：每页条数改变
    handlePageSizeChange(newPageSize) {
      this.pageSize = newPageSize;
      this.fetchBuyRequests();
    },
  },
};
</script>

<style scoped>
.block {
  padding: 20px;
  text-align: center;
}
</style>