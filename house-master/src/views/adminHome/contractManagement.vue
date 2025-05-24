<template>
    <div>
      <!-- 合同列表 -->
      <el-table stripe :data="contracts" style="width: 100%" v-loading="loading">
        
        <el-table-column prop="contractid" label="合同ID" width="100"></el-table-column>
        <el-table-column prop="houseid" label="房屋ID" width="100"></el-table-column>
        <el-table-column prop="buyer" label="买家" width="120"></el-table-column>
        <el-table-column prop="seller" label="卖家" width="120"></el-table-column>
        <el-table-column prop="contractDate" label="合同生效日期" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.contractDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="order" label="合同内容"></el-table-column>
        <el-table-column prop="status" label="房东状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ translateStatus(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="adminstatus" label="管理员状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.adminstatus)">
              {{ translateStatus(scope.row.adminstatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              @click="updateContractStatus(scope.row.contractid, 'approved')"
              :disabled="scope.row.status !== 'approved' || scope.row.adminstatus !== 'pending'"
            >通过</el-button>
            <el-button
              type="danger"
              size="mini"
              @click="updateContractStatus(scope.row.contractid, 'rejected')"
              :disabled="scope.row.status !== 'approved' || scope.row.adminstatus !== 'pending'"
            >拒绝</el-button>
          </template>
        </el-table-column>
  
        <!-- 空数据提示 -->
        <template #empty>
          <el-empty description="暂无合同数据" />
        </template>
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
  
  export default {
    data() {
      return {
        contracts: [], // 合同列表数据
        loading: false, // 加载状态
        page: 1, // 当前页码
        pageSize: 5, // 每页显示条数
        total: 0, // 总记录数
      };
    },
    created() {
      this.fetchContracts(); // 初始化时加载数据
    },
    methods: {
      // 获取合同列表数据
      async fetchContracts() {
        this.loading = true;
        try {
          const response = await request({
            method: "get",
            url: "/contract/all/paged",
            params: {
              page: this.page,
              pageSize: this.pageSize,
            },
          });
          this.contracts = response.data.data.rows || [];
          this.total = response.data.data.total || 0;
        } catch (error) {
          console.error("加载合同列表失败: ", error);
          this.$message.error("加载合同列表失败，请稍后重试");
        } finally {
          this.loading = false;
        }
      },
  
      // 更新合同状态
      async updateContractStatus(contractid, status) {
        try {
          const response = await request({
            method: "put",
            url: "/contract/updateStatus",
            params: { contractid, status, adminstatus: status },
          });
          if (response.data.code === 1) {
            this.$message.success("合同状态更新成功");
            this.fetchContracts(); // 刷新列表
            if (status === "approved") {
              this.updateHouseOwner(contractid);
            }
          } else {
            this.$message.error(response.data.msg || "合同状态更新失败");
          }
        } catch (error) {
          console.error("更新合同状态失败: ", error);
          this.$message.error("更新合同状态失败，请稍后重试");
        }
      },
  
      // 更新房屋所有者
      async updateHouseOwner(contractid) {
        try {
          const contract = this.contracts.find((c) => c.contractid === contractid);
          if (!contract) {
            this.$message.error("未找到合同信息");
            return;
          }
          const response = await request({
            method: "put",
            url: "/contract/updateHouseOwner",
            params: {
              houseid: contract.houseid,
              statue: 3,
              housename: contract.buyer,
              tenantname: contract.buyer,
            },
          });
          if (response.data.code === 1) {
            this.$message.success("房屋所有者更新成功");
          } else {
            this.$message.error(response.data.msg || "房屋所有者更新失败");
          }
        } catch (error) {
          console.error("更新房屋所有者失败: ", error);
          this.$message.error("更新房屋所有者失败，请稍后重试");
        }
      },
  
      // 格式化时间
      formatDate(dateString) {
        if (!dateString) return "";
        const date = new Date(dateString);
        const beijingTime = new Date(date.getTime() + 8 * 60 * 60 * 1000);
        return beijingTime.toISOString().replace("T", " ").substring(0, 19);
      },
  
      // 根据状态返回标签类型
      getStatusTagType(status) {
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
  
      // 处理页码变化
      handlePageChange(newPage) {
        this.page = newPage;
        this.fetchContracts();
      },
  
      // 处理每页条数变化
      handlePageSizeChange(newPageSize) {
        this.pageSize = newPageSize;
        this.fetchContracts();
      },
    },
  };
  </script>
  
  <style scoped>
  .container {
    padding: 16px;
    display: flex;
    flex-direction: column;
    height: 100%;
  }
  
  .el-table {
    margin-bottom: 16px;
  }
  
  .block {
  margin-top: 20px;
  text-align: center;
}
  </style>
  