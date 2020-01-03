"use strict";

Vue.component("select-department-user-dialog", {
  data: function data() {
    return {
      acInterface: {
        getDepartmentTreeData: "/Rest/SSO/Dept/Department/GetDepartmentsByOrganId",
        reloadListData: "/Rest/SSO/Dept/DepartmentUser/GetListData"
      },
      treeIdFieldName: "deptId",
      treeObj: null,
      treeSelectedNode: null,
      treeSetting: {
        async: {
          enable: true,
          url: ""
        },
        data: {
          key: {
            name: "deptName"
          },
          simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "deptParentId"
          }
        },
        callback: {
          onClick: function onClick(event, treeId, treeNode) {
            var _self = this.getZTreeObj(treeId)._host;

            _self.treeNodeSelected(event, treeId, treeNode);
          },
          onAsyncSuccess: function onAsyncSuccess(event, treeId, treeNode, msg) {
            appList.treeObj.expandAll(true);
          }
        }
      },
      idFieldName: "DU_ID",
      searchCondition: {
        userName: {
          value: "",
          type: SearchUtility.SearchFieldType.LikeStringType
        },
        account: {
          value: "",
          type: SearchUtility.SearchFieldType.LikeStringType
        },
        userPhoneNumber: {
          value: "",
          type: SearchUtility.SearchFieldType.LikeStringType
        },
        departmentId: {
          value: "",
          type: SearchUtility.SearchFieldType.StringType
        },
        searchInALL: {
          value: "否",
          type: SearchUtility.SearchFieldType.StringType
        }
      },
      columnsConfig: [{
        type: 'selection',
        width: 60,
        align: 'center'
      }, {
        title: '用户名',
        key: 'USER_NAME',
        align: "center"
      }, {
        title: '手机号码',
        key: 'USER_PHONE_NUMBER',
        width: 140,
        align: "center"
      }, {
        title: '组织机构',
        key: 'ORGAN_NAME',
        width: 140,
        align: "center"
      }, {
        title: '部门',
        key: 'DEPT_NAME',
        width: 140,
        align: "center"
      }, {
        title: '主属',
        key: 'DU_IS_MAIN',
        width: 70,
        align: "center"
      }],
      tableData: [],
      selectionRows: null,
      pageTotal: 0,
      pageSize: 12,
      pageNum: 1,
      listHeight: 270
    };
  },
  mounted: function mounted() {
    var oldSelectedOrganId = CookieUtility.GetCookie("DMORGSID");

    if (oldSelectedOrganId) {
      this.$refs.selectOrganComp.setOldSelectedOrgan(oldSelectedOrganId);
      this.initTree(oldSelectedOrganId);
    }
  },
  methods: {
    changeOrgan: function changeOrgan(organData) {
      CookieUtility.SetCookie1Month("DMORGSID", organData.organId);
      this.initTree(organData.organId);
      this.clearSearchCondition();
      this.tableData = [];
    },
    initTree: function initTree(organId) {
      AjaxUtility.Post(this.acInterface.getDepartmentTreeData, {
        "organId": organId
      }, function (result) {
        if (result.success) {
          this.$refs.zTreeUL.setAttribute("id", "select-department-user-dialog-" + StringUtility.Guid());
          this.treeObj = $.fn.zTree.init($(this.$refs.zTreeUL), this.treeSetting, result.data);
          this.treeObj.expandAll(true);
          this.treeObj._host = this;
        } else {
          DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, function () {});
        }
      }, this);
    },
    treeNodeSelected: function treeNodeSelected(event, treeId, treeNode) {
      this.treeSelectedNode = treeNode;
      this.selectionRows = null;
      this.pageNum = 1;
      this.clearSearchCondition();
      this.searchCondition.departmentId.value = this.treeSelectedNode[this.treeIdFieldName];
      this.reloadData();
    },
    clearSearchCondition: function clearSearchCondition() {
      for (var key in this.searchCondition) {
        this.searchCondition[key].value = "";
      }

      this.searchCondition["searchInALL"].value = "否";
    },
    selectionChange: function selectionChange(selection) {
      this.selectionRows = selection;
    },
    reloadData: function reloadData() {
      ListPageUtility.IViewTableBindDataBySearch({
        url: this.acInterface.reloadListData,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        searchCondition: this.searchCondition,
        pageAppObj: this,
        tableList: this,
        idField: this.idFieldName,
        autoSelectedOldRows: false,
        successFunc: null,
        loadDict: false,
        custParas: {}
      });
    },
    changePage: function changePage(pageNum) {
      this.pageNum = pageNum;
      this.reloadData();
      this.selectionRows = null;
    },
    search: function search() {
      this.pageNum = 1;
      this.reloadData();
    },
    beginSelect: function beginSelect() {
      var elem = this.$refs.selectDepartmentUserModelDialogWrap;
      var dialogHeight = 460;

      if (PageStyleUtility.GetPageHeight() > 700) {
        dialogHeight = 660;
      }

      this.listHeight = dialogHeight - 230;
      DialogUtility.DialogElemObj(elem, {
        modal: true,
        width: 970,
        height: dialogHeight,
        title: "选择组织机构"
      });
    },
    completed: function completed() {
      console.log(this.selectionRows);

      if (this.selectionRows) {
        this.$emit('on-selected-completed', this.selectionRows);
        this.handleClose();
      } else {
        DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, "请先选中人员!", null);
      }
    },
    handleClose: function handleClose() {
      DialogUtility.CloseDialogElem(this.$refs.selectDepartmentUserModelDialogWrap);
    }
  },
  template: "<div ref=\"selectDepartmentUserModelDialogWrap\" class=\"c1-select-model-wrap general-edit-page-wrap\" style=\"display: none\">\n                    <div class=\"list-2column\">\n                        <div class=\"left-outer-wrap\" style=\"width: 180px;top: 10px;left: 10px;bottom: 55px\">\n                            <select-organ-single-comp @on-selected-organ=\"changeOrgan\" ref=\"selectOrganComp\"></select-organ-single-comp>\n                            <div class=\"inner-wrap\" style=\"position:absolute;top: 30px;bottom: 10px;height: auto;overflow: auto\">\n                                <div>\n                                    <ul ref=\"zTreeUL\" class=\"ztree\"></ul>\n                                </div>\n                            </div>\n                        </div>\n                        <div class=\"right-outer-wrap iv-list-page-wrap\" style=\"padding: 10px;left: 200px;top: 10px;right: 10px;bottom: 55px\">\n                            <div class=\"list-simple-search-wrap\">\n                                <table class=\"ls-table\">\n                                    <colgroup>\n                                        <col style=\"width: 80px\">\n                                        <col style=\"\">\n                                        <col style=\"width: 100px\">\n                                        <col style=\"\">\n                                        <col style=\"width: 80px\">\n                                        <col style=\"width: 85px\">\n                                        <col style=\"width: 80px\">\n                                    </colgroup>\n                                    <tr class=\"ls-table-row\">\n                                        <td>\u7528\u6237\u540D\uFF1A</td>\n                                        <td>\n                                            <i-input v-model=\"searchCondition.userName.value\" placeholder=\"\"></i-input>\n                                        </td>\n                                        <td>\u624B\u673A\uFF1A</td>\n                                        <td>\n                                            <i-input v-model=\"searchCondition.userPhoneNumber.value\"></i-input>\n                                        </td>\n                                        <td>\u5168\u5C40\uFF1A</td>\n                                        <td>\n                                            <radio-group v-model=\"searchCondition.searchInALL.value\">\n                                                <radio label=\"\u662F\">\u662F</radio>\n                                                <radio label=\"\u5426\">\u5426</radio>\n                                            </radio-group>\n                                        </td>\n                                        <td><i-button type=\"primary\" @click=\"search\"><Icon type=\"android-search\"></Icon> \u67E5\u8BE2 </i-button></td>\n                                    </tr>\n                                </table>\n                            </div>\n                            <i-table :height=\"listHeight\" stripe border :columns=\"columnsConfig\" :data=\"tableData\"\n                                     class=\"iv-list-table\" :highlight-row=\"true\"\n                                     @on-selection-change=\"selectionChange\"></i-table>\n                            <div style=\"float: right;\">\n                                <page @on-change=\"changePage\" :current.sync=\"pageNum\" :page-size=\"pageSize\" show-total\n                                      :total=\"pageTotal\"></page>\n                            </div>\n                        </div>\n                    </div>\n                    <div class=\"button-outer-wrap\" style=\"bottom: 12px;right: 12px\">\n                        <div class=\"button-inner-wrap\">\n                            <button-group>\n                                <i-button type=\"primary\" @click=\"completed()\" icon=\"md-checkmark\">\u786E\u8BA4</i-button>\n                                <i-button @click=\"handleClose()\" icon=\"md-close\">\u5173\u95ED</i-button>\n                            </button-group>\n                        </div>\n                    </div>\n                </div>"
});
"use strict";

Vue.component("sso-app-detail-from-comp", {
  props: ["status", "appId", "isSubSystem"],
  watch: {
    appId: function appId(newVal) {
      this.appEntity.appId = newVal;
    },
    status: function status(newVal) {
      this.innerStatus = newVal;
    }
  },
  data: function data() {
    return {
      acInterface: {
        appLogoUrl: "/Rest/SSO/App/Application/GetAppLogo",
        getNewKeys: "/Rest/SSO/App/Application/GetNewKeys"
      },
      appEntity: {
        appId: "",
        appCode: "",
        appName: "",
        appPublicKey: "",
        appPrivateKey: "",
        appDomain: "",
        appIndexUrl: "",
        appMainImageId: "",
        appType: "",
        appMainId: "",
        appCategory: "web",
        appDesc: "",
        appStatus: "启用",
        appCreateTime: DateUtility.GetCurrentData()
      },
      ruleValidate: {
        appCode: [{
          required: true,
          message: '【系统编码】不能为空！',
          trigger: 'blur'
        }, {
          type: 'string',
          pattern: /^[A-Za-z0-9]+$/,
          message: '请使用字母或数字',
          trigger: 'blur'
        }],
        appName: [{
          required: true,
          message: '【系统名称】不能为空！',
          trigger: 'blur'
        }]
      },
      systemLogoImageSrc: "",
      innerStatus: "add"
    };
  },
  mounted: function mounted() {
    if (this.innerStatus == "add") {
      this.systemLogoImageSrc = BaseUtility.BuildAction(this.acInterface.appLogoUrl, {
        fileId: "defaultSSOAppLogoImage"
      });
    } else {
      this.systemLogoImageSrc = BaseUtility.BuildAction(this.acInterface.appLogoUrl, {
        fileId: ""
      });
    }
  },
  methods: {
    resetAppEntity: function resetAppEntity() {
      this.appEntity.appId = "";
      this.appEntity.appCode = "";
      this.appEntity.appName = "";
      this.appEntity.appPublicKey = "";
      this.appEntity.appPrivateKey = "";
      this.appEntity.appDomain = "";
      this.appEntity.appIndexUrl = "";
      this.appEntity.appMainImageId = "";
      this.appEntity.appType = "";
      this.appEntity.appMainId = "";
      this.appEntity.appCategory = "web";
      this.appEntity.appDesc = "";
      this.appEntity.appStatus = "启用";
      this.appEntity.appCreateTime = DateUtility.GetCurrentData();
    },
    uploadSystemLogoImageSuccess: function uploadSystemLogoImageSuccess(response, file, fileList) {
      if (response.success) {
        var data = response.data;
        this.appEntity.appMainImageId = data.fileId;
        this.systemLogoImageSrc = BaseUtility.BuildAction(this.acInterface.appLogoUrl, {
          fileId: this.appEntity.appMainImageId
        });
      } else {
        DialogUtility.AlertError(window, DialogUtility.DialogAlertErrorId, {}, response.message, null, null);
      }
    },
    getAppEntity: function getAppEntity() {
      return this.appEntity;
    },
    setAppEntity: function setAppEntity(appEntity) {
      this.appEntity = appEntity;
    },
    createKeys: function createKeys() {
      var _self = this;

      AjaxUtility.Post(this.acInterface.getNewKeys, {}, function (result) {
        if (result.success) {
          _self.appEntity.appPublicKey = result.data.publicKey;
          _self.appEntity.appPrivateKey = result.data.privateKey;
        } else {
          DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, null);
        }
      }, this);
    }
  },
  template: "<div>\n                    <div style=\"width: 80%;float: left\">\n                        <i-form ref=\"appEntity\" :model=\"appEntity\" :rules=\"ruleValidate\" :label-width=\"100\">\n                            <form-item label=\"\u7CFB\u7EDF\u7F16\u7801\uFF1A\" prop=\"appCode\">\n                                <row>\n                                    <i-col span=\"10\">\n                                        <form-item prop=\"appCode\">\n                                            <i-input v-model=\"appEntity.appCode\"></i-input>\n                                        </form-item>\n                                    </i-col>\n                                    <i-col span=\"4\" style=\"text-align: center\"><span style=\"color: red\">*</span> \u7CFB\u7EDF\u540D\u79F0\uFF1A</i-col>\n                                    <i-col span=\"10\">\n                                        <form-item prop=\"appName\">\n                                            <i-input v-model=\"appEntity.appName\"></i-input>\n                                        </form-item>\n                                    </i-col>\n                                </row>\n                            </form-item>\n                            <form-item label=\"\u57DF\u540D\uFF1A\">\n                                <row>\n                                    <i-col span=\"10\">\n                                        <i-input v-model=\"appEntity.appDomain\"></i-input>\n                                    </i-col>\n                                    <i-col span=\"4\" style=\"text-align: center\">\u7CFB\u7EDF\u7C7B\u522B\uFF1A</i-col>\n                                    <i-col span=\"10\">\n                                        <radio-group v-model=\"appEntity.appCategory\" type=\"button\">\n                                            <radio label=\"app\">\u79FB\u52A8App</radio>\n                                            <radio label=\"web\">Web\u7CFB\u7EDF</radio>\n                                        </radio-group>\n                                    </i-col>\n                                </row>\n                            </form-item>\n                            <form-item label=\"\u516C\u94A5\uFF1A\" v-if=\"isSubSystem=='0'\">\n                                <i-input placeholder=\"\u8BF7\u521B\u5EFA\u5BC6\u94A5\u5BF9,\u7528\u4E8E\u6570\u636E\u7684\u52A0\u5BC6\u4F7F\u7528\" search enter-button=\"\u521B\u5EFA\u5BC6\u94A5\u5BF9\" v-model=\"appEntity.appPublicKey\" @on-search=\"createKeys()\"></i-input>\n                            </form-item>\n                            <form-item label=\"\u79C1\u94A5\uFF1A\" v-if=\"isSubSystem==0\">\n                                <i-input v-model=\"appEntity.appPrivateKey\"></i-input>\n                            </form-item>\n                            <form-item label=\"\u521B\u5EFA\u65F6\u95F4\uFF1A\">\n                                <row>\n                                    <i-col span=\"10\">\n                                        <date-picker type=\"date\" placeholder=\"\u9009\u62E9\u521B\u5EFA\u65F6\u95F4\" v-model=\"appEntity.appCreateTime\" disabled\n                                                     readonly></date-picker>\n                                    </i-col>\n                                    <i-col span=\"4\" style=\"text-align: center\">\u72B6\u6001\uFF1A</i-col>\n                                    <i-col span=\"10\">\n                                        <form-item>\n                                            <radio-group v-model=\"appEntity.appStatus\">\n                                                <radio label=\"\u542F\u7528\">\u542F\u7528</radio>\n                                                <radio label=\"\u7981\u7528\">\u7981\u7528</radio>\n                                            </radio-group>\n                                        </form-item>\n                                    </i-col>\n                                </row>\n                            </form-item>\n                            <form-item label=\"\u9ED8\u8BA4\u5730\u5740\uFF1A\">\n                                <i-input v-model=\"appEntity.appIndexUrl\"></i-input>\n                            </form-item>\n                            <form-item label=\"\u5907\u6CE8\uFF1A\">\n                                <i-input v-model=\"appEntity.appDesc\" type=\"textarea\" :autosize=\"{minRows: 3,maxRows: 3}\"></i-input>\n                            </form-item>\n                        </i-form>\n                    </div>\n                    <div style=\"width: 19%;float: right\">\n                        <div style=\"border-radius: 8px;text-align: center;margin-top: 0px;margin-bottom: 30px\">\n                            <img :src=\"systemLogoImageSrc\" style=\"width: 110px;height: 110px\" />\n                        </div>\n                        <upload style=\"margin:10px 12px 0 20px\" :on-success=\"uploadSystemLogoImageSuccess\" :data=\"appEntity\" multiple type=\"drag\" name=\"file\" action=\"../../../Rest/SSO/App/Application/UploadAppLogo\" accept=\".png\">\n                            <div style=\"padding:10px 0px\">\n                                <icon type=\"ios-cloud-upload\" size=\"52\" style=\"color: #3399ff\"></icon>\n                                <p>\u4E0A\u4F20\u7CFB\u7EDFLogo</p>\n                            </div>\n                        </upload>\n                    </div>\n                </div>"
});
"use strict";

Vue.component("sso-app-interface-list-comp", {
  props: ["interfaceBelongAppId"],
  watch: {
    interfaceBelongAppId: function interfaceBelongAppId(newVal) {
      this.interfaceEntity.interfaceBelongAppId = newVal;
    }
  },
  data: function data() {
    var _self = this;

    return {
      acInterface: {
        delete: "/PlatFormRest/SSO/Application/DeleteInterface"
      },
      interfaceEntity: {
        interfaceId: "",
        interfaceBelongAppId: "",
        interfaceCode: "",
        interfaceName: "",
        interfaceUrl: "",
        interfaceParas: "",
        interfaceFormat: "",
        interfaceDesc: "",
        interfaceOrderNum: "",
        interfaceCreateTime: DateUtility.GetCurrentData(),
        interfaceStatus: "启用",
        interfaceCreaterId: "",
        interfaceOrganId: ""
      },
      list: {
        columnsConfig: [{
          type: 'selection',
          width: 60,
          align: 'center'
        }, {
          title: '接口类型',
          key: 'interfaceCode',
          align: "center",
          width: 100
        }, {
          title: '接口名称',
          key: 'interfaceName',
          align: "center",
          width: 280
        }, {
          title: '备注',
          key: 'interfaceDesc',
          align: "center"
        }, {
          title: '操作',
          key: 'interfaceId',
          width: 140,
          align: "center",
          render: function render(h, params) {
            return h('div', {
              class: "list-row-button-wrap"
            }, [ListPageUtility.IViewTableInnerButton.EditButton(h, params, "interfaceId", _self), ListPageUtility.IViewTableInnerButton.DeleteButton(h, params, "interfaceId", _self)]);
          }
        }],
        tableData: []
      },
      innerStatus: "add"
    };
  },
  mounted: function mounted() {},
  methods: {
    resetListData: function resetListData() {
      this.list.tableData = [];
    },
    addInterface: function addInterface() {
      var elem = this.$refs.ssoAppInterfaceEditModelDialogWrap;
      this.innerStatus == "add";
      this.interfaceEntity.interfaceId = "";
      this.interfaceEntity.interfaceBelongAppId = this.interfaceBelongAppId;
      this.interfaceEntity.interfaceCode = "";
      this.interfaceEntity.interfaceName = "";
      this.interfaceEntity.interfaceUrl = "";
      this.interfaceEntity.interfaceParas = "";
      this.interfaceEntity.interfaceFormat = "";
      this.interfaceEntity.interfaceDesc = "";
      this.interfaceEntity.interfaceOrderNum = "";
      this.interfaceEntity.interfaceCreateTime = DateUtility.GetCurrentData();
      this.interfaceEntity.interfaceStatus = "启用";
      this.interfaceEntity.interfaceCreaterId = "";
      this.interfaceEntity.interfaceOrganId = "";
      DialogUtility.DialogElemObj(elem, {
        modal: true,
        width: 570,
        height: 330,
        title: "接口设置"
      });
    },
    handleClose: function handleClose() {
      DialogUtility.CloseDialogElem(this.$refs.ssoAppInterfaceEditModelDialogWrap);
    },
    saveInterfaceEdit: function saveInterfaceEdit() {
      if (this.innerStatus == "add") {
        this.interfaceEntity.interfaceId = StringUtility.Guid();
        this.list.tableData.push(JsonUtility.CloneSimple(this.interfaceEntity));
      } else {
        for (var i = 0; i < this.list.tableData.length; i++) {
          if (this.list.tableData[i].interfaceId == this.interfaceEntity.interfaceId) {
            this.list.tableData[i].interfaceCode = this.interfaceEntity.interfaceCode;
            this.list.tableData[i].interfaceName = this.interfaceEntity.interfaceName;
            this.list.tableData[i].interfaceUrl = this.interfaceEntity.interfaceUrl;
            this.list.tableData[i].interfaceParas = this.interfaceEntity.interfaceParas;
            this.list.tableData[i].interfaceFormat = this.interfaceEntity.interfaceFormat;
            this.list.tableData[i].interfaceDesc = this.interfaceEntity.interfaceDesc;
            break;
          }
        }
      }

      this.handleClose();
    },
    changeInterfaceCode: function changeInterfaceCode(value) {
      this.interfaceEntity.interfaceCode = value;
    },
    getInterfaceListData: function getInterfaceListData() {
      return this.list.tableData;
    },
    setInterfaceListData: function setInterfaceListData(data) {
      this.list.tableData = data;
    },
    edit: function edit(interfaceId, params) {
      this.innerStatus = "update";
      this.interfaceEntity.interfaceId = params.row.interfaceId;
      this.interfaceEntity.interfaceCode = params.row.interfaceCode;
      this.interfaceEntity.interfaceName = params.row.interfaceName;
      this.interfaceEntity.interfaceUrl = params.row.interfaceUrl;
      this.interfaceEntity.interfaceParas = params.row.interfaceParas;
      this.interfaceEntity.interfaceFormat = params.row.interfaceFormat;
      this.interfaceEntity.interfaceDesc = params.row.interfaceDesc;
      this.interfaceEntity.interfaceOrderNum = params.row.interfaceOrderNum;
      this.interfaceEntity.interfaceCreateTime = params.row.interfaceCreateTime;
      this.interfaceEntity.interfaceStatus = params.row.interfaceStatus;
      this.interfaceEntity.interfaceCreaterId = params.row.interfaceCreaterId;
      this.interfaceEntity.interfaceOrganId = params.row.interfaceOrganId;
      this.interfaceEntity.interfaceBelongAppId = params.row.interfaceBelongAppId;
      var elem = this.$refs.ssoAppInterfaceEditModelDialogWrap;
      DialogUtility.DialogElemObj(elem, {
        modal: true,
        width: 570,
        height: 330,
        title: "接口设置"
      });
    },
    del: function del(interfaceId, params) {
      var _self = this;

      for (var i = 0; i < this.list.tableData.length; i++) {
        if (this.list.tableData[i].interfaceId == interfaceId) {
          _self.list.tableData.splice(i, 1);

          DialogUtility.Confirm(window, "确认要删除该接口吗？", function () {
            AjaxUtility.Delete(_self.acInterface.delete, {
              "interfaceId": interfaceId
            }, function (result) {
              if (result.success) {} else {
                DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, null);
              }
            }, this);
          });
        }
      }
    }
  },
  template: "<div class=\"iv-list-page-wrap\">\n                    <div ref=\"ssoAppInterfaceEditModelDialogWrap\" class=\"general-edit-page-wrap\" style=\"display: none;margin-top: 0px\">\n                        <i-form ref=\"interfaceEntity\" :model=\"interfaceEntity\" :label-width=\"130\">\n                            <form-item style=\"margin-bottom: 2px\">\n                                <span slot=\"label\"><span style=\"color: red\">*</span>&nbsp;\u63A5\u53E3\u7C7B\u578B\uFF1A</span>\n                                <i-input v-model=\"interfaceEntity.interfaceCode\" size=\"small\">\n                                    <Select slot=\"append\" style=\"width: 90px\" @on-change=\"changeInterfaceCode\">\n                                        <Option value=\"\u767B\u5F55\u63A5\u53E3\">\u767B\u5F55\u63A5\u53E3</Option>\n                                        <Option value=\"\u5176\u4ED6\">\u5176\u4ED6</Option>\n                                    </Select>\n                                </i-input>\n                            </form-item>\n                            <form-item style=\"margin-bottom: 2px\">\n                                <span slot=\"label\"><span style=\"color: red\">*</span>&nbsp;\u63A5\u53E3\u540D\u79F0\uFF1A</span>\n                                <i-input v-model=\"interfaceEntity.interfaceName\" size=\"small\"></i-input>\n                            </form-item>\n                            <form-item label=\"\u63A5\u53E3\u5730\u5740\uFF1A\" style=\"margin-bottom: 2px\">\n                                <i-input v-model=\"interfaceEntity.interfaceUrl\" size=\"small\"></i-input>\n                            </form-item>\n                            <form-item label=\"\u53C2\u6570\uFF1A\" style=\"margin-bottom: 2px\">\n                                <i-input v-model=\"interfaceEntity.interfaceParas\" type=\"textarea\" :autosize=\"{minRows: 2,maxRows: 2}\" size=\"small\"></i-input>    \n                            </form-item>\n                            <form-item label=\"\u683C\u5F0F\u5316\u65B9\u6CD5\uFF1A\" style=\"margin-bottom: 2px\">\n                                <i-input v-model=\"interfaceEntity.interfaceFormat\" size=\"small\"></i-input>\n                            </form-item>\n                            <form-item label=\"\u5907\u6CE8\uFF1A\" style=\"margin-bottom: 2px\">\n                                <i-input v-model=\"interfaceEntity.interfaceDesc\" size=\"small\"></i-input>\n                            </form-item>\n                        </i-form>\n                        <div class=\"button-outer-wrap\" style=\"margin-left: 8px\">\n                            <div class=\"button-inner-wrap\">\n                                <button-group size=\"small\">\n                                    <i-button type=\"primary\" @click=\"saveInterfaceEdit('interfaceEntity')\" icon=\"md-checkmark\"></i-button>\n                                    <i-button @click=\"handleClose()\" icon=\"md-close\"></i-button>\n                                </button-group>\n                            </div>\n                        </div>\n                    </div>\n                    <div id=\"list-button-wrap\" class=\"list-button-outer-wrap\">\n                        <div class=\"list-button-inner-wrap\">\n                            <ButtonGroup>\n                                <i-button  type=\"success\" @click=\"addInterface()\" icon=\"md-add\">\u65B0\u589E</i-button>\n                            </ButtonGroup>\n                        </div>\n                        <div style=\"clear: both\"></div>\n                    </div>\n                    <i-table :height=\"list.listHeight\" stripe border :columns=\"list.columnsConfig\" :data=\"list.tableData\"\n                         class=\"iv-list-table\" :highlight-row=\"true\"></i-table>\n                </div>"
});
"use strict";

Vue.component("sso-app-sub-system-list-comp", {
  props: ["status", "belongAppId"],
  data: function data() {
    return {
      acInterface: {
        saveSubAppUrl: "/PlatFormRest/SSO/Application/SaveSubApp",
        reloadData: "/PlatFormRest/SSO/Application/GetAllSubSsoApp",
        appLogoUrl: "/PlatFormRest/SSO/Application/GetAppLogo",
        delete: "/PlatFormRest/SSO/Application/Delete",
        getDataUrl: "/PlatFormRest/SSO/Application/GetAppVo"
      },
      appList: [],
      innerEditModelDialogStatus: "add"
    };
  },
  mounted: function mounted() {
    this.reloadData();
  },
  methods: {
    addIntegratedSystem: function addIntegratedSystem() {
      var elem = this.$refs.ssoAppSubSystemEditModelDialogWrap;
      this.$refs.subAppDetailFromComp.resetAppEntity();
      this.$refs.subAppInterfaceListComp.resetListData();
      this.innerEditModelDialogStatus = "add";
      DialogUtility.DialogElemObj(elem, {
        modal: true,
        width: 900,
        height: 500,
        title: "子系统设置"
      });
    },
    saveSubSystemSetting: function saveSubSystemSetting() {
      var _self = this;

      var ssoAppEntity = this.$refs.subAppDetailFromComp.getAppEntity();
      var ssoAppInterfaceEntityList = this.$refs.subAppInterfaceListComp.getInterfaceListData();
      ssoAppEntity.appMainId = this.belongAppId;

      if (this.innerEditModelDialogStatus == "add") {
        ssoAppEntity.appId = StringUtility.Guid();
      }

      if (ssoAppInterfaceEntityList) {
        for (var i = 0; i < ssoAppInterfaceEntityList.length; i++) {
          ssoAppInterfaceEntityList[i].interfaceBelongAppId = ssoAppEntity.appId;
        }
      }

      var vo = {
        "ssoAppEntity": ssoAppEntity,
        "ssoAppInterfaceEntityList": ssoAppInterfaceEntityList
      };
      var sendData = JSON.stringify(vo);
      AjaxUtility.PostRequestBody(this.acInterface.saveSubAppUrl, sendData, function (result) {
        if (result.success) {
          DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, function () {
            _self.reloadData();

            _self.handleClose();
          });
        } else {
          DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, null);
        }
      }, "json");
    },
    handleClose: function handleClose() {
      DialogUtility.CloseDialogElem(this.$refs.ssoAppSubSystemEditModelDialogWrap);
    },
    reloadData: function reloadData() {
      var _self = this;

      AjaxUtility.Post(this.acInterface.reloadData, {
        appId: _self.belongAppId
      }, function (result) {
        if (result.success) {
          _self.appList = result.data;
        } else {
          DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, null);
        }
      }, "json");
    },
    buildLogoUrl: function buildLogoUrl(app) {
      if (app.appMainImageId == "") {
        return BaseUtility.BuildAction(this.acInterface.appLogoUrl, {
          fileId: "defaultSSOAppLogoImage"
        });
      } else {
        return BaseUtility.BuildAction(this.acInterface.appLogoUrl, {
          fileId: app.appMainImageId
        });
      }
    },
    settingApp: function settingApp(app) {
      var elem = this.$refs.ssoAppSubSystemEditModelDialogWrap;
      this.innerEditModelDialogStatus = "update";

      var _self = this;

      AjaxUtility.Post(this.acInterface.getDataUrl, {
        appId: app.appId
      }, function (result) {
        console.log(result);

        if (result.success) {
          _self.$refs.subAppDetailFromComp.setAppEntity(result.data.ssoAppEntity);

          _self.$refs.subAppInterfaceListComp.setInterfaceListData(result.data.ssoAppInterfaceEntityList);

          DialogUtility.DialogElemObj(elem, {
            modal: true,
            width: 900,
            height: 500,
            title: "子系统设置"
          });
        } else {
          DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, null);
        }
      }, "json");
    },
    removeApp: function removeApp(app) {
      var _self = this;

      DialogUtility.Confirm(window, "确认要注销系统[" + app.appName + "]吗？", function () {
        AjaxUtility.Delete(_self.acInterface.delete, {
          appId: app.appId
        }, function (result) {
          if (result.success) {
            DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, function () {
              _self.reloadData();
            });
          } else {
            DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, result.message, null);
          }
        }, "json");
      });
    }
  },
  template: "<div>\n                    <div ref=\"ssoAppSubSystemEditModelDialogWrap\" class=\"general-edit-page-wrap\" style=\"display: none;margin-top: 0px\">\n                        <tabs>\n                            <tab-pane label=\"\u7CFB\u7EDF\u8BBE\u7F6E\">\n                                <sso-app-detail-from-comp ref=\"subAppDetailFromComp\" :is-sub-system=\"true\" :status=\"innerEditModelDialogStatus\"></sso-app-detail-from-comp>\n                            </tab-pane>\n                            <tab-pane label=\"\u63A5\u53E3\u8BBE\u7F6E\">\n                                <sso-app-interface-list-comp ref=\"subAppInterfaceListComp\"></sso-app-interface-list-comp>\n                            </tab-pane>\n                        </tabs>\n                        <div class=\"button-outer-wrap\" style=\"margin-right: 10px;margin-bottom: 10px\">\n                            <div class=\"button-inner-wrap\">\n                                <button-group>\n                                    <i-button type=\"primary\" v-if=\"status!='view'\" @click=\"saveSubSystemSetting()\" icon=\"md-checkmark\">\u4FDD\u5B58\u5B50\u7CFB\u7EDF\u8BBE\u7F6E</i-button>\n                                    <i-button v-if=\"status!='view'\" @click=\"handleClose()\" icon=\"md-close\">\u5173\u95ED</i-button>\n                                </button-group>\n                            </div>\n                        </div>\n                    </div>\n                    <div class=\"apps-manager-outer-wrap\">\n                        <div class=\"apps-outer-wrap\" ref=\"appsOuterWrap\" v-if=\"status!='add'\">\n                            <div  v-for=\"app in appList\" class=\"app-outer-wrap app-outer-wrap-sub-system\">\n                                <div class=\"title\">\n                                    <span>{{app.appName}}</span>\n                                </div>\n                                <div class=\"content\">\n                                    <div class=\"mainImg\">\n                                        <img :src=\"buildLogoUrl(app)\" />\n                                    </div>\n                                    <div class=\"button-wrap\">\n                                        <div class=\"button setting-button\" @click=\"settingApp(app)\">\n                                            \u8BBE\u7F6E\n                                        </div>\n                                        <div class=\"button remove-button\" @click=\"removeApp(app)\">\n                                            \u6CE8\u9500\n                                        </div>\n                                    </div>\n                                </div>\n                            </div>\n                            <div class=\"app-outer-wrap app-outer-wrap-sub-system new-system-outer-wrap\">\n                                <div class=\"add-system-button\" @click=\"addIntegratedSystem()\" style=\"margin-top: 60px\">\u65B0\u589E</div>\n                            </div>\n                        </div>\n                        <div v-if=\"status=='add'\">\u8BF7\u5148\u4FDD\u5B58\u4E3B\u7CFB\u7EDF,\u518D\u8BBE\u7F6E\u5176\u4E2D\u7684\u5B50\u7CFB\u7EDF!</div>\n                    </div>\n                 </div>"
});
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIkRpYWxvZy9zZWxlY3QtZGVwYXJ0bWVudC11c2VyLWRpYWxvZy5qcyIsIlNTTy9zc28tYXBwLWRldGFpbC1mcm9tLWNvbXAuanMiLCJTU08vc3NvLWFwcC1pbnRlcmZhY2UtbGlzdC1jb21wLmpzIiwiU1NPL3Nzby1hcHAtc3ViLXN5c3RlbS1saXN0LWNvbXAuanMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FDeE1BO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FDbEhBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQzFLQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EiLCJmaWxlIjoiU1NPVnVlRVhDb21wb25lbnQuanMiLCJzb3VyY2VzQ29udGVudCI6WyJcInVzZSBzdHJpY3RcIjtcblxuVnVlLmNvbXBvbmVudChcInNlbGVjdC1kZXBhcnRtZW50LXVzZXItZGlhbG9nXCIsIHtcbiAgZGF0YTogZnVuY3Rpb24gZGF0YSgpIHtcbiAgICByZXR1cm4ge1xuICAgICAgYWNJbnRlcmZhY2U6IHtcbiAgICAgICAgZ2V0RGVwYXJ0bWVudFRyZWVEYXRhOiBcIi9SZXN0L1NTTy9EZXB0L0RlcGFydG1lbnQvR2V0RGVwYXJ0bWVudHNCeU9yZ2FuSWRcIixcbiAgICAgICAgcmVsb2FkTGlzdERhdGE6IFwiL1Jlc3QvU1NPL0RlcHQvRGVwYXJ0bWVudFVzZXIvR2V0TGlzdERhdGFcIlxuICAgICAgfSxcbiAgICAgIHRyZWVJZEZpZWxkTmFtZTogXCJkZXB0SWRcIixcbiAgICAgIHRyZWVPYmo6IG51bGwsXG4gICAgICB0cmVlU2VsZWN0ZWROb2RlOiBudWxsLFxuICAgICAgdHJlZVNldHRpbmc6IHtcbiAgICAgICAgYXN5bmM6IHtcbiAgICAgICAgICBlbmFibGU6IHRydWUsXG4gICAgICAgICAgdXJsOiBcIlwiXG4gICAgICAgIH0sXG4gICAgICAgIGRhdGE6IHtcbiAgICAgICAgICBrZXk6IHtcbiAgICAgICAgICAgIG5hbWU6IFwiZGVwdE5hbWVcIlxuICAgICAgICAgIH0sXG4gICAgICAgICAgc2ltcGxlRGF0YToge1xuICAgICAgICAgICAgZW5hYmxlOiB0cnVlLFxuICAgICAgICAgICAgaWRLZXk6IFwiZGVwdElkXCIsXG4gICAgICAgICAgICBwSWRLZXk6IFwiZGVwdFBhcmVudElkXCJcbiAgICAgICAgICB9XG4gICAgICAgIH0sXG4gICAgICAgIGNhbGxiYWNrOiB7XG4gICAgICAgICAgb25DbGljazogZnVuY3Rpb24gb25DbGljayhldmVudCwgdHJlZUlkLCB0cmVlTm9kZSkge1xuICAgICAgICAgICAgdmFyIF9zZWxmID0gdGhpcy5nZXRaVHJlZU9iaih0cmVlSWQpLl9ob3N0O1xuXG4gICAgICAgICAgICBfc2VsZi50cmVlTm9kZVNlbGVjdGVkKGV2ZW50LCB0cmVlSWQsIHRyZWVOb2RlKTtcbiAgICAgICAgICB9LFxuICAgICAgICAgIG9uQXN5bmNTdWNjZXNzOiBmdW5jdGlvbiBvbkFzeW5jU3VjY2VzcyhldmVudCwgdHJlZUlkLCB0cmVlTm9kZSwgbXNnKSB7XG4gICAgICAgICAgICBhcHBMaXN0LnRyZWVPYmouZXhwYW5kQWxsKHRydWUpO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfSxcbiAgICAgIGlkRmllbGROYW1lOiBcIkRVX0lEXCIsXG4gICAgICBzZWFyY2hDb25kaXRpb246IHtcbiAgICAgICAgdXNlck5hbWU6IHtcbiAgICAgICAgICB2YWx1ZTogXCJcIixcbiAgICAgICAgICB0eXBlOiBTZWFyY2hVdGlsaXR5LlNlYXJjaEZpZWxkVHlwZS5MaWtlU3RyaW5nVHlwZVxuICAgICAgICB9LFxuICAgICAgICBhY2NvdW50OiB7XG4gICAgICAgICAgdmFsdWU6IFwiXCIsXG4gICAgICAgICAgdHlwZTogU2VhcmNoVXRpbGl0eS5TZWFyY2hGaWVsZFR5cGUuTGlrZVN0cmluZ1R5cGVcbiAgICAgICAgfSxcbiAgICAgICAgdXNlclBob25lTnVtYmVyOiB7XG4gICAgICAgICAgdmFsdWU6IFwiXCIsXG4gICAgICAgICAgdHlwZTogU2VhcmNoVXRpbGl0eS5TZWFyY2hGaWVsZFR5cGUuTGlrZVN0cmluZ1R5cGVcbiAgICAgICAgfSxcbiAgICAgICAgZGVwYXJ0bWVudElkOiB7XG4gICAgICAgICAgdmFsdWU6IFwiXCIsXG4gICAgICAgICAgdHlwZTogU2VhcmNoVXRpbGl0eS5TZWFyY2hGaWVsZFR5cGUuU3RyaW5nVHlwZVxuICAgICAgICB9LFxuICAgICAgICBzZWFyY2hJbkFMTDoge1xuICAgICAgICAgIHZhbHVlOiBcIuWQplwiLFxuICAgICAgICAgIHR5cGU6IFNlYXJjaFV0aWxpdHkuU2VhcmNoRmllbGRUeXBlLlN0cmluZ1R5cGVcbiAgICAgICAgfVxuICAgICAgfSxcbiAgICAgIGNvbHVtbnNDb25maWc6IFt7XG4gICAgICAgIHR5cGU6ICdzZWxlY3Rpb24nLFxuICAgICAgICB3aWR0aDogNjAsXG4gICAgICAgIGFsaWduOiAnY2VudGVyJ1xuICAgICAgfSwge1xuICAgICAgICB0aXRsZTogJ+eUqOaIt+WQjScsXG4gICAgICAgIGtleTogJ1VTRVJfTkFNRScsXG4gICAgICAgIGFsaWduOiBcImNlbnRlclwiXG4gICAgICB9LCB7XG4gICAgICAgIHRpdGxlOiAn5omL5py65Y+356CBJyxcbiAgICAgICAga2V5OiAnVVNFUl9QSE9ORV9OVU1CRVInLFxuICAgICAgICB3aWR0aDogMTQwLFxuICAgICAgICBhbGlnbjogXCJjZW50ZXJcIlxuICAgICAgfSwge1xuICAgICAgICB0aXRsZTogJ+e7hOe7h+acuuaehCcsXG4gICAgICAgIGtleTogJ09SR0FOX05BTUUnLFxuICAgICAgICB3aWR0aDogMTQwLFxuICAgICAgICBhbGlnbjogXCJjZW50ZXJcIlxuICAgICAgfSwge1xuICAgICAgICB0aXRsZTogJ+mDqOmXqCcsXG4gICAgICAgIGtleTogJ0RFUFRfTkFNRScsXG4gICAgICAgIHdpZHRoOiAxNDAsXG4gICAgICAgIGFsaWduOiBcImNlbnRlclwiXG4gICAgICB9LCB7XG4gICAgICAgIHRpdGxlOiAn5Li75bGeJyxcbiAgICAgICAga2V5OiAnRFVfSVNfTUFJTicsXG4gICAgICAgIHdpZHRoOiA3MCxcbiAgICAgICAgYWxpZ246IFwiY2VudGVyXCJcbiAgICAgIH1dLFxuICAgICAgdGFibGVEYXRhOiBbXSxcbiAgICAgIHNlbGVjdGlvblJvd3M6IG51bGwsXG4gICAgICBwYWdlVG90YWw6IDAsXG4gICAgICBwYWdlU2l6ZTogMTIsXG4gICAgICBwYWdlTnVtOiAxLFxuICAgICAgbGlzdEhlaWdodDogMjcwXG4gICAgfTtcbiAgfSxcbiAgbW91bnRlZDogZnVuY3Rpb24gbW91bnRlZCgpIHtcbiAgICB2YXIgb2xkU2VsZWN0ZWRPcmdhbklkID0gQ29va2llVXRpbGl0eS5HZXRDb29raWUoXCJETU9SR1NJRFwiKTtcblxuICAgIGlmIChvbGRTZWxlY3RlZE9yZ2FuSWQpIHtcbiAgICAgIHRoaXMuJHJlZnMuc2VsZWN0T3JnYW5Db21wLnNldE9sZFNlbGVjdGVkT3JnYW4ob2xkU2VsZWN0ZWRPcmdhbklkKTtcbiAgICAgIHRoaXMuaW5pdFRyZWUob2xkU2VsZWN0ZWRPcmdhbklkKTtcbiAgICB9XG4gIH0sXG4gIG1ldGhvZHM6IHtcbiAgICBjaGFuZ2VPcmdhbjogZnVuY3Rpb24gY2hhbmdlT3JnYW4ob3JnYW5EYXRhKSB7XG4gICAgICBDb29raWVVdGlsaXR5LlNldENvb2tpZTFNb250aChcIkRNT1JHU0lEXCIsIG9yZ2FuRGF0YS5vcmdhbklkKTtcbiAgICAgIHRoaXMuaW5pdFRyZWUob3JnYW5EYXRhLm9yZ2FuSWQpO1xuICAgICAgdGhpcy5jbGVhclNlYXJjaENvbmRpdGlvbigpO1xuICAgICAgdGhpcy50YWJsZURhdGEgPSBbXTtcbiAgICB9LFxuICAgIGluaXRUcmVlOiBmdW5jdGlvbiBpbml0VHJlZShvcmdhbklkKSB7XG4gICAgICBBamF4VXRpbGl0eS5Qb3N0KHRoaXMuYWNJbnRlcmZhY2UuZ2V0RGVwYXJ0bWVudFRyZWVEYXRhLCB7XG4gICAgICAgIFwib3JnYW5JZFwiOiBvcmdhbklkXG4gICAgICB9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge1xuICAgICAgICAgIHRoaXMuJHJlZnMuelRyZWVVTC5zZXRBdHRyaWJ1dGUoXCJpZFwiLCBcInNlbGVjdC1kZXBhcnRtZW50LXVzZXItZGlhbG9nLVwiICsgU3RyaW5nVXRpbGl0eS5HdWlkKCkpO1xuICAgICAgICAgIHRoaXMudHJlZU9iaiA9ICQuZm4uelRyZWUuaW5pdCgkKHRoaXMuJHJlZnMuelRyZWVVTCksIHRoaXMudHJlZVNldHRpbmcsIHJlc3VsdC5kYXRhKTtcbiAgICAgICAgICB0aGlzLnRyZWVPYmouZXhwYW5kQWxsKHRydWUpO1xuICAgICAgICAgIHRoaXMudHJlZU9iai5faG9zdCA9IHRoaXM7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBmdW5jdGlvbiAoKSB7fSk7XG4gICAgICAgIH1cbiAgICAgIH0sIHRoaXMpO1xuICAgIH0sXG4gICAgdHJlZU5vZGVTZWxlY3RlZDogZnVuY3Rpb24gdHJlZU5vZGVTZWxlY3RlZChldmVudCwgdHJlZUlkLCB0cmVlTm9kZSkge1xuICAgICAgdGhpcy50cmVlU2VsZWN0ZWROb2RlID0gdHJlZU5vZGU7XG4gICAgICB0aGlzLnNlbGVjdGlvblJvd3MgPSBudWxsO1xuICAgICAgdGhpcy5wYWdlTnVtID0gMTtcbiAgICAgIHRoaXMuY2xlYXJTZWFyY2hDb25kaXRpb24oKTtcbiAgICAgIHRoaXMuc2VhcmNoQ29uZGl0aW9uLmRlcGFydG1lbnRJZC52YWx1ZSA9IHRoaXMudHJlZVNlbGVjdGVkTm9kZVt0aGlzLnRyZWVJZEZpZWxkTmFtZV07XG4gICAgICB0aGlzLnJlbG9hZERhdGEoKTtcbiAgICB9LFxuICAgIGNsZWFyU2VhcmNoQ29uZGl0aW9uOiBmdW5jdGlvbiBjbGVhclNlYXJjaENvbmRpdGlvbigpIHtcbiAgICAgIGZvciAodmFyIGtleSBpbiB0aGlzLnNlYXJjaENvbmRpdGlvbikge1xuICAgICAgICB0aGlzLnNlYXJjaENvbmRpdGlvbltrZXldLnZhbHVlID0gXCJcIjtcbiAgICAgIH1cblxuICAgICAgdGhpcy5zZWFyY2hDb25kaXRpb25bXCJzZWFyY2hJbkFMTFwiXS52YWx1ZSA9IFwi5ZCmXCI7XG4gICAgfSxcbiAgICBzZWxlY3Rpb25DaGFuZ2U6IGZ1bmN0aW9uIHNlbGVjdGlvbkNoYW5nZShzZWxlY3Rpb24pIHtcbiAgICAgIHRoaXMuc2VsZWN0aW9uUm93cyA9IHNlbGVjdGlvbjtcbiAgICB9LFxuICAgIHJlbG9hZERhdGE6IGZ1bmN0aW9uIHJlbG9hZERhdGEoKSB7XG4gICAgICBMaXN0UGFnZVV0aWxpdHkuSVZpZXdUYWJsZUJpbmREYXRhQnlTZWFyY2goe1xuICAgICAgICB1cmw6IHRoaXMuYWNJbnRlcmZhY2UucmVsb2FkTGlzdERhdGEsXG4gICAgICAgIHBhZ2VOdW06IHRoaXMucGFnZU51bSxcbiAgICAgICAgcGFnZVNpemU6IHRoaXMucGFnZVNpemUsXG4gICAgICAgIHNlYXJjaENvbmRpdGlvbjogdGhpcy5zZWFyY2hDb25kaXRpb24sXG4gICAgICAgIHBhZ2VBcHBPYmo6IHRoaXMsXG4gICAgICAgIHRhYmxlTGlzdDogdGhpcyxcbiAgICAgICAgaWRGaWVsZDogdGhpcy5pZEZpZWxkTmFtZSxcbiAgICAgICAgYXV0b1NlbGVjdGVkT2xkUm93czogZmFsc2UsXG4gICAgICAgIHN1Y2Nlc3NGdW5jOiBudWxsLFxuICAgICAgICBsb2FkRGljdDogZmFsc2UsXG4gICAgICAgIGN1c3RQYXJhczoge31cbiAgICAgIH0pO1xuICAgIH0sXG4gICAgY2hhbmdlUGFnZTogZnVuY3Rpb24gY2hhbmdlUGFnZShwYWdlTnVtKSB7XG4gICAgICB0aGlzLnBhZ2VOdW0gPSBwYWdlTnVtO1xuICAgICAgdGhpcy5yZWxvYWREYXRhKCk7XG4gICAgICB0aGlzLnNlbGVjdGlvblJvd3MgPSBudWxsO1xuICAgIH0sXG4gICAgc2VhcmNoOiBmdW5jdGlvbiBzZWFyY2goKSB7XG4gICAgICB0aGlzLnBhZ2VOdW0gPSAxO1xuICAgICAgdGhpcy5yZWxvYWREYXRhKCk7XG4gICAgfSxcbiAgICBiZWdpblNlbGVjdDogZnVuY3Rpb24gYmVnaW5TZWxlY3QoKSB7XG4gICAgICB2YXIgZWxlbSA9IHRoaXMuJHJlZnMuc2VsZWN0RGVwYXJ0bWVudFVzZXJNb2RlbERpYWxvZ1dyYXA7XG4gICAgICB2YXIgZGlhbG9nSGVpZ2h0ID0gNDYwO1xuXG4gICAgICBpZiAoUGFnZVN0eWxlVXRpbGl0eS5HZXRQYWdlSGVpZ2h0KCkgPiA3MDApIHtcbiAgICAgICAgZGlhbG9nSGVpZ2h0ID0gNjYwO1xuICAgICAgfVxuXG4gICAgICB0aGlzLmxpc3RIZWlnaHQgPSBkaWFsb2dIZWlnaHQgLSAyMzA7XG4gICAgICBEaWFsb2dVdGlsaXR5LkRpYWxvZ0VsZW1PYmooZWxlbSwge1xuICAgICAgICBtb2RhbDogdHJ1ZSxcbiAgICAgICAgd2lkdGg6IDk3MCxcbiAgICAgICAgaGVpZ2h0OiBkaWFsb2dIZWlnaHQsXG4gICAgICAgIHRpdGxlOiBcIumAieaLqee7hOe7h+acuuaehFwiXG4gICAgICB9KTtcbiAgICB9LFxuICAgIGNvbXBsZXRlZDogZnVuY3Rpb24gY29tcGxldGVkKCkge1xuICAgICAgY29uc29sZS5sb2codGhpcy5zZWxlY3Rpb25Sb3dzKTtcblxuICAgICAgaWYgKHRoaXMuc2VsZWN0aW9uUm93cykge1xuICAgICAgICB0aGlzLiRlbWl0KCdvbi1zZWxlY3RlZC1jb21wbGV0ZWQnLCB0aGlzLnNlbGVjdGlvblJvd3MpO1xuICAgICAgICB0aGlzLmhhbmRsZUNsb3NlKCk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgXCLor7flhYjpgInkuK3kurrlkZghXCIsIG51bGwpO1xuICAgICAgfVxuICAgIH0sXG4gICAgaGFuZGxlQ2xvc2U6IGZ1bmN0aW9uIGhhbmRsZUNsb3NlKCkge1xuICAgICAgRGlhbG9nVXRpbGl0eS5DbG9zZURpYWxvZ0VsZW0odGhpcy4kcmVmcy5zZWxlY3REZXBhcnRtZW50VXNlck1vZGVsRGlhbG9nV3JhcCk7XG4gICAgfVxuICB9LFxuICB0ZW1wbGF0ZTogXCI8ZGl2IHJlZj1cXFwic2VsZWN0RGVwYXJ0bWVudFVzZXJNb2RlbERpYWxvZ1dyYXBcXFwiIGNsYXNzPVxcXCJjMS1zZWxlY3QtbW9kZWwtd3JhcCBnZW5lcmFsLWVkaXQtcGFnZS13cmFwXFxcIiBzdHlsZT1cXFwiZGlzcGxheTogbm9uZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJsaXN0LTJjb2x1bW5cXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImxlZnQtb3V0ZXItd3JhcFxcXCIgc3R5bGU9XFxcIndpZHRoOiAxODBweDt0b3A6IDEwcHg7bGVmdDogMTBweDtib3R0b206IDU1cHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8c2VsZWN0LW9yZ2FuLXNpbmdsZS1jb21wIEBvbi1zZWxlY3RlZC1vcmdhbj1cXFwiY2hhbmdlT3JnYW5cXFwiIHJlZj1cXFwic2VsZWN0T3JnYW5Db21wXFxcIj48L3NlbGVjdC1vcmdhbi1zaW5nbGUtY29tcD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiaW5uZXItd3JhcFxcXCIgc3R5bGU9XFxcInBvc2l0aW9uOmFic29sdXRlO3RvcDogMzBweDtib3R0b206IDEwcHg7aGVpZ2h0OiBhdXRvO292ZXJmbG93OiBhdXRvXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHVsIHJlZj1cXFwielRyZWVVTFxcXCIgY2xhc3M9XFxcInp0cmVlXFxcIj48L3VsPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcInJpZ2h0LW91dGVyLXdyYXAgaXYtbGlzdC1wYWdlLXdyYXBcXFwiIHN0eWxlPVxcXCJwYWRkaW5nOiAxMHB4O2xlZnQ6IDIwMHB4O3RvcDogMTBweDtyaWdodDogMTBweDtib3R0b206IDU1cHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJsaXN0LXNpbXBsZS1zZWFyY2gtd3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGFibGUgY2xhc3M9XFxcImxzLXRhYmxlXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Y29sZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxjb2wgc3R5bGU9XFxcIndpZHRoOiA4MHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGNvbCBzdHlsZT1cXFwiXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGNvbCBzdHlsZT1cXFwid2lkdGg6IDEwMHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGNvbCBzdHlsZT1cXFwiXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGNvbCBzdHlsZT1cXFwid2lkdGg6IDgwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Y29sIHN0eWxlPVxcXCJ3aWR0aDogODVweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxjb2wgc3R5bGU9XFxcIndpZHRoOiA4MHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2NvbGdyb3VwPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0ciBjbGFzcz1cXFwibHMtdGFibGUtcm93XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkPlxcdTc1MjhcXHU2MjM3XFx1NTQwRFxcdUZGMUE8L3RkPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJzZWFyY2hDb25kaXRpb24udXNlck5hbWUudmFsdWVcXFwiIHBsYWNlaG9sZGVyPVxcXCJcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC90ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkPlxcdTYyNEJcXHU2NzNBXFx1RkYxQTwvdGQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcInNlYXJjaENvbmRpdGlvbi51c2VyUGhvbmVOdW1iZXIudmFsdWVcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC90ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkPlxcdTUxNjhcXHU1QzQwXFx1RkYxQTwvdGQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyYWRpby1ncm91cCB2LW1vZGVsPVxcXCJzZWFyY2hDb25kaXRpb24uc2VhcmNoSW5BTEwudmFsdWVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyYWRpbyBsYWJlbD1cXFwiXFx1NjYyRlxcXCI+XFx1NjYyRjwvcmFkaW8+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvIGxhYmVsPVxcXCJcXHU1NDI2XFxcIj5cXHU1NDI2PC9yYWRpbz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvcmFkaW8tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvdGQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0ZD48aS1idXR0b24gdHlwZT1cXFwicHJpbWFyeVxcXCIgQGNsaWNrPVxcXCJzZWFyY2hcXFwiPjxJY29uIHR5cGU9XFxcImFuZHJvaWQtc2VhcmNoXFxcIj48L0ljb24+IFxcdTY3RTVcXHU4QkUyIDwvaS1idXR0b24+PC90ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3RyPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC90YWJsZT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLXRhYmxlIDpoZWlnaHQ9XFxcImxpc3RIZWlnaHRcXFwiIHN0cmlwZSBib3JkZXIgOmNvbHVtbnM9XFxcImNvbHVtbnNDb25maWdcXFwiIDpkYXRhPVxcXCJ0YWJsZURhdGFcXFwiXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGNsYXNzPVxcXCJpdi1saXN0LXRhYmxlXFxcIiA6aGlnaGxpZ2h0LXJvdz1cXFwidHJ1ZVxcXCJcXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQG9uLXNlbGVjdGlvbi1jaGFuZ2U9XFxcInNlbGVjdGlvbkNoYW5nZVxcXCI+PC9pLXRhYmxlPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IHN0eWxlPVxcXCJmbG9hdDogcmlnaHQ7XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwYWdlIEBvbi1jaGFuZ2U9XFxcImNoYW5nZVBhZ2VcXFwiIDpjdXJyZW50LnN5bmM9XFxcInBhZ2VOdW1cXFwiIDpwYWdlLXNpemU9XFxcInBhZ2VTaXplXFxcIiBzaG93LXRvdGFsXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA6dG90YWw9XFxcInBhZ2VUb3RhbFxcXCI+PC9wYWdlPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLW91dGVyLXdyYXBcXFwiIHN0eWxlPVxcXCJib3R0b206IDEycHg7cmlnaHQ6IDEycHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbi1pbm5lci13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGJ1dHRvbi1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWJ1dHRvbiB0eXBlPVxcXCJwcmltYXJ5XFxcIiBAY2xpY2s9XFxcImNvbXBsZXRlZCgpXFxcIiBpY29uPVxcXCJtZC1jaGVja21hcmtcXFwiPlxcdTc4NkVcXHU4QkE0PC9pLWJ1dHRvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWJ1dHRvbiBAY2xpY2s9XFxcImhhbmRsZUNsb3NlKClcXFwiIGljb249XFxcIm1kLWNsb3NlXFxcIj5cXHU1MTczXFx1OTVFRDwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvYnV0dG9uLWdyb3VwPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgIDwvZGl2PlwiXG59KTsiLCJcInVzZSBzdHJpY3RcIjtcblxuVnVlLmNvbXBvbmVudChcInNzby1hcHAtZGV0YWlsLWZyb20tY29tcFwiLCB7XG4gIHByb3BzOiBbXCJzdGF0dXNcIiwgXCJhcHBJZFwiLCBcImlzU3ViU3lzdGVtXCJdLFxuICB3YXRjaDoge1xuICAgIGFwcElkOiBmdW5jdGlvbiBhcHBJZChuZXdWYWwpIHtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcElkID0gbmV3VmFsO1xuICAgIH0sXG4gICAgc3RhdHVzOiBmdW5jdGlvbiBzdGF0dXMobmV3VmFsKSB7XG4gICAgICB0aGlzLmlubmVyU3RhdHVzID0gbmV3VmFsO1xuICAgIH1cbiAgfSxcbiAgZGF0YTogZnVuY3Rpb24gZGF0YSgpIHtcbiAgICByZXR1cm4ge1xuICAgICAgYWNJbnRlcmZhY2U6IHtcbiAgICAgICAgYXBwTG9nb1VybDogXCIvUmVzdC9TU08vQXBwL0FwcGxpY2F0aW9uL0dldEFwcExvZ29cIixcbiAgICAgICAgZ2V0TmV3S2V5czogXCIvUmVzdC9TU08vQXBwL0FwcGxpY2F0aW9uL0dldE5ld0tleXNcIlxuICAgICAgfSxcbiAgICAgIGFwcEVudGl0eToge1xuICAgICAgICBhcHBJZDogXCJcIixcbiAgICAgICAgYXBwQ29kZTogXCJcIixcbiAgICAgICAgYXBwTmFtZTogXCJcIixcbiAgICAgICAgYXBwUHVibGljS2V5OiBcIlwiLFxuICAgICAgICBhcHBQcml2YXRlS2V5OiBcIlwiLFxuICAgICAgICBhcHBEb21haW46IFwiXCIsXG4gICAgICAgIGFwcEluZGV4VXJsOiBcIlwiLFxuICAgICAgICBhcHBNYWluSW1hZ2VJZDogXCJcIixcbiAgICAgICAgYXBwVHlwZTogXCJcIixcbiAgICAgICAgYXBwTWFpbklkOiBcIlwiLFxuICAgICAgICBhcHBDYXRlZ29yeTogXCJ3ZWJcIixcbiAgICAgICAgYXBwRGVzYzogXCJcIixcbiAgICAgICAgYXBwU3RhdHVzOiBcIuWQr+eUqFwiLFxuICAgICAgICBhcHBDcmVhdGVUaW1lOiBEYXRlVXRpbGl0eS5HZXRDdXJyZW50RGF0YSgpXG4gICAgICB9LFxuICAgICAgcnVsZVZhbGlkYXRlOiB7XG4gICAgICAgIGFwcENvZGU6IFt7XG4gICAgICAgICAgcmVxdWlyZWQ6IHRydWUsXG4gICAgICAgICAgbWVzc2FnZTogJ+OAkOezu+e7n+e8lueggeOAkeS4jeiDveS4uuepuu+8gScsXG4gICAgICAgICAgdHJpZ2dlcjogJ2JsdXInXG4gICAgICAgIH0sIHtcbiAgICAgICAgICB0eXBlOiAnc3RyaW5nJyxcbiAgICAgICAgICBwYXR0ZXJuOiAvXltBLVphLXowLTldKyQvLFxuICAgICAgICAgIG1lc3NhZ2U6ICfor7fkvb/nlKjlrZfmr43miJbmlbDlrZcnLFxuICAgICAgICAgIHRyaWdnZXI6ICdibHVyJ1xuICAgICAgICB9XSxcbiAgICAgICAgYXBwTmFtZTogW3tcbiAgICAgICAgICByZXF1aXJlZDogdHJ1ZSxcbiAgICAgICAgICBtZXNzYWdlOiAn44CQ57O757uf5ZCN56ew44CR5LiN6IO95Li656m677yBJyxcbiAgICAgICAgICB0cmlnZ2VyOiAnYmx1cidcbiAgICAgICAgfV1cbiAgICAgIH0sXG4gICAgICBzeXN0ZW1Mb2dvSW1hZ2VTcmM6IFwiXCIsXG4gICAgICBpbm5lclN0YXR1czogXCJhZGRcIlxuICAgIH07XG4gIH0sXG4gIG1vdW50ZWQ6IGZ1bmN0aW9uIG1vdW50ZWQoKSB7XG4gICAgaWYgKHRoaXMuaW5uZXJTdGF0dXMgPT0gXCJhZGRcIikge1xuICAgICAgdGhpcy5zeXN0ZW1Mb2dvSW1hZ2VTcmMgPSBCYXNlVXRpbGl0eS5CdWlsZEFjdGlvbih0aGlzLmFjSW50ZXJmYWNlLmFwcExvZ29VcmwsIHtcbiAgICAgICAgZmlsZUlkOiBcImRlZmF1bHRTU09BcHBMb2dvSW1hZ2VcIlxuICAgICAgfSk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHRoaXMuc3lzdGVtTG9nb0ltYWdlU3JjID0gQmFzZVV0aWxpdHkuQnVpbGRBY3Rpb24odGhpcy5hY0ludGVyZmFjZS5hcHBMb2dvVXJsLCB7XG4gICAgICAgIGZpbGVJZDogXCJcIlxuICAgICAgfSk7XG4gICAgfVxuICB9LFxuICBtZXRob2RzOiB7XG4gICAgcmVzZXRBcHBFbnRpdHk6IGZ1bmN0aW9uIHJlc2V0QXBwRW50aXR5KCkge1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwSWQgPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwQ29kZSA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBOYW1lID0gXCJcIjtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcFB1YmxpY0tleSA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBQcml2YXRlS2V5ID0gXCJcIjtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcERvbWFpbiA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBJbmRleFVybCA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBNYWluSW1hZ2VJZCA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBUeXBlID0gXCJcIjtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcE1haW5JZCA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBDYXRlZ29yeSA9IFwid2ViXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBEZXNjID0gXCJcIjtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcFN0YXR1cyA9IFwi5ZCv55SoXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBDcmVhdGVUaW1lID0gRGF0ZVV0aWxpdHkuR2V0Q3VycmVudERhdGEoKTtcbiAgICB9LFxuICAgIHVwbG9hZFN5c3RlbUxvZ29JbWFnZVN1Y2Nlc3M6IGZ1bmN0aW9uIHVwbG9hZFN5c3RlbUxvZ29JbWFnZVN1Y2Nlc3MocmVzcG9uc2UsIGZpbGUsIGZpbGVMaXN0KSB7XG4gICAgICBpZiAocmVzcG9uc2Uuc3VjY2Vzcykge1xuICAgICAgICB2YXIgZGF0YSA9IHJlc3BvbnNlLmRhdGE7XG4gICAgICAgIHRoaXMuYXBwRW50aXR5LmFwcE1haW5JbWFnZUlkID0gZGF0YS5maWxlSWQ7XG4gICAgICAgIHRoaXMuc3lzdGVtTG9nb0ltYWdlU3JjID0gQmFzZVV0aWxpdHkuQnVpbGRBY3Rpb24odGhpcy5hY0ludGVyZmFjZS5hcHBMb2dvVXJsLCB7XG4gICAgICAgICAgZmlsZUlkOiB0aGlzLmFwcEVudGl0eS5hcHBNYWluSW1hZ2VJZFxuICAgICAgICB9KTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnRFcnJvcih3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRFcnJvcklkLCB7fSwgcmVzcG9uc2UubWVzc2FnZSwgbnVsbCwgbnVsbCk7XG4gICAgICB9XG4gICAgfSxcbiAgICBnZXRBcHBFbnRpdHk6IGZ1bmN0aW9uIGdldEFwcEVudGl0eSgpIHtcbiAgICAgIHJldHVybiB0aGlzLmFwcEVudGl0eTtcbiAgICB9LFxuICAgIHNldEFwcEVudGl0eTogZnVuY3Rpb24gc2V0QXBwRW50aXR5KGFwcEVudGl0eSkge1xuICAgICAgdGhpcy5hcHBFbnRpdHkgPSBhcHBFbnRpdHk7XG4gICAgfSxcbiAgICBjcmVhdGVLZXlzOiBmdW5jdGlvbiBjcmVhdGVLZXlzKCkge1xuICAgICAgdmFyIF9zZWxmID0gdGhpcztcblxuICAgICAgQWpheFV0aWxpdHkuUG9zdCh0aGlzLmFjSW50ZXJmYWNlLmdldE5ld0tleXMsIHt9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge1xuICAgICAgICAgIF9zZWxmLmFwcEVudGl0eS5hcHBQdWJsaWNLZXkgPSByZXN1bHQuZGF0YS5wdWJsaWNLZXk7XG4gICAgICAgICAgX3NlbGYuYXBwRW50aXR5LmFwcFByaXZhdGVLZXkgPSByZXN1bHQuZGF0YS5wcml2YXRlS2V5O1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCByZXN1bHQubWVzc2FnZSwgbnVsbCk7XG4gICAgICAgIH1cbiAgICAgIH0sIHRoaXMpO1xuICAgIH1cbiAgfSxcbiAgdGVtcGxhdGU6IFwiPGRpdj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgc3R5bGU9XFxcIndpZHRoOiA4MCU7ZmxvYXQ6IGxlZnRcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxpLWZvcm0gcmVmPVxcXCJhcHBFbnRpdHlcXFwiIDptb2RlbD1cXFwiYXBwRW50aXR5XFxcIiA6cnVsZXM9XFxcInJ1bGVWYWxpZGF0ZVxcXCIgOmxhYmVsLXdpZHRoPVxcXCIxMDBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU3Q0ZCXFx1N0VERlxcdTdGMTZcXHU3ODAxXFx1RkYxQVxcXCIgcHJvcD1cXFwiYXBwQ29kZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cm93PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCIxMFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gcHJvcD1cXFwiYXBwQ29kZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwQ29kZVxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCI0XFxcIiBzdHlsZT1cXFwidGV4dC1hbGlnbjogY2VudGVyXFxcIj48c3BhbiBzdHlsZT1cXFwiY29sb3I6IHJlZFxcXCI+Kjwvc3Bhbj4gXFx1N0NGQlxcdTdFREZcXHU1NDBEXFx1NzlGMFxcdUZGMUE8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCIxMFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gcHJvcD1cXFwiYXBwTmFtZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwTmFtZVxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9yb3c+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU1N0RGXFx1NTQwRFxcdUZGMUFcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJvdz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiMTBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwRG9tYWluXFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiNFxcXCIgc3R5bGU9XFxcInRleHQtYWxpZ246IGNlbnRlclxcXCI+XFx1N0NGQlxcdTdFREZcXHU3QzdCXFx1NTIyQlxcdUZGMUE8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCIxMFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyYWRpby1ncm91cCB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwQ2F0ZWdvcnlcXFwiIHR5cGU9XFxcImJ1dHRvblxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cmFkaW8gbGFiZWw9XFxcImFwcFxcXCI+XFx1NzlGQlxcdTUyQThBcHA8L3JhZGlvPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvIGxhYmVsPVxcXCJ3ZWJcXFwiPldlYlxcdTdDRkJcXHU3RURGPC9yYWRpbz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9yYWRpby1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9yb3c+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU1MTZDXFx1OTRBNVxcdUZGMUFcXFwiIHYtaWY9XFxcImlzU3ViU3lzdGVtPT0nMCdcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgcGxhY2Vob2xkZXI9XFxcIlxcdThCRjdcXHU1MjFCXFx1NUVGQVxcdTVCQzZcXHU5NEE1XFx1NUJGOSxcXHU3NTI4XFx1NEU4RVxcdTY1NzBcXHU2MzZFXFx1NzY4NFxcdTUyQTBcXHU1QkM2XFx1NEY3RlxcdTc1MjhcXFwiIHNlYXJjaCBlbnRlci1idXR0b249XFxcIlxcdTUyMUJcXHU1RUZBXFx1NUJDNlxcdTk0QTVcXHU1QkY5XFxcIiB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwUHVibGljS2V5XFxcIiBAb24tc2VhcmNoPVxcXCJjcmVhdGVLZXlzKClcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gbGFiZWw9XFxcIlxcdTc5QzFcXHU5NEE1XFx1RkYxQVxcXCIgdi1pZj1cXFwiaXNTdWJTeXN0ZW09PTBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcFByaXZhdGVLZXlcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gbGFiZWw9XFxcIlxcdTUyMUJcXHU1RUZBXFx1NjVGNlxcdTk1RjRcXHVGRjFBXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyb3c+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktY29sIHNwYW49XFxcIjEwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRhdGUtcGlja2VyIHR5cGU9XFxcImRhdGVcXFwiIHBsYWNlaG9sZGVyPVxcXCJcXHU5MDA5XFx1NjJFOVxcdTUyMUJcXHU1RUZBXFx1NjVGNlxcdTk1RjRcXFwiIHYtbW9kZWw9XFxcImFwcEVudGl0eS5hcHBDcmVhdGVUaW1lXFxcIiBkaXNhYmxlZFxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcmVhZG9ubHk+PC9kYXRlLXBpY2tlcj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCI0XFxcIiBzdHlsZT1cXFwidGV4dC1hbGlnbjogY2VudGVyXFxcIj5cXHU3MkI2XFx1NjAwMVxcdUZGMUE8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCIxMFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cmFkaW8tZ3JvdXAgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcFN0YXR1c1xcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvIGxhYmVsPVxcXCJcXHU1NDJGXFx1NzUyOFxcXCI+XFx1NTQyRlxcdTc1Mjg8L3JhZGlvPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyYWRpbyBsYWJlbD1cXFwiXFx1Nzk4MVxcdTc1MjhcXFwiPlxcdTc5ODFcXHU3NTI4PC9yYWRpbz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvcmFkaW8tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvaS1jb2w+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3Jvdz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gbGFiZWw9XFxcIlxcdTlFRDhcXHU4QkE0XFx1NTczMFxcdTU3NDBcXHVGRjFBXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImFwcEVudGl0eS5hcHBJbmRleFVybFxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBsYWJlbD1cXFwiXFx1NTkwN1xcdTZDRThcXHVGRjFBXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImFwcEVudGl0eS5hcHBEZXNjXFxcIiB0eXBlPVxcXCJ0ZXh0YXJlYVxcXCIgOmF1dG9zaXplPVxcXCJ7bWluUm93czogMyxtYXhSb3dzOiAzfVxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2ktZm9ybT5cXG4gICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPGRpdiBzdHlsZT1cXFwid2lkdGg6IDE5JTtmbG9hdDogcmlnaHRcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgc3R5bGU9XFxcImJvcmRlci1yYWRpdXM6IDhweDt0ZXh0LWFsaWduOiBjZW50ZXI7bWFyZ2luLXRvcDogMHB4O21hcmdpbi1ib3R0b206IDMwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aW1nIDpzcmM9XFxcInN5c3RlbUxvZ29JbWFnZVNyY1xcXCIgc3R5bGU9XFxcIndpZHRoOiAxMTBweDtoZWlnaHQ6IDExMHB4XFxcIiAvPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDx1cGxvYWQgc3R5bGU9XFxcIm1hcmdpbjoxMHB4IDEycHggMCAyMHB4XFxcIiA6b24tc3VjY2Vzcz1cXFwidXBsb2FkU3lzdGVtTG9nb0ltYWdlU3VjY2Vzc1xcXCIgOmRhdGE9XFxcImFwcEVudGl0eVxcXCIgbXVsdGlwbGUgdHlwZT1cXFwiZHJhZ1xcXCIgbmFtZT1cXFwiZmlsZVxcXCIgYWN0aW9uPVxcXCIuLi8uLi8uLi9SZXN0L1NTTy9BcHAvQXBwbGljYXRpb24vVXBsb2FkQXBwTG9nb1xcXCIgYWNjZXB0PVxcXCIucG5nXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBzdHlsZT1cXFwicGFkZGluZzoxMHB4IDBweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aWNvbiB0eXBlPVxcXCJpb3MtY2xvdWQtdXBsb2FkXFxcIiBzaXplPVxcXCI1MlxcXCIgc3R5bGU9XFxcImNvbG9yOiAjMzM5OWZmXFxcIj48L2ljb24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cD5cXHU0RTBBXFx1NEYyMFxcdTdDRkJcXHU3RURGTG9nbzwvcD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC91cGxvYWQ+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgPC9kaXY+XCJcbn0pOyIsIlwidXNlIHN0cmljdFwiO1xuXG5WdWUuY29tcG9uZW50KFwic3NvLWFwcC1pbnRlcmZhY2UtbGlzdC1jb21wXCIsIHtcbiAgcHJvcHM6IFtcImludGVyZmFjZUJlbG9uZ0FwcElkXCJdLFxuICB3YXRjaDoge1xuICAgIGludGVyZmFjZUJlbG9uZ0FwcElkOiBmdW5jdGlvbiBpbnRlcmZhY2VCZWxvbmdBcHBJZChuZXdWYWwpIHtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUJlbG9uZ0FwcElkID0gbmV3VmFsO1xuICAgIH1cbiAgfSxcbiAgZGF0YTogZnVuY3Rpb24gZGF0YSgpIHtcbiAgICB2YXIgX3NlbGYgPSB0aGlzO1xuXG4gICAgcmV0dXJuIHtcbiAgICAgIGFjSW50ZXJmYWNlOiB7XG4gICAgICAgIGRlbGV0ZTogXCIvUGxhdEZvcm1SZXN0L1NTTy9BcHBsaWNhdGlvbi9EZWxldGVJbnRlcmZhY2VcIlxuICAgICAgfSxcbiAgICAgIGludGVyZmFjZUVudGl0eToge1xuICAgICAgICBpbnRlcmZhY2VJZDogXCJcIixcbiAgICAgICAgaW50ZXJmYWNlQmVsb25nQXBwSWQ6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZUNvZGU6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZU5hbWU6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZVVybDogXCJcIixcbiAgICAgICAgaW50ZXJmYWNlUGFyYXM6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZUZvcm1hdDogXCJcIixcbiAgICAgICAgaW50ZXJmYWNlRGVzYzogXCJcIixcbiAgICAgICAgaW50ZXJmYWNlT3JkZXJOdW06IFwiXCIsXG4gICAgICAgIGludGVyZmFjZUNyZWF0ZVRpbWU6IERhdGVVdGlsaXR5LkdldEN1cnJlbnREYXRhKCksXG4gICAgICAgIGludGVyZmFjZVN0YXR1czogXCLlkK/nlKhcIixcbiAgICAgICAgaW50ZXJmYWNlQ3JlYXRlcklkOiBcIlwiLFxuICAgICAgICBpbnRlcmZhY2VPcmdhbklkOiBcIlwiXG4gICAgICB9LFxuICAgICAgbGlzdDoge1xuICAgICAgICBjb2x1bW5zQ29uZmlnOiBbe1xuICAgICAgICAgIHR5cGU6ICdzZWxlY3Rpb24nLFxuICAgICAgICAgIHdpZHRoOiA2MCxcbiAgICAgICAgICBhbGlnbjogJ2NlbnRlcidcbiAgICAgICAgfSwge1xuICAgICAgICAgIHRpdGxlOiAn5o6l5Y+j57G75Z6LJyxcbiAgICAgICAgICBrZXk6ICdpbnRlcmZhY2VDb2RlJyxcbiAgICAgICAgICBhbGlnbjogXCJjZW50ZXJcIixcbiAgICAgICAgICB3aWR0aDogMTAwXG4gICAgICAgIH0sIHtcbiAgICAgICAgICB0aXRsZTogJ+aOpeWPo+WQjeensCcsXG4gICAgICAgICAga2V5OiAnaW50ZXJmYWNlTmFtZScsXG4gICAgICAgICAgYWxpZ246IFwiY2VudGVyXCIsXG4gICAgICAgICAgd2lkdGg6IDI4MFxuICAgICAgICB9LCB7XG4gICAgICAgICAgdGl0bGU6ICflpIfms6gnLFxuICAgICAgICAgIGtleTogJ2ludGVyZmFjZURlc2MnLFxuICAgICAgICAgIGFsaWduOiBcImNlbnRlclwiXG4gICAgICAgIH0sIHtcbiAgICAgICAgICB0aXRsZTogJ+aTjeS9nCcsXG4gICAgICAgICAga2V5OiAnaW50ZXJmYWNlSWQnLFxuICAgICAgICAgIHdpZHRoOiAxNDAsXG4gICAgICAgICAgYWxpZ246IFwiY2VudGVyXCIsXG4gICAgICAgICAgcmVuZGVyOiBmdW5jdGlvbiByZW5kZXIoaCwgcGFyYW1zKSB7XG4gICAgICAgICAgICByZXR1cm4gaCgnZGl2Jywge1xuICAgICAgICAgICAgICBjbGFzczogXCJsaXN0LXJvdy1idXR0b24td3JhcFwiXG4gICAgICAgICAgICB9LCBbTGlzdFBhZ2VVdGlsaXR5LklWaWV3VGFibGVJbm5lckJ1dHRvbi5FZGl0QnV0dG9uKGgsIHBhcmFtcywgXCJpbnRlcmZhY2VJZFwiLCBfc2VsZiksIExpc3RQYWdlVXRpbGl0eS5JVmlld1RhYmxlSW5uZXJCdXR0b24uRGVsZXRlQnV0dG9uKGgsIHBhcmFtcywgXCJpbnRlcmZhY2VJZFwiLCBfc2VsZildKTtcbiAgICAgICAgICB9XG4gICAgICAgIH1dLFxuICAgICAgICB0YWJsZURhdGE6IFtdXG4gICAgICB9LFxuICAgICAgaW5uZXJTdGF0dXM6IFwiYWRkXCJcbiAgICB9O1xuICB9LFxuICBtb3VudGVkOiBmdW5jdGlvbiBtb3VudGVkKCkge30sXG4gIG1ldGhvZHM6IHtcbiAgICByZXNldExpc3REYXRhOiBmdW5jdGlvbiByZXNldExpc3REYXRhKCkge1xuICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YSA9IFtdO1xuICAgIH0sXG4gICAgYWRkSW50ZXJmYWNlOiBmdW5jdGlvbiBhZGRJbnRlcmZhY2UoKSB7XG4gICAgICB2YXIgZWxlbSA9IHRoaXMuJHJlZnMuc3NvQXBwSW50ZXJmYWNlRWRpdE1vZGVsRGlhbG9nV3JhcDtcbiAgICAgIHRoaXMuaW5uZXJTdGF0dXMgPT0gXCJhZGRcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUlkID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUJlbG9uZ0FwcElkID0gdGhpcy5pbnRlcmZhY2VCZWxvbmdBcHBJZDtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUNvZGUgPSBcIlwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlTmFtZSA9IFwiXCI7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VVcmwgPSBcIlwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlUGFyYXMgPSBcIlwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlRm9ybWF0ID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZURlc2MgPSBcIlwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlT3JkZXJOdW0gPSBcIlwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlQ3JlYXRlVGltZSA9IERhdGVVdGlsaXR5LkdldEN1cnJlbnREYXRhKCk7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VTdGF0dXMgPSBcIuWQr+eUqFwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlQ3JlYXRlcklkID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZU9yZ2FuSWQgPSBcIlwiO1xuICAgICAgRGlhbG9nVXRpbGl0eS5EaWFsb2dFbGVtT2JqKGVsZW0sIHtcbiAgICAgICAgbW9kYWw6IHRydWUsXG4gICAgICAgIHdpZHRoOiA1NzAsXG4gICAgICAgIGhlaWdodDogMzMwLFxuICAgICAgICB0aXRsZTogXCLmjqXlj6Porr7nva5cIlxuICAgICAgfSk7XG4gICAgfSxcbiAgICBoYW5kbGVDbG9zZTogZnVuY3Rpb24gaGFuZGxlQ2xvc2UoKSB7XG4gICAgICBEaWFsb2dVdGlsaXR5LkNsb3NlRGlhbG9nRWxlbSh0aGlzLiRyZWZzLnNzb0FwcEludGVyZmFjZUVkaXRNb2RlbERpYWxvZ1dyYXApO1xuICAgIH0sXG4gICAgc2F2ZUludGVyZmFjZUVkaXQ6IGZ1bmN0aW9uIHNhdmVJbnRlcmZhY2VFZGl0KCkge1xuICAgICAgaWYgKHRoaXMuaW5uZXJTdGF0dXMgPT0gXCJhZGRcIikge1xuICAgICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VJZCA9IFN0cmluZ1V0aWxpdHkuR3VpZCgpO1xuICAgICAgICB0aGlzLmxpc3QudGFibGVEYXRhLnB1c2goSnNvblV0aWxpdHkuQ2xvbmVTaW1wbGUodGhpcy5pbnRlcmZhY2VFbnRpdHkpKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIGZvciAodmFyIGkgPSAwOyBpIDwgdGhpcy5saXN0LnRhYmxlRGF0YS5sZW5ndGg7IGkrKykge1xuICAgICAgICAgIGlmICh0aGlzLmxpc3QudGFibGVEYXRhW2ldLmludGVyZmFjZUlkID09IHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUlkKSB7XG4gICAgICAgICAgICB0aGlzLmxpc3QudGFibGVEYXRhW2ldLmludGVyZmFjZUNvZGUgPSB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDb2RlO1xuICAgICAgICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VOYW1lID0gdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlTmFtZTtcbiAgICAgICAgICAgIHRoaXMubGlzdC50YWJsZURhdGFbaV0uaW50ZXJmYWNlVXJsID0gdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlVXJsO1xuICAgICAgICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VQYXJhcyA9IHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZVBhcmFzO1xuICAgICAgICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VGb3JtYXQgPSB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VGb3JtYXQ7XG4gICAgICAgICAgICB0aGlzLmxpc3QudGFibGVEYXRhW2ldLmludGVyZmFjZURlc2MgPSB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VEZXNjO1xuICAgICAgICAgICAgYnJlYWs7XG4gICAgICAgICAgfVxuICAgICAgICB9XG4gICAgICB9XG5cbiAgICAgIHRoaXMuaGFuZGxlQ2xvc2UoKTtcbiAgICB9LFxuICAgIGNoYW5nZUludGVyZmFjZUNvZGU6IGZ1bmN0aW9uIGNoYW5nZUludGVyZmFjZUNvZGUodmFsdWUpIHtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUNvZGUgPSB2YWx1ZTtcbiAgICB9LFxuICAgIGdldEludGVyZmFjZUxpc3REYXRhOiBmdW5jdGlvbiBnZXRJbnRlcmZhY2VMaXN0RGF0YSgpIHtcbiAgICAgIHJldHVybiB0aGlzLmxpc3QudGFibGVEYXRhO1xuICAgIH0sXG4gICAgc2V0SW50ZXJmYWNlTGlzdERhdGE6IGZ1bmN0aW9uIHNldEludGVyZmFjZUxpc3REYXRhKGRhdGEpIHtcbiAgICAgIHRoaXMubGlzdC50YWJsZURhdGEgPSBkYXRhO1xuICAgIH0sXG4gICAgZWRpdDogZnVuY3Rpb24gZWRpdChpbnRlcmZhY2VJZCwgcGFyYW1zKSB7XG4gICAgICB0aGlzLmlubmVyU3RhdHVzID0gXCJ1cGRhdGVcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUlkID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VJZDtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUNvZGUgPSBwYXJhbXMucm93LmludGVyZmFjZUNvZGU7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VOYW1lID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VOYW1lO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlVXJsID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VVcmw7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VQYXJhcyA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlUGFyYXM7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VGb3JtYXQgPSBwYXJhbXMucm93LmludGVyZmFjZUZvcm1hdDtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZURlc2MgPSBwYXJhbXMucm93LmludGVyZmFjZURlc2M7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VPcmRlck51bSA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlT3JkZXJOdW07XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDcmVhdGVUaW1lID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VDcmVhdGVUaW1lO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlU3RhdHVzID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VTdGF0dXM7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDcmVhdGVySWQgPSBwYXJhbXMucm93LmludGVyZmFjZUNyZWF0ZXJJZDtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZU9yZ2FuSWQgPSBwYXJhbXMucm93LmludGVyZmFjZU9yZ2FuSWQ7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VCZWxvbmdBcHBJZCA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlQmVsb25nQXBwSWQ7XG4gICAgICB2YXIgZWxlbSA9IHRoaXMuJHJlZnMuc3NvQXBwSW50ZXJmYWNlRWRpdE1vZGVsRGlhbG9nV3JhcDtcbiAgICAgIERpYWxvZ1V0aWxpdHkuRGlhbG9nRWxlbU9iaihlbGVtLCB7XG4gICAgICAgIG1vZGFsOiB0cnVlLFxuICAgICAgICB3aWR0aDogNTcwLFxuICAgICAgICBoZWlnaHQ6IDMzMCxcbiAgICAgICAgdGl0bGU6IFwi5o6l5Y+j6K6+572uXCJcbiAgICAgIH0pO1xuICAgIH0sXG4gICAgZGVsOiBmdW5jdGlvbiBkZWwoaW50ZXJmYWNlSWQsIHBhcmFtcykge1xuICAgICAgdmFyIF9zZWxmID0gdGhpcztcblxuICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCB0aGlzLmxpc3QudGFibGVEYXRhLmxlbmd0aDsgaSsrKSB7XG4gICAgICAgIGlmICh0aGlzLmxpc3QudGFibGVEYXRhW2ldLmludGVyZmFjZUlkID09IGludGVyZmFjZUlkKSB7XG4gICAgICAgICAgX3NlbGYubGlzdC50YWJsZURhdGEuc3BsaWNlKGksIDEpO1xuXG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5Db25maXJtKHdpbmRvdywgXCLnoa7orqTopoHliKDpmaTor6XmjqXlj6PlkJfvvJ9cIiwgZnVuY3Rpb24gKCkge1xuICAgICAgICAgICAgQWpheFV0aWxpdHkuRGVsZXRlKF9zZWxmLmFjSW50ZXJmYWNlLmRlbGV0ZSwge1xuICAgICAgICAgICAgICBcImludGVyZmFjZUlkXCI6IGludGVyZmFjZUlkXG4gICAgICAgICAgICB9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge30gZWxzZSB7XG4gICAgICAgICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBudWxsKTtcbiAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgfSwgdGhpcyk7XG4gICAgICAgICAgfSk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9XG4gIH0sXG4gIHRlbXBsYXRlOiBcIjxkaXYgY2xhc3M9XFxcIml2LWxpc3QtcGFnZS13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgcmVmPVxcXCJzc29BcHBJbnRlcmZhY2VFZGl0TW9kZWxEaWFsb2dXcmFwXFxcIiBjbGFzcz1cXFwiZ2VuZXJhbC1lZGl0LXBhZ2Utd3JhcFxcXCIgc3R5bGU9XFxcImRpc3BsYXk6IG5vbmU7bWFyZ2luLXRvcDogMHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8aS1mb3JtIHJlZj1cXFwiaW50ZXJmYWNlRW50aXR5XFxcIiA6bW9kZWw9XFxcImludGVyZmFjZUVudGl0eVxcXCIgOmxhYmVsLXdpZHRoPVxcXCIxMzBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIHN0eWxlPVxcXCJtYXJnaW4tYm90dG9tOiAycHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHNwYW4gc2xvdD1cXFwibGFiZWxcXFwiPjxzcGFuIHN0eWxlPVxcXCJjb2xvcjogcmVkXFxcIj4qPC9zcGFuPiZuYnNwO1xcdTYzQTVcXHU1M0UzXFx1N0M3QlxcdTU3OEJcXHVGRjFBPC9zcGFuPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUNvZGVcXFwiIHNpemU9XFxcInNtYWxsXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8U2VsZWN0IHNsb3Q9XFxcImFwcGVuZFxcXCIgc3R5bGU9XFxcIndpZHRoOiA5MHB4XFxcIiBAb24tY2hhbmdlPVxcXCJjaGFuZ2VJbnRlcmZhY2VDb2RlXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPE9wdGlvbiB2YWx1ZT1cXFwiXFx1NzY3QlxcdTVGNTVcXHU2M0E1XFx1NTNFM1xcXCI+XFx1NzY3QlxcdTVGNTVcXHU2M0E1XFx1NTNFMzwvT3B0aW9uPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8T3B0aW9uIHZhbHVlPVxcXCJcXHU1MTc2XFx1NEVENlxcXCI+XFx1NTE3NlxcdTRFRDY8L09wdGlvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L1NlbGVjdD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gc3R5bGU9XFxcIm1hcmdpbi1ib3R0b206IDJweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8c3BhbiBzbG90PVxcXCJsYWJlbFxcXCI+PHNwYW4gc3R5bGU9XFxcImNvbG9yOiByZWRcXFwiPio8L3NwYW4+Jm5ic3A7XFx1NjNBNVxcdTUzRTNcXHU1NDBEXFx1NzlGMFxcdUZGMUE8L3NwYW4+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJpbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlTmFtZVxcXCIgc2l6ZT1cXFwic21hbGxcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gbGFiZWw9XFxcIlxcdTYzQTVcXHU1M0UzXFx1NTczMFxcdTU3NDBcXHVGRjFBXFxcIiBzdHlsZT1cXFwibWFyZ2luLWJvdHRvbTogMnB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VVcmxcXFwiIHNpemU9XFxcInNtYWxsXFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU1M0MyXFx1NjU3MFxcdUZGMUFcXFwiIHN0eWxlPVxcXCJtYXJnaW4tYm90dG9tOiAycHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZVBhcmFzXFxcIiB0eXBlPVxcXCJ0ZXh0YXJlYVxcXCIgOmF1dG9zaXplPVxcXCJ7bWluUm93czogMixtYXhSb3dzOiAyfVxcXCIgc2l6ZT1cXFwic21hbGxcXFwiPjwvaS1pbnB1dD4gICAgXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU2ODNDXFx1NUYwRlxcdTUzMTZcXHU2NUI5XFx1NkNENVxcdUZGMUFcXFwiIHN0eWxlPVxcXCJtYXJnaW4tYm90dG9tOiAycHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUZvcm1hdFxcXCIgc2l6ZT1cXFwic21hbGxcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gbGFiZWw9XFxcIlxcdTU5MDdcXHU2Q0U4XFx1RkYxQVxcXCIgc3R5bGU9XFxcIm1hcmdpbi1ib3R0b206IDJweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJpbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlRGVzY1xcXCIgc2l6ZT1cXFwic21hbGxcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWZvcm0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLW91dGVyLXdyYXBcXFwiIHN0eWxlPVxcXCJtYXJnaW4tbGVmdDogOHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLWlubmVyLXdyYXBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGJ1dHRvbi1ncm91cCBzaXplPVxcXCJzbWFsbFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uIHR5cGU9XFxcInByaW1hcnlcXFwiIEBjbGljaz1cXFwic2F2ZUludGVyZmFjZUVkaXQoJ2ludGVyZmFjZUVudGl0eScpXFxcIiBpY29uPVxcXCJtZC1jaGVja21hcmtcXFwiPjwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uIEBjbGljaz1cXFwiaGFuZGxlQ2xvc2UoKVxcXCIgaWNvbj1cXFwibWQtY2xvc2VcXFwiPjwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2J1dHRvbi1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgaWQ9XFxcImxpc3QtYnV0dG9uLXdyYXBcXFwiIGNsYXNzPVxcXCJsaXN0LWJ1dHRvbi1vdXRlci13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJsaXN0LWJ1dHRvbi1pbm5lci13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPEJ1dHRvbkdyb3VwPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uICB0eXBlPVxcXCJzdWNjZXNzXFxcIiBAY2xpY2s9XFxcImFkZEludGVyZmFjZSgpXFxcIiBpY29uPVxcXCJtZC1hZGRcXFwiPlxcdTY1QjBcXHU1ODlFPC9pLWJ1dHRvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9CdXR0b25Hcm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IHN0eWxlPVxcXCJjbGVhcjogYm90aFxcXCI+PC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDxpLXRhYmxlIDpoZWlnaHQ9XFxcImxpc3QubGlzdEhlaWdodFxcXCIgc3RyaXBlIGJvcmRlciA6Y29sdW1ucz1cXFwibGlzdC5jb2x1bW5zQ29uZmlnXFxcIiA6ZGF0YT1cXFwibGlzdC50YWJsZURhdGFcXFwiXFxuICAgICAgICAgICAgICAgICAgICAgICAgIGNsYXNzPVxcXCJpdi1saXN0LXRhYmxlXFxcIiA6aGlnaGxpZ2h0LXJvdz1cXFwidHJ1ZVxcXCI+PC9pLXRhYmxlPlxcbiAgICAgICAgICAgICAgICA8L2Rpdj5cIlxufSk7IiwiXCJ1c2Ugc3RyaWN0XCI7XG5cblZ1ZS5jb21wb25lbnQoXCJzc28tYXBwLXN1Yi1zeXN0ZW0tbGlzdC1jb21wXCIsIHtcbiAgcHJvcHM6IFtcInN0YXR1c1wiLCBcImJlbG9uZ0FwcElkXCJdLFxuICBkYXRhOiBmdW5jdGlvbiBkYXRhKCkge1xuICAgIHJldHVybiB7XG4gICAgICBhY0ludGVyZmFjZToge1xuICAgICAgICBzYXZlU3ViQXBwVXJsOiBcIi9QbGF0Rm9ybVJlc3QvU1NPL0FwcGxpY2F0aW9uL1NhdmVTdWJBcHBcIixcbiAgICAgICAgcmVsb2FkRGF0YTogXCIvUGxhdEZvcm1SZXN0L1NTTy9BcHBsaWNhdGlvbi9HZXRBbGxTdWJTc29BcHBcIixcbiAgICAgICAgYXBwTG9nb1VybDogXCIvUGxhdEZvcm1SZXN0L1NTTy9BcHBsaWNhdGlvbi9HZXRBcHBMb2dvXCIsXG4gICAgICAgIGRlbGV0ZTogXCIvUGxhdEZvcm1SZXN0L1NTTy9BcHBsaWNhdGlvbi9EZWxldGVcIixcbiAgICAgICAgZ2V0RGF0YVVybDogXCIvUGxhdEZvcm1SZXN0L1NTTy9BcHBsaWNhdGlvbi9HZXRBcHBWb1wiXG4gICAgICB9LFxuICAgICAgYXBwTGlzdDogW10sXG4gICAgICBpbm5lckVkaXRNb2RlbERpYWxvZ1N0YXR1czogXCJhZGRcIlxuICAgIH07XG4gIH0sXG4gIG1vdW50ZWQ6IGZ1bmN0aW9uIG1vdW50ZWQoKSB7XG4gICAgdGhpcy5yZWxvYWREYXRhKCk7XG4gIH0sXG4gIG1ldGhvZHM6IHtcbiAgICBhZGRJbnRlZ3JhdGVkU3lzdGVtOiBmdW5jdGlvbiBhZGRJbnRlZ3JhdGVkU3lzdGVtKCkge1xuICAgICAgdmFyIGVsZW0gPSB0aGlzLiRyZWZzLnNzb0FwcFN1YlN5c3RlbUVkaXRNb2RlbERpYWxvZ1dyYXA7XG4gICAgICB0aGlzLiRyZWZzLnN1YkFwcERldGFpbEZyb21Db21wLnJlc2V0QXBwRW50aXR5KCk7XG4gICAgICB0aGlzLiRyZWZzLnN1YkFwcEludGVyZmFjZUxpc3RDb21wLnJlc2V0TGlzdERhdGEoKTtcbiAgICAgIHRoaXMuaW5uZXJFZGl0TW9kZWxEaWFsb2dTdGF0dXMgPSBcImFkZFwiO1xuICAgICAgRGlhbG9nVXRpbGl0eS5EaWFsb2dFbGVtT2JqKGVsZW0sIHtcbiAgICAgICAgbW9kYWw6IHRydWUsXG4gICAgICAgIHdpZHRoOiA5MDAsXG4gICAgICAgIGhlaWdodDogNTAwLFxuICAgICAgICB0aXRsZTogXCLlrZDns7vnu5/orr7nva5cIlxuICAgICAgfSk7XG4gICAgfSxcbiAgICBzYXZlU3ViU3lzdGVtU2V0dGluZzogZnVuY3Rpb24gc2F2ZVN1YlN5c3RlbVNldHRpbmcoKSB7XG4gICAgICB2YXIgX3NlbGYgPSB0aGlzO1xuXG4gICAgICB2YXIgc3NvQXBwRW50aXR5ID0gdGhpcy4kcmVmcy5zdWJBcHBEZXRhaWxGcm9tQ29tcC5nZXRBcHBFbnRpdHkoKTtcbiAgICAgIHZhciBzc29BcHBJbnRlcmZhY2VFbnRpdHlMaXN0ID0gdGhpcy4kcmVmcy5zdWJBcHBJbnRlcmZhY2VMaXN0Q29tcC5nZXRJbnRlcmZhY2VMaXN0RGF0YSgpO1xuICAgICAgc3NvQXBwRW50aXR5LmFwcE1haW5JZCA9IHRoaXMuYmVsb25nQXBwSWQ7XG5cbiAgICAgIGlmICh0aGlzLmlubmVyRWRpdE1vZGVsRGlhbG9nU3RhdHVzID09IFwiYWRkXCIpIHtcbiAgICAgICAgc3NvQXBwRW50aXR5LmFwcElkID0gU3RyaW5nVXRpbGl0eS5HdWlkKCk7XG4gICAgICB9XG5cbiAgICAgIGlmIChzc29BcHBJbnRlcmZhY2VFbnRpdHlMaXN0KSB7XG4gICAgICAgIGZvciAodmFyIGkgPSAwOyBpIDwgc3NvQXBwSW50ZXJmYWNlRW50aXR5TGlzdC5sZW5ndGg7IGkrKykge1xuICAgICAgICAgIHNzb0FwcEludGVyZmFjZUVudGl0eUxpc3RbaV0uaW50ZXJmYWNlQmVsb25nQXBwSWQgPSBzc29BcHBFbnRpdHkuYXBwSWQ7XG4gICAgICAgIH1cbiAgICAgIH1cblxuICAgICAgdmFyIHZvID0ge1xuICAgICAgICBcInNzb0FwcEVudGl0eVwiOiBzc29BcHBFbnRpdHksXG4gICAgICAgIFwic3NvQXBwSW50ZXJmYWNlRW50aXR5TGlzdFwiOiBzc29BcHBJbnRlcmZhY2VFbnRpdHlMaXN0XG4gICAgICB9O1xuICAgICAgdmFyIHNlbmREYXRhID0gSlNPTi5zdHJpbmdpZnkodm8pO1xuICAgICAgQWpheFV0aWxpdHkuUG9zdFJlcXVlc3RCb2R5KHRoaXMuYWNJbnRlcmZhY2Uuc2F2ZVN1YkFwcFVybCwgc2VuZERhdGEsIGZ1bmN0aW9uIChyZXN1bHQpIHtcbiAgICAgICAgaWYgKHJlc3VsdC5zdWNjZXNzKSB7XG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBmdW5jdGlvbiAoKSB7XG4gICAgICAgICAgICBfc2VsZi5yZWxvYWREYXRhKCk7XG5cbiAgICAgICAgICAgIF9zZWxmLmhhbmRsZUNsb3NlKCk7XG4gICAgICAgICAgfSk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBudWxsKTtcbiAgICAgICAgfVxuICAgICAgfSwgXCJqc29uXCIpO1xuICAgIH0sXG4gICAgaGFuZGxlQ2xvc2U6IGZ1bmN0aW9uIGhhbmRsZUNsb3NlKCkge1xuICAgICAgRGlhbG9nVXRpbGl0eS5DbG9zZURpYWxvZ0VsZW0odGhpcy4kcmVmcy5zc29BcHBTdWJTeXN0ZW1FZGl0TW9kZWxEaWFsb2dXcmFwKTtcbiAgICB9LFxuICAgIHJlbG9hZERhdGE6IGZ1bmN0aW9uIHJlbG9hZERhdGEoKSB7XG4gICAgICB2YXIgX3NlbGYgPSB0aGlzO1xuXG4gICAgICBBamF4VXRpbGl0eS5Qb3N0KHRoaXMuYWNJbnRlcmZhY2UucmVsb2FkRGF0YSwge1xuICAgICAgICBhcHBJZDogX3NlbGYuYmVsb25nQXBwSWRcbiAgICAgIH0sIGZ1bmN0aW9uIChyZXN1bHQpIHtcbiAgICAgICAgaWYgKHJlc3VsdC5zdWNjZXNzKSB7XG4gICAgICAgICAgX3NlbGYuYXBwTGlzdCA9IHJlc3VsdC5kYXRhO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCByZXN1bHQubWVzc2FnZSwgbnVsbCk7XG4gICAgICAgIH1cbiAgICAgIH0sIFwianNvblwiKTtcbiAgICB9LFxuICAgIGJ1aWxkTG9nb1VybDogZnVuY3Rpb24gYnVpbGRMb2dvVXJsKGFwcCkge1xuICAgICAgaWYgKGFwcC5hcHBNYWluSW1hZ2VJZCA9PSBcIlwiKSB7XG4gICAgICAgIHJldHVybiBCYXNlVXRpbGl0eS5CdWlsZEFjdGlvbih0aGlzLmFjSW50ZXJmYWNlLmFwcExvZ29VcmwsIHtcbiAgICAgICAgICBmaWxlSWQ6IFwiZGVmYXVsdFNTT0FwcExvZ29JbWFnZVwiXG4gICAgICAgIH0pO1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgcmV0dXJuIEJhc2VVdGlsaXR5LkJ1aWxkQWN0aW9uKHRoaXMuYWNJbnRlcmZhY2UuYXBwTG9nb1VybCwge1xuICAgICAgICAgIGZpbGVJZDogYXBwLmFwcE1haW5JbWFnZUlkXG4gICAgICAgIH0pO1xuICAgICAgfVxuICAgIH0sXG4gICAgc2V0dGluZ0FwcDogZnVuY3Rpb24gc2V0dGluZ0FwcChhcHApIHtcbiAgICAgIHZhciBlbGVtID0gdGhpcy4kcmVmcy5zc29BcHBTdWJTeXN0ZW1FZGl0TW9kZWxEaWFsb2dXcmFwO1xuICAgICAgdGhpcy5pbm5lckVkaXRNb2RlbERpYWxvZ1N0YXR1cyA9IFwidXBkYXRlXCI7XG5cbiAgICAgIHZhciBfc2VsZiA9IHRoaXM7XG5cbiAgICAgIEFqYXhVdGlsaXR5LlBvc3QodGhpcy5hY0ludGVyZmFjZS5nZXREYXRhVXJsLCB7XG4gICAgICAgIGFwcElkOiBhcHAuYXBwSWRcbiAgICAgIH0sIGZ1bmN0aW9uIChyZXN1bHQpIHtcbiAgICAgICAgY29uc29sZS5sb2cocmVzdWx0KTtcblxuICAgICAgICBpZiAocmVzdWx0LnN1Y2Nlc3MpIHtcbiAgICAgICAgICBfc2VsZi4kcmVmcy5zdWJBcHBEZXRhaWxGcm9tQ29tcC5zZXRBcHBFbnRpdHkocmVzdWx0LmRhdGEuc3NvQXBwRW50aXR5KTtcblxuICAgICAgICAgIF9zZWxmLiRyZWZzLnN1YkFwcEludGVyZmFjZUxpc3RDb21wLnNldEludGVyZmFjZUxpc3REYXRhKHJlc3VsdC5kYXRhLnNzb0FwcEludGVyZmFjZUVudGl0eUxpc3QpO1xuXG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5EaWFsb2dFbGVtT2JqKGVsZW0sIHtcbiAgICAgICAgICAgIG1vZGFsOiB0cnVlLFxuICAgICAgICAgICAgd2lkdGg6IDkwMCxcbiAgICAgICAgICAgIGhlaWdodDogNTAwLFxuICAgICAgICAgICAgdGl0bGU6IFwi5a2Q57O757uf6K6+572uXCJcbiAgICAgICAgICB9KTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgcmVzdWx0Lm1lc3NhZ2UsIG51bGwpO1xuICAgICAgICB9XG4gICAgICB9LCBcImpzb25cIik7XG4gICAgfSxcbiAgICByZW1vdmVBcHA6IGZ1bmN0aW9uIHJlbW92ZUFwcChhcHApIHtcbiAgICAgIHZhciBfc2VsZiA9IHRoaXM7XG5cbiAgICAgIERpYWxvZ1V0aWxpdHkuQ29uZmlybSh3aW5kb3csIFwi56Gu6K6k6KaB5rOo6ZSA57O757ufW1wiICsgYXBwLmFwcE5hbWUgKyBcIl3lkJfvvJ9cIiwgZnVuY3Rpb24gKCkge1xuICAgICAgICBBamF4VXRpbGl0eS5EZWxldGUoX3NlbGYuYWNJbnRlcmZhY2UuZGVsZXRlLCB7XG4gICAgICAgICAgYXBwSWQ6IGFwcC5hcHBJZFxuICAgICAgICB9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgICAgaWYgKHJlc3VsdC5zdWNjZXNzKSB7XG4gICAgICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgcmVzdWx0Lm1lc3NhZ2UsIGZ1bmN0aW9uICgpIHtcbiAgICAgICAgICAgICAgX3NlbGYucmVsb2FkRGF0YSgpO1xuICAgICAgICAgICAgfSk7XG4gICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCByZXN1bHQubWVzc2FnZSwgbnVsbCk7XG4gICAgICAgICAgfVxuICAgICAgICB9LCBcImpzb25cIik7XG4gICAgICB9KTtcbiAgICB9XG4gIH0sXG4gIHRlbXBsYXRlOiBcIjxkaXY+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IHJlZj1cXFwic3NvQXBwU3ViU3lzdGVtRWRpdE1vZGVsRGlhbG9nV3JhcFxcXCIgY2xhc3M9XFxcImdlbmVyYWwtZWRpdC1wYWdlLXdyYXBcXFwiIHN0eWxlPVxcXCJkaXNwbGF5OiBub25lO21hcmdpbi10b3A6IDBweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPHRhYnM+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0YWItcGFuZSBsYWJlbD1cXFwiXFx1N0NGQlxcdTdFREZcXHU4QkJFXFx1N0Y2RVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8c3NvLWFwcC1kZXRhaWwtZnJvbS1jb21wIHJlZj1cXFwic3ViQXBwRGV0YWlsRnJvbUNvbXBcXFwiIDppcy1zdWItc3lzdGVtPVxcXCJ0cnVlXFxcIiA6c3RhdHVzPVxcXCJpbm5lckVkaXRNb2RlbERpYWxvZ1N0YXR1c1xcXCI+PC9zc28tYXBwLWRldGFpbC1mcm9tLWNvbXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvdGFiLXBhbmU+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0YWItcGFuZSBsYWJlbD1cXFwiXFx1NjNBNVxcdTUzRTNcXHU4QkJFXFx1N0Y2RVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8c3NvLWFwcC1pbnRlcmZhY2UtbGlzdC1jb21wIHJlZj1cXFwic3ViQXBwSW50ZXJmYWNlTGlzdENvbXBcXFwiPjwvc3NvLWFwcC1pbnRlcmZhY2UtbGlzdC1jb21wPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3RhYi1wYW5lPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvdGFicz5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJidXR0b24tb3V0ZXItd3JhcFxcXCIgc3R5bGU9XFxcIm1hcmdpbi1yaWdodDogMTBweDttYXJnaW4tYm90dG9tOiAxMHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLWlubmVyLXdyYXBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGJ1dHRvbi1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1idXR0b24gdHlwZT1cXFwicHJpbWFyeVxcXCIgdi1pZj1cXFwic3RhdHVzIT0ndmlldydcXFwiIEBjbGljaz1cXFwic2F2ZVN1YlN5c3RlbVNldHRpbmcoKVxcXCIgaWNvbj1cXFwibWQtY2hlY2ttYXJrXFxcIj5cXHU0RkREXFx1NUI1OFxcdTVCNTBcXHU3Q0ZCXFx1N0VERlxcdThCQkVcXHU3RjZFPC9pLWJ1dHRvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1idXR0b24gdi1pZj1cXFwic3RhdHVzIT0ndmlldydcXFwiIEBjbGljaz1cXFwiaGFuZGxlQ2xvc2UoKVxcXCIgaWNvbj1cXFwibWQtY2xvc2VcXFwiPlxcdTUxNzNcXHU5NUVEPC9pLWJ1dHRvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvYnV0dG9uLWdyb3VwPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYXBwcy1tYW5hZ2VyLW91dGVyLXdyYXBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImFwcHMtb3V0ZXItd3JhcFxcXCIgcmVmPVxcXCJhcHBzT3V0ZXJXcmFwXFxcIiB2LWlmPVxcXCJzdGF0dXMhPSdhZGQnXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiAgdi1mb3I9XFxcImFwcCBpbiBhcHBMaXN0XFxcIiBjbGFzcz1cXFwiYXBwLW91dGVyLXdyYXAgYXBwLW91dGVyLXdyYXAtc3ViLXN5c3RlbVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJ0aXRsZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHNwYW4+e3thcHAuYXBwTmFtZX19PC9zcGFuPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJjb250ZW50XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJtYWluSW1nXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGltZyA6c3JjPVxcXCJidWlsZExvZ29VcmwoYXBwKVxcXCIgLz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJidXR0b24td3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbiBzZXR0aW5nLWJ1dHRvblxcXCIgQGNsaWNrPVxcXCJzZXR0aW5nQXBwKGFwcClcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXFx1OEJCRVxcdTdGNkVcXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbiByZW1vdmUtYnV0dG9uXFxcIiBAY2xpY2s9XFxcInJlbW92ZUFwcChhcHApXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFxcdTZDRThcXHU5NTAwXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJhcHAtb3V0ZXItd3JhcCBhcHAtb3V0ZXItd3JhcC1zdWItc3lzdGVtIG5ldy1zeXN0ZW0tb3V0ZXItd3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJhZGQtc3lzdGVtLWJ1dHRvblxcXCIgQGNsaWNrPVxcXCJhZGRJbnRlZ3JhdGVkU3lzdGVtKClcXFwiIHN0eWxlPVxcXCJtYXJnaW4tdG9wOiA2MHB4XFxcIj5cXHU2NUIwXFx1NTg5RTwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IHYtaWY9XFxcInN0YXR1cz09J2FkZCdcXFwiPlxcdThCRjdcXHU1MTQ4XFx1NEZERFxcdTVCNThcXHU0RTNCXFx1N0NGQlxcdTdFREYsXFx1NTE4RFxcdThCQkVcXHU3RjZFXFx1NTE3NlxcdTRFMkRcXHU3Njg0XFx1NUI1MFxcdTdDRkJcXHU3RURGITwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICA8L2Rpdj5cIlxufSk7Il19
