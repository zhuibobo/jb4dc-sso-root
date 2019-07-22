package com.jb4dc.sso.webserver.rest.systemsetting.dictionary;

import com.jb4dc.base.service.IBaseService;
import com.jb4dc.base.service.general.JB4DCSessionUtility;
import com.jb4dc.core.base.vo.JBuild4DCResponseVo;
import com.jb4dc.feb.dist.webserver.rest.base.GeneralRest;
import com.jb4dc.sso.dbentities.systemsetting.DictionaryGroupEntity;
import com.jb4dc.sso.service.systemsetting.IDictionaryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/Rest/SystemSetting/Dict/DictionaryGroup")
public class DictionaryGroupRest extends GeneralRest<DictionaryGroupEntity> {


    @Autowired
    IDictionaryGroupService dictionaryGroupService;

    @Override
    protected IBaseService<DictionaryGroupEntity> getBaseService() {
        return dictionaryGroupService;
    }

    @RequestMapping(value = "/MoveUp", method = RequestMethod.POST)
    public JBuild4DCResponseVo moveUp(String recordId) {
        //dictionaryGroupService.moveUp(recordId);
        return JBuild4DCResponseVo.opSuccess();
    }

    @RequestMapping(value = "/GetTreeData", method = RequestMethod.POST)
    public List<DictionaryGroupEntity> getTreeData() {
        //dictionaryGroupService.moveUp(recordId);
        List<DictionaryGroupEntity> dictionaryGroupEntityList=dictionaryGroupService.getALL(JB4DCSessionUtility.getSession());
        return dictionaryGroupEntityList;
    }

}
