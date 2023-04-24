const fs = require('fs');
const ZipFile = require('adm-zip');

function walkZip(inp) {
    // walk through zip files while collecting filenames
    let names = [];
    while (true) {
        // open as zip file
        let zip = new ZipFile(inp);

        // get first and only entry
        let entry = zip.getEntries()[0];
        let name = entry.entryName;
        names.push(name);

        // show progress
        console.log(name);

        // read its content in a buffer used in next iteration
        inp = entry.getData();
        if (!name.toLowerCase().endsWith('.zip')) {
            // display final text file message
            console.log(inp.toString('UTF-8'));
            break;
        }
    }
    return names;
}

// recursive walk
const names = walkZip('../png.zip');
console.log(`Depth = ${names.length}`);

// combined hex values
const str = names.map(n => n.substring(0, n.lastIndexOf('.'))).join('');
console.log(str);

// write bytes to output image
fs.writeFileSync('png.png', Buffer.from(str, 'hex'));
