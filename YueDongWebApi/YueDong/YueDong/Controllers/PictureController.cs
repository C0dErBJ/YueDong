using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Data.DAL;
using Data.Model;
using YueDong.Models;
using YueDong.Models.Picture;
using YueDong.Models.User;

namespace YueDong.Controllers
{
    public class PictureController : BaseController
    {
        [HttpPost]
        [Route("api/Picture")]
        public IHttpActionResult UploadPicture([FromBody] PictureModel pic)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (pic.FileData.Length == 0)
            {
                result.ResultMessage = "图片不能为空";
                return Ok(result);
            }
            if (pic.UserId == Guid.NewGuid())
            {
                result.ResultMessage = "用户Id不能为空";
                return Ok(result);
            }
            CommonPicture picture = new CommonPicture();
            picture.Id = Guid.NewGuid();
            picture.CreateTime = DateTime.Now;
            picture.FileData = Convert.FromBase64CharArray(pic.FileData.ToCharArray(), 0, pic.FileData.Length);
            picture.Status = 1;
            picture.UserId = pic.UserId;
            var old=new CommonPictureDAL().GetSingleByUserId(pic.UserId);
            if (old != null)
            {
                old.FileData = picture.FileData;
                var update=new CommonPictureDAL().Update(old);
                if (update)
                {
                    result.ResultData = picture;
                    result.ResultMessage = "Success";
                    result.ResultCode = "0";

                }
                else
                {
                    result.ResultMessage = "上传失败";
                }
                return Ok(result);
            }
            var insert = new CommonPictureDAL().Insert(picture);
            if (insert)
            {
                result.ResultData = picture;
                result.ResultMessage = "Success";
                result.ResultCode = "0";

            }
            else
            {
                result.ResultMessage = "上传失败";
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/Picture/{Id:guid}")]
        public IHttpActionResult DownloadPicture([FromUri] Guid Id)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var pic = new CommonPictureDAL().GetSingleByUserId(Id);
            if (pic != null)
            {
                result.ResultData = pic;
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            return Ok(result);
        }
    }
}
