/*
 * Author: jianqing
 * Date: Aug 3, 2020
 * Description: This document is created for
 */
package com.ppr.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.setting.Setting;
import com.ppr.model.Captcha;
import com.ppr.model.DatabaseConnector;
import com.ppr.model.GCaptcha;
import com.ppr.model.TimeConverter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jianqing
 */
@WebServlet(name = "Servlet", urlPatterns =
{
    "/index", "/RegisterPackage", "/ContactMe", "/Captcha","/Write","/GetDir"
}, loadOnStartup = 1)
public class Servlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Servlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String path = request.getServletPath();

        switch (path)
        {
            case "/index":
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            case "/Captcha":
                processCaptchaGET(request, response);
                break;
            case "/Write":
                PrintWriter w = response.getWriter();
                File file = new File("xixi" + RandomUtil.randomNumbers(4) + ".txt");
                file.createNewFile();
                FileUtil.writeString("wo ai ni!", file, "UTF-8");
                w.write("Success!");
                w.flush();
                w.close();
                break;
            case "/GetDir":
                PrintWriter w2 = response.getWriter();
                w2.write(new File("/WEB-INF/.").getCanonicalPath());
                w2.flush();
                w2.close();
                break;
            default:
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String path = request.getServletPath();

        if (path.equals("/RegisterPackage"))
        {
            processRegisterPackagePOST(request, response);
        } else if (path.equals("/ContactMe"))
        {
            processContactMePOST(request, response);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }

    @Override
    public void init()
    {
        System.out.println("Hi");
        try
        {
            com.mysql.cj.jdbc.Driver driver = new com.mysql.cj.jdbc.Driver();
//            int i = Integer.parseInt("shabi!");
        } catch (NumberFormatException | SQLException ex)
        {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
//            File log = new File("./whatever.txt");
//            if(!log.exists())
//            {
//                try
//                {
//                    log.createNewFile();
//                    FileUtil.writeString(ExceptionUtil.stacktraceToString(ex), log, "UTF-8");
//                } catch (IOException ex1)
//                {
//                    Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex1);
//                }
//            }
//            
//        

        }
    }

// </editor-fold>
    private void processContactMePOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String token = request.getParameter("token");
        String captchaCode = request.getParameter("code");
        HttpSession session = request.getSession();
        if (token == null && captchaCode == null)
        {
            response.sendRedirect(request.getContextPath() + "/index");
        }

        try
        {
            if (token != null)
            {
                Setting set = new Setting("gcaptcha.setting");//read the token from setting file.
                GCaptcha gc = GCaptcha.verifyGCaptchaResult(token, set.get("secret"));
                session.setAttribute("gc", gc/* new GCaptcha(true, captchaCode, token, captchaCode, 0.5)*/);

                if (gc.isSuccess())
                {
                    request.getRequestDispatcher("/WEB-INF/ContactMe.jsp").forward(request, response);
                } else
                {
                    response.getWriter().write("抱歉，身份验证无法通过，请重试");
                }
            } else if (captchaCode != null)
            {
                request.getRequestDispatcher("/WEB-INF/ContactMe.jsp").forward(request, response);
            }

        } catch (InterruptedException ex)
        {

            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);

            response.getWriter().print("抱歉，我们目前正在出错，请稍后再来。");
        }

    }

    protected void processCaptchaGET(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        //String userPath = request.getServletPath();

        //decide the returning datatype and avoid potential security issues.
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        Captcha captcha = Captcha.generateCaptcha(100/*width*/, 35 /*height*/);

        try (OutputStream os = response.getOutputStream())
        {
            captcha.setExpireTime(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(5));//set expire 5 mins later.
            session.setAttribute("captcha", captcha);//user may only hold one captcha at a time. The captcha will be used app wise.
            //no forward required.
            //write the image onto the page
            ImageIO.write(captcha.getImage(), "jpg", os);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void processRegisterPackagePOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String code = request.getParameter("code");
        String phone = request.getParameter("phone");
        HttpSession session = request.getSession();

        //check for empty request
        if (code == null || phone == null || code.isEmpty() || phone.isEmpty())
        {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else
        {
            try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
            {
                String guoguo = dbConn.getGuoguoByCode(code);

                if (guoguo != null)
                {
                    //update with the time
                    dbConn.updatePackageLocaltimePhoneByCode(TimeConverter.formatDateTime(LocalDateTime.now(ZoneId.of("UTC+8"))), phone, code);
                }

                session.setAttribute("guoguo", guoguo);

                request.getRequestDispatcher("/WEB-INF/RegisterPackage.jsp").forward(request, response);

            } catch (Exception ex)
            {
                ex.printStackTrace();

                try (PrintWriter pr = response.getWriter())
                {
                    pr.println("We get an error.");
                    ex.printStackTrace(pr);
                }

                //response.sendRedirect(request.getContextPath() + "/index");
            }

        }

    }

}
