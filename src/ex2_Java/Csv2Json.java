package ex2_Java;



//import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Csv2Json extends JFrame{
private static final long serialVersionUID = 1L;
private static File CSVFile;
private static BufferedReader read;
private static BufferedWriter write;

public Csv2Json(){
    FileNameExtensionFilter filter = new FileNameExtensionFilter("comma separated values", "csv");
    JFileChooser choice = new JFileChooser();
    choice.setFileFilter(filter); //limit the files displayed

    int option = choice.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
        CSVFile = choice.getSelectedFile();
    }
    else{
        JOptionPane.showMessageDialog(this, "Did not select file. Program will exit.", "System Dialog", JOptionPane.PLAIN_MESSAGE);         
        System.exit(1);
    }
}

public static void main(String args[]){
    Csv2Json parse = new Csv2Json();
    parse.convert();

    System.exit(0);
}

private void convert(){
    /*Converts a .csv file to .json. Assumes first line is header with columns*/
    try {
        read = new BufferedReader(new FileReader(CSVFile));

        String outputName = CSVFile.toString().substring(0, 
                CSVFile.toString().lastIndexOf(".")) + ".json"; 
        write = new BufferedWriter(new FileWriter(new File(outputName)));

        String line;
        String columns[]; //contains column names
        int num_cols;
        String tokens[];

        int progress = 0; //check progress

        //initialize columns
        line = read.readLine(); 
        columns = line.split(",");
        num_cols = columns.length;


        write.write("["); //begin file as array
        line = read.readLine();


        while(true) {
            tokens = line.split(",");

            if (tokens.length == num_cols){ //if number columns equal to number entries
                write.write("{");

                for (int k = 0; k < num_cols; ++k){ //for each column 
                    if (tokens[k].matches("^-?[0-9]*\\.?[0-9]*$")){ //if a number
                        write.write("\"" + columns[k] + "\": " + tokens[k]);
                        if (k < num_cols - 1) write.write(", ");                                                }
                    else { //if a string
                        write.write("\"" + columns[k] + "\": \"" + tokens[k] + "\"");
                        if (k < num_cols - 1) write.write(", ");
                    }
                }

                ++progress; //progress update
                if (progress % 10000 == 0) System.out.println(progress); //print progress           


                if((line = read.readLine()) != null){//if not last line
                    write.write("},");
                    write.newLine();
                }
                else{
                    write.write("}]");//if last line
                    write.newLine();
                    break;
                }
            }
            else{
                //line = read.readLine(); //read next line if wish to continue parsing despite error 
                JOptionPane.showMessageDialog(this, "ERROR: Formatting error line " + (progress + 2)
                 + ". Failed to parse.", 
                        "System Dialog", JOptionPane.PLAIN_MESSAGE);                    
                System.exit(-1); //error message
            }
        }

        JOptionPane.showMessageDialog(this, "File converted successfully to "     + outputName, 
                "System Dialog", JOptionPane.PLAIN_MESSAGE);

        write.close();
        read.close();
    }
    catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }       
}
}