package javangarch.mvc.gn;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projectbase.domain.ICommonBD;
import projectbase.mvc.Auth;
import projectbase.mvc.AutoMapperProfile;
import projectbase.mvc.Transaction;
import projectbase.mvc.WebConfigurationManager;
import projectbase.mvc.result.ActionResult;
import projectbase.mvc.result.RichClientJsonResult;
import projectbase.utils.HqlCriterion;
import projectbase.utils.Res;
import projectbase.utils.Util;

import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.CorpContact;
import javangarch.mvc.shared.AppBaseController;

    @Auth
    public class CorpController  extends  AppBaseController
    {
        @Resource public ICommonBD<Corp> CorpBD;

        public ActionResult Edit()
        {
            Corp corp = GetLoginCorp();
            CorpEditVM m = new CorpEditVM();

            Map(corp,m.getInput());
			Type listType = new TypeToken<List<CorpContactEditVM>>(){}.getType();
            m.getInput().setContactList(MapCollection(corp.getContacts(), listType));
            return ForView(m);
        }

        @RequestMapping(method=RequestMethod.POST)
        @Transaction
        public ActionResult Save(@Valid CorpEditVM editvm)
        {
         	Save(editvm.getInput());
            SetViewMessage(Message_SaveSuccessfully);
            return ClientShowMessage();
        }

        private void Save(CorpEditVM.EditInput input)
        {
            Corp corp;
            if (input.getId() == 0)
            {
                corp = new Corp();
            }
            else
            {
                corp = CorpBD.Get(input.getId());
            }
			corp.setName(input.getName());
			corp.setPhone(input.getPhone());
			
			List<CorpContact> cl=new ArrayList<CorpContact>();
			for(CorpContactEditVM i :input.getContactList()){
				cl.add(new CorpContact(i.getPhone(),i.getName(),i.getPosition()));
			}
			corp.setContacts(cl);
            CorpBD.Save(corp);
        }
}

