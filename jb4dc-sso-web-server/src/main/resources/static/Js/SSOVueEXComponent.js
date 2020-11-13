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

Vue.component("select-module-object-dialog", {
  data: function data() {
    return {
      acInterface: {
        getDBLinkData: "/Rest/SSO/Mu/Menu/GetFullDBLink",
        getModuleTreeData: "/Rest/SSO/Mu/Menu/GetMouldTreeData",
        reloadListData: "/Rest/SSO/Mu/Menu/GetSelectModuleObjectListByModuleId"
      },
      selectModuleObjectType: "",
      selectedDBLinkId: "",
      dbLinkArray: [],
      treeIdFieldName: "moduleId",
      treeObj: null,
      treeSelectedNode: null,
      treeSetting: {
        async: {
          enable: true,
          url: ""
        },
        data: {
          key: {
            name: "moduleText"
          },
          simpleData: {
            enable: true,
            idKey: "moduleId",
            pIdKey: "moduleParentId"
          }
        },
        callback: {
          onClick: function onClick(event, treeId, treeNode) {
            var _self = this.getZTreeObj(treeId)._host;

            _self.treeNodeSelected(event, treeId, treeNode);
          },
          onAsyncSuccess: function onAsyncSuccess(event, treeId, treeNode, msg) {}
        }
      },
      selectedModuleObject: null,
      idFieldName: "ID",
      searchCondition: {
        selectModuleObjectType: {
          value: "",
          type: SearchUtility.SearchFieldType.StringType
        },
        selectModuleId: {
          value: "",
          type: SearchUtility.SearchFieldType.StringType
        }
      },
      columnsConfig: [{
        title: '名称',
        key: 'NAME',
        align: "center"
      }, {
        title: '备注',
        key: 'DESCRIPTION',
        width: 240,
        align: "center"
      }],
      tableData: [],
      pageTotal: 0,
      pageSize: 12,
      pageNum: 1,
      listHeight: 510
    };
  },
  mounted: function mounted() {
    this.bindDBLink();
  },
  methods: {
    bindDBLink: function bindDBLink() {
      AjaxUtility.Post(this.acInterface.getDBLinkData, {}, function (result) {
        if (result.success) {
          console.log(result.data);
          this.dbLinkArray = result.data;
          var oldSelectedOrganId = CookieUtility.GetCookie("SMODDBLINKID");
          this.selectedDBLinkId = oldSelectedOrganId;

          if (this.selectedDBLinkId) {
            this.initTree(this.selectedDBLinkId);
          }
        }
      }, this);
    },
    changeDBLink: function changeDBLink() {
      console.log(this.selectedDBLinkId);
      this.initTree(this.selectedDBLinkId);
      CookieUtility.SetCookie1Month("SMODDBLINKID", this.selectedDBLinkId);
    },
    initTree: function initTree(selectedDBLinkId) {
      AjaxUtility.Post(this.acInterface.getModuleTreeData, {
        "dbLinkId": selectedDBLinkId
      }, function (result) {
        console.log(result);

        if (result.success) {
          for (var i = 0; i < result.data.length; i++) {
            if (result.data[i].moduleParentId != "-1") {
              result.data[i].icon = "../../../Themes/Png16X16/layout-sidebar.png";
            }
          }

          this.$refs.zTreeUL.setAttribute("id", "select-module-object-dialog-" + StringUtility.Guid());
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
      this.pageNum = 1;
      this.searchCondition.selectModuleObjectType.value = this.selectModuleObjectType;
      this.searchCondition.selectModuleId.value = treeNode.moduleId;
      this.reloadData();
    },
    moduleObjectClick: function moduleObjectClick(data, index) {
      this.selectedModuleObject = data;
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
        successFunc: function successFunc(result) {
          console.log(result);
        },
        loadDict: false,
        custParas: {}
      });
    },
    changePage: function changePage(pageNum) {
      this.pageNum = pageNum;
      this.reloadData();
    },
    search: function search() {
      this.pageNum = 1;
      this.reloadData();
    },
    beginSelect: function beginSelect(selectModuleObjectType) {
      var elem = this.$refs.selectModuleObjectDialogWrap;
      this.selectModuleObjectType = selectModuleObjectType;
      var dialogHeight = 660;
      DialogUtility.DialogElemObj(elem, {
        modal: true,
        width: 870,
        height: dialogHeight,
        title: "选择关联对象1"
      });
    },
    buildDisplayName: function buildDisplayName(selectedModuleObject) {
      var txt = "路径:【" + TreeUtility.BuildNodePathName(this.treeSelectedNode, "moduleText", selectedModuleObject.NAME + "(" + selectedModuleObject.CODE + ")") + "】";

      if (this.selectModuleObjectType == "Web模块列表") {
        txt += "，数据集:（" + selectedModuleObject.LIST_DATASET_NAME + "）";
      }

      return txt;
    },
    completed: function completed() {
      if (this.selectedModuleObject) {
        this.$emit('on-selected-completed', this.selectedModuleObject, this.selectedModuleObject.ID, this.buildDisplayName(this.selectedModuleObject), JsonUtility.JsonToString(this.selectedModuleObject));
        this.handleClose();
      } else {
        DialogUtility.Alert(window, DialogUtility.DialogAlertId, {}, "请先选择绑定对象!", null);
      }
    },
    handleClose: function handleClose() {
      DialogUtility.CloseDialogElem(this.$refs.selectModuleObjectDialogWrap);
    }
  },
  template: "<div ref=\"selectModuleObjectDialogWrap\" class=\"c1-select-model-wrap general-edit-page-wrap\" style=\"display: none\">\n                    <div class=\"list-2column\">\n                        <div class=\"left-outer-wrap\" style=\"width: 220px;top: 10px;left: 10px;bottom: 55px\">\n                            <i-select @on-change=\"changeDBLink\" v-model=\"selectedDBLinkId\">\n                                <i-option v-for=\"item in dbLinkArray\" :value=\"item.dbId\" :key=\"item.dbId\">{{ item.dbLinkName }}</i-option>\n                            </i-select>\n                            <div class=\"inner-wrap\" style=\"position:absolute;top: 34px;bottom: 10px;height: auto;overflow: auto\">\n                                <div>\n                                    \n                                    <ul ref=\"zTreeUL\" class=\"ztree\"></ul>\n                                </div>\n                            </div>\n                        </div>\n                        <div class=\"right-outer-wrap iv-list-page-wrap\" style=\"padding: 10px;left: 240px;top: 10px;right: 10px;bottom: 55px\">\n                            <i-table :height=\"listHeight\" stripe border :columns=\"columnsConfig\" :data=\"tableData\"\n                                     class=\"iv-list-table\" :highlight-row=\"true\"\n                                     @on-row-click=\"moduleObjectClick\"></i-table>\n                        </div>\n                    </div>\n                    <div class=\"button-outer-wrap\" style=\"bottom: 12px;right: 12px\">\n                        <div class=\"button-inner-wrap\">\n                            <button-group>\n                                <i-button type=\"primary\" @click=\"completed()\" icon=\"md-checkmark\">\u786E\u8BA4</i-button>\n                                <i-button @click=\"handleClose()\" icon=\"md-close\">\u5173\u95ED</i-button>\n                            </button-group>\n                        </div>\n                    </div>\n                </div>"
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
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIkRpYWxvZy9zZWxlY3QtZGVwYXJ0bWVudC11c2VyLWRpYWxvZy5qcyIsIkRpYWxvZy9zZWxlY3QtbW9kdWxlLW9iamVjdC1kaWFsb2cuanMiLCJTU08vc3NvLWFwcC1kZXRhaWwtZnJvbS1jb21wLmpzIiwiU1NPL3Nzby1hcHAtaW50ZXJmYWNlLWxpc3QtY29tcC5qcyIsIlNTTy9zc28tYXBwLXN1Yi1zeXN0ZW0tbGlzdC1jb21wLmpzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQ3hNQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUN0TEE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUNsSEE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FDMUtBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSIsImZpbGUiOiJTU09WdWVFWENvbXBvbmVudC5qcyIsInNvdXJjZXNDb250ZW50IjpbIlwidXNlIHN0cmljdFwiO1xuXG5WdWUuY29tcG9uZW50KFwic2VsZWN0LWRlcGFydG1lbnQtdXNlci1kaWFsb2dcIiwge1xuICBkYXRhOiBmdW5jdGlvbiBkYXRhKCkge1xuICAgIHJldHVybiB7XG4gICAgICBhY0ludGVyZmFjZToge1xuICAgICAgICBnZXREZXBhcnRtZW50VHJlZURhdGE6IFwiL1Jlc3QvU1NPL0RlcHQvRGVwYXJ0bWVudC9HZXREZXBhcnRtZW50c0J5T3JnYW5JZFwiLFxuICAgICAgICByZWxvYWRMaXN0RGF0YTogXCIvUmVzdC9TU08vRGVwdC9EZXBhcnRtZW50VXNlci9HZXRMaXN0RGF0YVwiXG4gICAgICB9LFxuICAgICAgdHJlZUlkRmllbGROYW1lOiBcImRlcHRJZFwiLFxuICAgICAgdHJlZU9iajogbnVsbCxcbiAgICAgIHRyZWVTZWxlY3RlZE5vZGU6IG51bGwsXG4gICAgICB0cmVlU2V0dGluZzoge1xuICAgICAgICBhc3luYzoge1xuICAgICAgICAgIGVuYWJsZTogdHJ1ZSxcbiAgICAgICAgICB1cmw6IFwiXCJcbiAgICAgICAgfSxcbiAgICAgICAgZGF0YToge1xuICAgICAgICAgIGtleToge1xuICAgICAgICAgICAgbmFtZTogXCJkZXB0TmFtZVwiXG4gICAgICAgICAgfSxcbiAgICAgICAgICBzaW1wbGVEYXRhOiB7XG4gICAgICAgICAgICBlbmFibGU6IHRydWUsXG4gICAgICAgICAgICBpZEtleTogXCJkZXB0SWRcIixcbiAgICAgICAgICAgIHBJZEtleTogXCJkZXB0UGFyZW50SWRcIlxuICAgICAgICAgIH1cbiAgICAgICAgfSxcbiAgICAgICAgY2FsbGJhY2s6IHtcbiAgICAgICAgICBvbkNsaWNrOiBmdW5jdGlvbiBvbkNsaWNrKGV2ZW50LCB0cmVlSWQsIHRyZWVOb2RlKSB7XG4gICAgICAgICAgICB2YXIgX3NlbGYgPSB0aGlzLmdldFpUcmVlT2JqKHRyZWVJZCkuX2hvc3Q7XG5cbiAgICAgICAgICAgIF9zZWxmLnRyZWVOb2RlU2VsZWN0ZWQoZXZlbnQsIHRyZWVJZCwgdHJlZU5vZGUpO1xuICAgICAgICAgIH0sXG4gICAgICAgICAgb25Bc3luY1N1Y2Nlc3M6IGZ1bmN0aW9uIG9uQXN5bmNTdWNjZXNzKGV2ZW50LCB0cmVlSWQsIHRyZWVOb2RlLCBtc2cpIHtcbiAgICAgICAgICAgIGFwcExpc3QudHJlZU9iai5leHBhbmRBbGwodHJ1ZSk7XG4gICAgICAgICAgfVxuICAgICAgICB9XG4gICAgICB9LFxuICAgICAgaWRGaWVsZE5hbWU6IFwiRFVfSURcIixcbiAgICAgIHNlYXJjaENvbmRpdGlvbjoge1xuICAgICAgICB1c2VyTmFtZToge1xuICAgICAgICAgIHZhbHVlOiBcIlwiLFxuICAgICAgICAgIHR5cGU6IFNlYXJjaFV0aWxpdHkuU2VhcmNoRmllbGRUeXBlLkxpa2VTdHJpbmdUeXBlXG4gICAgICAgIH0sXG4gICAgICAgIGFjY291bnQ6IHtcbiAgICAgICAgICB2YWx1ZTogXCJcIixcbiAgICAgICAgICB0eXBlOiBTZWFyY2hVdGlsaXR5LlNlYXJjaEZpZWxkVHlwZS5MaWtlU3RyaW5nVHlwZVxuICAgICAgICB9LFxuICAgICAgICB1c2VyUGhvbmVOdW1iZXI6IHtcbiAgICAgICAgICB2YWx1ZTogXCJcIixcbiAgICAgICAgICB0eXBlOiBTZWFyY2hVdGlsaXR5LlNlYXJjaEZpZWxkVHlwZS5MaWtlU3RyaW5nVHlwZVxuICAgICAgICB9LFxuICAgICAgICBkZXBhcnRtZW50SWQ6IHtcbiAgICAgICAgICB2YWx1ZTogXCJcIixcbiAgICAgICAgICB0eXBlOiBTZWFyY2hVdGlsaXR5LlNlYXJjaEZpZWxkVHlwZS5TdHJpbmdUeXBlXG4gICAgICAgIH0sXG4gICAgICAgIHNlYXJjaEluQUxMOiB7XG4gICAgICAgICAgdmFsdWU6IFwi5ZCmXCIsXG4gICAgICAgICAgdHlwZTogU2VhcmNoVXRpbGl0eS5TZWFyY2hGaWVsZFR5cGUuU3RyaW5nVHlwZVxuICAgICAgICB9XG4gICAgICB9LFxuICAgICAgY29sdW1uc0NvbmZpZzogW3tcbiAgICAgICAgdHlwZTogJ3NlbGVjdGlvbicsXG4gICAgICAgIHdpZHRoOiA2MCxcbiAgICAgICAgYWxpZ246ICdjZW50ZXInXG4gICAgICB9LCB7XG4gICAgICAgIHRpdGxlOiAn55So5oi35ZCNJyxcbiAgICAgICAga2V5OiAnVVNFUl9OQU1FJyxcbiAgICAgICAgYWxpZ246IFwiY2VudGVyXCJcbiAgICAgIH0sIHtcbiAgICAgICAgdGl0bGU6ICfmiYvmnLrlj7fnoIEnLFxuICAgICAgICBrZXk6ICdVU0VSX1BIT05FX05VTUJFUicsXG4gICAgICAgIHdpZHRoOiAxNDAsXG4gICAgICAgIGFsaWduOiBcImNlbnRlclwiXG4gICAgICB9LCB7XG4gICAgICAgIHRpdGxlOiAn57uE57uH5py65p6EJyxcbiAgICAgICAga2V5OiAnT1JHQU5fTkFNRScsXG4gICAgICAgIHdpZHRoOiAxNDAsXG4gICAgICAgIGFsaWduOiBcImNlbnRlclwiXG4gICAgICB9LCB7XG4gICAgICAgIHRpdGxlOiAn6YOo6ZeoJyxcbiAgICAgICAga2V5OiAnREVQVF9OQU1FJyxcbiAgICAgICAgd2lkdGg6IDE0MCxcbiAgICAgICAgYWxpZ246IFwiY2VudGVyXCJcbiAgICAgIH0sIHtcbiAgICAgICAgdGl0bGU6ICfkuLvlsZ4nLFxuICAgICAgICBrZXk6ICdEVV9JU19NQUlOJyxcbiAgICAgICAgd2lkdGg6IDcwLFxuICAgICAgICBhbGlnbjogXCJjZW50ZXJcIlxuICAgICAgfV0sXG4gICAgICB0YWJsZURhdGE6IFtdLFxuICAgICAgc2VsZWN0aW9uUm93czogbnVsbCxcbiAgICAgIHBhZ2VUb3RhbDogMCxcbiAgICAgIHBhZ2VTaXplOiAxMixcbiAgICAgIHBhZ2VOdW06IDEsXG4gICAgICBsaXN0SGVpZ2h0OiAyNzBcbiAgICB9O1xuICB9LFxuICBtb3VudGVkOiBmdW5jdGlvbiBtb3VudGVkKCkge1xuICAgIHZhciBvbGRTZWxlY3RlZE9yZ2FuSWQgPSBDb29raWVVdGlsaXR5LkdldENvb2tpZShcIkRNT1JHU0lEXCIpO1xuXG4gICAgaWYgKG9sZFNlbGVjdGVkT3JnYW5JZCkge1xuICAgICAgdGhpcy4kcmVmcy5zZWxlY3RPcmdhbkNvbXAuc2V0T2xkU2VsZWN0ZWRPcmdhbihvbGRTZWxlY3RlZE9yZ2FuSWQpO1xuICAgICAgdGhpcy5pbml0VHJlZShvbGRTZWxlY3RlZE9yZ2FuSWQpO1xuICAgIH1cbiAgfSxcbiAgbWV0aG9kczoge1xuICAgIGNoYW5nZU9yZ2FuOiBmdW5jdGlvbiBjaGFuZ2VPcmdhbihvcmdhbkRhdGEpIHtcbiAgICAgIENvb2tpZVV0aWxpdHkuU2V0Q29va2llMU1vbnRoKFwiRE1PUkdTSURcIiwgb3JnYW5EYXRhLm9yZ2FuSWQpO1xuICAgICAgdGhpcy5pbml0VHJlZShvcmdhbkRhdGEub3JnYW5JZCk7XG4gICAgICB0aGlzLmNsZWFyU2VhcmNoQ29uZGl0aW9uKCk7XG4gICAgICB0aGlzLnRhYmxlRGF0YSA9IFtdO1xuICAgIH0sXG4gICAgaW5pdFRyZWU6IGZ1bmN0aW9uIGluaXRUcmVlKG9yZ2FuSWQpIHtcbiAgICAgIEFqYXhVdGlsaXR5LlBvc3QodGhpcy5hY0ludGVyZmFjZS5nZXREZXBhcnRtZW50VHJlZURhdGEsIHtcbiAgICAgICAgXCJvcmdhbklkXCI6IG9yZ2FuSWRcbiAgICAgIH0sIGZ1bmN0aW9uIChyZXN1bHQpIHtcbiAgICAgICAgaWYgKHJlc3VsdC5zdWNjZXNzKSB7XG4gICAgICAgICAgdGhpcy4kcmVmcy56VHJlZVVMLnNldEF0dHJpYnV0ZShcImlkXCIsIFwic2VsZWN0LWRlcGFydG1lbnQtdXNlci1kaWFsb2ctXCIgKyBTdHJpbmdVdGlsaXR5Lkd1aWQoKSk7XG4gICAgICAgICAgdGhpcy50cmVlT2JqID0gJC5mbi56VHJlZS5pbml0KCQodGhpcy4kcmVmcy56VHJlZVVMKSwgdGhpcy50cmVlU2V0dGluZywgcmVzdWx0LmRhdGEpO1xuICAgICAgICAgIHRoaXMudHJlZU9iai5leHBhbmRBbGwodHJ1ZSk7XG4gICAgICAgICAgdGhpcy50cmVlT2JqLl9ob3N0ID0gdGhpcztcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgcmVzdWx0Lm1lc3NhZ2UsIGZ1bmN0aW9uICgpIHt9KTtcbiAgICAgICAgfVxuICAgICAgfSwgdGhpcyk7XG4gICAgfSxcbiAgICB0cmVlTm9kZVNlbGVjdGVkOiBmdW5jdGlvbiB0cmVlTm9kZVNlbGVjdGVkKGV2ZW50LCB0cmVlSWQsIHRyZWVOb2RlKSB7XG4gICAgICB0aGlzLnRyZWVTZWxlY3RlZE5vZGUgPSB0cmVlTm9kZTtcbiAgICAgIHRoaXMuc2VsZWN0aW9uUm93cyA9IG51bGw7XG4gICAgICB0aGlzLnBhZ2VOdW0gPSAxO1xuICAgICAgdGhpcy5jbGVhclNlYXJjaENvbmRpdGlvbigpO1xuICAgICAgdGhpcy5zZWFyY2hDb25kaXRpb24uZGVwYXJ0bWVudElkLnZhbHVlID0gdGhpcy50cmVlU2VsZWN0ZWROb2RlW3RoaXMudHJlZUlkRmllbGROYW1lXTtcbiAgICAgIHRoaXMucmVsb2FkRGF0YSgpO1xuICAgIH0sXG4gICAgY2xlYXJTZWFyY2hDb25kaXRpb246IGZ1bmN0aW9uIGNsZWFyU2VhcmNoQ29uZGl0aW9uKCkge1xuICAgICAgZm9yICh2YXIga2V5IGluIHRoaXMuc2VhcmNoQ29uZGl0aW9uKSB7XG4gICAgICAgIHRoaXMuc2VhcmNoQ29uZGl0aW9uW2tleV0udmFsdWUgPSBcIlwiO1xuICAgICAgfVxuXG4gICAgICB0aGlzLnNlYXJjaENvbmRpdGlvbltcInNlYXJjaEluQUxMXCJdLnZhbHVlID0gXCLlkKZcIjtcbiAgICB9LFxuICAgIHNlbGVjdGlvbkNoYW5nZTogZnVuY3Rpb24gc2VsZWN0aW9uQ2hhbmdlKHNlbGVjdGlvbikge1xuICAgICAgdGhpcy5zZWxlY3Rpb25Sb3dzID0gc2VsZWN0aW9uO1xuICAgIH0sXG4gICAgcmVsb2FkRGF0YTogZnVuY3Rpb24gcmVsb2FkRGF0YSgpIHtcbiAgICAgIExpc3RQYWdlVXRpbGl0eS5JVmlld1RhYmxlQmluZERhdGFCeVNlYXJjaCh7XG4gICAgICAgIHVybDogdGhpcy5hY0ludGVyZmFjZS5yZWxvYWRMaXN0RGF0YSxcbiAgICAgICAgcGFnZU51bTogdGhpcy5wYWdlTnVtLFxuICAgICAgICBwYWdlU2l6ZTogdGhpcy5wYWdlU2l6ZSxcbiAgICAgICAgc2VhcmNoQ29uZGl0aW9uOiB0aGlzLnNlYXJjaENvbmRpdGlvbixcbiAgICAgICAgcGFnZUFwcE9iajogdGhpcyxcbiAgICAgICAgdGFibGVMaXN0OiB0aGlzLFxuICAgICAgICBpZEZpZWxkOiB0aGlzLmlkRmllbGROYW1lLFxuICAgICAgICBhdXRvU2VsZWN0ZWRPbGRSb3dzOiBmYWxzZSxcbiAgICAgICAgc3VjY2Vzc0Z1bmM6IG51bGwsXG4gICAgICAgIGxvYWREaWN0OiBmYWxzZSxcbiAgICAgICAgY3VzdFBhcmFzOiB7fVxuICAgICAgfSk7XG4gICAgfSxcbiAgICBjaGFuZ2VQYWdlOiBmdW5jdGlvbiBjaGFuZ2VQYWdlKHBhZ2VOdW0pIHtcbiAgICAgIHRoaXMucGFnZU51bSA9IHBhZ2VOdW07XG4gICAgICB0aGlzLnJlbG9hZERhdGEoKTtcbiAgICAgIHRoaXMuc2VsZWN0aW9uUm93cyA9IG51bGw7XG4gICAgfSxcbiAgICBzZWFyY2g6IGZ1bmN0aW9uIHNlYXJjaCgpIHtcbiAgICAgIHRoaXMucGFnZU51bSA9IDE7XG4gICAgICB0aGlzLnJlbG9hZERhdGEoKTtcbiAgICB9LFxuICAgIGJlZ2luU2VsZWN0OiBmdW5jdGlvbiBiZWdpblNlbGVjdCgpIHtcbiAgICAgIHZhciBlbGVtID0gdGhpcy4kcmVmcy5zZWxlY3REZXBhcnRtZW50VXNlck1vZGVsRGlhbG9nV3JhcDtcbiAgICAgIHZhciBkaWFsb2dIZWlnaHQgPSA0NjA7XG5cbiAgICAgIGlmIChQYWdlU3R5bGVVdGlsaXR5LkdldFBhZ2VIZWlnaHQoKSA+IDcwMCkge1xuICAgICAgICBkaWFsb2dIZWlnaHQgPSA2NjA7XG4gICAgICB9XG5cbiAgICAgIHRoaXMubGlzdEhlaWdodCA9IGRpYWxvZ0hlaWdodCAtIDIzMDtcbiAgICAgIERpYWxvZ1V0aWxpdHkuRGlhbG9nRWxlbU9iaihlbGVtLCB7XG4gICAgICAgIG1vZGFsOiB0cnVlLFxuICAgICAgICB3aWR0aDogOTcwLFxuICAgICAgICBoZWlnaHQ6IGRpYWxvZ0hlaWdodCxcbiAgICAgICAgdGl0bGU6IFwi6YCJ5oup57uE57uH5py65p6EXCJcbiAgICAgIH0pO1xuICAgIH0sXG4gICAgY29tcGxldGVkOiBmdW5jdGlvbiBjb21wbGV0ZWQoKSB7XG4gICAgICBjb25zb2xlLmxvZyh0aGlzLnNlbGVjdGlvblJvd3MpO1xuXG4gICAgICBpZiAodGhpcy5zZWxlY3Rpb25Sb3dzKSB7XG4gICAgICAgIHRoaXMuJGVtaXQoJ29uLXNlbGVjdGVkLWNvbXBsZXRlZCcsIHRoaXMuc2VsZWN0aW9uUm93cyk7XG4gICAgICAgIHRoaXMuaGFuZGxlQ2xvc2UoKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCBcIuivt+WFiOmAieS4reS6uuWRmCFcIiwgbnVsbCk7XG4gICAgICB9XG4gICAgfSxcbiAgICBoYW5kbGVDbG9zZTogZnVuY3Rpb24gaGFuZGxlQ2xvc2UoKSB7XG4gICAgICBEaWFsb2dVdGlsaXR5LkNsb3NlRGlhbG9nRWxlbSh0aGlzLiRyZWZzLnNlbGVjdERlcGFydG1lbnRVc2VyTW9kZWxEaWFsb2dXcmFwKTtcbiAgICB9XG4gIH0sXG4gIHRlbXBsYXRlOiBcIjxkaXYgcmVmPVxcXCJzZWxlY3REZXBhcnRtZW50VXNlck1vZGVsRGlhbG9nV3JhcFxcXCIgY2xhc3M9XFxcImMxLXNlbGVjdC1tb2RlbC13cmFwIGdlbmVyYWwtZWRpdC1wYWdlLXdyYXBcXFwiIHN0eWxlPVxcXCJkaXNwbGF5OiBub25lXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImxpc3QtMmNvbHVtblxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwibGVmdC1vdXRlci13cmFwXFxcIiBzdHlsZT1cXFwid2lkdGg6IDE4MHB4O3RvcDogMTBweDtsZWZ0OiAxMHB4O2JvdHRvbTogNTVweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxzZWxlY3Qtb3JnYW4tc2luZ2xlLWNvbXAgQG9uLXNlbGVjdGVkLW9yZ2FuPVxcXCJjaGFuZ2VPcmdhblxcXCIgcmVmPVxcXCJzZWxlY3RPcmdhbkNvbXBcXFwiPjwvc2VsZWN0LW9yZ2FuLXNpbmdsZS1jb21wPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJpbm5lci13cmFwXFxcIiBzdHlsZT1cXFwicG9zaXRpb246YWJzb2x1dGU7dG9wOiAzMHB4O2JvdHRvbTogMTBweDtoZWlnaHQ6IGF1dG87b3ZlcmZsb3c6IGF1dG9cXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dWwgcmVmPVxcXCJ6VHJlZVVMXFxcIiBjbGFzcz1cXFwienRyZWVcXFwiPjwvdWw+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwicmlnaHQtb3V0ZXItd3JhcCBpdi1saXN0LXBhZ2Utd3JhcFxcXCIgc3R5bGU9XFxcInBhZGRpbmc6IDEwcHg7bGVmdDogMjAwcHg7dG9wOiAxMHB4O3JpZ2h0OiAxMHB4O2JvdHRvbTogNTVweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImxpc3Qtc2ltcGxlLXNlYXJjaC13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0YWJsZSBjbGFzcz1cXFwibHMtdGFibGVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxjb2xncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGNvbCBzdHlsZT1cXFwid2lkdGg6IDgwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Y29sIHN0eWxlPVxcXCJcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Y29sIHN0eWxlPVxcXCJ3aWR0aDogMTAwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Y29sIHN0eWxlPVxcXCJcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Y29sIHN0eWxlPVxcXCJ3aWR0aDogODBweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxjb2wgc3R5bGU9XFxcIndpZHRoOiA4NXB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGNvbCBzdHlsZT1cXFwid2lkdGg6IDgwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvY29sZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRyIGNsYXNzPVxcXCJscy10YWJsZS1yb3dcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGQ+XFx1NzUyOFxcdTYyMzdcXHU1NDBEXFx1RkYxQTwvdGQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDx0ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcInNlYXJjaENvbmRpdGlvbi51c2VyTmFtZS52YWx1ZVxcXCIgcGxhY2Vob2xkZXI9XFxcIlxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3RkPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGQ+XFx1NjI0QlxcdTY3M0FcXHVGRjFBPC90ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwic2VhcmNoQ29uZGl0aW9uLnVzZXJQaG9uZU51bWJlci52YWx1ZVxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3RkPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGQ+XFx1NTE2OFxcdTVDNDBcXHVGRjFBPC90ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvLWdyb3VwIHYtbW9kZWw9XFxcInNlYXJjaENvbmRpdGlvbi5zZWFyY2hJbkFMTC52YWx1ZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvIGxhYmVsPVxcXCJcXHU2NjJGXFxcIj5cXHU2NjJGPC9yYWRpbz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cmFkaW8gbGFiZWw9XFxcIlxcdTU0MjZcXFwiPlxcdTU0MjY8L3JhZGlvPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9yYWRpby1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC90ZD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkPjxpLWJ1dHRvbiB0eXBlPVxcXCJwcmltYXJ5XFxcIiBAY2xpY2s9XFxcInNlYXJjaFxcXCI+PEljb24gdHlwZT1cXFwiYW5kcm9pZC1zZWFyY2hcXFwiPjwvSWNvbj4gXFx1NjdFNVxcdThCRTIgPC9pLWJ1dHRvbj48L3RkPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvdHI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3RhYmxlPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktdGFibGUgOmhlaWdodD1cXFwibGlzdEhlaWdodFxcXCIgc3RyaXBlIGJvcmRlciA6Y29sdW1ucz1cXFwiY29sdW1uc0NvbmZpZ1xcXCIgOmRhdGE9XFxcInRhYmxlRGF0YVxcXCJcXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgY2xhc3M9XFxcIml2LWxpc3QtdGFibGVcXFwiIDpoaWdobGlnaHQtcm93PVxcXCJ0cnVlXFxcIlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBAb24tc2VsZWN0aW9uLWNoYW5nZT1cXFwic2VsZWN0aW9uQ2hhbmdlXFxcIj48L2ktdGFibGU+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgc3R5bGU9XFxcImZsb2F0OiByaWdodDtcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHBhZ2UgQG9uLWNoYW5nZT1cXFwiY2hhbmdlUGFnZVxcXCIgOmN1cnJlbnQuc3luYz1cXFwicGFnZU51bVxcXCIgOnBhZ2Utc2l6ZT1cXFwicGFnZVNpemVcXFwiIHNob3ctdG90YWxcXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDp0b3RhbD1cXFwicGFnZVRvdGFsXFxcIj48L3BhZ2U+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJidXR0b24tb3V0ZXItd3JhcFxcXCIgc3R5bGU9XFxcImJvdHRvbTogMTJweDtyaWdodDogMTJweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLWlubmVyLXdyYXBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8YnV0dG9uLWdyb3VwPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uIHR5cGU9XFxcInByaW1hcnlcXFwiIEBjbGljaz1cXFwiY29tcGxldGVkKClcXFwiIGljb249XFxcIm1kLWNoZWNrbWFya1xcXCI+XFx1Nzg2RVxcdThCQTQ8L2ktYnV0dG9uPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uIEBjbGljaz1cXFwiaGFuZGxlQ2xvc2UoKVxcXCIgaWNvbj1cXFwibWQtY2xvc2VcXFwiPlxcdTUxNzNcXHU5NUVEPC9pLWJ1dHRvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9idXR0b24tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgPC9kaXY+XCJcbn0pOyIsIlwidXNlIHN0cmljdFwiO1xuXG5WdWUuY29tcG9uZW50KFwic2VsZWN0LW1vZHVsZS1vYmplY3QtZGlhbG9nXCIsIHtcbiAgZGF0YTogZnVuY3Rpb24gZGF0YSgpIHtcbiAgICByZXR1cm4ge1xuICAgICAgYWNJbnRlcmZhY2U6IHtcbiAgICAgICAgZ2V0REJMaW5rRGF0YTogXCIvUmVzdC9TU08vTXUvTWVudS9HZXRGdWxsREJMaW5rXCIsXG4gICAgICAgIGdldE1vZHVsZVRyZWVEYXRhOiBcIi9SZXN0L1NTTy9NdS9NZW51L0dldE1vdWxkVHJlZURhdGFcIixcbiAgICAgICAgcmVsb2FkTGlzdERhdGE6IFwiL1Jlc3QvU1NPL011L01lbnUvR2V0U2VsZWN0TW9kdWxlT2JqZWN0TGlzdEJ5TW9kdWxlSWRcIlxuICAgICAgfSxcbiAgICAgIHNlbGVjdE1vZHVsZU9iamVjdFR5cGU6IFwiXCIsXG4gICAgICBzZWxlY3RlZERCTGlua0lkOiBcIlwiLFxuICAgICAgZGJMaW5rQXJyYXk6IFtdLFxuICAgICAgdHJlZUlkRmllbGROYW1lOiBcIm1vZHVsZUlkXCIsXG4gICAgICB0cmVlT2JqOiBudWxsLFxuICAgICAgdHJlZVNlbGVjdGVkTm9kZTogbnVsbCxcbiAgICAgIHRyZWVTZXR0aW5nOiB7XG4gICAgICAgIGFzeW5jOiB7XG4gICAgICAgICAgZW5hYmxlOiB0cnVlLFxuICAgICAgICAgIHVybDogXCJcIlxuICAgICAgICB9LFxuICAgICAgICBkYXRhOiB7XG4gICAgICAgICAga2V5OiB7XG4gICAgICAgICAgICBuYW1lOiBcIm1vZHVsZVRleHRcIlxuICAgICAgICAgIH0sXG4gICAgICAgICAgc2ltcGxlRGF0YToge1xuICAgICAgICAgICAgZW5hYmxlOiB0cnVlLFxuICAgICAgICAgICAgaWRLZXk6IFwibW9kdWxlSWRcIixcbiAgICAgICAgICAgIHBJZEtleTogXCJtb2R1bGVQYXJlbnRJZFwiXG4gICAgICAgICAgfVxuICAgICAgICB9LFxuICAgICAgICBjYWxsYmFjazoge1xuICAgICAgICAgIG9uQ2xpY2s6IGZ1bmN0aW9uIG9uQ2xpY2soZXZlbnQsIHRyZWVJZCwgdHJlZU5vZGUpIHtcbiAgICAgICAgICAgIHZhciBfc2VsZiA9IHRoaXMuZ2V0WlRyZWVPYmoodHJlZUlkKS5faG9zdDtcblxuICAgICAgICAgICAgX3NlbGYudHJlZU5vZGVTZWxlY3RlZChldmVudCwgdHJlZUlkLCB0cmVlTm9kZSk7XG4gICAgICAgICAgfSxcbiAgICAgICAgICBvbkFzeW5jU3VjY2VzczogZnVuY3Rpb24gb25Bc3luY1N1Y2Nlc3MoZXZlbnQsIHRyZWVJZCwgdHJlZU5vZGUsIG1zZykge31cbiAgICAgICAgfVxuICAgICAgfSxcbiAgICAgIHNlbGVjdGVkTW9kdWxlT2JqZWN0OiBudWxsLFxuICAgICAgaWRGaWVsZE5hbWU6IFwiSURcIixcbiAgICAgIHNlYXJjaENvbmRpdGlvbjoge1xuICAgICAgICBzZWxlY3RNb2R1bGVPYmplY3RUeXBlOiB7XG4gICAgICAgICAgdmFsdWU6IFwiXCIsXG4gICAgICAgICAgdHlwZTogU2VhcmNoVXRpbGl0eS5TZWFyY2hGaWVsZFR5cGUuU3RyaW5nVHlwZVxuICAgICAgICB9LFxuICAgICAgICBzZWxlY3RNb2R1bGVJZDoge1xuICAgICAgICAgIHZhbHVlOiBcIlwiLFxuICAgICAgICAgIHR5cGU6IFNlYXJjaFV0aWxpdHkuU2VhcmNoRmllbGRUeXBlLlN0cmluZ1R5cGVcbiAgICAgICAgfVxuICAgICAgfSxcbiAgICAgIGNvbHVtbnNDb25maWc6IFt7XG4gICAgICAgIHRpdGxlOiAn5ZCN56ewJyxcbiAgICAgICAga2V5OiAnTkFNRScsXG4gICAgICAgIGFsaWduOiBcImNlbnRlclwiXG4gICAgICB9LCB7XG4gICAgICAgIHRpdGxlOiAn5aSH5rOoJyxcbiAgICAgICAga2V5OiAnREVTQ1JJUFRJT04nLFxuICAgICAgICB3aWR0aDogMjQwLFxuICAgICAgICBhbGlnbjogXCJjZW50ZXJcIlxuICAgICAgfV0sXG4gICAgICB0YWJsZURhdGE6IFtdLFxuICAgICAgcGFnZVRvdGFsOiAwLFxuICAgICAgcGFnZVNpemU6IDEyLFxuICAgICAgcGFnZU51bTogMSxcbiAgICAgIGxpc3RIZWlnaHQ6IDUxMFxuICAgIH07XG4gIH0sXG4gIG1vdW50ZWQ6IGZ1bmN0aW9uIG1vdW50ZWQoKSB7XG4gICAgdGhpcy5iaW5kREJMaW5rKCk7XG4gIH0sXG4gIG1ldGhvZHM6IHtcbiAgICBiaW5kREJMaW5rOiBmdW5jdGlvbiBiaW5kREJMaW5rKCkge1xuICAgICAgQWpheFV0aWxpdHkuUG9zdCh0aGlzLmFjSW50ZXJmYWNlLmdldERCTGlua0RhdGEsIHt9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge1xuICAgICAgICAgIGNvbnNvbGUubG9nKHJlc3VsdC5kYXRhKTtcbiAgICAgICAgICB0aGlzLmRiTGlua0FycmF5ID0gcmVzdWx0LmRhdGE7XG4gICAgICAgICAgdmFyIG9sZFNlbGVjdGVkT3JnYW5JZCA9IENvb2tpZVV0aWxpdHkuR2V0Q29va2llKFwiU01PRERCTElOS0lEXCIpO1xuICAgICAgICAgIHRoaXMuc2VsZWN0ZWREQkxpbmtJZCA9IG9sZFNlbGVjdGVkT3JnYW5JZDtcblxuICAgICAgICAgIGlmICh0aGlzLnNlbGVjdGVkREJMaW5rSWQpIHtcbiAgICAgICAgICAgIHRoaXMuaW5pdFRyZWUodGhpcy5zZWxlY3RlZERCTGlua0lkKTtcbiAgICAgICAgICB9XG4gICAgICAgIH1cbiAgICAgIH0sIHRoaXMpO1xuICAgIH0sXG4gICAgY2hhbmdlREJMaW5rOiBmdW5jdGlvbiBjaGFuZ2VEQkxpbmsoKSB7XG4gICAgICBjb25zb2xlLmxvZyh0aGlzLnNlbGVjdGVkREJMaW5rSWQpO1xuICAgICAgdGhpcy5pbml0VHJlZSh0aGlzLnNlbGVjdGVkREJMaW5rSWQpO1xuICAgICAgQ29va2llVXRpbGl0eS5TZXRDb29raWUxTW9udGgoXCJTTU9EREJMSU5LSURcIiwgdGhpcy5zZWxlY3RlZERCTGlua0lkKTtcbiAgICB9LFxuICAgIGluaXRUcmVlOiBmdW5jdGlvbiBpbml0VHJlZShzZWxlY3RlZERCTGlua0lkKSB7XG4gICAgICBBamF4VXRpbGl0eS5Qb3N0KHRoaXMuYWNJbnRlcmZhY2UuZ2V0TW9kdWxlVHJlZURhdGEsIHtcbiAgICAgICAgXCJkYkxpbmtJZFwiOiBzZWxlY3RlZERCTGlua0lkXG4gICAgICB9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGNvbnNvbGUubG9nKHJlc3VsdCk7XG5cbiAgICAgICAgaWYgKHJlc3VsdC5zdWNjZXNzKSB7XG4gICAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCByZXN1bHQuZGF0YS5sZW5ndGg7IGkrKykge1xuICAgICAgICAgICAgaWYgKHJlc3VsdC5kYXRhW2ldLm1vZHVsZVBhcmVudElkICE9IFwiLTFcIikge1xuICAgICAgICAgICAgICByZXN1bHQuZGF0YVtpXS5pY29uID0gXCIuLi8uLi8uLi9UaGVtZXMvUG5nMTZYMTYvbGF5b3V0LXNpZGViYXIucG5nXCI7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgdGhpcy4kcmVmcy56VHJlZVVMLnNldEF0dHJpYnV0ZShcImlkXCIsIFwic2VsZWN0LW1vZHVsZS1vYmplY3QtZGlhbG9nLVwiICsgU3RyaW5nVXRpbGl0eS5HdWlkKCkpO1xuICAgICAgICAgIHRoaXMudHJlZU9iaiA9ICQuZm4uelRyZWUuaW5pdCgkKHRoaXMuJHJlZnMuelRyZWVVTCksIHRoaXMudHJlZVNldHRpbmcsIHJlc3VsdC5kYXRhKTtcbiAgICAgICAgICB0aGlzLnRyZWVPYmouZXhwYW5kQWxsKHRydWUpO1xuICAgICAgICAgIHRoaXMudHJlZU9iai5faG9zdCA9IHRoaXM7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBmdW5jdGlvbiAoKSB7fSk7XG4gICAgICAgIH1cbiAgICAgIH0sIHRoaXMpO1xuICAgIH0sXG4gICAgdHJlZU5vZGVTZWxlY3RlZDogZnVuY3Rpb24gdHJlZU5vZGVTZWxlY3RlZChldmVudCwgdHJlZUlkLCB0cmVlTm9kZSkge1xuICAgICAgdGhpcy50cmVlU2VsZWN0ZWROb2RlID0gdHJlZU5vZGU7XG4gICAgICB0aGlzLnBhZ2VOdW0gPSAxO1xuICAgICAgdGhpcy5zZWFyY2hDb25kaXRpb24uc2VsZWN0TW9kdWxlT2JqZWN0VHlwZS52YWx1ZSA9IHRoaXMuc2VsZWN0TW9kdWxlT2JqZWN0VHlwZTtcbiAgICAgIHRoaXMuc2VhcmNoQ29uZGl0aW9uLnNlbGVjdE1vZHVsZUlkLnZhbHVlID0gdHJlZU5vZGUubW9kdWxlSWQ7XG4gICAgICB0aGlzLnJlbG9hZERhdGEoKTtcbiAgICB9LFxuICAgIG1vZHVsZU9iamVjdENsaWNrOiBmdW5jdGlvbiBtb2R1bGVPYmplY3RDbGljayhkYXRhLCBpbmRleCkge1xuICAgICAgdGhpcy5zZWxlY3RlZE1vZHVsZU9iamVjdCA9IGRhdGE7XG4gICAgfSxcbiAgICByZWxvYWREYXRhOiBmdW5jdGlvbiByZWxvYWREYXRhKCkge1xuICAgICAgTGlzdFBhZ2VVdGlsaXR5LklWaWV3VGFibGVCaW5kRGF0YUJ5U2VhcmNoKHtcbiAgICAgICAgdXJsOiB0aGlzLmFjSW50ZXJmYWNlLnJlbG9hZExpc3REYXRhLFxuICAgICAgICBwYWdlTnVtOiB0aGlzLnBhZ2VOdW0sXG4gICAgICAgIHBhZ2VTaXplOiB0aGlzLnBhZ2VTaXplLFxuICAgICAgICBzZWFyY2hDb25kaXRpb246IHRoaXMuc2VhcmNoQ29uZGl0aW9uLFxuICAgICAgICBwYWdlQXBwT2JqOiB0aGlzLFxuICAgICAgICB0YWJsZUxpc3Q6IHRoaXMsXG4gICAgICAgIGlkRmllbGQ6IHRoaXMuaWRGaWVsZE5hbWUsXG4gICAgICAgIGF1dG9TZWxlY3RlZE9sZFJvd3M6IGZhbHNlLFxuICAgICAgICBzdWNjZXNzRnVuYzogZnVuY3Rpb24gc3VjY2Vzc0Z1bmMocmVzdWx0KSB7XG4gICAgICAgICAgY29uc29sZS5sb2cocmVzdWx0KTtcbiAgICAgICAgfSxcbiAgICAgICAgbG9hZERpY3Q6IGZhbHNlLFxuICAgICAgICBjdXN0UGFyYXM6IHt9XG4gICAgICB9KTtcbiAgICB9LFxuICAgIGNoYW5nZVBhZ2U6IGZ1bmN0aW9uIGNoYW5nZVBhZ2UocGFnZU51bSkge1xuICAgICAgdGhpcy5wYWdlTnVtID0gcGFnZU51bTtcbiAgICAgIHRoaXMucmVsb2FkRGF0YSgpO1xuICAgIH0sXG4gICAgc2VhcmNoOiBmdW5jdGlvbiBzZWFyY2goKSB7XG4gICAgICB0aGlzLnBhZ2VOdW0gPSAxO1xuICAgICAgdGhpcy5yZWxvYWREYXRhKCk7XG4gICAgfSxcbiAgICBiZWdpblNlbGVjdDogZnVuY3Rpb24gYmVnaW5TZWxlY3Qoc2VsZWN0TW9kdWxlT2JqZWN0VHlwZSkge1xuICAgICAgdmFyIGVsZW0gPSB0aGlzLiRyZWZzLnNlbGVjdE1vZHVsZU9iamVjdERpYWxvZ1dyYXA7XG4gICAgICB0aGlzLnNlbGVjdE1vZHVsZU9iamVjdFR5cGUgPSBzZWxlY3RNb2R1bGVPYmplY3RUeXBlO1xuICAgICAgdmFyIGRpYWxvZ0hlaWdodCA9IDY2MDtcbiAgICAgIERpYWxvZ1V0aWxpdHkuRGlhbG9nRWxlbU9iaihlbGVtLCB7XG4gICAgICAgIG1vZGFsOiB0cnVlLFxuICAgICAgICB3aWR0aDogODcwLFxuICAgICAgICBoZWlnaHQ6IGRpYWxvZ0hlaWdodCxcbiAgICAgICAgdGl0bGU6IFwi6YCJ5oup5YWz6IGU5a+56LGhMVwiXG4gICAgICB9KTtcbiAgICB9LFxuICAgIGJ1aWxkRGlzcGxheU5hbWU6IGZ1bmN0aW9uIGJ1aWxkRGlzcGxheU5hbWUoc2VsZWN0ZWRNb2R1bGVPYmplY3QpIHtcbiAgICAgIHZhciB0eHQgPSBcIui3r+W+hDrjgJBcIiArIFRyZWVVdGlsaXR5LkJ1aWxkTm9kZVBhdGhOYW1lKHRoaXMudHJlZVNlbGVjdGVkTm9kZSwgXCJtb2R1bGVUZXh0XCIsIHNlbGVjdGVkTW9kdWxlT2JqZWN0Lk5BTUUgKyBcIihcIiArIHNlbGVjdGVkTW9kdWxlT2JqZWN0LkNPREUgKyBcIilcIikgKyBcIuOAkVwiO1xuXG4gICAgICBpZiAodGhpcy5zZWxlY3RNb2R1bGVPYmplY3RUeXBlID09IFwiV2Vi5qih5Z2X5YiX6KGoXCIpIHtcbiAgICAgICAgdHh0ICs9IFwi77yM5pWw5o2u6ZuGOu+8iFwiICsgc2VsZWN0ZWRNb2R1bGVPYmplY3QuTElTVF9EQVRBU0VUX05BTUUgKyBcIu+8iVwiO1xuICAgICAgfVxuXG4gICAgICByZXR1cm4gdHh0O1xuICAgIH0sXG4gICAgY29tcGxldGVkOiBmdW5jdGlvbiBjb21wbGV0ZWQoKSB7XG4gICAgICBpZiAodGhpcy5zZWxlY3RlZE1vZHVsZU9iamVjdCkge1xuICAgICAgICB0aGlzLiRlbWl0KCdvbi1zZWxlY3RlZC1jb21wbGV0ZWQnLCB0aGlzLnNlbGVjdGVkTW9kdWxlT2JqZWN0LCB0aGlzLnNlbGVjdGVkTW9kdWxlT2JqZWN0LklELCB0aGlzLmJ1aWxkRGlzcGxheU5hbWUodGhpcy5zZWxlY3RlZE1vZHVsZU9iamVjdCksIEpzb25VdGlsaXR5Lkpzb25Ub1N0cmluZyh0aGlzLnNlbGVjdGVkTW9kdWxlT2JqZWN0KSk7XG4gICAgICAgIHRoaXMuaGFuZGxlQ2xvc2UoKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCBcIuivt+WFiOmAieaLqee7keWumuWvueixoSFcIiwgbnVsbCk7XG4gICAgICB9XG4gICAgfSxcbiAgICBoYW5kbGVDbG9zZTogZnVuY3Rpb24gaGFuZGxlQ2xvc2UoKSB7XG4gICAgICBEaWFsb2dVdGlsaXR5LkNsb3NlRGlhbG9nRWxlbSh0aGlzLiRyZWZzLnNlbGVjdE1vZHVsZU9iamVjdERpYWxvZ1dyYXApO1xuICAgIH1cbiAgfSxcbiAgdGVtcGxhdGU6IFwiPGRpdiByZWY9XFxcInNlbGVjdE1vZHVsZU9iamVjdERpYWxvZ1dyYXBcXFwiIGNsYXNzPVxcXCJjMS1zZWxlY3QtbW9kZWwtd3JhcCBnZW5lcmFsLWVkaXQtcGFnZS13cmFwXFxcIiBzdHlsZT1cXFwiZGlzcGxheTogbm9uZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJsaXN0LTJjb2x1bW5cXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImxlZnQtb3V0ZXItd3JhcFxcXCIgc3R5bGU9XFxcIndpZHRoOiAyMjBweDt0b3A6IDEwcHg7bGVmdDogMTBweDtib3R0b206IDU1cHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1zZWxlY3QgQG9uLWNoYW5nZT1cXFwiY2hhbmdlREJMaW5rXFxcIiB2LW1vZGVsPVxcXCJzZWxlY3RlZERCTGlua0lkXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLW9wdGlvbiB2LWZvcj1cXFwiaXRlbSBpbiBkYkxpbmtBcnJheVxcXCIgOnZhbHVlPVxcXCJpdGVtLmRiSWRcXFwiIDprZXk9XFxcIml0ZW0uZGJJZFxcXCI+e3sgaXRlbS5kYkxpbmtOYW1lIH19PC9pLW9wdGlvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9pLXNlbGVjdD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiaW5uZXItd3JhcFxcXCIgc3R5bGU9XFxcInBvc2l0aW9uOmFic29sdXRlO3RvcDogMzRweDtib3R0b206IDEwcHg7aGVpZ2h0OiBhdXRvO292ZXJmbG93OiBhdXRvXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHVsIHJlZj1cXFwielRyZWVVTFxcXCIgY2xhc3M9XFxcInp0cmVlXFxcIj48L3VsPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcInJpZ2h0LW91dGVyLXdyYXAgaXYtbGlzdC1wYWdlLXdyYXBcXFwiIHN0eWxlPVxcXCJwYWRkaW5nOiAxMHB4O2xlZnQ6IDI0MHB4O3RvcDogMTBweDtyaWdodDogMTBweDtib3R0b206IDU1cHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS10YWJsZSA6aGVpZ2h0PVxcXCJsaXN0SGVpZ2h0XFxcIiBzdHJpcGUgYm9yZGVyIDpjb2x1bW5zPVxcXCJjb2x1bW5zQ29uZmlnXFxcIiA6ZGF0YT1cXFwidGFibGVEYXRhXFxcIlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBjbGFzcz1cXFwiaXYtbGlzdC10YWJsZVxcXCIgOmhpZ2hsaWdodC1yb3c9XFxcInRydWVcXFwiXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEBvbi1yb3ctY2xpY2s9XFxcIm1vZHVsZU9iamVjdENsaWNrXFxcIj48L2ktdGFibGU+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbi1vdXRlci13cmFwXFxcIiBzdHlsZT1cXFwiYm90dG9tOiAxMnB4O3JpZ2h0OiAxMnB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJidXR0b24taW5uZXItd3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxidXR0b24tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1idXR0b24gdHlwZT1cXFwicHJpbWFyeVxcXCIgQGNsaWNrPVxcXCJjb21wbGV0ZWQoKVxcXCIgaWNvbj1cXFwibWQtY2hlY2ttYXJrXFxcIj5cXHU3ODZFXFx1OEJBNDwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1idXR0b24gQGNsaWNrPVxcXCJoYW5kbGVDbG9zZSgpXFxcIiBpY29uPVxcXCJtZC1jbG9zZVxcXCI+XFx1NTE3M1xcdTk1RUQ8L2ktYnV0dG9uPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2J1dHRvbi1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICA8L2Rpdj5cIlxufSk7IiwiXCJ1c2Ugc3RyaWN0XCI7XG5cblZ1ZS5jb21wb25lbnQoXCJzc28tYXBwLWRldGFpbC1mcm9tLWNvbXBcIiwge1xuICBwcm9wczogW1wic3RhdHVzXCIsIFwiYXBwSWRcIiwgXCJpc1N1YlN5c3RlbVwiXSxcbiAgd2F0Y2g6IHtcbiAgICBhcHBJZDogZnVuY3Rpb24gYXBwSWQobmV3VmFsKSB7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBJZCA9IG5ld1ZhbDtcbiAgICB9LFxuICAgIHN0YXR1czogZnVuY3Rpb24gc3RhdHVzKG5ld1ZhbCkge1xuICAgICAgdGhpcy5pbm5lclN0YXR1cyA9IG5ld1ZhbDtcbiAgICB9XG4gIH0sXG4gIGRhdGE6IGZ1bmN0aW9uIGRhdGEoKSB7XG4gICAgcmV0dXJuIHtcbiAgICAgIGFjSW50ZXJmYWNlOiB7XG4gICAgICAgIGFwcExvZ29Vcmw6IFwiL1Jlc3QvU1NPL0FwcC9BcHBsaWNhdGlvbi9HZXRBcHBMb2dvXCIsXG4gICAgICAgIGdldE5ld0tleXM6IFwiL1Jlc3QvU1NPL0FwcC9BcHBsaWNhdGlvbi9HZXROZXdLZXlzXCJcbiAgICAgIH0sXG4gICAgICBhcHBFbnRpdHk6IHtcbiAgICAgICAgYXBwSWQ6IFwiXCIsXG4gICAgICAgIGFwcENvZGU6IFwiXCIsXG4gICAgICAgIGFwcE5hbWU6IFwiXCIsXG4gICAgICAgIGFwcFB1YmxpY0tleTogXCJcIixcbiAgICAgICAgYXBwUHJpdmF0ZUtleTogXCJcIixcbiAgICAgICAgYXBwRG9tYWluOiBcIlwiLFxuICAgICAgICBhcHBJbmRleFVybDogXCJcIixcbiAgICAgICAgYXBwTWFpbkltYWdlSWQ6IFwiXCIsXG4gICAgICAgIGFwcFR5cGU6IFwiXCIsXG4gICAgICAgIGFwcE1haW5JZDogXCJcIixcbiAgICAgICAgYXBwQ2F0ZWdvcnk6IFwid2ViXCIsXG4gICAgICAgIGFwcERlc2M6IFwiXCIsXG4gICAgICAgIGFwcFN0YXR1czogXCLlkK/nlKhcIixcbiAgICAgICAgYXBwQ3JlYXRlVGltZTogRGF0ZVV0aWxpdHkuR2V0Q3VycmVudERhdGEoKVxuICAgICAgfSxcbiAgICAgIHJ1bGVWYWxpZGF0ZToge1xuICAgICAgICBhcHBDb2RlOiBbe1xuICAgICAgICAgIHJlcXVpcmVkOiB0cnVlLFxuICAgICAgICAgIG1lc3NhZ2U6ICfjgJDns7vnu5/nvJbnoIHjgJHkuI3og73kuLrnqbrvvIEnLFxuICAgICAgICAgIHRyaWdnZXI6ICdibHVyJ1xuICAgICAgICB9LCB7XG4gICAgICAgICAgdHlwZTogJ3N0cmluZycsXG4gICAgICAgICAgcGF0dGVybjogL15bQS1aYS16MC05XSskLyxcbiAgICAgICAgICBtZXNzYWdlOiAn6K+35L2/55So5a2X5q+N5oiW5pWw5a2XJyxcbiAgICAgICAgICB0cmlnZ2VyOiAnYmx1cidcbiAgICAgICAgfV0sXG4gICAgICAgIGFwcE5hbWU6IFt7XG4gICAgICAgICAgcmVxdWlyZWQ6IHRydWUsXG4gICAgICAgICAgbWVzc2FnZTogJ+OAkOezu+e7n+WQjeensOOAkeS4jeiDveS4uuepuu+8gScsXG4gICAgICAgICAgdHJpZ2dlcjogJ2JsdXInXG4gICAgICAgIH1dXG4gICAgICB9LFxuICAgICAgc3lzdGVtTG9nb0ltYWdlU3JjOiBcIlwiLFxuICAgICAgaW5uZXJTdGF0dXM6IFwiYWRkXCJcbiAgICB9O1xuICB9LFxuICBtb3VudGVkOiBmdW5jdGlvbiBtb3VudGVkKCkge1xuICAgIGlmICh0aGlzLmlubmVyU3RhdHVzID09IFwiYWRkXCIpIHtcbiAgICAgIHRoaXMuc3lzdGVtTG9nb0ltYWdlU3JjID0gQmFzZVV0aWxpdHkuQnVpbGRBY3Rpb24odGhpcy5hY0ludGVyZmFjZS5hcHBMb2dvVXJsLCB7XG4gICAgICAgIGZpbGVJZDogXCJkZWZhdWx0U1NPQXBwTG9nb0ltYWdlXCJcbiAgICAgIH0pO1xuICAgIH0gZWxzZSB7XG4gICAgICB0aGlzLnN5c3RlbUxvZ29JbWFnZVNyYyA9IEJhc2VVdGlsaXR5LkJ1aWxkQWN0aW9uKHRoaXMuYWNJbnRlcmZhY2UuYXBwTG9nb1VybCwge1xuICAgICAgICBmaWxlSWQ6IFwiXCJcbiAgICAgIH0pO1xuICAgIH1cbiAgfSxcbiAgbWV0aG9kczoge1xuICAgIHJlc2V0QXBwRW50aXR5OiBmdW5jdGlvbiByZXNldEFwcEVudGl0eSgpIHtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcElkID0gXCJcIjtcbiAgICAgIHRoaXMuYXBwRW50aXR5LmFwcENvZGUgPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwTmFtZSA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBQdWJsaWNLZXkgPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwUHJpdmF0ZUtleSA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBEb21haW4gPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwSW5kZXhVcmwgPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwTWFpbkltYWdlSWQgPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwVHlwZSA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBNYWluSWQgPSBcIlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwQ2F0ZWdvcnkgPSBcIndlYlwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwRGVzYyA9IFwiXCI7XG4gICAgICB0aGlzLmFwcEVudGl0eS5hcHBTdGF0dXMgPSBcIuWQr+eUqFwiO1xuICAgICAgdGhpcy5hcHBFbnRpdHkuYXBwQ3JlYXRlVGltZSA9IERhdGVVdGlsaXR5LkdldEN1cnJlbnREYXRhKCk7XG4gICAgfSxcbiAgICB1cGxvYWRTeXN0ZW1Mb2dvSW1hZ2VTdWNjZXNzOiBmdW5jdGlvbiB1cGxvYWRTeXN0ZW1Mb2dvSW1hZ2VTdWNjZXNzKHJlc3BvbnNlLCBmaWxlLCBmaWxlTGlzdCkge1xuICAgICAgaWYgKHJlc3BvbnNlLnN1Y2Nlc3MpIHtcbiAgICAgICAgdmFyIGRhdGEgPSByZXNwb25zZS5kYXRhO1xuICAgICAgICB0aGlzLmFwcEVudGl0eS5hcHBNYWluSW1hZ2VJZCA9IGRhdGEuZmlsZUlkO1xuICAgICAgICB0aGlzLnN5c3RlbUxvZ29JbWFnZVNyYyA9IEJhc2VVdGlsaXR5LkJ1aWxkQWN0aW9uKHRoaXMuYWNJbnRlcmZhY2UuYXBwTG9nb1VybCwge1xuICAgICAgICAgIGZpbGVJZDogdGhpcy5hcHBFbnRpdHkuYXBwTWFpbkltYWdlSWRcbiAgICAgICAgfSk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0RXJyb3Iod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0RXJyb3JJZCwge30sIHJlc3BvbnNlLm1lc3NhZ2UsIG51bGwsIG51bGwpO1xuICAgICAgfVxuICAgIH0sXG4gICAgZ2V0QXBwRW50aXR5OiBmdW5jdGlvbiBnZXRBcHBFbnRpdHkoKSB7XG4gICAgICByZXR1cm4gdGhpcy5hcHBFbnRpdHk7XG4gICAgfSxcbiAgICBzZXRBcHBFbnRpdHk6IGZ1bmN0aW9uIHNldEFwcEVudGl0eShhcHBFbnRpdHkpIHtcbiAgICAgIHRoaXMuYXBwRW50aXR5ID0gYXBwRW50aXR5O1xuICAgIH0sXG4gICAgY3JlYXRlS2V5czogZnVuY3Rpb24gY3JlYXRlS2V5cygpIHtcbiAgICAgIHZhciBfc2VsZiA9IHRoaXM7XG5cbiAgICAgIEFqYXhVdGlsaXR5LlBvc3QodGhpcy5hY0ludGVyZmFjZS5nZXROZXdLZXlzLCB7fSwgZnVuY3Rpb24gKHJlc3VsdCkge1xuICAgICAgICBpZiAocmVzdWx0LnN1Y2Nlc3MpIHtcbiAgICAgICAgICBfc2VsZi5hcHBFbnRpdHkuYXBwUHVibGljS2V5ID0gcmVzdWx0LmRhdGEucHVibGljS2V5O1xuICAgICAgICAgIF9zZWxmLmFwcEVudGl0eS5hcHBQcml2YXRlS2V5ID0gcmVzdWx0LmRhdGEucHJpdmF0ZUtleTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgcmVzdWx0Lm1lc3NhZ2UsIG51bGwpO1xuICAgICAgICB9XG4gICAgICB9LCB0aGlzKTtcbiAgICB9XG4gIH0sXG4gIHRlbXBsYXRlOiBcIjxkaXY+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IHN0eWxlPVxcXCJ3aWR0aDogODAlO2Zsb2F0OiBsZWZ0XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8aS1mb3JtIHJlZj1cXFwiYXBwRW50aXR5XFxcIiA6bW9kZWw9XFxcImFwcEVudGl0eVxcXCIgOnJ1bGVzPVxcXCJydWxlVmFsaWRhdGVcXFwiIDpsYWJlbC13aWR0aD1cXFwiMTAwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBsYWJlbD1cXFwiXFx1N0NGQlxcdTdFREZcXHU3RjE2XFx1NzgwMVxcdUZGMUFcXFwiIHByb3A9XFxcImFwcENvZGVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJvdz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiMTBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIHByb3A9XFxcImFwcENvZGVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcENvZGVcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiNFxcXCIgc3R5bGU9XFxcInRleHQtYWxpZ246IGNlbnRlclxcXCI+PHNwYW4gc3R5bGU9XFxcImNvbG9yOiByZWRcXFwiPio8L3NwYW4+IFxcdTdDRkJcXHU3RURGXFx1NTQwRFxcdTc5RjBcXHVGRjFBPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiMTBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIHByb3A9XFxcImFwcE5hbWVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcE5hbWVcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvcm93PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBsYWJlbD1cXFwiXFx1NTdERlxcdTU0MERcXHVGRjFBXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyb3c+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktY29sIHNwYW49XFxcIjEwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcERvbWFpblxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvaS1jb2w+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktY29sIHNwYW49XFxcIjRcXFwiIHN0eWxlPVxcXCJ0ZXh0LWFsaWduOiBjZW50ZXJcXFwiPlxcdTdDRkJcXHU3RURGXFx1N0M3QlxcdTUyMkJcXHVGRjFBPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiMTBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cmFkaW8tZ3JvdXAgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcENhdGVnb3J5XFxcIiB0eXBlPVxcXCJidXR0b25cXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvIGxhYmVsPVxcXCJhcHBcXFwiPlxcdTc5RkJcXHU1MkE4QXBwPC9yYWRpbz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyYWRpbyBsYWJlbD1cXFwid2ViXFxcIj5XZWJcXHU3Q0ZCXFx1N0VERjwvcmFkaW8+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvcmFkaW8tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvcm93PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBsYWJlbD1cXFwiXFx1NTE2Q1xcdTk0QTVcXHVGRjFBXFxcIiB2LWlmPVxcXCJpc1N1YlN5c3RlbT09JzAnXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHBsYWNlaG9sZGVyPVxcXCJcXHU4QkY3XFx1NTIxQlxcdTVFRkFcXHU1QkM2XFx1OTRBNVxcdTVCRjksXFx1NzUyOFxcdTRFOEVcXHU2NTcwXFx1NjM2RVxcdTc2ODRcXHU1MkEwXFx1NUJDNlxcdTRGN0ZcXHU3NTI4XFxcIiBzZWFyY2ggZW50ZXItYnV0dG9uPVxcXCJcXHU1MjFCXFx1NUVGQVxcdTVCQzZcXHU5NEE1XFx1NUJGOVxcXCIgdi1tb2RlbD1cXFwiYXBwRW50aXR5LmFwcFB1YmxpY0tleVxcXCIgQG9uLXNlYXJjaD1cXFwiY3JlYXRlS2V5cygpXFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU3OUMxXFx1OTRBNVxcdUZGMUFcXFwiIHYtaWY9XFxcImlzU3ViU3lzdGVtPT0wXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImFwcEVudGl0eS5hcHBQcml2YXRlS2V5XFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU1MjFCXFx1NUVGQVxcdTY1RjZcXHU5NUY0XFx1RkYxQVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cm93PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWNvbCBzcGFuPVxcXCIxMFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkYXRlLXBpY2tlciB0eXBlPVxcXCJkYXRlXFxcIiBwbGFjZWhvbGRlcj1cXFwiXFx1OTAwOVxcdTYyRTlcXHU1MjFCXFx1NUVGQVxcdTY1RjZcXHU5NUY0XFxcIiB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwQ3JlYXRlVGltZVxcXCIgZGlzYWJsZWRcXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHJlYWRvbmx5PjwvZGF0ZS1waWNrZXI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiNFxcXCIgc3R5bGU9XFxcInRleHQtYWxpZ246IGNlbnRlclxcXCI+XFx1NzJCNlxcdTYwMDFcXHVGRjFBPC9pLWNvbD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1jb2wgc3Bhbj1cXFwiMTBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHJhZGlvLWdyb3VwIHYtbW9kZWw9XFxcImFwcEVudGl0eS5hcHBTdGF0dXNcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxyYWRpbyBsYWJlbD1cXFwiXFx1NTQyRlxcdTc1MjhcXFwiPlxcdTU0MkZcXHU3NTI4PC9yYWRpbz5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8cmFkaW8gbGFiZWw9XFxcIlxcdTc5ODFcXHU3NTI4XFxcIj5cXHU3OTgxXFx1NzUyODwvcmFkaW8+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3JhZGlvLWdyb3VwPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2ktY29sPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9yb3c+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU5RUQ4XFx1OEJBNFxcdTU3MzBcXHU1NzQwXFx1RkYxQVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwSW5kZXhVcmxcXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxmb3JtLWl0ZW0gbGFiZWw9XFxcIlxcdTU5MDdcXHU2Q0U4XFx1RkYxQVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJhcHBFbnRpdHkuYXBwRGVzY1xcXCIgdHlwZT1cXFwidGV4dGFyZWFcXFwiIDphdXRvc2l6ZT1cXFwie21pblJvd3M6IDMsbWF4Um93czogM31cXFwiPjwvaS1pbnB1dD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9mb3JtLWl0ZW0+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9pLWZvcm0+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgc3R5bGU9XFxcIndpZHRoOiAxOSU7ZmxvYXQ6IHJpZ2h0XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IHN0eWxlPVxcXCJib3JkZXItcmFkaXVzOiA4cHg7dGV4dC1hbGlnbjogY2VudGVyO21hcmdpbi10b3A6IDBweDttYXJnaW4tYm90dG9tOiAzMHB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGltZyA6c3JjPVxcXCJzeXN0ZW1Mb2dvSW1hZ2VTcmNcXFwiIHN0eWxlPVxcXCJ3aWR0aDogMTEwcHg7aGVpZ2h0OiAxMTBweFxcXCIgLz5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8dXBsb2FkIHN0eWxlPVxcXCJtYXJnaW46MTBweCAxMnB4IDAgMjBweFxcXCIgOm9uLXN1Y2Nlc3M9XFxcInVwbG9hZFN5c3RlbUxvZ29JbWFnZVN1Y2Nlc3NcXFwiIDpkYXRhPVxcXCJhcHBFbnRpdHlcXFwiIG11bHRpcGxlIHR5cGU9XFxcImRyYWdcXFwiIG5hbWU9XFxcImZpbGVcXFwiIGFjdGlvbj1cXFwiLi4vLi4vLi4vUmVzdC9TU08vQXBwL0FwcGxpY2F0aW9uL1VwbG9hZEFwcExvZ29cXFwiIGFjY2VwdD1cXFwiLnBuZ1xcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgc3R5bGU9XFxcInBhZGRpbmc6MTBweCAwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGljb24gdHlwZT1cXFwiaW9zLWNsb3VkLXVwbG9hZFxcXCIgc2l6ZT1cXFwiNTJcXFwiIHN0eWxlPVxcXCJjb2xvcjogIzMzOTlmZlxcXCI+PC9pY29uPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHA+XFx1NEUwQVxcdTRGMjBcXHU3Q0ZCXFx1N0VERkxvZ288L3A+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvdXBsb2FkPlxcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgIDwvZGl2PlwiXG59KTsiLCJcInVzZSBzdHJpY3RcIjtcblxuVnVlLmNvbXBvbmVudChcInNzby1hcHAtaW50ZXJmYWNlLWxpc3QtY29tcFwiLCB7XG4gIHByb3BzOiBbXCJpbnRlcmZhY2VCZWxvbmdBcHBJZFwiXSxcbiAgd2F0Y2g6IHtcbiAgICBpbnRlcmZhY2VCZWxvbmdBcHBJZDogZnVuY3Rpb24gaW50ZXJmYWNlQmVsb25nQXBwSWQobmV3VmFsKSB7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VCZWxvbmdBcHBJZCA9IG5ld1ZhbDtcbiAgICB9XG4gIH0sXG4gIGRhdGE6IGZ1bmN0aW9uIGRhdGEoKSB7XG4gICAgdmFyIF9zZWxmID0gdGhpcztcblxuICAgIHJldHVybiB7XG4gICAgICBhY0ludGVyZmFjZToge1xuICAgICAgICBkZWxldGU6IFwiL1BsYXRGb3JtUmVzdC9TU08vQXBwbGljYXRpb24vRGVsZXRlSW50ZXJmYWNlXCJcbiAgICAgIH0sXG4gICAgICBpbnRlcmZhY2VFbnRpdHk6IHtcbiAgICAgICAgaW50ZXJmYWNlSWQ6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZUJlbG9uZ0FwcElkOiBcIlwiLFxuICAgICAgICBpbnRlcmZhY2VDb2RlOiBcIlwiLFxuICAgICAgICBpbnRlcmZhY2VOYW1lOiBcIlwiLFxuICAgICAgICBpbnRlcmZhY2VVcmw6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZVBhcmFzOiBcIlwiLFxuICAgICAgICBpbnRlcmZhY2VGb3JtYXQ6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZURlc2M6IFwiXCIsXG4gICAgICAgIGludGVyZmFjZU9yZGVyTnVtOiBcIlwiLFxuICAgICAgICBpbnRlcmZhY2VDcmVhdGVUaW1lOiBEYXRlVXRpbGl0eS5HZXRDdXJyZW50RGF0YSgpLFxuICAgICAgICBpbnRlcmZhY2VTdGF0dXM6IFwi5ZCv55SoXCIsXG4gICAgICAgIGludGVyZmFjZUNyZWF0ZXJJZDogXCJcIixcbiAgICAgICAgaW50ZXJmYWNlT3JnYW5JZDogXCJcIlxuICAgICAgfSxcbiAgICAgIGxpc3Q6IHtcbiAgICAgICAgY29sdW1uc0NvbmZpZzogW3tcbiAgICAgICAgICB0eXBlOiAnc2VsZWN0aW9uJyxcbiAgICAgICAgICB3aWR0aDogNjAsXG4gICAgICAgICAgYWxpZ246ICdjZW50ZXInXG4gICAgICAgIH0sIHtcbiAgICAgICAgICB0aXRsZTogJ+aOpeWPo+exu+WeiycsXG4gICAgICAgICAga2V5OiAnaW50ZXJmYWNlQ29kZScsXG4gICAgICAgICAgYWxpZ246IFwiY2VudGVyXCIsXG4gICAgICAgICAgd2lkdGg6IDEwMFxuICAgICAgICB9LCB7XG4gICAgICAgICAgdGl0bGU6ICfmjqXlj6PlkI3np7AnLFxuICAgICAgICAgIGtleTogJ2ludGVyZmFjZU5hbWUnLFxuICAgICAgICAgIGFsaWduOiBcImNlbnRlclwiLFxuICAgICAgICAgIHdpZHRoOiAyODBcbiAgICAgICAgfSwge1xuICAgICAgICAgIHRpdGxlOiAn5aSH5rOoJyxcbiAgICAgICAgICBrZXk6ICdpbnRlcmZhY2VEZXNjJyxcbiAgICAgICAgICBhbGlnbjogXCJjZW50ZXJcIlxuICAgICAgICB9LCB7XG4gICAgICAgICAgdGl0bGU6ICfmk43kvZwnLFxuICAgICAgICAgIGtleTogJ2ludGVyZmFjZUlkJyxcbiAgICAgICAgICB3aWR0aDogMTQwLFxuICAgICAgICAgIGFsaWduOiBcImNlbnRlclwiLFxuICAgICAgICAgIHJlbmRlcjogZnVuY3Rpb24gcmVuZGVyKGgsIHBhcmFtcykge1xuICAgICAgICAgICAgcmV0dXJuIGgoJ2RpdicsIHtcbiAgICAgICAgICAgICAgY2xhc3M6IFwibGlzdC1yb3ctYnV0dG9uLXdyYXBcIlxuICAgICAgICAgICAgfSwgW0xpc3RQYWdlVXRpbGl0eS5JVmlld1RhYmxlSW5uZXJCdXR0b24uRWRpdEJ1dHRvbihoLCBwYXJhbXMsIFwiaW50ZXJmYWNlSWRcIiwgX3NlbGYpLCBMaXN0UGFnZVV0aWxpdHkuSVZpZXdUYWJsZUlubmVyQnV0dG9uLkRlbGV0ZUJ1dHRvbihoLCBwYXJhbXMsIFwiaW50ZXJmYWNlSWRcIiwgX3NlbGYpXSk7XG4gICAgICAgICAgfVxuICAgICAgICB9XSxcbiAgICAgICAgdGFibGVEYXRhOiBbXVxuICAgICAgfSxcbiAgICAgIGlubmVyU3RhdHVzOiBcImFkZFwiXG4gICAgfTtcbiAgfSxcbiAgbW91bnRlZDogZnVuY3Rpb24gbW91bnRlZCgpIHt9LFxuICBtZXRob2RzOiB7XG4gICAgcmVzZXRMaXN0RGF0YTogZnVuY3Rpb24gcmVzZXRMaXN0RGF0YSgpIHtcbiAgICAgIHRoaXMubGlzdC50YWJsZURhdGEgPSBbXTtcbiAgICB9LFxuICAgIGFkZEludGVyZmFjZTogZnVuY3Rpb24gYWRkSW50ZXJmYWNlKCkge1xuICAgICAgdmFyIGVsZW0gPSB0aGlzLiRyZWZzLnNzb0FwcEludGVyZmFjZUVkaXRNb2RlbERpYWxvZ1dyYXA7XG4gICAgICB0aGlzLmlubmVyU3RhdHVzID09IFwiYWRkXCI7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VJZCA9IFwiXCI7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VCZWxvbmdBcHBJZCA9IHRoaXMuaW50ZXJmYWNlQmVsb25nQXBwSWQ7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDb2RlID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZU5hbWUgPSBcIlwiO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlVXJsID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZVBhcmFzID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUZvcm1hdCA9IFwiXCI7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VEZXNjID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZU9yZGVyTnVtID0gXCJcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUNyZWF0ZVRpbWUgPSBEYXRlVXRpbGl0eS5HZXRDdXJyZW50RGF0YSgpO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlU3RhdHVzID0gXCLlkK/nlKhcIjtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZUNyZWF0ZXJJZCA9IFwiXCI7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VPcmdhbklkID0gXCJcIjtcbiAgICAgIERpYWxvZ1V0aWxpdHkuRGlhbG9nRWxlbU9iaihlbGVtLCB7XG4gICAgICAgIG1vZGFsOiB0cnVlLFxuICAgICAgICB3aWR0aDogNTcwLFxuICAgICAgICBoZWlnaHQ6IDMzMCxcbiAgICAgICAgdGl0bGU6IFwi5o6l5Y+j6K6+572uXCJcbiAgICAgIH0pO1xuICAgIH0sXG4gICAgaGFuZGxlQ2xvc2U6IGZ1bmN0aW9uIGhhbmRsZUNsb3NlKCkge1xuICAgICAgRGlhbG9nVXRpbGl0eS5DbG9zZURpYWxvZ0VsZW0odGhpcy4kcmVmcy5zc29BcHBJbnRlcmZhY2VFZGl0TW9kZWxEaWFsb2dXcmFwKTtcbiAgICB9LFxuICAgIHNhdmVJbnRlcmZhY2VFZGl0OiBmdW5jdGlvbiBzYXZlSW50ZXJmYWNlRWRpdCgpIHtcbiAgICAgIGlmICh0aGlzLmlubmVyU3RhdHVzID09IFwiYWRkXCIpIHtcbiAgICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlSWQgPSBTdHJpbmdVdGlsaXR5Lkd1aWQoKTtcbiAgICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YS5wdXNoKEpzb25VdGlsaXR5LkNsb25lU2ltcGxlKHRoaXMuaW50ZXJmYWNlRW50aXR5KSk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICBmb3IgKHZhciBpID0gMDsgaSA8IHRoaXMubGlzdC50YWJsZURhdGEubGVuZ3RoOyBpKyspIHtcbiAgICAgICAgICBpZiAodGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VJZCA9PSB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VJZCkge1xuICAgICAgICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VDb2RlID0gdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlQ29kZTtcbiAgICAgICAgICAgIHRoaXMubGlzdC50YWJsZURhdGFbaV0uaW50ZXJmYWNlTmFtZSA9IHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZU5hbWU7XG4gICAgICAgICAgICB0aGlzLmxpc3QudGFibGVEYXRhW2ldLmludGVyZmFjZVVybCA9IHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZVVybDtcbiAgICAgICAgICAgIHRoaXMubGlzdC50YWJsZURhdGFbaV0uaW50ZXJmYWNlUGFyYXMgPSB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VQYXJhcztcbiAgICAgICAgICAgIHRoaXMubGlzdC50YWJsZURhdGFbaV0uaW50ZXJmYWNlRm9ybWF0ID0gdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlRm9ybWF0O1xuICAgICAgICAgICAgdGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VEZXNjID0gdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlRGVzYztcbiAgICAgICAgICAgIGJyZWFrO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICB0aGlzLmhhbmRsZUNsb3NlKCk7XG4gICAgfSxcbiAgICBjaGFuZ2VJbnRlcmZhY2VDb2RlOiBmdW5jdGlvbiBjaGFuZ2VJbnRlcmZhY2VDb2RlKHZhbHVlKSB7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDb2RlID0gdmFsdWU7XG4gICAgfSxcbiAgICBnZXRJbnRlcmZhY2VMaXN0RGF0YTogZnVuY3Rpb24gZ2V0SW50ZXJmYWNlTGlzdERhdGEoKSB7XG4gICAgICByZXR1cm4gdGhpcy5saXN0LnRhYmxlRGF0YTtcbiAgICB9LFxuICAgIHNldEludGVyZmFjZUxpc3REYXRhOiBmdW5jdGlvbiBzZXRJbnRlcmZhY2VMaXN0RGF0YShkYXRhKSB7XG4gICAgICB0aGlzLmxpc3QudGFibGVEYXRhID0gZGF0YTtcbiAgICB9LFxuICAgIGVkaXQ6IGZ1bmN0aW9uIGVkaXQoaW50ZXJmYWNlSWQsIHBhcmFtcykge1xuICAgICAgdGhpcy5pbm5lclN0YXR1cyA9IFwidXBkYXRlXCI7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VJZCA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlSWQ7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDb2RlID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VDb2RlO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlTmFtZSA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlTmFtZTtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZVVybCA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlVXJsO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlUGFyYXMgPSBwYXJhbXMucm93LmludGVyZmFjZVBhcmFzO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlRm9ybWF0ID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VGb3JtYXQ7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VEZXNjID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VEZXNjO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlT3JkZXJOdW0gPSBwYXJhbXMucm93LmludGVyZmFjZU9yZGVyTnVtO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlQ3JlYXRlVGltZSA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlQ3JlYXRlVGltZTtcbiAgICAgIHRoaXMuaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZVN0YXR1cyA9IHBhcmFtcy5yb3cuaW50ZXJmYWNlU3RhdHVzO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlQ3JlYXRlcklkID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VDcmVhdGVySWQ7XG4gICAgICB0aGlzLmludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VPcmdhbklkID0gcGFyYW1zLnJvdy5pbnRlcmZhY2VPcmdhbklkO1xuICAgICAgdGhpcy5pbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlQmVsb25nQXBwSWQgPSBwYXJhbXMucm93LmludGVyZmFjZUJlbG9uZ0FwcElkO1xuICAgICAgdmFyIGVsZW0gPSB0aGlzLiRyZWZzLnNzb0FwcEludGVyZmFjZUVkaXRNb2RlbERpYWxvZ1dyYXA7XG4gICAgICBEaWFsb2dVdGlsaXR5LkRpYWxvZ0VsZW1PYmooZWxlbSwge1xuICAgICAgICBtb2RhbDogdHJ1ZSxcbiAgICAgICAgd2lkdGg6IDU3MCxcbiAgICAgICAgaGVpZ2h0OiAzMzAsXG4gICAgICAgIHRpdGxlOiBcIuaOpeWPo+iuvue9rlwiXG4gICAgICB9KTtcbiAgICB9LFxuICAgIGRlbDogZnVuY3Rpb24gZGVsKGludGVyZmFjZUlkLCBwYXJhbXMpIHtcbiAgICAgIHZhciBfc2VsZiA9IHRoaXM7XG5cbiAgICAgIGZvciAodmFyIGkgPSAwOyBpIDwgdGhpcy5saXN0LnRhYmxlRGF0YS5sZW5ndGg7IGkrKykge1xuICAgICAgICBpZiAodGhpcy5saXN0LnRhYmxlRGF0YVtpXS5pbnRlcmZhY2VJZCA9PSBpbnRlcmZhY2VJZCkge1xuICAgICAgICAgIF9zZWxmLmxpc3QudGFibGVEYXRhLnNwbGljZShpLCAxKTtcblxuICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQ29uZmlybSh3aW5kb3csIFwi56Gu6K6k6KaB5Yig6Zmk6K+l5o6l5Y+j5ZCX77yfXCIsIGZ1bmN0aW9uICgpIHtcbiAgICAgICAgICAgIEFqYXhVdGlsaXR5LkRlbGV0ZShfc2VsZi5hY0ludGVyZmFjZS5kZWxldGUsIHtcbiAgICAgICAgICAgICAgXCJpbnRlcmZhY2VJZFwiOiBpbnRlcmZhY2VJZFxuICAgICAgICAgICAgfSwgZnVuY3Rpb24gKHJlc3VsdCkge1xuICAgICAgICAgICAgICBpZiAocmVzdWx0LnN1Y2Nlc3MpIHt9IGVsc2Uge1xuICAgICAgICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCByZXN1bHQubWVzc2FnZSwgbnVsbCk7XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH0sIHRoaXMpO1xuICAgICAgICAgIH0pO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICB9LFxuICB0ZW1wbGF0ZTogXCI8ZGl2IGNsYXNzPVxcXCJpdi1saXN0LXBhZ2Utd3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IHJlZj1cXFwic3NvQXBwSW50ZXJmYWNlRWRpdE1vZGVsRGlhbG9nV3JhcFxcXCIgY2xhc3M9XFxcImdlbmVyYWwtZWRpdC1wYWdlLXdyYXBcXFwiIHN0eWxlPVxcXCJkaXNwbGF5OiBub25lO21hcmdpbi10b3A6IDBweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGktZm9ybSByZWY9XFxcImludGVyZmFjZUVudGl0eVxcXCIgOm1vZGVsPVxcXCJpbnRlcmZhY2VFbnRpdHlcXFwiIDpsYWJlbC13aWR0aD1cXFwiMTMwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBzdHlsZT1cXFwibWFyZ2luLWJvdHRvbTogMnB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxzcGFuIHNsb3Q9XFxcImxhYmVsXFxcIj48c3BhbiBzdHlsZT1cXFwiY29sb3I6IHJlZFxcXCI+Kjwvc3Bhbj4mbmJzcDtcXHU2M0E1XFx1NTNFM1xcdTdDN0JcXHU1NzhCXFx1RkYxQTwvc3Bhbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VDb2RlXFxcIiBzaXplPVxcXCJzbWFsbFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPFNlbGVjdCBzbG90PVxcXCJhcHBlbmRcXFwiIHN0eWxlPVxcXCJ3aWR0aDogOTBweFxcXCIgQG9uLWNoYW5nZT1cXFwiY2hhbmdlSW50ZXJmYWNlQ29kZVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxPcHRpb24gdmFsdWU9XFxcIlxcdTc2N0JcXHU1RjU1XFx1NjNBNVxcdTUzRTNcXFwiPlxcdTc2N0JcXHU1RjU1XFx1NjNBNVxcdTUzRTM8L09wdGlvbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPE9wdGlvbiB2YWx1ZT1cXFwiXFx1NTE3NlxcdTRFRDZcXFwiPlxcdTUxNzZcXHU0RUQ2PC9PcHRpb24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9TZWxlY3Q+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIHN0eWxlPVxcXCJtYXJnaW4tYm90dG9tOiAycHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHNwYW4gc2xvdD1cXFwibGFiZWxcXFwiPjxzcGFuIHN0eWxlPVxcXCJjb2xvcjogcmVkXFxcIj4qPC9zcGFuPiZuYnNwO1xcdTYzQTVcXHU1M0UzXFx1NTQwRFxcdTc5RjBcXHVGRjFBPC9zcGFuPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZU5hbWVcXFwiIHNpemU9XFxcInNtYWxsXFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU2M0E1XFx1NTNFM1xcdTU3MzBcXHU1NzQwXFx1RkYxQVxcXCIgc3R5bGU9XFxcIm1hcmdpbi1ib3R0b206IDJweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8aS1pbnB1dCB2LW1vZGVsPVxcXCJpbnRlcmZhY2VFbnRpdHkuaW50ZXJmYWNlVXJsXFxcIiBzaXplPVxcXCJzbWFsbFxcXCI+PC9pLWlucHV0PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBsYWJlbD1cXFwiXFx1NTNDMlxcdTY1NzBcXHVGRjFBXFxcIiBzdHlsZT1cXFwibWFyZ2luLWJvdHRvbTogMnB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VQYXJhc1xcXCIgdHlwZT1cXFwidGV4dGFyZWFcXFwiIDphdXRvc2l6ZT1cXFwie21pblJvd3M6IDIsbWF4Um93czogMn1cXFwiIHNpemU9XFxcInNtYWxsXFxcIj48L2ktaW5wdXQ+ICAgIFxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Zvcm0taXRlbT5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGZvcm0taXRlbSBsYWJlbD1cXFwiXFx1NjgzQ1xcdTVGMEZcXHU1MzE2XFx1NjVCOVxcdTZDRDVcXHVGRjFBXFxcIiBzdHlsZT1cXFwibWFyZ2luLWJvdHRvbTogMnB4XFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWlucHV0IHYtbW9kZWw9XFxcImludGVyZmFjZUVudGl0eS5pbnRlcmZhY2VGb3JtYXRcXFwiIHNpemU9XFxcInNtYWxsXFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8Zm9ybS1pdGVtIGxhYmVsPVxcXCJcXHU1OTA3XFx1NkNFOFxcdUZGMUFcXFwiIHN0eWxlPVxcXCJtYXJnaW4tYm90dG9tOiAycHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktaW5wdXQgdi1tb2RlbD1cXFwiaW50ZXJmYWNlRW50aXR5LmludGVyZmFjZURlc2NcXFwiIHNpemU9XFxcInNtYWxsXFxcIj48L2ktaW5wdXQ+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZm9ybS1pdGVtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvaS1mb3JtPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbi1vdXRlci13cmFwXFxcIiBzdHlsZT1cXFwibWFyZ2luLWxlZnQ6IDhweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbi1pbm5lci13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxidXR0b24tZ3JvdXAgc2l6ZT1cXFwic21hbGxcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWJ1dHRvbiB0eXBlPVxcXCJwcmltYXJ5XFxcIiBAY2xpY2s9XFxcInNhdmVJbnRlcmZhY2VFZGl0KCdpbnRlcmZhY2VFbnRpdHknKVxcXCIgaWNvbj1cXFwibWQtY2hlY2ttYXJrXFxcIj48L2ktYnV0dG9uPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWJ1dHRvbiBAY2xpY2s9XFxcImhhbmRsZUNsb3NlKClcXFwiIGljb249XFxcIm1kLWNsb3NlXFxcIj48L2ktYnV0dG9uPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9idXR0b24tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8ZGl2IGlkPVxcXCJsaXN0LWJ1dHRvbi13cmFwXFxcIiBjbGFzcz1cXFwibGlzdC1idXR0b24tb3V0ZXItd3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwibGlzdC1idXR0b24taW5uZXItd3JhcFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxCdXR0b25Hcm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpLWJ1dHRvbiAgdHlwZT1cXFwic3VjY2Vzc1xcXCIgQGNsaWNrPVxcXCJhZGRJbnRlcmZhY2UoKVxcXCIgaWNvbj1cXFwibWQtYWRkXFxcIj5cXHU2NUIwXFx1NTg5RTwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvQnV0dG9uR3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBzdHlsZT1cXFwiY2xlYXI6IGJvdGhcXFwiPjwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8aS10YWJsZSA6aGVpZ2h0PVxcXCJsaXN0Lmxpc3RIZWlnaHRcXFwiIHN0cmlwZSBib3JkZXIgOmNvbHVtbnM9XFxcImxpc3QuY29sdW1uc0NvbmZpZ1xcXCIgOmRhdGE9XFxcImxpc3QudGFibGVEYXRhXFxcIlxcbiAgICAgICAgICAgICAgICAgICAgICAgICBjbGFzcz1cXFwiaXYtbGlzdC10YWJsZVxcXCIgOmhpZ2hsaWdodC1yb3c9XFxcInRydWVcXFwiPjwvaS10YWJsZT5cXG4gICAgICAgICAgICAgICAgPC9kaXY+XCJcbn0pOyIsIlwidXNlIHN0cmljdFwiO1xuXG5WdWUuY29tcG9uZW50KFwic3NvLWFwcC1zdWItc3lzdGVtLWxpc3QtY29tcFwiLCB7XG4gIHByb3BzOiBbXCJzdGF0dXNcIiwgXCJiZWxvbmdBcHBJZFwiXSxcbiAgZGF0YTogZnVuY3Rpb24gZGF0YSgpIHtcbiAgICByZXR1cm4ge1xuICAgICAgYWNJbnRlcmZhY2U6IHtcbiAgICAgICAgc2F2ZVN1YkFwcFVybDogXCIvUGxhdEZvcm1SZXN0L1NTTy9BcHBsaWNhdGlvbi9TYXZlU3ViQXBwXCIsXG4gICAgICAgIHJlbG9hZERhdGE6IFwiL1BsYXRGb3JtUmVzdC9TU08vQXBwbGljYXRpb24vR2V0QWxsU3ViU3NvQXBwXCIsXG4gICAgICAgIGFwcExvZ29Vcmw6IFwiL1BsYXRGb3JtUmVzdC9TU08vQXBwbGljYXRpb24vR2V0QXBwTG9nb1wiLFxuICAgICAgICBkZWxldGU6IFwiL1BsYXRGb3JtUmVzdC9TU08vQXBwbGljYXRpb24vRGVsZXRlXCIsXG4gICAgICAgIGdldERhdGFVcmw6IFwiL1BsYXRGb3JtUmVzdC9TU08vQXBwbGljYXRpb24vR2V0QXBwVm9cIlxuICAgICAgfSxcbiAgICAgIGFwcExpc3Q6IFtdLFxuICAgICAgaW5uZXJFZGl0TW9kZWxEaWFsb2dTdGF0dXM6IFwiYWRkXCJcbiAgICB9O1xuICB9LFxuICBtb3VudGVkOiBmdW5jdGlvbiBtb3VudGVkKCkge1xuICAgIHRoaXMucmVsb2FkRGF0YSgpO1xuICB9LFxuICBtZXRob2RzOiB7XG4gICAgYWRkSW50ZWdyYXRlZFN5c3RlbTogZnVuY3Rpb24gYWRkSW50ZWdyYXRlZFN5c3RlbSgpIHtcbiAgICAgIHZhciBlbGVtID0gdGhpcy4kcmVmcy5zc29BcHBTdWJTeXN0ZW1FZGl0TW9kZWxEaWFsb2dXcmFwO1xuICAgICAgdGhpcy4kcmVmcy5zdWJBcHBEZXRhaWxGcm9tQ29tcC5yZXNldEFwcEVudGl0eSgpO1xuICAgICAgdGhpcy4kcmVmcy5zdWJBcHBJbnRlcmZhY2VMaXN0Q29tcC5yZXNldExpc3REYXRhKCk7XG4gICAgICB0aGlzLmlubmVyRWRpdE1vZGVsRGlhbG9nU3RhdHVzID0gXCJhZGRcIjtcbiAgICAgIERpYWxvZ1V0aWxpdHkuRGlhbG9nRWxlbU9iaihlbGVtLCB7XG4gICAgICAgIG1vZGFsOiB0cnVlLFxuICAgICAgICB3aWR0aDogOTAwLFxuICAgICAgICBoZWlnaHQ6IDUwMCxcbiAgICAgICAgdGl0bGU6IFwi5a2Q57O757uf6K6+572uXCJcbiAgICAgIH0pO1xuICAgIH0sXG4gICAgc2F2ZVN1YlN5c3RlbVNldHRpbmc6IGZ1bmN0aW9uIHNhdmVTdWJTeXN0ZW1TZXR0aW5nKCkge1xuICAgICAgdmFyIF9zZWxmID0gdGhpcztcblxuICAgICAgdmFyIHNzb0FwcEVudGl0eSA9IHRoaXMuJHJlZnMuc3ViQXBwRGV0YWlsRnJvbUNvbXAuZ2V0QXBwRW50aXR5KCk7XG4gICAgICB2YXIgc3NvQXBwSW50ZXJmYWNlRW50aXR5TGlzdCA9IHRoaXMuJHJlZnMuc3ViQXBwSW50ZXJmYWNlTGlzdENvbXAuZ2V0SW50ZXJmYWNlTGlzdERhdGEoKTtcbiAgICAgIHNzb0FwcEVudGl0eS5hcHBNYWluSWQgPSB0aGlzLmJlbG9uZ0FwcElkO1xuXG4gICAgICBpZiAodGhpcy5pbm5lckVkaXRNb2RlbERpYWxvZ1N0YXR1cyA9PSBcImFkZFwiKSB7XG4gICAgICAgIHNzb0FwcEVudGl0eS5hcHBJZCA9IFN0cmluZ1V0aWxpdHkuR3VpZCgpO1xuICAgICAgfVxuXG4gICAgICBpZiAoc3NvQXBwSW50ZXJmYWNlRW50aXR5TGlzdCkge1xuICAgICAgICBmb3IgKHZhciBpID0gMDsgaSA8IHNzb0FwcEludGVyZmFjZUVudGl0eUxpc3QubGVuZ3RoOyBpKyspIHtcbiAgICAgICAgICBzc29BcHBJbnRlcmZhY2VFbnRpdHlMaXN0W2ldLmludGVyZmFjZUJlbG9uZ0FwcElkID0gc3NvQXBwRW50aXR5LmFwcElkO1xuICAgICAgICB9XG4gICAgICB9XG5cbiAgICAgIHZhciB2byA9IHtcbiAgICAgICAgXCJzc29BcHBFbnRpdHlcIjogc3NvQXBwRW50aXR5LFxuICAgICAgICBcInNzb0FwcEludGVyZmFjZUVudGl0eUxpc3RcIjogc3NvQXBwSW50ZXJmYWNlRW50aXR5TGlzdFxuICAgICAgfTtcbiAgICAgIHZhciBzZW5kRGF0YSA9IEpTT04uc3RyaW5naWZ5KHZvKTtcbiAgICAgIEFqYXhVdGlsaXR5LlBvc3RSZXF1ZXN0Qm9keSh0aGlzLmFjSW50ZXJmYWNlLnNhdmVTdWJBcHBVcmwsIHNlbmREYXRhLCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge1xuICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCByZXN1bHQubWVzc2FnZSwgZnVuY3Rpb24gKCkge1xuICAgICAgICAgICAgX3NlbGYucmVsb2FkRGF0YSgpO1xuXG4gICAgICAgICAgICBfc2VsZi5oYW5kbGVDbG9zZSgpO1xuICAgICAgICAgIH0pO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIERpYWxvZ1V0aWxpdHkuQWxlcnQod2luZG93LCBEaWFsb2dVdGlsaXR5LkRpYWxvZ0FsZXJ0SWQsIHt9LCByZXN1bHQubWVzc2FnZSwgbnVsbCk7XG4gICAgICAgIH1cbiAgICAgIH0sIFwianNvblwiKTtcbiAgICB9LFxuICAgIGhhbmRsZUNsb3NlOiBmdW5jdGlvbiBoYW5kbGVDbG9zZSgpIHtcbiAgICAgIERpYWxvZ1V0aWxpdHkuQ2xvc2VEaWFsb2dFbGVtKHRoaXMuJHJlZnMuc3NvQXBwU3ViU3lzdGVtRWRpdE1vZGVsRGlhbG9nV3JhcCk7XG4gICAgfSxcbiAgICByZWxvYWREYXRhOiBmdW5jdGlvbiByZWxvYWREYXRhKCkge1xuICAgICAgdmFyIF9zZWxmID0gdGhpcztcblxuICAgICAgQWpheFV0aWxpdHkuUG9zdCh0aGlzLmFjSW50ZXJmYWNlLnJlbG9hZERhdGEsIHtcbiAgICAgICAgYXBwSWQ6IF9zZWxmLmJlbG9uZ0FwcElkXG4gICAgICB9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge1xuICAgICAgICAgIF9zZWxmLmFwcExpc3QgPSByZXN1bHQuZGF0YTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgcmVzdWx0Lm1lc3NhZ2UsIG51bGwpO1xuICAgICAgICB9XG4gICAgICB9LCBcImpzb25cIik7XG4gICAgfSxcbiAgICBidWlsZExvZ29Vcmw6IGZ1bmN0aW9uIGJ1aWxkTG9nb1VybChhcHApIHtcbiAgICAgIGlmIChhcHAuYXBwTWFpbkltYWdlSWQgPT0gXCJcIikge1xuICAgICAgICByZXR1cm4gQmFzZVV0aWxpdHkuQnVpbGRBY3Rpb24odGhpcy5hY0ludGVyZmFjZS5hcHBMb2dvVXJsLCB7XG4gICAgICAgICAgZmlsZUlkOiBcImRlZmF1bHRTU09BcHBMb2dvSW1hZ2VcIlxuICAgICAgICB9KTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIHJldHVybiBCYXNlVXRpbGl0eS5CdWlsZEFjdGlvbih0aGlzLmFjSW50ZXJmYWNlLmFwcExvZ29VcmwsIHtcbiAgICAgICAgICBmaWxlSWQ6IGFwcC5hcHBNYWluSW1hZ2VJZFxuICAgICAgICB9KTtcbiAgICAgIH1cbiAgICB9LFxuICAgIHNldHRpbmdBcHA6IGZ1bmN0aW9uIHNldHRpbmdBcHAoYXBwKSB7XG4gICAgICB2YXIgZWxlbSA9IHRoaXMuJHJlZnMuc3NvQXBwU3ViU3lzdGVtRWRpdE1vZGVsRGlhbG9nV3JhcDtcbiAgICAgIHRoaXMuaW5uZXJFZGl0TW9kZWxEaWFsb2dTdGF0dXMgPSBcInVwZGF0ZVwiO1xuXG4gICAgICB2YXIgX3NlbGYgPSB0aGlzO1xuXG4gICAgICBBamF4VXRpbGl0eS5Qb3N0KHRoaXMuYWNJbnRlcmZhY2UuZ2V0RGF0YVVybCwge1xuICAgICAgICBhcHBJZDogYXBwLmFwcElkXG4gICAgICB9LCBmdW5jdGlvbiAocmVzdWx0KSB7XG4gICAgICAgIGNvbnNvbGUubG9nKHJlc3VsdCk7XG5cbiAgICAgICAgaWYgKHJlc3VsdC5zdWNjZXNzKSB7XG4gICAgICAgICAgX3NlbGYuJHJlZnMuc3ViQXBwRGV0YWlsRnJvbUNvbXAuc2V0QXBwRW50aXR5KHJlc3VsdC5kYXRhLnNzb0FwcEVudGl0eSk7XG5cbiAgICAgICAgICBfc2VsZi4kcmVmcy5zdWJBcHBJbnRlcmZhY2VMaXN0Q29tcC5zZXRJbnRlcmZhY2VMaXN0RGF0YShyZXN1bHQuZGF0YS5zc29BcHBJbnRlcmZhY2VFbnRpdHlMaXN0KTtcblxuICAgICAgICAgIERpYWxvZ1V0aWxpdHkuRGlhbG9nRWxlbU9iaihlbGVtLCB7XG4gICAgICAgICAgICBtb2RhbDogdHJ1ZSxcbiAgICAgICAgICAgIHdpZHRoOiA5MDAsXG4gICAgICAgICAgICBoZWlnaHQ6IDUwMCxcbiAgICAgICAgICAgIHRpdGxlOiBcIuWtkOezu+e7n+iuvue9rlwiXG4gICAgICAgICAgfSk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBudWxsKTtcbiAgICAgICAgfVxuICAgICAgfSwgXCJqc29uXCIpO1xuICAgIH0sXG4gICAgcmVtb3ZlQXBwOiBmdW5jdGlvbiByZW1vdmVBcHAoYXBwKSB7XG4gICAgICB2YXIgX3NlbGYgPSB0aGlzO1xuXG4gICAgICBEaWFsb2dVdGlsaXR5LkNvbmZpcm0od2luZG93LCBcIuehruiupOimgeazqOmUgOezu+e7n1tcIiArIGFwcC5hcHBOYW1lICsgXCJd5ZCX77yfXCIsIGZ1bmN0aW9uICgpIHtcbiAgICAgICAgQWpheFV0aWxpdHkuRGVsZXRlKF9zZWxmLmFjSW50ZXJmYWNlLmRlbGV0ZSwge1xuICAgICAgICAgIGFwcElkOiBhcHAuYXBwSWRcbiAgICAgICAgfSwgZnVuY3Rpb24gKHJlc3VsdCkge1xuICAgICAgICAgIGlmIChyZXN1bHQuc3VjY2Vzcykge1xuICAgICAgICAgICAgRGlhbG9nVXRpbGl0eS5BbGVydCh3aW5kb3csIERpYWxvZ1V0aWxpdHkuRGlhbG9nQWxlcnRJZCwge30sIHJlc3VsdC5tZXNzYWdlLCBmdW5jdGlvbiAoKSB7XG4gICAgICAgICAgICAgIF9zZWxmLnJlbG9hZERhdGEoKTtcbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgICBEaWFsb2dVdGlsaXR5LkFsZXJ0KHdpbmRvdywgRGlhbG9nVXRpbGl0eS5EaWFsb2dBbGVydElkLCB7fSwgcmVzdWx0Lm1lc3NhZ2UsIG51bGwpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSwgXCJqc29uXCIpO1xuICAgICAgfSk7XG4gICAgfVxuICB9LFxuICB0ZW1wbGF0ZTogXCI8ZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgPGRpdiByZWY9XFxcInNzb0FwcFN1YlN5c3RlbUVkaXRNb2RlbERpYWxvZ1dyYXBcXFwiIGNsYXNzPVxcXCJnZW5lcmFsLWVkaXQtcGFnZS13cmFwXFxcIiBzdHlsZT1cXFwiZGlzcGxheTogbm9uZTttYXJnaW4tdG9wOiAwcHhcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgIDx0YWJzPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGFiLXBhbmUgbGFiZWw9XFxcIlxcdTdDRkJcXHU3RURGXFx1OEJCRVxcdTdGNkVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHNzby1hcHAtZGV0YWlsLWZyb20tY29tcCByZWY9XFxcInN1YkFwcERldGFpbEZyb21Db21wXFxcIiA6aXMtc3ViLXN5c3RlbT1cXFwidHJ1ZVxcXCIgOnN0YXR1cz1cXFwiaW5uZXJFZGl0TW9kZWxEaWFsb2dTdGF0dXNcXFwiPjwvc3NvLWFwcC1kZXRhaWwtZnJvbS1jb21wPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L3RhYi1wYW5lPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dGFiLXBhbmUgbGFiZWw9XFxcIlxcdTYzQTVcXHU1M0UzXFx1OEJCRVxcdTdGNkVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHNzby1hcHAtaW50ZXJmYWNlLWxpc3QtY29tcCByZWY9XFxcInN1YkFwcEludGVyZmFjZUxpc3RDb21wXFxcIj48L3Nzby1hcHAtaW50ZXJmYWNlLWxpc3QtY29tcD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC90YWItcGFuZT5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8L3RhYnM+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLW91dGVyLXdyYXBcXFwiIHN0eWxlPVxcXCJtYXJnaW4tcmlnaHQ6IDEwcHg7bWFyZ2luLWJvdHRvbTogMTBweFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImJ1dHRvbi1pbm5lci13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxidXR0b24tZ3JvdXA+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uIHR5cGU9XFxcInByaW1hcnlcXFwiIHYtaWY9XFxcInN0YXR1cyE9J3ZpZXcnXFxcIiBAY2xpY2s9XFxcInNhdmVTdWJTeXN0ZW1TZXR0aW5nKClcXFwiIGljb249XFxcIm1kLWNoZWNrbWFya1xcXCI+XFx1NEZERFxcdTVCNThcXHU1QjUwXFx1N0NGQlxcdTdFREZcXHU4QkJFXFx1N0Y2RTwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGktYnV0dG9uIHYtaWY9XFxcInN0YXR1cyE9J3ZpZXcnXFxcIiBAY2xpY2s9XFxcImhhbmRsZUNsb3NlKClcXFwiIGljb249XFxcIm1kLWNsb3NlXFxcIj5cXHU1MTczXFx1OTVFRDwvaS1idXR0b24+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2J1dHRvbi1ncm91cD5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDxkaXYgY2xhc3M9XFxcImFwcHMtbWFuYWdlci1vdXRlci13cmFwXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJhcHBzLW91dGVyLXdyYXBcXFwiIHJlZj1cXFwiYXBwc091dGVyV3JhcFxcXCIgdi1pZj1cXFwic3RhdHVzIT0nYWRkJ1xcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxkaXYgIHYtZm9yPVxcXCJhcHAgaW4gYXBwTGlzdFxcXCIgY2xhc3M9XFxcImFwcC1vdXRlci13cmFwIGFwcC1vdXRlci13cmFwLXN1Yi1zeXN0ZW1cXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwidGl0bGVcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxzcGFuPnt7YXBwLmFwcE5hbWV9fTwvc3Bhbj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiY29udGVudFxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwibWFpbkltZ1xcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpbWcgOnNyYz1cXFwiYnVpbGRMb2dvVXJsKGFwcClcXFwiIC8+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYnV0dG9uLXdyYXBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJidXR0b24gc2V0dGluZy1idXR0b25cXFwiIEBjbGljaz1cXFwic2V0dGluZ0FwcChhcHApXFxcIj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFxcdThCQkVcXHU3RjZFXFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IGNsYXNzPVxcXCJidXR0b24gcmVtb3ZlLWJ1dHRvblxcXCIgQGNsaWNrPVxcXCJyZW1vdmVBcHAoYXBwKVxcXCI+XFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBcXHU2Q0U4XFx1OTUwMFxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYXBwLW91dGVyLXdyYXAgYXBwLW91dGVyLXdyYXAtc3ViLXN5c3RlbSBuZXctc3lzdGVtLW91dGVyLXdyYXBcXFwiPlxcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzcz1cXFwiYWRkLXN5c3RlbS1idXR0b25cXFwiIEBjbGljaz1cXFwiYWRkSW50ZWdyYXRlZFN5c3RlbSgpXFxcIiBzdHlsZT1cXFwibWFyZ2luLXRvcDogNjBweFxcXCI+XFx1NjVCMFxcdTU4OUU8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+XFxuICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiB2LWlmPVxcXCJzdGF0dXM9PSdhZGQnXFxcIj5cXHU4QkY3XFx1NTE0OFxcdTRGRERcXHU1QjU4XFx1NEUzQlxcdTdDRkJcXHU3RURGLFxcdTUxOERcXHU4QkJFXFx1N0Y2RVxcdTUxNzZcXHU0RTJEXFx1NzY4NFxcdTVCNTBcXHU3Q0ZCXFx1N0VERiE8L2Rpdj5cXG4gICAgICAgICAgICAgICAgICAgIDwvZGl2PlxcbiAgICAgICAgICAgICAgICAgPC9kaXY+XCJcbn0pOyJdfQ==
