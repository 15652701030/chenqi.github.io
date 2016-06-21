package com.chenqi.random;


import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈琪
 * @Date: 2016/6/16 22:30
 * To change this template use File | Settings | File Templates.
 */
public class RandomCodeDemo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 25;
        int heigth = 120;
        // 得到一个图像缓冲区 BufferedImage
        BufferedImage bufferedImage = new BufferedImage(width,heigth,BufferedImage.TYPE_INT_BGR);
        // 得到一个画笔 Graphics
        Graphics graphics = bufferedImage.getGraphics();
        // 画矩形，填背景色 画干扰线条 画字符串
        // 画矩形框时，可以先调画笔颜色
        graphics.setColor(Color.BLUE);
        graphics.drawRect(0,0,width,heigth);
        // 填充背景
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(1,1,width - 2,heigth - 2);
        // 画干扰线条
        graphics.setColor(Color.BLACK);
        Random random = new Random();
        for(int i = 0; i < 20; i++){
            // 保证X、Y不超出范围
            graphics.drawLine(random.nextInt(width),random.nextInt(heigth),random.nextInt(width),random.nextInt(heigth));
        }
        // 画随机字符串 先要控制字符颜色，及字体大小
        graphics.setColor(Color.RED);
        // Font.BOLD|Font.ITALIC 两种类型做叠加
        graphics.setFont(new Font("黑体",Font.BOLD|Font.ITALIC,20));
        // 将画好的缓冲区的图像写入到浏览器中
        // 服务器要通过响应消息头，告知客户端，给他写的内容是一个一副图片
        response.setHeader("Content-Type","image/jpeg");
        // 为了更好地让验证码，在客户端不要缓存，设置响应头，告知客户端不要缓存
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Pragma","no-cache");
        response.setHeader("Expires","-1");
        // 写数据到浏览器 ImageIO
        ImageIO.write(bufferedImage,"jpg",response.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
