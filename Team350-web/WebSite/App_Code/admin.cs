using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Data.SqlClient;
using System.Data;
using System.Web.Script.Serialization;
using System.Globalization;

using System.IO;
using System.Collections;
using System.Web.Services.Protocols;
using System.ComponentModel;

/// <summary>
/// Summary description for admin
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]
public class admin : System.Web.Services.WebService {

    string conString = "Data Source=sql5107.site4now.net;Initial Catalog=db_a8418b_team360;Persist Security Info=True;User ID=db_a8418b_team360_admin;Password=Mihir2012";

                                                         
     public admin() { }

   


    [WebMethod]
    public void verifyLoginUser(string username, string password)
    {


        SqlConnection con = new SqlConnection(conString);

        SqlCommand cmd = new SqlCommand("SELECT * FROM [db_a8418b_team360].[dbo].[USER_MASTER] where Username=@Username	and [Password]=@Password", con);
        cmd.Parameters.AddWithValue("@Username", username);
        cmd.Parameters.AddWithValue("@Password", password);


        List<Dictionary<string, object>> list = new List<Dictionary<string, object>>();

        try
        {
            con.Open();

            DataTable dt = new DataTable();

            new SqlDataAdapter(cmd).Fill(dt);


            if (dt.Rows.Count > 0)
            {
                Dictionary<string, object> row;

                foreach (DataRow dr in dt.Rows)
                {
                    row = new Dictionary<string, object>();

                    foreach (DataColumn col in dt.Columns)
                    {
                        row.Add(col.ColumnName, dr[col]);
                    }
                    list.Add(row);
                }
            }

        }
        catch (Exception e) { }
        finally
        {
            con.Close();
        }

        Context.Response.Write(new JavaScriptSerializer().Serialize(list));

    }



    [WebMethod]
    public void verifyLoginAdmin(string username, string password)
    {


        SqlConnection con = new SqlConnection(conString);

        SqlCommand cmd = new SqlCommand("SELECT * FROM [db_a8418b_team360].[dbo].[ADMIN_MASTER] where [Username] = @Username AND[Password] = @Password", con);
        cmd.Parameters.AddWithValue("@Username", username);
        cmd.Parameters.AddWithValue("@Password", password);


        List<Dictionary<string, object>> list = new List<Dictionary<string, object>>();

        try
        {
            con.Open();

            DataTable dt = new DataTable();

            new SqlDataAdapter(cmd).Fill(dt);


            if (dt.Rows.Count > 0)
            {
                Dictionary<string, object> row;

                foreach (DataRow dr in dt.Rows)
                {
                    row = new Dictionary<string, object>();

                    foreach (DataColumn col in dt.Columns)
                    {
                        row.Add(col.ColumnName, dr[col]);
                    }
                    list.Add(row);
                }
            }

        }
        catch (Exception e) { }
        finally
        {
            con.Close();
        }

        Context.Response.Write(new JavaScriptSerializer().Serialize(list));

    }




    [WebMethod]
    public void addEmployee(string Admin_Id, string Admin_Name, string User_Name, string Phone, string Email,string Username, string Password, string Remark, string Remark_)
    {
        string arr;
        SqlConnection con = new SqlConnection(conString);
        SqlTransaction transaction;
        con.Open();
        transaction = con.BeginTransaction();
        try
        {


            SqlCommand cmd = new SqlCommand("INSERT INTO [dbo].[USER_MASTER] ([Admin_Id] ,[Admin_Name],[User_Name] ,[Phone] ,[Email],[Username],[Password],[Remark],[Remark_] ,[Added_On]) VALUES" +
                " (@Admin_Id, @Admin_Name, @User_Name, @Phone, @Email, @Username, @Password, @Remark, @Remark_, @Added_On)", con, transaction);

            cmd.Parameters.AddWithValue("@Admin_Id", Admin_Id);
            cmd.Parameters.AddWithValue("@Admin_Name", Admin_Name);

            cmd.Parameters.AddWithValue("@User_Name", User_Name);
            cmd.Parameters.AddWithValue("@Phone", Phone);
            cmd.Parameters.AddWithValue("@Email", Email);
            cmd.Parameters.AddWithValue("@Username", Username);
            cmd.Parameters.AddWithValue("@Password", Password);
            cmd.Parameters.AddWithValue("@Remark", Remark);
            cmd.Parameters.AddWithValue("@Remark_", Remark_);
            TimeZoneInfo INDIAN_ZONE = TimeZoneInfo.FindSystemTimeZoneById("India Standard Time");
            DateTime indianTime = TimeZoneInfo.ConvertTimeFromUtc(DateTime.UtcNow, INDIAN_ZONE);
            

            cmd.Parameters.AddWithValue("@Added_On", indianTime.ToString("dd-MM-yyyy hh:mm:ss tt"));
            cmd.ExecuteNonQuery();

            transaction.Commit();

            arr = "Success";
            //          GKNotify.SendToOne("Remarks", "Remarks - " + Remarks, ID);

        }
        catch (SqlException sq)
        {
            transaction.Rollback();
            arr = "Unsuccessfull  SQL";
        }
        catch (Exception e)
        {
            arr = "Unsuccessfull";
        }
        finally
        {
            con.Close();
        }

        Dictionary<string, object> result = new Dictionary<string, object>();

        result.Add("POST_REQUEST_STATUS", arr);

        Context.Response.Write(new JavaScriptSerializer().Serialize(result));

    }




    [WebMethod]
    public void getEmployee(string admin_id)
    {


        SqlConnection con = new SqlConnection(conString);

        SqlCommand cmd = new SqlCommand("SELECT * FROM [dbo].[USER_MASTER] WHERE [Admin_Id]=@Admin_Id ORDER BY [User_Id] DESC", con);
        cmd.Parameters.AddWithValue("@Admin_Id", admin_id);
   


        List<Dictionary<string, object>> list = new List<Dictionary<string, object>>();

        try
        {
            con.Open();

            DataTable dt = new DataTable();

            new SqlDataAdapter(cmd).Fill(dt);


            if (dt.Rows.Count > 0)
            {
                Dictionary<string, object> row;

                foreach (DataRow dr in dt.Rows)
                {
                    row = new Dictionary<string, object>();

                    foreach (DataColumn col in dt.Columns)
                    {
                        row.Add(col.ColumnName, dr[col]);
                    }
                    list.Add(row);
                }
            }

        }
        catch (Exception e) { }
        finally
        {
            con.Close();
        }

        Context.Response.Write(new JavaScriptSerializer().Serialize(list));

    }






    [WebMethod]
    public void markLocation(string User_Id,string Admin_Id, string Longitude, string Latitude, string Remark, string Remark_)
    {
        string arr;
        SqlConnection con = new SqlConnection(conString);
        SqlTransaction transaction;
        con.Open();
        transaction = con.BeginTransaction();
        try
        {


            SqlCommand cmd = new SqlCommand("INSERT INTO [dbo].[Locations] ([User_Id] ,[Admin_Id],[Longitude] ,[Latitude],[Added_On],[Added_On_Date],[Remark],[Remark_]) VALUES( " +
                "@User_Id, @Admin_Id, @Longitude,@Latitude,@Added_On,@Added_On_Date,@Remark, @Remark_)", con, transaction);
            cmd.Parameters.AddWithValue("@User_Id", User_Id);
            cmd.Parameters.AddWithValue("@Admin_Id", Admin_Id);
      
            cmd.Parameters.AddWithValue("@Longitude", Longitude);
            cmd.Parameters.AddWithValue("@Latitude", Latitude);

            TimeZoneInfo INDIAN_ZONE = TimeZoneInfo.FindSystemTimeZoneById("India Standard Time");
            DateTime indianTime = TimeZoneInfo.ConvertTimeFromUtc(DateTime.UtcNow, INDIAN_ZONE);
            cmd.Parameters.AddWithValue("@Added_On", indianTime.ToString("dd-MM-yyyy hh:mm:ss tt"));

            cmd.Parameters.AddWithValue("@Added_On_Date", indianTime.ToString("dd-MM-yyyy"));

            cmd.Parameters.AddWithValue("@Remark", Remark);
            cmd.Parameters.AddWithValue("@Remark_", Remark_);
            cmd.ExecuteNonQuery();

            transaction.Commit();

            arr = "Success";
            //          GKNotify.SendToOne("Remarks", "Remarks - " + Remarks, ID);

        }
        catch (SqlException sq)
        {
            transaction.Rollback();
            arr = "Unsuccessfull  SQL";
        }
        catch (Exception e)
        {
            arr = "Unsuccessfull";
        }
        finally
        {
            con.Close();
        }

        Dictionary<string, object> result = new Dictionary<string, object>();

        result.Add("POST_REQUEST_STATUS", arr);

        Context.Response.Write(new JavaScriptSerializer().Serialize(result));

    }






    [WebMethod]
    public void getLocationDate(string user_id)
    {


        SqlConnection con = new SqlConnection(conString);

        SqlCommand cmd = new SqlCommand("SELECT * FROM [db_a8418b_team360].[dbo].[Locations] WHERE [User_Id]=@user_id  ORDER BY [Location_Id] DESC", con);
        cmd.Parameters.AddWithValue("@user_id", user_id);



        List<Dictionary<string, object>> list = new List<Dictionary<string, object>>();

        try
        {
            con.Open();

            DataTable dt = new DataTable();

            new SqlDataAdapter(cmd).Fill(dt);


            if (dt.Rows.Count > 0)
            {
                Dictionary<string, object> row;

                foreach (DataRow dr in dt.Rows)
                {
                    row = new Dictionary<string, object>();

                    foreach (DataColumn col in dt.Columns)
                    {
                        row.Add(col.ColumnName, dr[col]);
                    }
                    list.Add(row);
                }
            }

        }
        catch (Exception e) { }
        finally
        {
            con.Close();
        }

        Context.Response.Write(new JavaScriptSerializer().Serialize(list));

    }



    [WebMethod]
    public void getLocationDateWise(string user_id,string date_)
    {


        SqlConnection con = new SqlConnection(conString);

        SqlCommand cmd = new SqlCommand("SELECT * FROM [db_a8418b_team360].[dbo].[Locations] WHERE [User_Id]=@user_id  AND [Added_On_Date]=@date_  ORDER BY [Location_Id] DESC", con);
        cmd.Parameters.AddWithValue("@user_id", user_id);
        cmd.Parameters.AddWithValue("@date_", date_);


        List<Dictionary<string, object>> list = new List<Dictionary<string, object>>();

        try
        {
            con.Open();

            DataTable dt = new DataTable();

            new SqlDataAdapter(cmd).Fill(dt);


            if (dt.Rows.Count > 0)
            {
                Dictionary<string, object> row;

                foreach (DataRow dr in dt.Rows)
                {
                    row = new Dictionary<string, object>();

                    foreach (DataColumn col in dt.Columns)
                    {
                        row.Add(col.ColumnName, dr[col]);
                    }
                    list.Add(row);
                }
            }

        }
        catch (Exception e) { }
        finally
        {
            con.Close();
        }

        Context.Response.Write(new JavaScriptSerializer().Serialize(list));

    }





}
