using System;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.IO.Compression;

public class Extract
{
    private static List<string> WalkZip(Stream inp)
    {
        // walk through zip files while collecting filenames
        List<string> names = new List<string>();
        while (true) {
            // open as zip file
            using (ZipArchive zip = new ZipArchive(inp, ZipArchiveMode.Read)) {
                // get first and only entry
                ZipArchiveEntry entry = zip.Entries[0];
                string name = entry.FullName;
                names.Add(name);

                // show progress
                Console.WriteLine(name);

                // read its content in a buffer used in next iteration
                inp = new MemoryStream();
                using (Stream s = entry.Open()) {
                    s.CopyTo(inp);
                }
                if (!name.EndsWith(".zip", StringComparison.OrdinalIgnoreCase)) {
                    // display final text file message
                    using (Stream s = entry.Open())
                    using (StreamReader r = new StreamReader(s)) {
                        Console.WriteLine(r.ReadToEnd());
                    }
                    break;
                }
            }
        }
        return names;
    }

    public static void Main(string[] args)
    {
        // recursive walk
        List<string> names;
        using (FileStream fs = File.OpenRead(@"../png.zip")) {
            names = WalkZip(fs);
            Console.WriteLine($"Depth = {names.Count}");
        }

        // combined hex values
        string str = String.Join("",
            names.Select(n => Path.GetFileNameWithoutExtension(n)));
        Console.WriteLine(str);

        // write bytes to output image
        using (FileStream fs = File.Create("png.png")) {
            byte[] b = Convert.FromHexString(str);
            fs.Write(b, 0, b.Length);
        }
    }
}
