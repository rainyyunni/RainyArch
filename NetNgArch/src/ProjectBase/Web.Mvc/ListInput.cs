using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Linq.Expressions;

using ProjectBase.Utils;

namespace ProjectBase.Web.Mvc
{

    public class ListInput
    {
        public ListInput()
        {
            SelectedValues=new int[]{};
        }
        public ListInput(int pageSize)
        {
            SelectedValues = new int[] { };
            Pager = new Pager(pageSize);
        }
        public int[] SelectedValues { get; set; }
        public string OrderExpression { get; set; }
        public Pager Pager { get; set; }
    }

}
