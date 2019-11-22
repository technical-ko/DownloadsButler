import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DLButler
{

/*
Last Updated: 11/21/2019
Author: Keith O'Neal
Class Description: 
DLButler
This program is designed to keep your downloads folder tidy! 
Files are organized into folders grouped by month, and subfolders grouped by filetype.
Filenames have their date of download appended to them. 

________________________


UML:

*/

    private File downloadsDirectory;


    public DLButler(File downloadsDirectory)
    {
        if(!downloadsDirectory.exists()){throw new IllegalArgumentException(getClass().getName() + "downloads directory does not exist");}

        this.downloadsDirectory = downloadsDirectory;
    }


    //renames fileToName according to the current date
    //Can add functionality to process FileExtension information later using my FileExtension class 
    private File processFile (File fileToProcess)
    {
        Calendar calendar;
        String newName;
        String oldFileName;
        String fileExtension;
        calendar = Calendar.getInstance(TimeZone.getDefault());        
        fileExtension = "";
        oldFileName = fileToProcess.getName();
    
        //remove file extension
        if(oldFileName.lastIndexOf(".") < 0)//there is no extension
        {
            fileExtension = "";
            newName = oldFileName;
        }
        else
        {
            fileExtension = oldFileName.substring(oldFileName.lastIndexOf("."), oldFileName.length());
            newName = oldFileName.substring(0, oldFileName.lastIndexOf("."));
        }

        int month =  calendar.get(Calendar.MONTH) + 1;
        int day   = calendar.get(Calendar.DATE);
        int year  = calendar.get(Calendar.YEAR);
        String monthDayYear = "-" + month + "-" +  day + "-" + year;

        //if month-day-year is not already at the end of newName
        if(newName.indexOf(monthDayYear, newName.length() - monthDayYear.length()) == -1)
        {
            // append -month-day-year -> xx-xx-xxxx
            newName = newName + monthDayYear;

            // append file extension
            newName = newName + fileExtension;

            //rename the file to newName
            //NOTE: if there exists a file in directory with the same name as newName,
            //then it will be overwritten.
            Path source = Paths.get(fileToProcess.getPath());
            
            try {Files.move(source, source.resolveSibling(newName));}
            catch (IOException e) {
                JFrame frame = new JFrame("ErrorMsg");
                JOptionPane.showMessageDialog(frame, "There was an error renaming " + fileToProcess.getName() + ".", "DLButler Error", JOptionPane.WARNING_MESSAGE);
            }
            
        }

        moveToDirectory(fileToProcess, fileExtension, month, year);

        return fileToProcess;
    }



    private void moveToDirectory(File fileToMove, String fileExtension, int month, int year)
    {
        //create path to correct month directory and fileExt. subdirectory
        //Currently this method checks for the directory and subdirectory separately, making each if they don't exist.
        //This should be changed if possible
        //RESEARCH PATH(S), FILES, ETC. to clean this up


        String PATH = this.downloadsDirectory.getPath();
        PATH = PATH + "\\" + getMonthAsString(month) + year;
        File dir = new File(PATH);

        //if appropriate date directory doesn't exist
        if(!dir.exists()){dir.mkdirs();}

        PATH = dir.getPath() + "\\" + fileExtension;
        dir = new File(PATH);

        //if appropriate fileExt. directory doesn't exist
        if(!dir.exists()){dir.mkdirs();}

        Path newDir = Paths.get(dir.getPath());

        try {Files.move(Paths.get(fileToMove.getPath()), newDir.resolve(fileToMove.getName()));}
         catch (IOException e)
          {
            /*
            this error gets repeatedly thrown when the program is run. As far as I am aware, these I/O exceptions aren't doing anything harmful (no corrupted files in my testing so far).
            I think they indicate that my implementation of threading in this program is pretty inefficient.
            */
          // JFrame frame = new JFrame("ErrorMsg");
          //  JOptionPane.showMessageDialog(frame, "There was an error moving " + fileToMove.getName() + " to its proper folder.", "DLButler Error", JOptionPane.WARNING_MESSAGE);
        }
    }



    public TimerTask getCleanDownloadsTask(){return new CleanDownloadsTask();}




    private class CleanDownloadsTask extends TimerTask
    {
        int count;
        
        //Constructor for the CleanDownloadsTask
        public CleanDownloadsTask()
        {
            this.count = 1;            
        }
        
        public void run()//Makes this DLButler object clean the downloads directory
        {
            //for testing
            System.out.println("Running " + count);
            count++;

            List<File> fileList = Arrays.asList(downloadsDirectory.listFiles());
            ListIterator<File> files;
            File currentFile;
            Calendar calendar = Calendar.getInstance();


            //reset iterator
            files = fileList.listIterator();

            //Process all normal files in the downloads folder            
            while(files.hasNext())
            {
                currentFile = files.next();
                //do not process directories, only normal files
                if(currentFile.isFile())
                {
                    //if the file hasn't been updated in the last 3 seconds, process it. otherwise, skip it.
                    //this is to ensure that a file has finished downloading before it is moved
                    if(calendar.getTimeInMillis() - currentFile.lastModified() > 3000)
                    {
                        currentFile = processFile(currentFile);
                    }

                }
            }//while

        }

    }//end inner class


//helper method for returning the month as a string
  private String getMonthAsString(int month)
  {
    String result = "";
    if(month == 1){result = "january";}
    if(month == 2){result = "february";}
    if(month == 3){result = "march";}
    if(month == 4){result = "april";}
    if(month == 5){result = "may";}
    if(month == 6){result = "june";}
    if(month == 7){result = "july";}
    if(month == 8){result = "august";}
    if(month == 9){result = "september";}
    if(month == 10){result = "october";}
    if(month == 11){result = "november";}
    if(month == 12){result = "december";}
    return result;
}
  
  



}//end class