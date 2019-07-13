package com.jb4dc.sso.dao.menu;

import com.jb4dc.base.dbaccess.dao.BaseMapper;
import com.jb4dc.sso.dbentities.menu.MenuEntity;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/13
 * To change this template use File | Settings | File Templates.
 */
public interface MenuMapper  extends BaseMapper<MenuEntity> {
    MenuEntity selectLessThanRecord(String id);

    MenuEntity selectGreaterThanRecord(String id);
}
