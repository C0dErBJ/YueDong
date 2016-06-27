using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace YueDong.Models.User
{
    public class FreeTimeModel
    {
        public DateTime StartTime { get; set; }
        public DateTime EndTime { get; set; }
        public int Status { get; set; }
        public int Week { get; set; }
        public Guid UserId { get; set; }
    }

   
}