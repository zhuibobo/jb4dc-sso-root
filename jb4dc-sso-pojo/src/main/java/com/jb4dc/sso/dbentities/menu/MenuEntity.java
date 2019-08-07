package com.jb4dc.sso.dbentities.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb4dc.base.dbaccess.anno.DBKeyField;
import com.jb4dc.base.service.po.MenuPO;

import java.util.Date;

/**
 *
 * This class was generated JBuild4DC.
 * This class corresponds to the database table :TSSO_MENU
 *
 * JBuild4DC do_not_delete_during_merge
 */
public class MenuEntity extends MenuPO {

    public MenuEntity(String menuId, String menuName, String menuSystemId, String menuText, String menuValue, String menuType, String menuOuterId, String menuOuterName, String menuCreatorId, String menuOrganId, String menuOrganName, String menuIsExpand, String menuIsSystem, String menuLeftUrl, String menuLeftUrlPara, String menuRightUrl, String menuRightUrlPara, Integer menuOrderNum, String menuParentId, String menuParentIdList, String menuTarget, Date menuCreateTime, String menuUpdater, Date menuUpdateTime, String menuUseOrganName, String menuUseOrganId, String menuUseOrganTypeName, String menuUseOrganTypeId, String menuClassName, String menuClassNameHover, String menuClassNameSelected, Integer menuChildCount, String menuDescription, String menuStatus, String menuJsExpression) {
        super(menuId, menuName, menuSystemId, menuText, menuValue, menuType, menuOuterId, menuOuterName, menuCreatorId, menuOrganId, menuOrganName, menuIsExpand, menuIsSystem, menuLeftUrl, menuLeftUrlPara, menuRightUrl, menuRightUrlPara, menuOrderNum, menuParentId, menuParentIdList, menuTarget, menuCreateTime, menuUpdater, menuUpdateTime, menuUseOrganName, menuUseOrganId, menuUseOrganTypeName, menuUseOrganTypeId, menuClassName, menuClassNameHover, menuClassNameSelected, menuChildCount, menuDescription, menuStatus, menuJsExpression);
    }

    public MenuEntity() {
    }
}