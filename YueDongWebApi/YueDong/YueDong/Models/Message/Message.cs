using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace YueDong.Models.Message
{
    public class Message
    {
        public string MessageContent;
        public Guid ToWho;
        public Guid FromWho;
        public byte[] Picture;
        public string Batch;
    }
}