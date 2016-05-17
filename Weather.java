//Weather Finder by Max Rosen using Yahoo Weather API
//May 2016

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.awt.Window;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.SwingUtilities;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.Date;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.Locale;
import java.util.TimeZone;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.LocalTime;
import javax.swing.UIManager;

public class Weather implements ActionListener {

   static String cityName;
   static String stateName;
   static String highToday; 
   static String lowToday;
   static String current;
   static String description;
   static String weekForecast = "";
   static String code;
   
   static String windChill;
   static String windSpeed;
   static String humidity;
   static String visibility;
   static String sunrise;
   static String sunset;   
   
   static JButton searchButton;
   static JTextField cityText;
   static JTextField stateText;
   static JFrame frame;
   
   static JLabel icon;
   static ImageIcon pix;

   public static void main(String argv[]) throws IOException
   {
      Scanner in = new Scanner(System.in);
            
      Locale currentLocale = Locale.getDefault();

      displayGUI();
   }   

   public static void getForecast(String city, String state)
   {   
      String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%2C%20"+state+"%22)&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
   
      try 
      {
      	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      	Document doc = dBuilder.parse(url);
               			
      	doc.getDocumentElement().normalize();

         NodeList nList = doc.getElementsByTagName("yweather:forecast");
   		Node node = nList.item(0);						
   		if (node.getNodeType() == Node.ELEMENT_NODE) 
         {
   	      Element eElement = (Element) node;
            highToday = eElement.getAttribute("high");
            lowToday = eElement.getAttribute("low");
         } 
                      
         nList = doc.getElementsByTagName("yweather:wind");
   		node = nList.item(0);						
   		if (node.getNodeType() == Node.ELEMENT_NODE) 
         {
   	      Element eElement = (Element) node;
            windChill = eElement.getAttribute("chill");
            windSpeed = eElement.getAttribute("speed");
         } 

         nList = doc.getElementsByTagName("yweather:atmosphere");
   		node = nList.item(0);						
   		if (node.getNodeType() == Node.ELEMENT_NODE) 
         {
   	      Element eElement = (Element) node;
            humidity = eElement.getAttribute("humidity");
            visibility = eElement.getAttribute("visibility");
         }                   
         
         nList = doc.getElementsByTagName("yweather:astronomy");
   		node = nList.item(0);						
   		if (node.getNodeType() == Node.ELEMENT_NODE) 
         {
   	      Element eElement = (Element) node;
            sunrise = eElement.getAttribute("sunrise");
            sunset = eElement.getAttribute("sunset");
         }   
         
         nList = doc.getElementsByTagName("yweather:location");    
   		node = nList.item(0);						
   		if (node.getNodeType() == Node.ELEMENT_NODE) 
         {
   	      Element eElement = (Element) node;
            cityName = eElement.getAttribute("city");
            stateName = eElement.getAttribute("region");
         }                         
                  
         nList = doc.getElementsByTagName("yweather:forecast");
         weekForecast +=("10 Day Forecast\n◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
      	for (int temp = 0; temp < nList.getLength(); temp++) 
         {
      		Node nNode = nList.item(temp);
      								
      		if (nNode.getNodeType() == Node.ELEMENT_NODE) 
            {
      	      Element eElement = (Element) nNode;
               weekForecast += "" + eElement.getAttribute("day")+", "+eElement.getAttribute("date");
               weekForecast += "\n   High: "+eElement.getAttribute("high")+"°  --  Low: "+eElement.getAttribute("low")+"°";
               weekForecast += "\n   "+eElement.getAttribute("text");
               weekForecast +=("\n◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
            }
     	   }      
      } 
      catch (Exception e) 
      {
   	   e.printStackTrace();
      }
          
   }
   
   public static void getCurrent(String city, String state)
   {
      String url = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%2C%20"+state+"%22)&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
      
      try 
      {
      	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      	Document doc = dBuilder.parse(url);
      			
      	doc.getDocumentElement().normalize();
      			               
      	NodeList nList = doc.getElementsByTagName("yweather:condition");
      	for (int temp = 0; temp < nList.getLength(); temp++) 
         {
      		Node nNode = nList.item(temp);
      								
      		if (nNode.getNodeType() == Node.ELEMENT_NODE) 
            {
      			Element eElement = (Element) nNode;
               current = eElement.getAttribute("temp");
               description = eElement.getAttribute("text");
               code = eElement.getAttribute("code");
      		}
      	}             
      } 
      catch (Exception e) 
      {
   	   e.printStackTrace();
      }   
   }
   
   public static void displayGUI()
   {  
      LocalTime t = LocalTime.now();
      int hour = t.getHour();
      //hour = 22;
      //hour = 12;
      Color endGradient;
      Color startGradient;
      Color textColor;
      
      //Night
      if( hour>=19 || hour<=7 ){ 
         endGradient = new Color(0, 0, 128); 
         startGradient = Color.black;
         textColor = Color.white;
      }
      //Day
      else { 
         endGradient = new Color(128, 170, 255);
         startGradient = Color.white;
         textColor = Color.black;
      }
   
      JPanel panel = new JPanel(){
                 @Override
           protected void paintComponent(Graphics grphcs) {
               super.paintComponent(grphcs);
               Graphics2D g2d = (Graphics2D) grphcs;
               g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);
               GradientPaint gp = new GradientPaint(
                  0, 0, startGradient, 
                  getWidth(), getHeight(), endGradient);
               g2d.setPaint(gp);
               g2d.fillRect(0, 0, getWidth(), getHeight());          
         
        }
      };
      panel.setSize(600,420);
      panel.setLayout(null);
      panel.setForeground(Color.white);
      
      JLabel overlay = new JLabel();
      overlay.setForeground(textColor);
      overlay.setBounds(-10,-10,620,400);
      overlay.setVisible(false);
      
      JTextArea forecast = new JTextArea("");
      forecast.setForeground(textColor);
      JScrollPane scroller = new JScrollPane();
      forecast.setEditable(false);
      forecast.setCursor(null);
      forecast.setOpaque(false);
      forecast.setFocusable(false); 
      forecast.setLineWrap(true);   
      forecast.setSize(200,400);
      scroller.setBounds(130, 10, 175, 290);
      scroller.setBorder(BorderFactory.createEmptyBorder());
      scroller.setVisible(false);
      scroller.setOpaque(false);   
      scroller.getViewport().setOpaque(false);
      scroller.getVerticalScrollBar().setPreferredSize(new Dimension(9,0));   
      //scroller.getVerticalScrollBar().setOpaque(false);
      scroller.add(forecast); 
      panel.add(scroller);
      scroller.setViewportView(forecast);         
      
      JLabel location =  new JLabel();
      location.setForeground(textColor);
      location.setFont(location.getFont().deriveFont(28.0f));
      location.setBounds(320,25,300,40); 
      panel.add(location);
      
      JLabel highDay =  new JLabel();
      highDay.setForeground(textColor);
      highDay.setBounds(320,120,120,30); 
      panel.add(highDay);
      
      JLabel lowDay =  new JLabel();
      lowDay.setForeground(textColor);
      lowDay.setBounds(320,140,120,30); 
      panel.add(lowDay);

      JLabel currentTemp =  new JLabel();
      currentTemp.setForeground(textColor);
      currentTemp.setFont(currentTemp.getFont().deriveFont(48.0f));
      currentTemp.setBounds(320,70,100,50); 
      panel.add(currentTemp);

      JLabel windChillLabel = new JLabel();
      windChillLabel.setForeground(textColor);
      ImageIcon windChillPic = new ImageIcon("Windchill.png");
      windChillLabel.setIcon(windChillPic);
      windChillLabel.setBounds(475,150,55,65); 
      windChillLabel.setVisible(false); 
      windChillLabel.setVerticalTextPosition(JLabel.BOTTOM);
      windChillLabel.setHorizontalTextPosition(JLabel.CENTER);
      panel.add(windChillLabel);
      
      JLabel windSpeedLabel = new JLabel("",null,JLabel.CENTER);
      windSpeedLabel.setForeground(textColor);
      ImageIcon windSpeedPic = new ImageIcon("Speed-48.png");
      windSpeedLabel.setIcon(windSpeedPic);
      windSpeedLabel.setBounds(535,150,55,65);
      windSpeedLabel.setVisible(false); 
      windSpeedLabel.setVerticalTextPosition(JLabel.BOTTOM);
      windSpeedLabel.setHorizontalTextPosition(JLabel.CENTER);
      panel.add(windSpeedLabel); 

      JLabel sunriseLabel = new JLabel();
      sunriseLabel.setForeground(textColor);
      ImageIcon sunrisePic = new ImageIcon("Sunrise.png");
      sunriseLabel.setIcon(sunrisePic);
      sunriseLabel.setBounds(475,70,55,65); 
      sunriseLabel.setVisible(false); 
      sunriseLabel.setVerticalTextPosition(JLabel.BOTTOM);
      sunriseLabel.setHorizontalTextPosition(JLabel.CENTER);
      panel.add(sunriseLabel);
      
      JLabel sunsetLabel = new JLabel("",null,JLabel.CENTER);
      sunsetLabel.setForeground(textColor);
      ImageIcon sunsetPic = new ImageIcon("Sunset.png");
      sunsetLabel.setIcon(sunsetPic);
      sunsetLabel.setBounds(535,70,55,65);
      sunsetLabel.setVisible(false); 
      sunsetLabel.setVerticalTextPosition(JLabel.BOTTOM);
      sunsetLabel.setHorizontalTextPosition(JLabel.CENTER);
      panel.add(sunsetLabel); 
      
      JLabel HumidityLabel = new JLabel();
      HumidityLabel.setForeground(textColor);
      ImageIcon humidityPic = new ImageIcon("Humidity.png");
      HumidityLabel.setIcon(humidityPic);
      HumidityLabel.setBounds(475,220,55,65); 
      HumidityLabel.setVisible(false); 
      HumidityLabel.setVerticalTextPosition(JLabel.BOTTOM);
      HumidityLabel.setHorizontalTextPosition(JLabel.CENTER);
      panel.add(HumidityLabel);
      
      JLabel VisibilityLabel = new JLabel("",null,JLabel.CENTER);
      VisibilityLabel.setForeground(textColor);
      ImageIcon visibilityPic = new ImageIcon("Opera Glasses.png");
      VisibilityLabel.setIcon(visibilityPic);
      VisibilityLabel.setBounds(535,220,55,65);
      VisibilityLabel.setVisible(false); 
      VisibilityLabel.setVerticalTextPosition(JLabel.BOTTOM);
      VisibilityLabel.setHorizontalTextPosition(JLabel.CENTER);
      panel.add(VisibilityLabel);       
      
      cityText = new JTextField("city");
      cityText.setBounds(10,10,100,30); 
      panel.add(cityText);
      
      stateText = new JTextField("state");
      stateText.setBounds(10,40,100,30); 
      panel.add(stateText);
      
      JLabel infoText = new JLabel();
      infoText.setForeground(textColor);
      infoText.setBounds(320,170,120,30); 
      //panel.add(infoText);
            
      searchButton = new JButton("Search");
      searchButton.setBounds(10,70,100,30);
      
      JLabel icon = new JLabel();
      icon.setForeground(textColor);
      icon.setBounds(320,150,200,150);
            
      JLabel tempText =  new JLabel("Weather \n Finder");
      tempText.setForeground(textColor);
      tempText.setFont(currentTemp.getFont().deriveFont(72.0f));
      tempText.setBounds(35,15,580,300); 
      panel.add(tempText);
            
      searchButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
         weekForecast = "";
         icon.setIcon(null);
         tempText.setVisible(false);
         getForecast(cityText.getText(),stateText.getText());
         getCurrent(cityText.getText(),stateText.getText());
         String imgURL = "icon-"+code+".png";
         ImageIcon pix = new ImageIcon(imgURL);
         icon.setIcon(pix);
         icon.setText(description);
         icon.setVerticalTextPosition(JLabel.BOTTOM);
         icon.setHorizontalTextPosition(JLabel.CENTER);            
         panel.add(icon); 
         lowDay.setText("Low Today: "+lowToday+"°");
         highDay.setText("High Today: "+highToday+"°");
         scroller.setVisible(true);
         currentTemp.setText(current+"°");
         sunriseLabel.setText(sunrise);
         sunriseLabel.setVisible(true); 
         sunsetLabel.setText(sunset);
         sunsetLabel.setVisible(true); 
         windSpeedLabel.setText(windSpeed+" mph");
         windSpeedLabel.setVisible(true);
         windChillLabel.setText(windChill+"°");
         windChillLabel.setVisible(true); 
         VisibilityLabel.setText(visibility+" mi");
         VisibilityLabel.setVisible(true);
         HumidityLabel.setText(humidity+"%");
         HumidityLabel.setVisible(true);
         forecast.setText(weekForecast);
         infoText.setText(description);
         scroller.getViewport().setViewPosition(new Point(0,0));
         forecast.setCaretPosition(0);
         location.setText(cityName+","+stateName);
         frame.revalidate();
       }
      });
      
      panel.add(searchButton);
      frame = new JFrame(".:Weather:.");
      frame.getRootPane().setDefaultButton(searchButton);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(scroller);
      frame.getContentPane().add(panel);
      frame.pack();
      frame.setVisible(true);
      frame.setSize(620,330);
   } 
   
	public void actionPerformed(ActionEvent e) 
   {

   }  
   
    public static String upperCaseAllFirst(String value) {
   	char[] array = value.toCharArray();
   	// Uppercase first letter.
   	array[0] = Character.toUpperCase(array[0]);
   
   	// Uppercase all letters that follow a whitespace character.
   	for (int i = 1; i < array.length; i++) {
   	    if (Character.isWhitespace(array[i - 1])) {
   		array[i] = Character.toUpperCase(array[i]);
   	    }
   	}
   
   	// Result.
   	return new String(array);
    }
           
}  