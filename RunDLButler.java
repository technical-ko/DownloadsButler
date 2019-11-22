import java.util.*;
import java.io.*;
import java.nio.file.Paths;


public class RunDLButler
{

/*
Last Updated: 11/21/2019
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

        File downloadsDirectory;

        if(args.length > 0 ){downloadsDirectory = new File(args[0]);}
        else
        {
            downloadsDirectory = Paths.get(System.getProperty("user.home") + "\\Downloads").toFile();
        }

        //File downloadsDirectory = new File("C:\\Users\\bneal\\OneDrive\\Desktop\\DLButler\\test");//Hardcode for testing

        if(!downloadsDirectory.exists())
        {
            throw new IllegalArgumentException("Given downloads directory doesn't exist.");
        }

        DLButler dlButler = new DLButler(downloadsDirectory);
        TimerTask task = dlButler.getCleanDownloadsTask();
        Timer timer = new Timer();
        Scanner scanner = new Scanner(System.in);

        timer.scheduleAtFixedRate(task, 300, 2*1000);

        
        while(true)
        {
            System.out.println("Type 'q' and enter to terminate the application");
            String line = scanner.nextLine();
            if(line.charAt(0) == 'q')
            {
                System.out.println("Quitting DLButler...");
                scanner.close();
                timer.cancel();
                System.exit(0);
            }
        }

        
    }//end main

    



}//end class