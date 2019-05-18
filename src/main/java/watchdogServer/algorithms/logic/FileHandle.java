package watchdogServer.algorithms.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;
import watchdogServer.algorithms.seviceClasses.Log;

public class FileHandle {

    static List<Location> data; // list of XML from readFromXML function
    static List<Location> data2; // list of JSON from readFromJSON function

    public static void handleData() throws ParseException, NumberFormatException, IOException {

        readFromXML("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\data.xml");

        writeToJSON("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\json.json");

    }

    public static void addXMLs(String[] files) throws IOException {
        OutputStream out = new FileOutputStream("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\big_xml.xml");
        byte[] buf = new byte[256];
        for (String file : files) {
            InputStream in = new FileInputStream(file);
            int b = 0;
            while ((b = in.read(buf)) >= 0) {
                out.write(buf, 0, b);
                out.flush();
            }
        }
        out.close();
    }

    public static List<Log> readLogFromJSON(String filePath)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);

        List<Log> logList = mapper.readValue(file, new TypeReference<List<Log>>() {
        });

        System.out.println("JSON file read");
        return logList;
    }


    public static List<Location> readFromJSON(String filePath)
            throws JsonParseException, JsonMappingException, IOException {

        List<Location> list = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);

        list = mapper.readValue(file, new TypeReference<List<Location>>() {
        });

        System.out.println("JSON file read");
        return list;
    }

    public static void writeToJSON(String filePath) throws JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        writer.writeValue(new File(filePath), data);

        System.out.println("JSON file created");
    }

    public static void writeToJSON(String filePath, List<Location> locationList) throws JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        writer.writeValue(new File(filePath), locationList);

        System.out.println("JSON file created");
    }

    public static void readFromXML(String filePath) throws NumberFormatException, IOException, ParseException {
        data = new ArrayList<>();

        File file = new File(filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            if (!line.contains("trkpt"))
                continue;

            // pull lat from xml file
            int startingPointOfLat = line.indexOf("lat");
            int i = 5;
            String latStr = "";
            while (line.charAt(startingPointOfLat + i) != '\"') {
                latStr += line.charAt(startingPointOfLat + i++);
            }
            double lat = Double.parseDouble(latStr);

            // pull lon from xml file
            int startingPointOfLon = line.indexOf("lon");
            i = 5;
            String lonStr = "";
            while (line.charAt(startingPointOfLon + i) != '\"') {
                lonStr += line.charAt(startingPointOfLon + i++);
            }
            double lon = Double.parseDouble(lonStr);

			/*
			 * // pull ele from xml file int startingPointOfEle = line.indexOf("ele"); i =
			 * 4; String ele = ""; while (line.charAt(startingPointOfEle + i) != '<') { ele
			 * += line.charAt(startingPointOfEle + i++); }
			 */

            // pull time from xml file
            int startingPointOfTime = line.indexOf("time");
            String year = (String) line.subSequence(startingPointOfTime + 5, startingPointOfTime + 9);
            String month = (String) line.subSequence(startingPointOfTime + 10, startingPointOfTime + 12);

            String day = (String) line.subSequence(startingPointOfTime + 13, startingPointOfTime + 15);
            String hour = (String) line.subSequence(startingPointOfTime + 16, startingPointOfTime + 18);
            String min = (String) line.subSequence(startingPointOfTime + 19, startingPointOfTime + 21);
            String sec = (String) line.subSequence(startingPointOfTime + 22, startingPointOfTime + 24);

            String timeStr = year + "-" + month + "-" + day + "-" + hour + "-" + min + "-" + sec;

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
            fmt.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
            Date time = fmt.parse(timeStr);

            Location l = new Location(new Position(lat, lon), time);
            data.add(l);
        }

        data.sort( new LocationComparator() );
        System.out.println("XML file read");
    }
	/*
	 * static public class Line { public double lat; public double lon; public
	 * String ele; public Date time;
	 *
	 * public Line(double lat, double lon, String ele, Date time) { this.lat = lat;
	 * this.lon = lon; this.ele = ele; this.time = time; }
	 *
	 * public Line() { lat = 0; lon = 0; ele = ""; time = null; }
	 *
	 * public String toString() { return "lat = " + lat + "\tlon = " + lon +
	 * "\tele = " + ele + "\ttime = " + time; } }
	 */
}
