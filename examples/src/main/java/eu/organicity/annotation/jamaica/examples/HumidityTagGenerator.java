package eu.organicity.annotation.jamaica.examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Dataset Tag Generator.
 */
public class HumidityTagGenerator {

    public static void main(String[] args) {

        //Read all data
        try (BufferedReader br = new BufferedReader(new FileReader("humidity-full.dat"))) {

            //Open the classified data file
            FileWriter fw = new FileWriter("humidity.classified");
            BufferedWriter bw = new BufferedWriter(fw);

            String line;
            while ((line = br.readLine()) != null) {
                //Parse each entry of the file
                Double value = Double.valueOf(line);
                String res;
                if (value < 0 || value > 100) {
                    res = ("malfunction," + value);
                } else if (value < 30) {
                    res = ("low," + value);
                } else if (value < 70) {
                    res = ("normal," + value);
                } else {
                    res = ("high," + value);
                }
                System.out.println(res);
                //Output the classified data
                bw.write(res + "\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
