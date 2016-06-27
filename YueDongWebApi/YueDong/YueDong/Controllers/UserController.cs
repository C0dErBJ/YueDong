using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Web.Http;
using Data.DAL;
using Data.Model;
using YueDong.Models;
using YueDong.Models.User;

namespace YueDong.Controllers
{
    public class UserController : BaseController
    {

        [HttpPost]
        [Route("api/User/Login")]
        public IHttpActionResult Login([FromBody] UserModel User)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var user = new CommonUserDAL().GetSingleByName(User.username);
            if (user == null)
            {
                result.ResultMessage = "用户未注册";
                return Ok(result);
            }
            user = new CommonUserDAL().GetSingleByNameAndPassword(User.username, User.password);
            if (user != null)
            {
                result.ResultData = user;
                result.ResultMessage = "Success";
                result.ResultCode = "0";

            }
            else
            {
                result.ResultMessage = "用户名或密码错误";
            }
            return Ok(result);
        }

        [HttpPost]
        [Route("api/User/Regist")]
        public IHttpActionResult Regist([FromBody] UserModel User)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (string.IsNullOrEmpty(User.username))
            {
                result.ResultMessage = "用户名不能为空";
                return Ok(result);
            }
            if (string.IsNullOrEmpty(User.password))
            {
                result.ResultMessage = "密码不能为空";
                return Ok(result);
            }
            if (string.IsNullOrEmpty(User.nickname))
            {
                result.ResultMessage = "昵称不能为空";
                return Ok(result);
            }
            var olduser = new CommonUserDAL().GetSingleByName(User.username);
            if (olduser != null)
            {
                result.ResultMessage = "不能重复注册";
                return Ok(result);
            }
            CommonUser user = new CommonUser();
            user.Id = Guid.NewGuid();
            user.UserName = User.username;
            user.Password = User.password;
            user.Status = 1;
            user.Version = 1;
            user.CreateTime = DateTime.Now;
            user.Gender = "男";
            user.NickName = User.nickname;
            var results = new CommonUserDAL().Insert(user);
            if (results)
            {
                result.ResultData = user;
                result.ResultMessage = "Success";
                result.ResultCode = "0";

            }
            else
            {
                result.ResultMessage = "注册失败";
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/User/UserDetail/{Id:guid}")]
        public IHttpActionResult UserDetail([FromUri] Guid Id)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var user = new CommonUserDAL().GetSingleById(Id);
            if (user != null)
            {
                result.ResultData = user;
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            else
            {
                result.ResultMessage = "未找到用户";
            }
            return Ok(result);
        }

        [HttpGet]
        [Route("api/User")]
        public IHttpActionResult Users()
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var user = new CommonUserDAL().GetList();
            if (user != null)
            {
                result.ResultData = user;
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            else
            {
                result.ResultMessage = "未找到用户";
            }
            return Ok(result);
        }

        [HttpPost]
        [Route("api/User/UpdateUserDetail")]
        public IHttpActionResult UpdateUserDetail([FromBody] CommonUser User)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (User == null)
            {
                result.ResultMessage = "用户不能为空";
                return Ok(result);
            }
            if (User.Id == Guid.Empty)
            {
                result.ResultMessage = "UID不能为空";
                return Ok(result);
            }

            var olduser = new CommonUserDAL().GetSingleById(User.Id);
            if (olduser == null)
            {
                result.ResultMessage = "未找到用户";
                return Ok(result);
            }
            CommonUser newUser = olduser;
            if (User.Age != null)
            {
                newUser.Age = User.Age;
            }
            if (User.Gender != null)
            {
                newUser.Gender = User.Gender;
            }
            if (User.Hobby != null)
            {
                newUser.Hobby = User.Hobby;
            }
            if (User.NickName != null)
            {
                newUser.NickName = User.NickName;
            }
            newUser.UpdateTime = DateTime.Now;
            var results = new CommonUserDAL().Update(newUser);
            if (results)
            {
                result.ResultData = newUser;
                result.ResultMessage = "Success";
                result.ResultCode = "0";

            }
            else
            {
                result.ResultMessage = "修改成功";
            }
            return Ok(result);
        }

        [HttpPost]
        [Route("api/User/InsertFreeTime")]
        public IHttpActionResult InsertFreeTime([FromBody] FreeTime time)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (time.UserId == null)
            {
                result.ResultMessage = "用户不能为空";
                return Ok(result);
            }
            if (time.Week == null)
            {
                result.ResultMessage = "星期不能为空";
                return Ok(result);
            }
            if (time.StartTime == null)
            {
                result.ResultMessage = "开始时间不能为空";
                return Ok(result);
            }
            if (time.EndTime == null)
            {
                result.ResultMessage = "结束时间不能为空";
                return Ok(result);
            }
            time.Id = Guid.NewGuid();
            time.Status = 1;
            time.Version = 1;
            var inserttime = new FreeTimeDAL().Insert(time);
            if (inserttime)
            {
                result.ResultData = time;
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            else
            {
                result.ResultMessage = "修改成功";
            }
            return Ok(result);
        }
        [HttpPost]
        [Route("api/User/RemoveFreeTime")]
        public IHttpActionResult RemoveFreeTime([FromBody] FreeTime time)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (time.UserId == null)
            {
                result.ResultMessage = "用户不能为空";
                return Ok(result);
            }
            if (time.Week == null)
            {
                result.ResultMessage = "星期不能为空";
                return Ok(result);
            }
            if (time.StartTime == null)
            {
                result.ResultMessage = "开始时间不能为空";
                return Ok(result);
            }
            if (time.EndTime == null)
            {
                result.ResultMessage = "结束时间不能为空";
                return Ok(result);
            }
            time.Id = Guid.NewGuid();
            time.Status = 1;
            time.Version = 1;
            var inserttime = new FreeTimeDAL().DeleteByUserIdAndWeekTime(time);
            if (inserttime)
            {
                result.ResultData = time;
                result.ResultMessage = "Success";
                result.ResultCode = "0";
            }
            else
            {
                result.ResultMessage = "修改成功";
            }
            return Ok(result);
        }

        [HttpPost]
        [Route("api/User/UpdateFreeTime")]
        public IHttpActionResult UpdateFreeTime([FromBody] FreeTimeModel[] time)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            foreach (var ti in time)
            {
                if (ti.Status == 0)
                {
                    new FreeTimeDAL().DeleteByUserIdAndWeekTime(new FreeTime()
                    {
                        UserId = ti.UserId,
                        StartTime = ti.StartTime,
                        EndTime = ti.EndTime,
                        Week = ti.Week
                    });
                }
                else
                {
                    new FreeTimeDAL().Insert(new FreeTime()
                    {
                        Id = Guid.NewGuid(),
                        Status = 1,
                        Version = 1,
                        UserId = ti.UserId,
                        StartTime = ti.StartTime,
                        EndTime = ti.EndTime,
                        Week = ti.Week
                    });
                }
            }
            result.ResultCode = "0";
            result.ResultMessage = "Success";
            return Ok(result);
        }

        [HttpGet]
        [Route("api/User/FreeTime/{Id:guid}")]
        public IHttpActionResult FreeTime([FromUri] Guid Id)
        {
            var result = new BaseResult();
            result.ResultCode = "0";
            result.ResultMessage = "Success";
            var results = new FreeTimeDAL().GetListByUserId(Id);
            result.ResultData = results.Select(a => new
            {
                Week = a.Week.Value,
                StartTime = a.StartTime.Value.ToString("HH:mm"),
                EndTime = a.EndTime.Value.ToString("HH:mm"),
            });
            return Ok(result);
        }

        [HttpGet]
        [Route("api/User/Sport/{Id:guid}")]
        public IHttpActionResult Sport([FromUri] Guid Id)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            var sport = new SportRecordDAL().GetListByUserId(Id);
            SportModel model = new SportModel();
            model.Count = sport.Count;
            model.LastTime = sport.Sum(a => a.SportLastTime.Value);
            result.ResultCode = "0";
            result.ResultMessage = "Success";
            result.ResultData = model;
            return Ok(result);
        }

        [HttpPost]
        [Route("api/User/Sport")]
        public IHttpActionResult InsertSport([FromBody]SportInsertModel model)
        {
            var result = new BaseResult();
            result.ResultCode = "1";
            result.ResultMessage = "Error";
            if (model.UserId == Guid.Empty)
            {
                result.ResultMessage = "用户ID不能为空";
                return Ok(result);
            }
            SportRecord record = new SportRecord();
            record.Id = Guid.NewGuid();
            record.SportLastTime = model.LastTime;
            record.CreateTime = DateTime.Now;
            record.UpdateTime = DateTime.Now;
            record.UserId = model.UserId;
            record.Version = 1;
            record.Status = 1;
            record.OperationUser = model.UserId.ToString();
            var re = new SportRecordDAL().Insert(record);
            if (re)
            {
                result.ResultCode = "0";
                result.ResultMessage = "上传成功";
            }
            return Ok(result);
        }

    }
}