using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace YueDong.Models
{
    public class BaseResult
    {
        public string ResultCode { get; set; }

        public string ResultMessage { get; set; }

        public dynamic ResultData { get; set; }
    }
}