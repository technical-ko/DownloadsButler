import java.util.*;
import java.io.*;

public class RunDLButler
{

/*
Last Updated: 11/16/2019
Author: Keith O'Neal
Class Description: 
RunDLButler
This program is designed to keep your downloads folder tidy! 
Files are organized into folders grouped by month, and subfolders grouped by filetype.
Filenames have their date of download appended to them. 

Main file for running the DLButler program. 
________________________


UML:

*/


    public static void main( String[] args )
    {

        //ADD SANITIZATION of args[1] IN HERE
      //  File downloadsDirectory = new File(args[1]);
        File downloadsDirectory = new File("C:\\Users\\bneal\\OneDrive\\Desktop\\DLButler\\test");//Hardcode for now.

        if(!downloadsDirectory.exists())
        {
            throw new IllegalArgumentException("Given downloads directory doesn't exist.");
        }

        DLButler dlButler = new DLButler(downloadsDirectory);
        TimerTask task = dlButler.getCleanDownloadsTask();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(task, 300, 2*1000);

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
        System.out.println("TimerTask cancelled");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        
    }//end main

    



}//end class