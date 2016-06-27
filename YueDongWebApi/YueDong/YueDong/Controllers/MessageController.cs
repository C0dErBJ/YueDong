using System;
using System.Linq;
using System.Web.Http;
using Data.DAL;
using Data.Model;
using YueDong.Models;
using Message = YueDong.Models.Message.Message;

namespace YueDong.Controllers
{
    public class MessageController : BaseController
    {
        [HttpPost]
        [Route("api/Message/NewMessage")]
        public IHttpActionResult NewMessage([FromBody] Message message)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (message.MessageContent == null)
            {
                result.ResultMessage = "消息内容不能为空";
                return Ok(result);
            }
            Data.Model.Message newMessage = new Data.Model.Message();
            newMessage.Id = Guid.NewGuid();
            newMessage.CreateTime = DateTime.Now;
            newMessage.Status = 1;
            newMessage.Text = message.MessageContent;
            newMessage.FromWho = message.FromWho;
            newMessage.ToWho = message.ToWho;
            newMessage.MessageBatch = message.FromWho + "-" + DateTime.Now.ToString("yyyyMMddHHmmss");
            if (message.Picture != null && message.Picture.Length > 0)
            {
                CommonPicture pic = new CommonPicture();
                pic.Id = Guid.NewGuid();
                pic.CreateTime = DateTime.Now;
                pic.FileData = message.Picture;
                pic.Status = 1;
                pic.UserId = message.FromWho;
                new CommonPictureDAL().Insert(pic);
                newMessage.PicId = pic.Id;
            }
            var re = new MessageDAL().Insert(newMessage);
            if (re)
            {
                result.ResultCode = "0";
                result.ResultMessage = "Success";
                result.ResultData = new
                {
                    Batch = newMessage.MessageBatch,
                    FromWho = new CommonUserDAL().GetSingleById(newMessage.FromWho.Value).NickName,
                    ToWho = new CommonUserDAL().GetSingleById(newMessage.ToWho.Value).NickName,
                    CreateTime = newMessage.CreateTime.Value.ToString("yyyy-MM-dd HH:mm:ss"),
                    Text = newMessage.Text,
                    FromId = newMessage.FromWho,
                    ToId = newMessage.ToWho
                };
            }
            else
            {
                result.ResultMessage = "发送消息失败";
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/Message/GetMessage/{batch}")]
        public IHttpActionResult GetMessage([FromUri] string batch)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var list = new MessageDAL().GetListByBatch(batch);
            if (list.Any())
            {
                result.ResultCode = "1";
                result.ResultMessage = "Success";
                result.ResultData = list;
            }
            return Ok(result);
        }

        [HttpPost]
        [Route("api/Message/Replay")]
        public IHttpActionResult Replay([FromBody] Message message)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (message.MessageContent == null)
            {
                result.ResultMessage = "消息内容不能为空";
                return Ok(result);
            }
            if (message.Batch == null)
            {
                result.ResultMessage = "消息号不能为空";
                return Ok(result);
            }
            Data.Model.Message newMessage = new Data.Model.Message();
            newMessage.Id = Guid.NewGuid();
            newMessage.CreateTime = DateTime.Now;
            newMessage.Status = 1;
            newMessage.Text = message.MessageContent;
            newMessage.FromWho = message.FromWho;
            newMessage.ToWho = message.ToWho;
            newMessage.MessageBatch = message.Batch;
            if (message.Picture != null && message.Picture.Length > 0)
            {
                CommonPicture pic = new CommonPicture();
                pic.Id = Guid.NewGuid();
                pic.CreateTime = DateTime.Now;
                pic.FileData = message.Picture;
                pic.Status = 1;
                pic.UserId = message.FromWho;
                new CommonPictureDAL().Insert(pic);
                newMessage.PicId = pic.Id;
            }
            var re = new MessageDAL().Insert(newMessage);
            if (re)
            {
                result.ResultCode = "0";
                result.ResultMessage = "Success";
                result.ResultData = new
                {
                    Batch = newMessage.MessageBatch,
                    FromWho = new CommonUserDAL().GetSingleById(newMessage.FromWho.Value).NickName,
                    ToWho = new CommonUserDAL().GetSingleById(newMessage.ToWho.Value).NickName,
                    CreateTime = newMessage.CreateTime.Value.ToString("yyyy-MM-dd HH:mm:ss"),
                    Text = newMessage.Text,
                    FromId = newMessage.FromWho,
                    ToId = newMessage.ToWho
                };
            }
            else
            {
                result.ResultMessage = "发送消息失败";
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/Message/GetMessage/{UserId:guid}")]
        public IHttpActionResult GetMessage([FromUri] Guid UserId)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var list = new MessageDAL().GetListByUserId(UserId).OrderBy(a => a.CreateTime).ThenBy(a => a.MessageBatch);
            if (list.Any())
            {
                result.ResultCode = "0";
                result.ResultMessage = "Success";
                result.ResultData = list.Select(a => new
                {
                    Batch = a.MessageBatch,
                    FromWho = new CommonUserDAL().GetSingleById(a.FromWho.Value).NickName,
                    ToWho = new CommonUserDAL().GetSingleById(a.ToWho.Value).NickName,
                    CreateTime = a.CreateTime.Value.ToString("yyyy-MM-dd"),
                    Text = a.Text
                });
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/Message/GetLatestMessage/{UserId:guid}")]
        public IHttpActionResult GetLatestMessage([FromUri] Guid UserId)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var list = new MessageDAL().GetListByUserId(UserId).GroupBy(a => a.MessageBatch).SelectMany(a => a.OrderBy(b => b.CreateTime)).Take(1);
            if (list.Any())
            {
                result.ResultCode = "0";
                result.ResultMessage = "Success";
                result.ResultData = list.Select(a => new
                {
                    Batch = a.MessageBatch,
                    FromWho = new CommonUserDAL().GetSingleById(a.FromWho.Value).NickName,
                    ToWho = new CommonUserDAL().GetSingleById(a.ToWho.Value).NickName,
                    CreateTime = a.CreateTime.Value.ToString("yyyy-MM-dd"),
                    Text = a.Text,
                    FromId = a.FromWho,
                    ToId = a.ToWho
                }).ToList();
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/Message/GetTargetMessage/{UserId1:guid}/{UserId2:guid}")]
        public IHttpActionResult GetTargetMessage([FromUri] Guid UserId1, [FromUri] Guid UserId2)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var list = new MessageDAL().GetList().Where(a => a.ToWho == UserId1 || a.ToWho == UserId2 || a.FromWho == UserId1 || a.FromWho == UserId2).OrderBy(a => a.CreateTime);
            if (list.Any())
            {
                result.ResultCode = "0";
                result.ResultMessage = "Success";
                result.ResultData = list.Select(a => new
                {
                    Batch = a.MessageBatch,
                    FromWho = new CommonUserDAL().GetSingleById(a.FromWho.Value).NickName,
                    ToWho = new CommonUserDAL().GetSingleById(a.ToWho.Value).NickName,
                    CreateTime = a.CreateTime.Value.ToString("yyyy-MM-dd HH:mm:ss"),
                    Text = a.Text,
                    FromId = a.FromWho,
                    ToId = a.ToWho
                });
            }
            return Ok(result);
        }

    }
}