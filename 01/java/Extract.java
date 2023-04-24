import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.HexFormat;

public class Extract {
    private static byte[] getData(ZipInputStream zip) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] arr = new byte[1024];
        int count;
        while ((count = zip.read(arr, 0, arr.length)) > 0) {
            out.write(arr, 0, count);
        }
        return out.toByteArray();
    }

    private static List<String> walkZip(InputStream inp) throws IOException {
        // walk through zip files while collecting filenames
        List<String> names = new ArrayList<String>();
        while (true) {
            // open as zip file
            try (ZipInputStream zip = new ZipInputStream(inp)) {
                // get first and only entry
                ZipEntry entry = zip.getNextEntry();
                String name = entry.getName();
                names.add(name);

                // show progress
                System.out.println(name);

                // read its content in a buffer used in next iteration
                byte[] data = getData(zip);
                inp = new ByteArrayInputStream(data);
                if (!name.toLowerCase().endsWith(".zip")) {
                    // display final text file message
                    System.out.println(new String(data, "UTF-8"));
                    break;
                }
            }
        }
        return names;
    }

    public static void main(String[] args) throws IOException {
        // recursive walk
        List<String> names;
        try (FileInputStream fs = new FileInputStream("../png.zip")) {
            names = walkZip(fs);
            System.out.println("Depth = " + names.size());
        }

        // combined hex values
        String s = names.stream()
            .map(n -> n.substring(0, n.lastIndexOf(".")))
            .collect(Collectors.joining());
        System.out.println(s);

        // write bytes to output image
        try (FileOutputStream fs = new FileOutputStream("png.png")) {
            fs.write(HexFormat.of().parseHex(s));
        }
    }
}
