using System;
using System.Collections.Generic;
using System.Web.Mvc;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Web.Mvc.Shared;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using System.Linq.Expressions;


namespace NetNgArch.Web.Mvc.GN
{
    [Auth]
    public class CorpController : AppBaseController
    {
        public ICommonBD<Corp> CorpBD { get; set; }


        public ActionResult Edit()
        {
            var m = new CorpEditVM
            {
                Input = Map<Corp, CorpEditVM.EditInput>(GetLoginCorp())
            };
            return ForView(m);
        }


        [HttpPost]
        [Transaction]
        public ActionResult Save(CorpEditVM editvm)
        {
            if (ModelState.IsValid)
            {
                Save(editvm.Input);
                SetViewMessage(Message_SaveSuccessfully);
                if (editvm.Input.Id == 0) return RcJson();
            }
            return ClientShowMessage();
        }

        private void Save(CorpEditVM.EditInput input)
        {
            Corp corp;
            if (input.Id == 0)
            {
                corp = new Corp();
            }
            else
            {
                corp = CorpBD.Get(input.Id);
            }

            corp.Name = input.Name;
            corp.Phone = input.Phone;


            CorpBD.Save(corp);
        }

    }



}
