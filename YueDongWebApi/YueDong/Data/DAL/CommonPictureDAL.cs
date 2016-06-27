using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data.Model;

namespace Data.DAL
{
    public class CommonPictureDAL
    {
        public bool Insert(CommonPicture model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    context.CommonPictures.Add(model);

                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }

        public CommonPicture GetSingleById(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonPictures.Where(a => a.Status > 0 && a.Id == Id);
                    if (result.Any())
                    {
                        return result.FirstOrDefault();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        public CommonPicture GetSingleByUserId(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonPictures.Where(a => a.Status > 0 && a.UserId.Value == Id);
                    if (result.Any())
                    {
                        return result.FirstOrDefault();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        public List<CommonPicture> GetList()
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonPictures.Where(a => a.Status > 0);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<CommonPicture>();
        }

        public bool Update(CommonPicture model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonPictures.FirstOrDefault(a => a.Id == model.Id);
                    if (result != null)
                    {
                        result.CreateTime = model.CreateTime;
                        result.FileData = model.FileData;
                        result.UserId = model.UserId;
                    }
                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }
    }
}
