using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using Data.DAL;
using Data.Model;
using Microsoft.Ajax.Utilities;
using YueDong.Common;
using YueDong.Models;

namespace YueDong.Controllers
{
    public class StadiumController : BaseController
    {
        [HttpGet]
        [Route("api/Stadium")]
        public IHttpActionResult NearByStadium([FromUri] double lng, [FromUri] double lat, [FromUri] double distance, [FromUri] string keyword)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var sta = new StadiumDAL().GetList();
            List<Stadium> nearby = new List<Stadium>();
            if (sta.Any())
            {
                nearby = sta.Where(a => Distance.GetDistance(a.Latitude, a.Longitude, lat, lng) <= distance).ToList();
            }
            if (!keyword.IsNullOrWhiteSpace())
            {
                nearby = nearby.Where(a => a.Name.Contains(keyword) || a.Address.Contains(keyword)).ToList();
            }
            if (nearby.Any())
            {
                result.ResultData = nearby.Select(a => new
                {
                    Id = a.Id,
                    CreateTime = a.CreateTime,
                    UpdateTime = a.UpdateTime,
                    OperationUser = a.OperationUser,
                    Status = a.Status,
                    Version = a.Version,
                    Name = a.Name,
                    Address = a.Address,
                    OpenStartTime = a.OpenStartTime.HasValue?a.OpenStartTime.Value.ToString("HH:mm"):"",
                    OpenEndTime = a.OpenEndTime.HasValue ? a.OpenEndTime.Value.ToString("HH:mm") : "",
                    Price = a.Price,
                    Phone = a.Phone,
                    Longitude = a.Longitude,
                    Latitude = a.Latitude,
                });
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            else
            {
                result.ResultMessage = "未找到数据";
            }
            return Ok(result);
        }
        [HttpGet]
        [Route("api/Stadium/{key}")]
        public IHttpActionResult KeyWordNearByStadium([FromUri] string key)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            List<Stadium> nearby = new StadiumDAL().GetListByKeyWord(key);

            if (nearby.Any())
            {
                result.ResultData = nearby;
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            else
            {
                result.ResultMessage = "未找到数据";
            }
            return Ok(result);
        }

    }
}
