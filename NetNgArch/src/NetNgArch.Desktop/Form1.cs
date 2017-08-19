using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Desktop;
using ProjectBase.Domain;

namespace NetNgArch.Desktop
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            var UserBD= BaseApplication.WindsorContainer.Resolve<ICommonBD<User>>();
            txt1.Text = UserBD.GetOneByQuery(o => o.Code == "Admin").Name;
        }
    }
}
